/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.util.ECPApplicableTester;
import org.eclipse.emf.ecp.editor.util.StaticApplicableTester;
import org.eclipse.emf.ecp.internal.edit.Activator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.osgi.framework.Bundle;

/**
 * The ControlFactory is a Singelton which reads the org.eclipse.emf.ecp.editor.widgets ExtensionPoint and provides a
 * method ({@link #createControl(Composite, IItemPropertyDescriptor, EditModelElementContext)}) for creating a suitable
 * control for with the known widgets.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class ControlFactory {

	private static final String CONTROL_EXTENSION = "org.eclipse.emf.ecp.edit.controls";
	
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String COMPOSITE_CLASS_ATTRIBUTE = "supportedCompositeClass";
	private static final String LABEL_ATTRIBUTE = "showLabel";
	
	private static final String TEST_DYNAMIC = "dynamicTest";
	private static final String CONTROL_TESTER = "testClass";
	
	private static final String TEST_STATIC = "staticTest";
	private static final String TESTER_PRIORITY = "priority";
	private static final String TESTER_CLASSTYPE = "supportedClassType";
	private static final String TESTER_EOBJECT = "supportedEObject";
	private static final String TESTER_FEATURE = "supportedFeature";
	private static final String TESTER_SINGLEVALUE = "singleValue";
	
	private Set<ControlDescription> controlDescriptors = new HashSet<ControlDescription>();

	/**
	 * The Singleton for accessing the ControlFactory.
	 */
	public static final ControlFactory INSTANCE = new ControlFactory();

	private ControlFactory() {
		readExtensionPoint();
	}

	private void readExtensionPoint() {
		IConfigurationElement[] controls = Platform.getExtensionRegistry().getConfigurationElementsFor(
			CONTROL_EXTENSION);
		for (IConfigurationElement e : controls) {
			try {
				String clazz = e.getAttribute(CLASS_ATTRIBUTE);
				Class<? extends AbstractControl<?>> resolvedClass = loadClass(e.getContributor().getName(), clazz);
				String compositeClazz = e.getAttribute(COMPOSITE_CLASS_ATTRIBUTE);
				Class<?> resolvedCompositeClass = loadClass(e.getContributor().getName(), compositeClazz);
				boolean showLabel = Boolean.parseBoolean(e.getAttribute(LABEL_ATTRIBUTE));
				
				ECPApplicableTester tester=null;
				for(IConfigurationElement testerExtension: e.getChildren()){
					if(TEST_DYNAMIC.equals(testerExtension.getName())){
						tester=(ECPApplicableTester) testerExtension.createExecutableExtension(CONTROL_TESTER);
					}
					else if(TEST_STATIC.equals(testerExtension.getName())){
						boolean singleValue = Boolean.parseBoolean(testerExtension.getAttribute(TESTER_SINGLEVALUE));
						int priority = Integer.parseInt(testerExtension.getAttribute(TESTER_PRIORITY));
						
						String type = testerExtension.getAttribute(TESTER_CLASSTYPE);
						Class<?> supportedClassType = Class.forName(type);
						
						String eObject = testerExtension.getAttribute(TESTER_EOBJECT);
						if (eObject == null) {
							eObject = "org.eclipse.emf.ecore.EObject";
						}
						Class<? extends EObject> supportedEObject = loadClass(testerExtension.getContributor().getName(), eObject);
						
						String supportedFeature = testerExtension.getAttribute(TESTER_FEATURE);
						
						tester=new StaticApplicableTester(singleValue, priority, supportedClassType, supportedEObject, supportedFeature);
					}
				}
				ControlDescription controlDescription = new ControlDescription(resolvedClass,resolvedCompositeClass,showLabel,tester);
				controlDescriptors.add(controlDescription);
			} catch (ClassNotFoundException e1) {
				Activator.logException(e1);
			} catch (CoreException e1) {
				Activator.logException(e1);
			}
		}
	}

	/**
	 * @param name
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + " cannot be loaded because bundle " + bundleName
				+ " cannot be resolved");
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	/**
	 * Creates an {@link AbstractControl} from the provided {@link IItemPropertyDescriptor} and the
	 * {@link EditModelElementContext}.
	 * 
	 * @param <T> the Type of the composite where we want to add the control onto
	 * @param parent the Composite the control will be added to
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param context the {@link EditModelElementContext}
	 * @return the created {@link AbstractControl} or null if nothing fitting could be created
	 */
	public <T> AbstractControl<T> createControl(T parent, IItemPropertyDescriptor itemPropertyDescriptor,
		EditModelElementContext context) {
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(context.getModelElement());

		ControlDescription controlDescription = getControlCandidate(parent.getClass(),itemPropertyDescriptor, context.getModelElement());
		if(controlDescription==null){
			return null;
		}
		AbstractControl<T> control = getControlInstance(controlDescription,itemPropertyDescriptor,feature,context);

		
		return control;
	}
	/**
	 * A copy of all known {@link ControlDescription}.
	 * @return a copy of the set of all known controlDescriptions
	 */
	public Set<ControlDescription> getControlDescriptors() {
		return new HashSet<ControlDescription>(controlDescriptors);
	}

	@SuppressWarnings("unchecked")
	private <T> AbstractControl<T> getControlInstance(ControlDescription controlDescription,
		IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature, EditModelElementContext modelElementContext) {

		try {
			Constructor<? extends AbstractControl<?>> controlConstructor = controlDescription.getControlClass().getConstructor(boolean.class,
				IItemPropertyDescriptor.class, EStructuralFeature.class, EditModelElementContext.class,boolean.class);
			return (AbstractControl<T>) controlConstructor.newInstance(controlDescription.isShowLabel(),itemPropertyDescriptor, feature, modelElementContext,false);
		} catch (IllegalArgumentException ex) {
			Activator.logException(ex);
		} catch (InstantiationException ex) {
			Activator.logException(ex);
		} catch (IllegalAccessException ex) {
			Activator.logException(ex);
		} catch (InvocationTargetException ex) {
			Activator.logException(ex);
		} catch (SecurityException ex) {
			Activator.logException(ex);
		} catch (NoSuchMethodException ex) {
			Activator.logException(ex);
		}
		return null;
	}

	private ControlDescription getControlCandidate(Class<?> compositeClass,IItemPropertyDescriptor itemPropertyDescriptor,
		EObject modelElement) {
		int highestPriority = -1;
		ControlDescription bestCandidate = null;
		for (ControlDescription description : controlDescriptors) {
			
			if(!description.getSupportedCompositeClass().isAssignableFrom(compositeClass)){
				continue;
			}
			int currentPriority=-1;
			currentPriority=description.getTester().isApplicable(itemPropertyDescriptor, modelElement);
			
			if(currentPriority>highestPriority){
				highestPriority=currentPriority;
				bestCandidate=description;
			}
		}
		return bestCandidate;
	}

}

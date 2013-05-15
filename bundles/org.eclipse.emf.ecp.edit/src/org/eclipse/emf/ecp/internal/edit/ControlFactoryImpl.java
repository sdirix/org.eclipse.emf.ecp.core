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
package org.eclipse.emf.ecp.internal.edit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPAbstractControl;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.ECPControlDescription;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.util.ECPApplicableTester;
import org.eclipse.emf.ecp.edit.util.ECPStaticApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.osgi.framework.Bundle;

/**
 * The ControlFactoryImpl is a Singelton which reads the org.eclipse.emf.ecp.editor.widgets ExtensionPoint and provides a
 * method ({@link #createControl(T, IItemPropertyDescriptor, ECPControlContext)}) for creating a suitable
 * control for with the known widgets.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class ControlFactoryImpl implements ECPControlFactory{

	private static final String CONTROL_EXTENSION = "org.eclipse.emf.ecp.edit.controls"; //$NON-NLS-1$
	
	private static final String CLASS_ATTRIBUTE = "class";//$NON-NLS-1$
	private static final String CONTROL_ID = "id";//$NON-NLS-1$
	private static final String LABEL_ATTRIBUTE = "showLabel";//$NON-NLS-1$
	
	private static final String TEST_DYNAMIC = "dynamicTest";//$NON-NLS-1$
	private static final String CONTROL_TESTER = "testClass";//$NON-NLS-1$
	
	private static final String TEST_STATIC = "staticTest";//$NON-NLS-1$
	private static final String TESTER_PRIORITY = "priority";//$NON-NLS-1$
	private static final String TESTER_CLASSTYPE = "supportedClassType";//$NON-NLS-1$
	private static final String TESTER_EOBJECT = "supportedEObject";//$NON-NLS-1$
	private static final String TESTER_FEATURE = "supportedFeature";//$NON-NLS-1$
	private static final String TESTER_SINGLEVALUE = "singleValue";//$NON-NLS-1$
	
	private Set<ECPControlDescription> controlDescriptors = new HashSet<ECPControlDescription>();

	/**
	 * The Singleton for accessing the ControlFactoryImpl.
	 */
	public static final ControlFactoryImpl INSTANCE = new ControlFactoryImpl();

	private ControlFactoryImpl() {
		readExtensionPoint();
	}

	private void readExtensionPoint() {
		IConfigurationElement[] controls = Platform.getExtensionRegistry().getConfigurationElementsFor(
			CONTROL_EXTENSION);
		for (IConfigurationElement e : controls) {
			try {
				String id=e.getAttribute(CONTROL_ID);
				String clazz = e.getAttribute(CLASS_ATTRIBUTE);
				Class<? extends ECPAbstractControl> resolvedClass = loadClass(e.getContributor().getName(), clazz);
				boolean showLabel = Boolean.parseBoolean(e.getAttribute(LABEL_ATTRIBUTE));
				
//				ECPApplicableTester tester=null;
				Set<ECPApplicableTester> tester=new HashSet<ECPApplicableTester>();
				for(IConfigurationElement testerExtension: e.getChildren()){
					if(TEST_DYNAMIC.equals(testerExtension.getName())){
						tester.add((ECPApplicableTester) testerExtension.createExecutableExtension(CONTROL_TESTER));
					}
					else if(TEST_STATIC.equals(testerExtension.getName())){
						boolean singleValue = Boolean.parseBoolean(testerExtension.getAttribute(TESTER_SINGLEVALUE));
						int priority = Integer.parseInt(testerExtension.getAttribute(TESTER_PRIORITY));
						
						String type = testerExtension.getAttribute(TESTER_CLASSTYPE);
						Class<?> supportedClassType = Class.forName(type);
						
						String eObject = testerExtension.getAttribute(TESTER_EOBJECT);
						if (eObject == null) {
							eObject = "org.eclipse.emf.ecore.EObject";//$NON-NLS-1$
						}
						Class<? extends EObject> supportedEObject = loadClass(testerExtension.getContributor().getName(), eObject);
						
						String supportedFeature = testerExtension.getAttribute(TESTER_FEATURE);
						
						tester.add(new ECPStaticApplicableTester(singleValue, priority, supportedClassType, supportedEObject, supportedFeature));
					}
				}
				ECPControlDescription controlDescription = new ECPControlDescription(id,resolvedClass,showLabel,tester);
				controlDescriptors.add(controlDescription);
			} catch (ClassNotFoundException e1) {
				Activator.logException(e1);
			} catch (CoreException e1) {
				Activator.logException(e1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			//TODO externalize strings
			throw new ClassNotFoundException(clazz + " cannot be loaded because bundle " + bundleName //$NON-NLS-1$
				+ " cannot be resolved"); //$NON-NLS-1$
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	/**
	 * {@inheritDoc}
	 */
	public <T extends ECPControl> T createControl(Class<T> controlType, IItemPropertyDescriptor itemPropertyDescriptor,
		ECPControlContext context) {

		ECPControlDescription controlDescription = getControlCandidate(controlType,itemPropertyDescriptor, context.getModelElement());
		if(controlDescription==null){
			return null;
		}
		T control = getControlInstance(controlDescription,itemPropertyDescriptor,context);

		
		return control;
	}
	/**
	 * {@inheritDoc}
	 */
	public <T extends ECPControl> T createControl(IItemPropertyDescriptor itemPropertyDescriptor,
		ECPControlContext context, String controlId) {
		
		ECPControlDescription controlDescription = null;
		for(ECPControlDescription desc:controlDescriptors){
			if(desc.getId().equals(controlId)){
				controlDescription=desc;
				break;
			}
		}
		if(controlDescription==null){
			return null;
		}
		T control = getControlInstance(controlDescription,itemPropertyDescriptor,context);

		
		return control;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<ECPControlDescription> getControlDescriptors() {
		return new HashSet<ECPControlDescription>(controlDescriptors);
	}

	@SuppressWarnings("unchecked")
	private static <T extends ECPControl> T getControlInstance(ECPControlDescription controlDescription,
		IItemPropertyDescriptor itemPropertyDescriptor,  ECPControlContext modelElementContext) {
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(modelElementContext.getModelElement());
		try {
			Constructor<? extends ECPAbstractControl> controlConstructor = controlDescription.getControlClass().getConstructor(boolean.class,
				IItemPropertyDescriptor.class, EStructuralFeature.class, ECPControlContext.class,boolean.class);
			return (T) controlConstructor.newInstance(controlDescription.isShowLabel(),itemPropertyDescriptor, feature, modelElementContext,false);
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

	private ECPControlDescription getControlCandidate(Class<?> controlClass,IItemPropertyDescriptor itemPropertyDescriptor,
		EObject modelElement) {
		int highestPriority = -1;
		ECPControlDescription bestCandidate = null;
		for (ECPControlDescription description : controlDescriptors) {
			
			if(!controlClass.isAssignableFrom(description.getControlClass())){
				continue;
			}
			int currentPriority=-1;
			
			for(ECPApplicableTester tester:description.getTester()){
				int testerPriority=tester.isApplicable(itemPropertyDescriptor, modelElement);
				if(testerPriority>currentPriority){
					currentPriority=testerPriority;
				}
				
			}
			
			if(currentPriority>highestPriority){
				highestPriority=currentPriority;
				bestCandidate=description;
			}
		}
		return bestCandidate;
	}

	
	

}

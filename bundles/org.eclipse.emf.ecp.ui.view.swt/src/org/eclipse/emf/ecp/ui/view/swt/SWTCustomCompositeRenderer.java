package org.eclipse.emf.ecp.ui.view.swt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.framework.Bundle;

public class SWTCustomCompositeRenderer extends AbstractSWTRenderer<CustomComposite> {

	@Override
	public Control render(Node<CustomComposite> node,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		CustomComposite customComposite = node.getRenderable();
		
		// TODO: handle exceptions
		try {
			Class<?> clazz = getClass(customComposite.getBundle(), customComposite.getClassName());
			Constructor<?> constructor = clazz.getConstructor(Composite.class, EObject.class);
			Object obj = constructor.newInstance(getParent(), controlContext.getModelElement());
			Composite categoryComposite = (Composite) obj;
						
			node.lift(withSWT(categoryComposite));
			
			GridDataFactory.fillDefaults()
				.grab(true, true)
				.align(SWT.FILL, SWT.FILL)
				.applyTo(categoryComposite);
		
			return categoryComposite;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Class<?> getClass(String pluginID, String className) throws ClassNotFoundException {
		Bundle bundle = Platform.getBundle(pluginID);
		if (bundle == null) {
			throw new ClassNotFoundException(className
					+ " cannot be loaded because because bundle " + pluginID
					+ " cannot be resolved");
		} else {
			return bundle.loadClass(className);
		}
	}


}

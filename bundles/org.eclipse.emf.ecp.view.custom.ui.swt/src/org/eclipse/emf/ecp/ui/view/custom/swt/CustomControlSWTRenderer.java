package org.eclipse.emf.ecp.ui.view.custom.swt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.framework.Bundle;

public class CustomControlSWTRenderer extends
		AbstractSWTRenderer<CustomControl> {

	@Override
	protected Control renderSWT(Node<CustomControl> node,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator,Object...initData)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		CustomControl customControl = node.getRenderable();

		// TODO: handle exceptions
		try {
			Class<?> clazz = getClass(customControl.getBundle(),
					customControl.getClassName());
			Constructor<?> constructor = clazz.getConstructor();
			Object obj = constructor.newInstance();
			ECPAbstractCustomControlSWT categoryComposite = (ECPAbstractCustomControlSWT) obj;
			categoryComposite.init(node.getControlContext());
			
			Composite composite = categoryComposite.createControl(getParentFromInitData(initData));
			node.addRenderingResultDelegator(new SWTRenderingResultCustomControl(categoryComposite,customControl,composite));

			GridDataFactory.fillDefaults().grab(true, true)
					.align(SWT.FILL, SWT.FILL).applyTo(composite);

			return composite;
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

	private Class<?> getClass(String pluginID, String className)
			throws ClassNotFoundException {
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

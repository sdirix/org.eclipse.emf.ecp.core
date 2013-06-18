package org.eclipse.emf.ecp.internal.ui.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class ViewProviderHelper {

	private static Set<IViewProvider> viewProviders=new HashSet<IViewProvider>();

	public static Set<IViewProvider> getViewProviders() {
		if (viewProviders == null || viewProviders.isEmpty())
			readViewProviders();
		return viewProviders;
	}

	private static void readViewProviders() {
		IConfigurationElement[] controls = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						"org.eclipse.emf.ecp.ui.view.viewModelProviders");
		for (IConfigurationElement e : controls) {
			try {
				String clazz = e.getAttribute("class");
				Class<? extends IViewProvider> resolvedClass = loadClass(e
						.getContributor().getName(), clazz);
				Constructor<? extends IViewProvider> controlConstructor = resolvedClass
						.getConstructor();
				IViewProvider viewProvider = controlConstructor.newInstance();
				viewProviders.add(viewProvider);
			} catch (ClassNotFoundException ex) {
				Activator.log(ex);
			} catch (NoSuchMethodException ex) {
				Activator.log(ex);
			} catch (SecurityException ex) {
				Activator.log(ex);
			} catch (InstantiationException ex) {
				Activator.log(ex);
			} catch (IllegalAccessException ex) {
				Activator.log(ex);
			} catch (IllegalArgumentException ex) {
				Activator.log(ex);
			} catch (InvocationTargetException ex) {
				Activator.log(ex);
			}

		}

	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz)
			throws ClassNotFoundException {
		Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			// TODO externalize strings
			throw new ClassNotFoundException(clazz
					+ " cannot be loaded because bundle " + bundleName //$NON-NLS-1$
					+ " cannot be resolved"); //$NON-NLS-1$
		}
		return (Class<T>) bundle.loadClass(clazz);

	}
}

package org.eclipse.emf.ecp.view.model.internal.fx;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.Node;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.view.model.VElement;
import org.osgi.framework.Bundle;
@SuppressWarnings("restriction")
//TODO no api
public final class RendererFactoryImpl implements RendererFactory {

	private RendererFactoryImpl() {
		readRenderer();
	}

	private void readRenderer() {
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint("org.eclipse.emf.ecp.view.model.renderer");
		for (final IExtension extension : extensionPoint.getExtensions()) {

			for (IConfigurationElement configurationElement : extension
					.getConfigurationElements()) {
				try {
					@SuppressWarnings("unchecked")
					final RendererFX<VElement> renderer = (RendererFX<VElement>) configurationElement
							.createExecutableExtension("renderer");
					String clazz = configurationElement
							.getAttribute("renderable");

					final Class<VElement> renderable = loadClass(extension
							.getContributor().getName(), clazz);

					rendererMapping.put(renderable, renderer);
				} catch (final CoreException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InvalidRegistryObjectException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz)
			throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + bundleName);
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	private static RendererFactoryImpl INSTANCE;

	public static RendererFactory getInstance() {
		if (INSTANCE == null)
			INSTANCE = new RendererFactoryImpl();
		return INSTANCE;
	}

	private Map<Class<VElement>, RendererFX<VElement>> rendererMapping = new LinkedHashMap<>();

	@Override
	public <T extends VElement> Node render(T renderable) {
		return rendererMapping.get(renderable.getClass().getInterfaces()[0])
				.render(renderable);
	}

}

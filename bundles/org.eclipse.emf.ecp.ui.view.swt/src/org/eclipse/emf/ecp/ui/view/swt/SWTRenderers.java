package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.internal.ui.view.renderer.TreeRendererNodeVisitor;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;

public final class SWTRenderers implements SWTRenderer<org.eclipse.emf.ecp.view.model.Composite> {

	public static final SWTRenderers INSTANCE = new SWTRenderers();

    @SuppressWarnings("rawtypes")
	private Map<Class<? extends org.eclipse.emf.ecp.view.model.Composite>, SWTRenderer> renderers;

	private Composite currentParent;
	private boolean renderersInitialized;
	
	public SWTRenderers() {
	}
	
	@SuppressWarnings("rawtypes")
	private void initRenderers() {
		renderers = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Composite>, SWTRenderer>();
		renderers.put(ColumnComposite.class, new SWTColumnCompositeRenderer());
		renderers.put(Column.class, new SWTColumnRenderer());
		renderers.put(Group.class, new SWTGroupRenderer());
		renderers.put(TableControl.class, new SWTTableControlRenderer());
		renderers.put(Control.class, new SWTControlRenderer());
		renderers.put(Seperator.class, new SWTSeparatorRenderer());
		renderers.put(CustomComposite.class, new SWTCustomCompositeRenderer());
		for (CustomSWTRenderer customRenderer : getCustomRenderers()) {
			for (Map.Entry<Class<? extends org.eclipse.emf.ecp.view.model.Composite>, SWTRenderer<?>> renderEntry :
				customRenderer.getCustomRenderers().entrySet()) {
				renderers.put(renderEntry.getKey(), renderEntry.getValue());
			}
		}
	}
	
	public RendererNode<org.eclipse.swt.widgets.Control> render(
			Composite parent,
            final org.eclipse.emf.ecp.view.model.Composite model, 
            ECPControlContext controlContext, 
            AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		currentParent = parent;
		return render(model, controlContext, adapterFactoryItemDelegator);
	}
		
	public RendererNode<org.eclipse.swt.widgets.Control> render(
            final org.eclipse.emf.ecp.view.model.Composite model, 
            ECPControlContext controlContext, 
            AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		if (!renderersInitialized) {
			initRenderers();
		}
		
		Class c = null;
		for (Class cls : renderers.keySet()) {
			Class<?>[] interfaces = model.getClass().getInterfaces();
			int indexOf = Arrays.asList(interfaces).indexOf(cls);
			if (indexOf != -1) {
				c = interfaces[indexOf];
				break;
			}

		}

		if (c != null) {
			@SuppressWarnings("rawtypes") 
			SWTRenderer swtRenderer = renderers.get(c);
			swtRenderer.initialize(new Object[] {currentParent});
			@SuppressWarnings("unchecked")
			RendererNode<org.eclipse.swt.widgets.Control> node = swtRenderer.render(
					model, controlContext, adapterFactoryItemDelegator);
			
			if (node == null) {
				return null;
			}
			
			node.execute(new TreeRendererNodeVisitor<org.eclipse.swt.widgets.Control>() {
				@Override
				public void executeOnNode(RendererNode<org.eclipse.swt.widgets.Control> node) {
					org.eclipse.swt.widgets.Control renderedResult = node.getRenderedResult();
					renderedResult.setBackground(currentParent.getBackground());
				}
			});
			return node;
		}
		
		return null;
//		throw new NoRendererFoundException();
    }

	@Override
	public void initialize(Object[] initData) {
		currentParent = (Composite) initData[0];
		
	}

	public Set<CustomSWTRenderer> getCustomRenderers() {
		Set<CustomSWTRenderer> renderers = new LinkedHashSet<CustomSWTRenderer>();
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
				"org.eclipse.emf.ecp.ui.view.swt.customSWTRenderers");
		for (IExtension extension : extensionPoint.getExtensions()) {
			IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				CustomSWTRenderer renderer = (CustomSWTRenderer) configurationElement.createExecutableExtension("class");
				renderers.add(renderer);
			} catch (CoreException ex) {
				Activator.log(ex);
			}
		}
		
		return renderers;
	}
}

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
import org.eclipse.emf.ecp.internal.ui.view.renderer.ControlRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.WithRenderedObject;
import org.eclipse.emf.ecp.internal.ui.view.renderer.TreeRendererNodeVisitor;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;

public final class SWTRenderers  implements SWTRenderer {

	public static final SWTRenderers INSTANCE = new SWTRenderers();

    @SuppressWarnings("rawtypes")
	private Map<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, SWTRenderer> renderers;

	private Composite currentParent;
	private boolean renderersInitialized;
	
	public SWTRenderers() {
	}
	
	@SuppressWarnings({ "rawtypes", "serial" })
	private void initRenderers() {
		
		renderers = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, SWTRenderer>() {{
			put(ColumnComposite.class, new SWTColumnCompositeRenderer());
			put(Column.class, new SWTColumnRenderer());
			put(Group.class, new SWTGroupRenderer());
			put(TableControl.class, new SWTTableControlRenderer());
			put(Control.class, new SWTControlRenderer());
			put(Seperator.class, new SWTSeparatorRenderer());
			put(CustomComposite.class, new SWTCustomCompositeRenderer());
			put(View.class, new SWTViewRenderer());
			put(Category.class, new SWTCategoryRenderer());
		}};
		
		for (CustomSWTRenderer customRenderer : getCustomRenderers()) {
			for (Map.Entry<Class<? extends Renderable>, SWTRenderer<?>> renderEntry :
				customRenderer.getCustomRenderers().entrySet()) {
				renderers.put(renderEntry.getKey(), renderEntry.getValue());
			}
		}
		
		renderersInitialized = true;
	}
	
//	public SWTLifted render(
//			Composite parent,
//            final org.eclipse.emf.ecp.view.model.Renderable renderable, 
//            ECPControlContext controlContext, 
//            AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
//		currentParent = parent;
//		return render(renderable, controlContext, adapterFactoryItemDelegator);
//	}
//		
//	public SWTLifted render(
//            final org.eclipse.emf.ecp.view.model.Renderable renderable, 
//            ECPControlContext controlContext, 
//            AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
//		
//		if (!renderersInitialized) {
//			initRenderers();
//		}
//		
//		Class c = null;
//		for (Class cls : renderers.keySet()) {
//			Class<?>[] interfaces = renderable.getClass().getInterfaces();
//			int indexOf = Arrays.asList(interfaces).indexOf(cls);
//			if (indexOf != -1) {
//				c = interfaces[indexOf];
//				break;
//			}
//
//		}
//
//		if (c != null) {
//			@SuppressWarnings("rawtypes") 
//			SWTRenderer swtRenderer = renderers.get(c);
//			swtRenderer.initialize(new Object[] {currentParent});
//			@SuppressWarnings("unchecked")
//			SWTLifted node = (SWTLifted) swtRenderer.render(
//					renderable, controlContext, adapterFactoryItemDelegator);
//			return node;
//		}
//		
//		throw new NoRendererFoundException("No renderer found for renderable " + renderable);
//    }

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
	
	@Override
	public org.eclipse.swt.widgets.Control render(Node node,
			ECPControlContext controlContext,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		if (!renderersInitialized) {
			initRenderers();
		}
		
		Class c = null;
		for (Class cls : renderers.keySet()) {
			Class<?>[] interfaces = node.getRenderable().getClass().getInterfaces();
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
			Object render = swtRenderer.render(node, controlContext, adapterFactoryItemDelegator);
			// TODO:? ?
			return (org.eclipse.swt.widgets.Control) render;
		}
		
		throw new NoRendererFoundException("No renderer found for renderable " + node.getRenderable());
	}
	
	public org.eclipse.swt.widgets.Control render(Composite parent, Node node,
			ECPControlContext controlContext,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		currentParent = parent;
		return render(node, controlContext, adapterFactoryItemDelegator);		
	}
}

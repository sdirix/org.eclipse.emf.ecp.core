package org.eclipse.emf.ecp.internal.ui.view.builders;

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
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
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
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

public class NodeBuilders implements NodeBuilder {

	public static final NodeBuilders INSTANCE = new NodeBuilders();

    @SuppressWarnings("rawtypes")
	private Map<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, NodeBuilder<? extends Renderable>> builders;
    private boolean buildersInitialized;
	
	private NodeBuilders() {
	}
	
	@SuppressWarnings({ "serial" })
	private void initBuilders() {
		
		builders = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, NodeBuilder<? extends Renderable>>() {{
			put(ColumnComposite.class, new CompositeCollectionNodeBuilder<ColumnComposite>());
			put(Column.class, new CompositeCollectionNodeBuilder<Column>());
			put(Group.class, new CompositeCollectionNodeBuilder<Group>());
			put(TableControl.class, new RenderableNodeBuilder<TableControl>());
			put(Control.class, new RenderableNodeBuilder<Control>());
			put(Seperator.class, new RenderableNodeBuilder<Seperator>());
			put(CustomComposite.class, new RenderableNodeBuilder<CustomComposite>());
			put(View.class, new ViewNodeBuilder());
			put(Category.class, new CategoryNodeBuilder());
			put(Categorization.class, new CategorizationNodeBuilder());
			put(TableControl.class, new ControlNodeBuilder<TableControl>());
			put(Control.class, new ControlNodeBuilder<Control>());
			put(TreeCategory.class, new TreeCategoryNodeBuilder());


		}};
		
		for (CustomNodeBuilder customBuilder : getCustomNodeBuilders()) {
			for (Map.Entry<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> entry :
				customBuilder.getCustomNodeBuilders().entrySet()) {
				builders.put(entry.getKey(), entry.getValue());
			}
		}
		
		buildersInitialized = true;
	}
	
	public Set<CustomNodeBuilder> getCustomNodeBuilders() {
		Set<CustomNodeBuilder> builders = new LinkedHashSet<CustomNodeBuilder>();
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
				"org.eclipse.emf.ecp.ui.view.customNodeBuilders");
		for (IExtension extension : extensionPoint.getExtensions()) {
			IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				CustomNodeBuilder renderer = (CustomNodeBuilder) configurationElement.createExecutableExtension("class");
				builders.add(renderer);
			} catch (CoreException ex) {
				Activator.log(ex);
			}
		}
		
		return builders;
	}
	
	public Node build(Renderable renderable, ECPControlContext controlContext) {
		
		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
    			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
		
		Node build = build(renderable, controlContext, adapterFactoryItemDelegator);
		composedAdapterFactory.dispose();
		return build;
	}

	public Node build(Renderable renderable, ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		if (!buildersInitialized) {
			initBuilders();
		}
		
		Class c = null;
		for (Class cls : builders.keySet()) {
			Class<?>[] interfaces = renderable.getClass().getInterfaces();
			int indexOf = Arrays.asList(interfaces).indexOf(cls);
			if (indexOf != -1) {
				c = interfaces[indexOf];
				break;
			}

		}

		if (c != null) {
			@SuppressWarnings("rawtypes") 
			NodeBuilder builder = builders.get(c);
			return builder.build(renderable, controlContext, adapterFactoryItemDelegator);
		}
		
		return null;
	}
}

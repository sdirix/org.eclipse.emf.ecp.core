package org.eclipse.emf.ecp.internal.ui.view.renderer.swt;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTTreeRenderNode;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTColumnCompositeRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTColumnRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTControlRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTCustomCompositeRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTGroupRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTSeparatorRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTTableControlRenderer;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public final class SWTRendererRegistry implements SWTRenderer<org.eclipse.emf.ecp.view.model.Composite> {

	public static final SWTRendererRegistry INSTANCE = new SWTRendererRegistry();

	// TODO: may be use EClass as key type
    private Map<Class<? extends org.eclipse.emf.ecp.view.model.Composite>, SWTRenderer> renderers;
	
	private SWTRendererRegistry() {
		initRenderers();
	}
	
	private void initRenderers() {
		renderers = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Composite>, SWTRenderer>();
		renderers.put(ColumnComposite.class, new SWTColumnCompositeRenderer());
		renderers.put(Column.class, new SWTColumnRenderer());
		renderers.put(Group.class, new SWTGroupRenderer());
		renderers.put(TableControl.class, new SWTTableControlRenderer());
		renderers.put(Control.class, new SWTControlRenderer());
		renderers.put(Seperator.class, new SWTSeparatorRenderer());
		renderers.put(CustomComposite.class, new SWTCustomCompositeRenderer());
	}

	public SWTTreeRenderNode render(final Composite parent,
            final org.eclipse.emf.ecp.view.model.Composite model, 
            ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
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
			SWTRenderer swtRenderer = renderers.get(c);//.render(parent, model, controlContext);
			return swtRenderer.render(parent, model, controlContext, adapterFactoryItemDelegator);
		}
		
		return null;
		
//		throw new NoRendererFoundException();
    }

}

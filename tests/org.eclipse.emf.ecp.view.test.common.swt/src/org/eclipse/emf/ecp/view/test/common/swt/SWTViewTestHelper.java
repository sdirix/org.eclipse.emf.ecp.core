package org.eclipse.emf.ecp.view.test.common.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderers;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTViewTestHelper {

	public static Shell createShell() {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		return shell;
	}

	public static Control render(Renderable renderable, EObject input, Shell shell) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final ECPControlContext context = ViewTestHelper.createECPControlContext(
			input, shell);
		final Node<Renderable> node = NodeBuilders.INSTANCE.build(renderable, context);
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);
		final Control control = SWTRenderers.INSTANCE.render(shell, node,
			adapterFactoryItemDelegator);
		return control;

	}

	/**
	 * @param view
	 * @param shell
	 * @return
	 * @throws NoPropertyDescriptorFoundExeption
	 * @throws NoRendererFoundException
	 */
	public static Control render(Renderable renderable, Shell shell) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		return render(renderable, ViewFactory.eINSTANCE.createView(), shell);
	}

}

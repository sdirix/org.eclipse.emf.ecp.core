package org.eclipse.emf.ecp.view.groupedgrid.ui.swt.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.model.Alignment;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SWTControlRenderer extends AbstractSWTRenderer<Control> {
	public static final SWTControlRenderer INSTANCE = new SWTControlRenderer();
	private static final int IDENT = 10;

	@Override
	public List<RenderingResultRow<org.eclipse.swt.widgets.Control>> renderSWT(Node<Control> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Control modelControl = node.getRenderable();
		final EClass dataClass = modelControl.getTargetFeature().getEContainingClass();
		final ECPControlContext subContext = node.getControlContext();

		if (dataClass == null) {
			return null;
		}

		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
			.getPropertyDescriptor(subContext.getModelElement(),
				modelControl.getTargetFeature());

		if (itemPropertyDescriptor == null) {
			throw new NoPropertyDescriptorFoundExeption(subContext.getModelElement(), modelControl.getTargetFeature());
		}

		final ECPControlFactory controlFactory = Activator.getDefault().getECPControlFactory();

		if (controlFactory == null) {
			Activator.getDefault().ungetECPControlFactory();
			return null;
		}

		final SWTControl control = controlFactory.createControl(SWTControl.class, itemPropertyDescriptor,
			subContext);

		if (control != null) {
			Composite parent = getParentFromInitData(initData);
			int numControl = 2;
			Label label = null;
			if (modelControl.getLabelAlignment() == Alignment.TOP) {
				final Composite newParent = new Composite(parent, SWT.NONE);
				newParent.setBackground(parent.getBackground());
				parent = newParent;
				GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).applyTo(parent);
			}
			if (modelControl.getLabelAlignment() == Alignment.LEFT || modelControl.getLabelAlignment() == Alignment.TOP) {
				numControl = 1;
				label = new Label(parent, SWT.NONE);
				label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label");
				label.setBackground(parent.getBackground());
				String extra = "";
				if (((EStructuralFeature) itemPropertyDescriptor.getFeature(null)).getLowerBound() > 0) {
					extra = "*";
				}
				label.setText(itemPropertyDescriptor.getDisplayName(subContext.getModelElement())
					+ extra);
				label.setToolTipText(itemPropertyDescriptor.getDescription(subContext.getModelElement()));
				GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);

				GridDataFactory.createFrom((GridData) label.getLayoutData()).indent(IDENT, 0).applyTo(label);
			}

			final Composite controlComposite = control.createControl(parent);
			controlComposite.setEnabled(!modelControl.isReadonly());
			controlComposite.setBackground(parent.getBackground());

			if (label == null) {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, controlComposite));
			} else {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, controlComposite, label));
			}

			if (!node.isVisible()) {
				node.show(false);
			}

			GridDataFactory.fillDefaults().grab(true, false)
				.align(SWT.FILL, SWT.CENTER).span(numControl, 1)
				.applyTo(controlComposite);

			final List<RenderingResultRow<org.eclipse.swt.widgets.Control>> result = new ArrayList<RenderingResultRow<org.eclipse.swt.widgets.Control>>();

			if (modelControl.getLabelAlignment() == Alignment.TOP) {
				return createResult(parent);
			}
			else if (label == null) {
				return createResult(controlComposite);
			}
			return createResult(label, controlComposite);
		}

		Activator.getDefault().ungetECPControlFactory();

		return null;
	}
}

package org.eclipse.emf.ecp.view.model.internal.fx;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.Label;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.internal.fx.ECPControlFactoryFX;
import org.eclipse.emf.ecp.edit.spi.ECPControlFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.renderer.RenderingResultRow;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

@SuppressWarnings("restriction")
// TODO no api
public class ControlRendererFX extends RendererFX<VControl> {

	@Override
	public Set<RenderingResultRow<Node>> render(VControl renderable,
			final ViewModelContext viewContext) {
		ECPControlFactory factory = Activator.getContext().getService(
				Activator.getContext().getServiceReference(
						ECPControlFactory.class));

		final Setting setting = renderable.getDomainModelReference()
				.getIterator().next();

		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
				composedAdapterFactory);

		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
				.getPropertyDescriptor(setting.getEObject(),
						setting.getEStructuralFeature());

		if (itemPropertyDescriptor == null)
			return Collections.emptySet();

		ECPControlFactoryFX createControl = factory
				.createControl(ECPControlFactoryFX.class,
						renderable.getDomainModelReference());
		if (createControl == null)
			return Collections.emptySet();
		final Set<RenderingResultRow<Node>> controlsRows = createControl
				.render(renderable);

		Label label = null;
		if (renderable.getLabelAlignment() == LabelAlignment.LEFT) {
			label = new Label();
			label.setText(itemPropertyDescriptor.getDisplayName(setting
					.getEObject()));

		}

		if (controlsRows.size() != 1)
			return controlsRows;

		final Set<Node> result = new LinkedHashSet<>();
		if (label != null)
			result.add(label);
		result.addAll(controlsRows.iterator().next().getControls());
		RenderingResultRow<Node> controlsRow = new RenderingResultRow<Node>() {

			@Override
			public Set<Node> getControls() {
				return result;
			}

			@Override
			public Node getMainControl() {
				return controlsRows.iterator().next().getMainControl();
			}
		};
		return Collections.singleton(controlsRow);
	}

}

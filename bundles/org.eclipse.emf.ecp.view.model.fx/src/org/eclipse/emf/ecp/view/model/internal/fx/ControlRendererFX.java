package org.eclipse.emf.ecp.view.model.internal.fx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.internal.fx.ECPFXControl;
import org.eclipse.emf.ecp.edit.spi.ECPControlFactory;
import org.eclipse.emf.ecp.view.model.LabelAlignment;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

@SuppressWarnings("restriction")
// TODO no api
public class ControlRendererFX implements RendererFX<VControl> {

	@Override
	public Node render(VControl renderable) {
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
			return new Label("No ItemPropertyProvider could be found!");

		ECPFXControl createControl = factory.createControl(ECPFXControl.class,
				renderable.getDomainModelReference());
		if(createControl==null)
			return new Label("No fitting Control could be found!");
		createControl.init(null, renderable.getDomainModelReference());

		HBox box = new HBox();
		if (renderable.getLabelAlignment() == LabelAlignment.LEFT) {
			Label label = new Label();
			label.setText(itemPropertyDescriptor.getDisplayName(setting
					.getEObject()));
			box.getChildren().add(label);
		}

		Node controlNode = createControl.render();
		box.getChildren().add(controlNode);
		return box;
	}

}

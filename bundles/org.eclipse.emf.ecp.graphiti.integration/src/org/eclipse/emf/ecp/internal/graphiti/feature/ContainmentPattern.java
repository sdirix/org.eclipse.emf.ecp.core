package org.eclipse.emf.ecp.internal.graphiti.feature;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractConnectionPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

public class ContainmentPattern extends AbstractConnectionPattern {
	private static final IColorConstant FOREGROUND = new ColorConstant(98, 131,
			167);

	@Override
	public PictogramElement add(IAddContext context) {
		IAddConnectionContext addConContext = (IAddConnectionContext) context;

		IPeCreateService peCreateService = Graphiti.getPeCreateService();

		// CONNECTION WITH POLYLINE
		Connection connection = peCreateService
				.createFreeFormConnection(getDiagram());

		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());

		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPolyline(connection);
		polyline.setLineWidth(2);
		polyline.setForeground(manageColor(FOREGROUND));

		ConnectionDecorator cd = peCreateService.createConnectionDecorator(
				connection, false, 1.0, true);
		createArrow(cd);

		return connection;
	}

	private Polyline createArrow(GraphicsAlgorithmContainer gaContainer) {
		IGaService gaService = Graphiti.getGaService();
		Polygon polygon=gaService.createPolygon(gaContainer, new int[] {-10,-10,
				-20, 0, -10, 10, 0, 0 });
		polygon.setBackground(manageColor(FOREGROUND));
		polygon.setFilled(true);
		polygon.setLineVisible(false);
		return polygon;
	}

	public boolean canAdd(IAddContext context) {
		// return true if given business object is an EReference
		// note, that the context must be an instance of IAddConnectionContext
		if(!IAddConnectionContext.class.isInstance(context))
			return false;
		IAddConnectionContext connectionContext=(IAddConnectionContext)context;
		EObject sourceObject=(EObject) getFeatureProvider().getBusinessObjectForPictogramElement((PictogramElement) connectionContext.getSourceAnchor().eContainer());
		EObject targetObject=(EObject) getFeatureProvider().getBusinessObjectForPictogramElement((PictogramElement) connectionContext.getTargetAnchor().eContainer());
		return targetObject.eContainer()==sourceObject;
		
	}
}

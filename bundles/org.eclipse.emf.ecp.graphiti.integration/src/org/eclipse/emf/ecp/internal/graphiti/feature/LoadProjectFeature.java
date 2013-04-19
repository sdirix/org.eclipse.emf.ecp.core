package org.eclipse.emf.ecp.internal.graphiti.feature;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class LoadProjectFeature extends AbstractAddFeature {

	private ECPProject project;
	private static final int DEFAULT_OFFSET_X = 20;
	private static final int DEFAULT_OFFSET_Y = 20;

	public LoadProjectFeature(IFeatureProvider fp, ECPProject project) {
		super(fp);
		this.project = project;
	}

	public boolean canAdd(IAddContext context) {
		return true;
	}

	public PictogramElement add(IAddContext context) {
		Diagram createDiagram = (Diagram) context.getTargetContainer();
		// Object object=getBusinessObjectForPictogramElement(createDiagram);

		int currentXOffset = DEFAULT_OFFSET_X;
		for (Object rootObject : project.getElements()) {
			if (!EObject.class.isInstance(rootObject)) {
				continue;
			}
			int maxWidth = createEObjectAddContext(createDiagram,
					(EObject) rootObject, currentXOffset, 0);
			int nextXOffset = ContainerSizes.eObjectWidth + DEFAULT_OFFSET_X;
			if (maxWidth > nextXOffset)
				currentXOffset += maxWidth + DEFAULT_OFFSET_X;
			else
				currentXOffset += nextXOffset;
		}

		for (Object rootObject : project.getElements()) {
			if (!EObject.class.isInstance(rootObject))
				continue;
			EObject eObject = (EObject) rootObject;
			for (EObject crossReference : eObject.eCrossReferences()) {
				ContainerShape shapeFrom = (ContainerShape) getFeatureProvider()
						.getPictogramElementForBusinessObject(eObject);
				ContainerShape shapeTo = (ContainerShape) getFeatureProvider()
						.getPictogramElementForBusinessObject(crossReference);
				AddConnectionContext referenceConnection = new AddConnectionContext(
						shapeFrom.getAnchors().get(0), shapeTo.getAnchors()
								.get(0));
				getFeatureProvider().addIfPossible(referenceConnection);
			}
			createCrossReferences(eObject.eContents());
		}

		return null;
	}

	private ContainerShape getContainerShape(EObject eObject) {
		PictogramElement[] fromShapes = getFeatureProvider()
				.getAllPictogramElementsForBusinessObject(eObject);
		ContainerShape shapeFrom = null;
		for (int i = 0; i < fromShapes.length; i ++) {
			if (fromShapes[i].getLink().getBusinessObjects().get(0) == eObject) {
				shapeFrom = (ContainerShape) fromShapes[i];
				break;
			}
		}
		return shapeFrom;
	}

	private void createCrossReferences(EList<EObject> elements) {
		for (EObject eObject : elements) {
			for (EObject crossReference : eObject.eCrossReferences()) {

				ContainerShape shapeFrom = getContainerShape(eObject);
				ContainerShape shapeTo = getContainerShape(crossReference);
				AddConnectionContext referenceConnection = new AddConnectionContext(
						shapeFrom.getAnchors().get(0), shapeTo.getAnchors()
								.get(0));
				getFeatureProvider().addIfPossible(referenceConnection);
			}
			createCrossReferences(eObject.eContents());
		}
	}

	private int createEObjectAddContext(Diagram createDiagram,
			EObject rootObject, int widthOffset, int heightOffset) {
		AddContext addContext = new AddContext();
		addContext.setNewObject(rootObject);
		addContext.setTargetContainer(createDiagram);
		int currentXOffset = widthOffset;
		addContext.setLocation(currentXOffset, DEFAULT_OFFSET_Y + heightOffset);
		getFeatureProvider().addIfPossible(addContext);
		int maxWidth = rootObject.eContents().size()
				* (DEFAULT_OFFSET_X + ContainerSizes.eObjectWidth);
		if (maxWidth == 0) {
			maxWidth = ContainerSizes.eObjectWidth;
		}
		
		for (EObject eObejct : rootObject.eContents()) {
			int thisMaxWidth = createEObjectAddContext(createDiagram, eObejct,
					currentXOffset, DEFAULT_OFFSET_Y + heightOffset
							+ ContainerSizes.eObjectHeight);
			int nextXOffset = ContainerSizes.eObjectWidth + DEFAULT_OFFSET_X;
			if (thisMaxWidth > nextXOffset) {
				maxWidth = thisMaxWidth;
				currentXOffset += maxWidth + DEFAULT_OFFSET_X;
			} else
				currentXOffset += nextXOffset;

			ContainerShape shapeFrom = getContainerShape(rootObject);
			ContainerShape shapeTo = getContainerShape(eObejct);
			if (shapeTo != null) {
				AddConnectionContext containmentConnection = new AddConnectionContext(
						shapeFrom.getAnchors().get(0), shapeTo.getAnchors()
								.get(0));
				getFeatureProvider().addIfPossible(containmentConnection);
			}
		}
		return maxWidth;
	}

}

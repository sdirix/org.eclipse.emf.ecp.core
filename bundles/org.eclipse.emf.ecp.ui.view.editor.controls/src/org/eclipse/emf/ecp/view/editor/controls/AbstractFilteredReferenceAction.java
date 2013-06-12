package org.eclipse.emf.ecp.view.editor.controls;

import java.net.URL;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.OverlayImageDescriptor;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractFilteredReferenceAction extends Action {
    protected Shell shell;

    public AbstractFilteredReferenceAction(EReference eReference,
            IItemPropertyDescriptor descriptor, Shell shell) {
        this.shell = shell;
        Object obj = null;
        if (!eReference.getEReferenceType().isAbstract()) {
            obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance()
                    .create(eReference.getEReferenceType());
        }
        IItemLabelProvider labelProvider = descriptor.getLabelProvider(obj);
        Object labelProviderImageResult = labelProvider.getImage(obj);
        if (ComposedImage.class.isInstance(labelProviderImageResult)) {
            labelProviderImageResult = ((ComposedImage) labelProviderImageResult).getImages()
                    .get(0);
        }

        Image image = Activator.getImage(obj == null ? null : (URL) labelProviderImageResult);
        String overlayString = "icons/link_overlay.png";
        if (eReference.isContainment()) {
            overlayString = "icons/containment_overlay.png";
        }
        ImageDescriptor addOverlay = Activator.getImageDescriptor(overlayString);
        OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
                OverlayImageDescriptor.LOWER_RIGHT);
        setImageDescriptor(imageDescriptor);

        String attribute = descriptor.getDisplayName(eReference);
        // make singular attribute labels
        if (attribute.endsWith("ies")) {
            attribute = attribute.substring(0, attribute.length() - 3) + "y";
        } else if (attribute.endsWith("s")) {
            attribute = attribute.substring(0, attribute.length() - 1);
        }

        setToolTipText("Link " + attribute);

    }
}

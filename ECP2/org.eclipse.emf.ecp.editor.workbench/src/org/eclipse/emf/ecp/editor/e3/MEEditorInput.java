/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.e3;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.editor.internal.e3.MEEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * The {@link IEditorInput} for the {@link MEEditor}.
 * 
 * @author helming
 * @author shterev
 * @author naughton
 */
public class MEEditorInput implements IEditorInput {

	private EStructuralFeature problemFeature;
//	private DecoratingLabelProvider labelProvider;
	private EditModelElementContext modelElementContext;
//	private ComposedAdapterFactory composedAdapterFactory;
//	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;
//	private IDecoratorManager decoratorManager;

	/**
	 * Constructor to add a probleFeature.
	 * 
	 * @param context context of the model element
	 * @param problemFeature the problem feature
	 */
	public MEEditorInput(EditModelElementContext context, EStructuralFeature problemFeature) {
		this(context);
		this.problemFeature = problemFeature;
	}

	/**
	 * Default constructor.
	 * 
	 * @param context context of the modelelement
	 */
	public MEEditorInput(EditModelElementContext context) {
		super();
		this.modelElementContext = context;
		
//		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
//		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
//			composedAdapterFactory);
//		decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
//		labelProvider = new DecoratingLabelProvider(adapterFactoryLabelProvider, decoratorManager.getLabelDecorator());
//	
//		if (labelProvider.getLabelProvider().getText(modelElementContext.getModelElement()) == null) {
//			final Shell activeShell = Display.getCurrent().getActiveShell();
//			boolean doSetName = MessageDialog
//				.openQuestion(
//					activeShell,
//					"Missing title",
//					"The element you are trying to open does not have a proper name and cannot be opened.\nDo you want to set a custom name for it or use a default one?");
//			String newName = "new " + modelElementContext.getModelElement().eClass().getName();
//			if (doSetName) {
//				final InputDialog inputDialog = new InputDialog(activeShell, "New title",
//					"Please enter the new name for this element", newName, null);
//				inputDialog.setBlockOnOpen(true);
//				if (inputDialog.open() == IDialogConstants.OK_ID && inputDialog.getValue() != "") {
//					newName = inputDialog.getValue();
//				}
//
//			}
//		}
	}
//	/**
//	 * Getter for the label provider.
//	 * 
//	 * @return the label provider
//	 */
//	public DecoratingLabelProvider getLabelProvider() {
//		return labelProvider;
//	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	public ImageDescriptor getImageDescriptor() {
//		Image image = labelProvider.getImage(modelElementContext.getModelElement());
//		if (image != null) {
//			ImageDescriptor descriptor = ImageDescriptor.createFromImage(image);
//			return descriptor;
//		}
//		return null;
//	}

//	/**
//	 * {@inheritDoc}
//	 */
//	public String getName() {
//		return labelProvider.getLabelProvider().getText(modelElementContext.getModelElement());
//	}

	/**
	 * {@inheritDoc}
	 */
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	public String getToolTipText() {
//		return getName();
//	}

	/**
	 * @return the problemFeature
	 */
	public EStructuralFeature getProblemFeature() {
		return problemFeature;
	}

	/**
	 * @param problemFeature the problemFeature to set
	 */
	public void setProblemFeature(EStructuralFeature problemFeature) {
		this.problemFeature = problemFeature;
	}

	/**
	 * Custom equals() for this class.
	 * 
	 * @param obj the compared object.
	 * @return the boolean state. {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MEEditorInput) {
			MEEditorInput other = (MEEditorInput) obj;
			boolean ret = modelElementContext.getModelElement().equals(other.modelElementContext.getModelElement());
			return ret;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class clazz) {

		if (clazz.equals(EObject.class)) {
			return modelElementContext.getModelElement();
		}
		return null;
	}

	/**
	 * Returns the {@link ECPModelelemenContext}.
	 * 
	 * @return {@link EditModelElementContext}
	 */
	public EditModelElementContext getModelElementContext() {
		return modelElementContext;
	}

	
	public void dispose(){
//		labelProvider.dispose();
//		adapterFactoryLabelProvider.dispose();
//		composedAdapterFactory.dispose();
//		decoratorManager.dispose();
		modelElementContext.dispose();
		modelElementContext=null;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return modelElementContext.getModelElement().eClass().getName();
	}

	public String getToolTipText() {
		return getName();
	}

//	public ComposedAdapterFactory getComposedAdapterFactory() {
//		return composedAdapterFactory;
//	}
}

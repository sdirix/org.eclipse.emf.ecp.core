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
package org.eclipse.emf.ecp.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.EditModelElementContextListener;
import org.eclipse.emf.ecp.editor.input.MEEditorInput;
import org.eclipse.emf.ecp.ui.util.ShortLabelProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;

/**
 * GUI view for editing MEs.
 * 
 * @author helming
 * @author naughton
 */
public class MEEditor extends SharedHeaderFormEditor {

	/**
	 * The Id for MEEditor. We need this to open a model element.
	 */
	public static final String ID = "org.eclipse.emf.ecp.editor";

	private MEEditorPage mePage;

	private ILabelProviderListener labelProviderListener;

	private StatusMessageProvider statusMessageProvider;

	private ModelElementChangeListener modelElementChangeListener;

	private EditModelElementContext modelElementContext;

	private EditModelElementContextListener modelElementContextListener;

	private MEEditorInput meInput;

	private ShortLabelProvider shortLabelProvider;


	private ComposedAdapterFactory composedAdapterFactory;

	/**
	 * Default constructor.
	 */
	public MEEditor() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addPages() {
		String editorID = "Edit";
		String editorDesc = "Standard View";
		MEEditorInput editorInput = (MEEditorInput) getEditorInput();

		// add pages from the extension point
		IConfigurationElement[] configTemp = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.ecp.editor.pages");
		IConfigurationElement[] configIn = null;

		boolean replaceMEEditor = false;
		int counter = 0;

		for (int i = 0; i < configTemp.length; i++) {
			if (configTemp[i].getAttribute("replace") != null && configTemp[i].getAttribute("replace").equals(editorID)) {
				// if a replacement is found, create this page, so it becomes the first one
				replaceMEEditor = true;
				AbstractMEEditorPage newPage;

				try {
					newPage = (AbstractMEEditorPage) configTemp[i].createExecutableExtension("class");
					FormPage createPage = newPage.createPage(this, modelElementContext);
					if (createPage != null) {
						addPage(createPage);
					}
				} catch (CoreException e1) {
					Activator.logException(e1);
				}

				// put remaining pages into the original configIn array
				configIn = new IConfigurationElement[configTemp.length - 1];
				for (int j = 0, k = 0; j < configTemp.length - 1; j++, k++) {
					if (counter == j) {
						j--;
					} else {
						configIn[j] = configTemp[k];
					}
				}

				break;
			}
			counter++;
		}

		// create original MEEditor standard view if no replacement exists
		// and put remaining pages into the original configIn array
		if (!replaceMEEditor) {
			try {
				if (editorInput.getProblemFeature() != null) {
					mePage = new MEEditorPage(this, editorID, editorDesc, modelElementContext,
						modelElementContext.getModelElement(), editorInput.getProblemFeature());
				} else {
					mePage = new MEEditorPage(this, editorID, editorDesc, modelElementContext,
						modelElementContext.getModelElement());
				}

				addPage(mePage);
				configIn = configTemp;
			} catch (PartInitException e) {
				// JH Auto-generated catch block
				Activator.logException(e);
			}
		}

		// Sort the pages by the "after" attribute and omit replaced pages
		List<IConfigurationElement> config = PageCandidate.getPages(configIn);
		for (IConfigurationElement e : config) {
			try {
				AbstractMEEditorPage newPage = (AbstractMEEditorPage) e.createExecutableExtension("class");
				FormPage createPage = newPage.createPage(this, modelElementContext);
				if (createPage != null) {
					addPage(createPage);
				}
			} catch (CoreException e1) {
				Activator.logException(e1);
			}
		}

		// commentsPage = new METhreadPage(this, "Discussion", "Discussion", editingDomain, modelElement);
		// descriptionPage = new MEDescriptionPage(this, "Description", "Description", editingDomain, modelElement);
		// addPage(descriptionPage);
		// addPage(commentsPage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		modelElementContext.save();
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doSaveAs() {
	}

	/**
	 * Save is not allowed as the editor can only modify model elements.
	 * 
	 * @return false
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IEditorSite site, final IEditorInput input) throws PartInitException {
		super.init(site, input);
		if (input instanceof MEEditorInput) {
			setInput(input);
			meInput = (MEEditorInput) input;
			
			this.modelElementContext = meInput.getModelElementContext();
			composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			shortLabelProvider=new ShortLabelProvider(composedAdapterFactory);
			setPartName(shortLabelProvider.getText(modelElementContext.getModelElement()));
				setTitleImage(shortLabelProvider.getImage(modelElementContext.getModelElement()));
			

			modelElementContextListener = new EditModelElementContextListener() {

				public void onModelElementDeleted(EObject deleted) {
					if (modelElementContext.getModelElement().equals(deleted)) {
						close(false);
					} else {
						if (!modelElementContext.contains(modelElementContext.getModelElement())) {
							close(false);
						}
					}

				}

				public void onContextDeleted() {
					onModelElementDeleted(modelElementContext.getModelElement());

				}
			};
			modelElementContext.addModelElementContextListener(modelElementContextListener);
			modelElementChangeListener = new ModelElementChangeListener(modelElementContext.getModelElement()) {

				@Override
				public void onChange(Notification notification) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							updateIcon();
							setPartName(shortLabelProvider.getText(modelElementContext.getModelElement()));
							if (mePage != null) {
								mePage.updateSectionTitle();
								// mePage.updateLiveValidation();
							}
							updateStatusMessage();
						}
					});

				}
			};

			initStatusProvider();
			updateStatusMessage();

//			labelProviderListener = new ILabelProviderListener() {
//				public void labelProviderChanged(LabelProviderChangedEvent event) {
//					if(!titleImage.isDisposed())
//						titleImage.dispose();
//					titleImage=meInput.getImageDescriptor().createImage();
//					updateIcon();
//				}
//			};
//			meInput.getLabelProvider().addListener(labelProviderListener);

		} else {
			throw new PartInitException("MEEditor is only appliable for MEEditorInputs");
		}
	}

	private void initStatusProvider() {
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.ecp.editor.statusmessage");
		ArrayList<IConfigurationElement> provider = new ArrayList<IConfigurationElement>();
		provider.addAll(Arrays.asList(configurationElements));
		int priority = 0;
		for (IConfigurationElement e : provider) {
			try {
				StatusMessageProvider statusMessageProvider = (StatusMessageProvider) e
					.createExecutableExtension("class");
				int newpriority = statusMessageProvider.canRender(modelElementContext.getModelElement());
				if (newpriority > priority) {
					priority = newpriority;
					this.statusMessageProvider = statusMessageProvider;
				}
			} catch (CoreException e1) {
				Activator.logException(e1);
			}
		}
	}

	private void updateStatusMessage() {
		if (statusMessageProvider != null) {
			getEditorSite().getActionBars().getStatusLineManager()
				.setMessage(statusMessageProvider.getMessage(modelElementContext.getModelElement()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDirty() {
		return modelElementContext.isDirty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {

		super.setFocus();
		if (mePage != null) {
			mePage.setFocus();
		}
		updateStatusMessage();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		modelElementChangeListener.remove();
		modelElementContext.removeModelElementContextListener(modelElementContextListener);
		modelElementContext.dispose();
		meInput.dispose();
//		((MEEditorInput) getEditorInput()).getLabelProvider().removeListener(labelProviderListener);
		composedAdapterFactory.dispose();
		shortLabelProvider.dispose();
//		meInput.dispose();
		getSite().setSelectionProvider(null);
		super.dispose();
		
	}

	private void updateIcon() {
		
		setTitleImage(shortLabelProvider.getImage(modelElementContext.getModelElement()));
		// TODO AS: Debug why sometimes the page is null - not disposed Adapter?
		if (mePage != null) {
			try {
				mePage.getManagedForm().getForm().setImage(shortLabelProvider.getImage(modelElementContext.getModelElement()));
			} catch (SWTException e) {
				// Catch in case Editor is directly closed after change.
			}
		}
	}
}

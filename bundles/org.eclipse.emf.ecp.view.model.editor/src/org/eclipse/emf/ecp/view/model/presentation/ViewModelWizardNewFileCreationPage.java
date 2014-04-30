/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.presentation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * @author Alexandra Buzila
 * 
 */
/**
 * This is the first page of the wizard. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ViewModelWizardNewFileCreationPage extends
	WizardNewFileCreationPage {

	/**
	 * The supported extensions for created files. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<String> FILE_EXTENSIONS = Collections
		.unmodifiableList(Arrays.asList(ViewEditorPlugin.INSTANCE
			.getString("_UI_ViewEditorFilenameExtensions").split( //$NON-NLS-1$
				"\\s*,\\s*"))); //$NON-NLS-1$

	/**
	 * A formatted list of supported file extensions, suitable for display. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String FORMATTED_FILE_EXTENSIONS = ViewEditorPlugin.INSTANCE
		.getString("_UI_ViewEditorFilenameExtensions").replaceAll( //$NON-NLS-1$
			"\\s*,\\s*", ", "); //$NON-NLS-1$ //$NON-NLS-2$

	private String eClassName;
	private final IStructuredSelection selection;

	/**
	 * Pass in the selection. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ViewModelWizardNewFileCreationPage(String pageId,
		IStructuredSelection selection) {
		super(pageId, selection);
		this.selection = selection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {

			// Try and get the resource selection to determine a current directory
			// for the file dialog.
			if (selection != null && !selection.isEmpty()) {
				// Get the resource...
				final Object selectedElement = selection.iterator().next();
				if (selectedElement instanceof IResource) {
					// Get the resource parent, if its a file.
					IResource selectedResource = (IResource) selectedElement;
					if (selectedResource.getType() == IResource.FILE) {
						selectedResource = selectedResource.getParent();
					}

					// This gives us a directory...
					if (selectedResource instanceof IFolder
						|| selectedResource instanceof IProject) {
						// Set this for the container.
						setContainerFullPath(selectedResource
							.getFullPath());
					}
					// Make up a unique new name here.
					final String defaultModelBaseFilename = eClassName;
					final String defaultModelFilenameExtension = FILE_EXTENSIONS
						.get(0);
					String modelFilename = defaultModelBaseFilename + "." //$NON-NLS-1$
						+ defaultModelFilenameExtension;
					for (int i = 1; ((IContainer) selectedResource)
						.findMember(modelFilename) != null; ++i) {
						modelFilename = defaultModelBaseFilename + i + "." //$NON-NLS-1$
							+ defaultModelFilenameExtension;
					}
					setFileName(modelFilename);
					setPageComplete(true);
					// getWizard().canFinish();
				}
			}
		}
		else {
			setPageComplete(false);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
	}

	/**
	 * @param name
	 */
	public void setSelectedEClassName(String name) {
		eClassName = name;
	}

	/**
	 * The framework calls this to see if the file is correct. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected boolean validatePage() {
		if (super.validatePage()) {
			final String extension = new Path(getFileName()).getFileExtension();
			if (extension == null || !FILE_EXTENSIONS.contains(extension)) {
				final String key = FILE_EXTENSIONS.size() > 1 ? "_WARN_FilenameExtensions" //$NON-NLS-1$
					: "_WARN_FilenameExtension"; //$NON-NLS-1$
				setErrorMessage(ViewEditorPlugin.INSTANCE.getString(key,
					new Object[] { FORMATTED_FILE_EXTENSIONS }));
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IFile getModelFile() {
		return ResourcesPlugin.getWorkspace().getRoot()
			.getFile(getContainerFullPath().append(getFileName()));
	}
}

// /**
// * This is the page where the type of object to create is selected. <!--
// * begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// public class ViewModelWizardInitialObjectCreationPage extends WizardPage {
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// protected Combo initialObjectField;
//
// /**
// * @generated <!-- begin-user-doc --> <!-- end-user-doc -->
// */
// protected List<String> encodings;
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// protected Combo encodingField;
//
// /**
// * Pass in the selection. <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// public ViewModelWizardInitialObjectCreationPage(String pageId) {
// super(pageId);
// }
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// @Override
// public void createControl(Composite parent) {
// final Composite composite = new Composite(parent, SWT.NONE);
// {
// final GridLayout layout = new GridLayout();
// layout.numColumns = 1;
// layout.verticalSpacing = 12;
// composite.setLayout(layout);
//
// final GridData data = new GridData();
// data.verticalAlignment = GridData.FILL;
// data.grabExcessVerticalSpace = true;
// data.horizontalAlignment = GridData.FILL;
// composite.setLayoutData(data);
// }
//
// final Label containerLabel = new Label(composite, SWT.LEFT);
// {
// containerLabel.setText(ViewEditorPlugin.INSTANCE
//					.getString("_UI_ModelObject")); //$NON-NLS-1$
//
// final GridData data = new GridData();
// data.horizontalAlignment = GridData.FILL;
// containerLabel.setLayoutData(data);
// }
//
// initialObjectField = new Combo(composite, SWT.BORDER);
// {
// final GridData data = new GridData();
// data.horizontalAlignment = GridData.FILL;
// data.grabExcessHorizontalSpace = true;
// initialObjectField.setLayoutData(data);
// }
//
// for (final String objectName : getInitialObjectNames()) {
// initialObjectField.add(getLabel(objectName));
// }
//
// if (initialObjectField.getItemCount() == 1) {
// initialObjectField.select(0);
// }
// initialObjectField.addModifyListener(validator);
//
// final Label encodingLabel = new Label(composite, SWT.LEFT);
// {
// encodingLabel.setText(ViewEditorPlugin.INSTANCE
//					.getString("_UI_XMLEncoding")); //$NON-NLS-1$
//
// final GridData data = new GridData();
// data.horizontalAlignment = GridData.FILL;
// encodingLabel.setLayoutData(data);
// }
// encodingField = new Combo(composite, SWT.BORDER);
// {
// final GridData data = new GridData();
// data.horizontalAlignment = GridData.FILL;
// data.grabExcessHorizontalSpace = true;
// encodingField.setLayoutData(data);
// }
//
// for (final String encoding : getEncodings()) {
// encodingField.add(encoding);
// }
//
// encodingField.select(0);
// encodingField.addModifyListener(validator);
//
// setPageComplete(validatePage());
// setControl(composite);
// }
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// protected ModifyListener validator = new ModifyListener() {
// @Override
// public void modifyText(ModifyEvent e) {
// setPageComplete(validatePage());
// }
// };
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// protected boolean validatePage() {
// return getInitialObjectName() != null
// && getEncodings().contains(encodingField.getText());
// }
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// @Override
// public void setVisible(boolean visible) {
// super.setVisible(visible);
// if (visible) {
// if (initialObjectField.getItemCount() == 1) {
// initialObjectField.clearSelection();
// encodingField.setFocus();
// } else {
// encodingField.clearSelection();
// initialObjectField.setFocus();
// }
// }
// }
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// public String getInitialObjectName() {
// final String label = initialObjectField.getText();
//
// for (final String name : getInitialObjectNames()) {
// if (getLabel(name).equals(label)) {
// return name;
// }
// }
// return null;
// }
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// public String getEncoding() {
// return encodingField.getText();
// }
//
// /**
// * Returns the label for the specified type name. <!-- begin-user-doc
// * --> <!-- end-user-doc -->
// *
// * @generated
// */
// protected String getLabel(String typeName) {
// try {
//				return ViewEditPlugin.INSTANCE.getString("_UI_" + typeName //$NON-NLS-1$
//					+ "_type"); //$NON-NLS-1$
// } catch (final MissingResourceException mre) {
// ViewEditorPlugin.INSTANCE.log(mre);
// }
// return typeName;
// }
//
// /**
// * <!-- begin-user-doc --> <!-- end-user-doc -->
// *
// * @generated
// */
// protected Collection<String> getEncodings() {
// if (encodings == null) {
// encodings = new ArrayList<String>();
// for (final StringTokenizer stringTokenizer = new StringTokenizer(
// ViewEditorPlugin.INSTANCE
//						.getString("_UI_XMLEncodingChoices")); stringTokenizer //$NON-NLS-1$
// .hasMoreTokens();) {
// encodings.add(stringTokenizer.nextToken());
// }
// }
// return encodings;
// }
// }

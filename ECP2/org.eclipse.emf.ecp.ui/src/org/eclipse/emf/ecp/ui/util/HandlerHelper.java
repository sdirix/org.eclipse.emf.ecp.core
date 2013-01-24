/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.ui.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPCloseable;
import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.ui.Messages;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.ui.composites.AddRepositoryComposite;
import org.eclipse.emf.ecp.ui.composites.CheckedSelectModelClassComposite;
import org.eclipse.emf.ecp.ui.composites.CheckoutProjectComposite;
import org.eclipse.emf.ecp.ui.composites.CreateProjectComposite;
import org.eclipse.emf.ecp.ui.composites.SelectModelClassComposite;
import org.eclipse.emf.ecp.ui.composites.SelectModelElementComposite;
import org.eclipse.emf.ecp.ui.dialogs.DeleteDialog;
import org.eclipse.emf.ecp.wizards.AddRepositoryWizard;
import org.eclipse.emf.ecp.wizards.CheckoutProjectWizard;
import org.eclipse.emf.ecp.wizards.CreateProjectWizard;
import org.eclipse.emf.ecp.wizards.FilterModelElementWizard;
import org.eclipse.emf.ecp.wizards.NewModelElementWizard;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a utility class providing commonly necessary methods.
 * 
 * @author Eugen Neufeld
 */
public final class HandlerHelper {
	private HandlerHelper() {
	}

	private static void showError(Shell shell, String title, String message) {
		MessageDialog.openError(shell, title, message);
	}

	/**
	 * This method allows to checkout a list of {@link ECPCheckoutSource} into the workspace.
	 * 
	 * @param checkoutObjects the List of {@link ECPCheckoutSource} to checkout
	 * @param shell the {@link Shell} to use for diplaying UI
	 */
	public static void checkout(final List<ECPCheckoutSource> checkoutObjects, final Shell shell) {
		for (ECPCheckoutSource checkoutSource : checkoutObjects) {
			CheckoutProjectComposite checkoutCompposite = new CheckoutProjectComposite(checkoutSource);
			CheckoutProjectWizard wizard = new CheckoutProjectWizard();
			wizard.setCompositeProvider(checkoutCompposite);

			WizardDialog wd = new WizardDialog(shell, wizard);

			int result = wd.open();
			if (result == WizardDialog.OK) {

				String projectName = checkoutCompposite.getProjectName();
				ECPProperties projectProperties = checkoutCompposite.getProjectProperties();
				try {
					checkoutSource.checkout(projectName, projectProperties);
				} catch (ProjectWithNameExistsException ex) {
					showError(shell, "Cannot checkout project", "A project with name " + projectName
						+ " already exists in the workspace.");
				}
			}
		}
	}

	/**
	 * This helper method is used to delete model elements from a project.
	 * 
	 * @param project the project to delete from
	 * @param eObjects the model elements to delete
	 */
	public static void deleteModelElement(final ECPProject project, final Collection<EObject> eObjects) {
		if (project != null) {
			project.delete(eObjects);
		}
	}

	/**
	 * This method creates a new project.
	 * 
	 * @param shell the shell for displaying the wizard
	 * @return the created {@link ECPProject}
	 */
	public static ECPProject createProject(final Shell shell) {
		List<ECPProvider> providers = new ArrayList<ECPProvider>();
		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
			if (provider.hasUnsharedProjectSupport()) {
				providers.add(provider);
			}
		}
		if (providers.size() == 0) {
			showError(shell, "No Provider", "Please check if a suitable provider is installed.");
			return null;
		}
		CreateProjectComposite createProjectComposite = new CreateProjectComposite(providers);
		CreateProjectWizard wizard = new CreateProjectWizard();
		wizard.setCompositeProvider(createProjectComposite);

		WizardDialog wd = new WizardDialog(shell, wizard);

		int result = wd.open();
		if (result == WizardDialog.OK) {
			ECPProvider selectedProvider = createProjectComposite.getProvider();
			if (selectedProvider == null) {
				showError(shell, "No project created", "Please check if a suitable provider is installed.");
				return null;
			}
			ECPProperties projectProperties = ECPUtil.createProperties();

			String projectName = createProjectComposite.getProjectName();
			ECPProject project = null;
			try {
				project = ECPProjectManager.INSTANCE.createProject(selectedProvider, projectName, projectProperties);
			} catch (ProjectWithNameExistsException ex) {
				showError(shell, "No project created", "A project with name " + projectName
					+ " already exists in the workspace.");
				return null;
			}
			if (project == null) {
				showError(shell, "No project created", "Please check the log.");
				return null;
			}
			((InternalProvider) selectedProvider).handleLifecycle(project, LifecycleEvent.CREATE);
			project.open();
			return project;
		}
		return null;
	}

	// TODO never used
	private static void createNewReferenceElement(final EditingDomain editingDomain, final EObject eObject,
		final EReference eReference, final Object input, final Shell shell) {

		SelectModelElementComposite composite = new SelectModelElementComposite(input);
		NewModelElementWizard wizard = new NewModelElementWizard("");
		wizard.setCompositeProvider(composite);
		WizardDialog wd = new WizardDialog(shell, wizard);

		int wizardResult = wd.open();
		if (wizardResult == WizardDialog.OK) {
			Object[] results = composite.getSelection();
			if (eReference.isMany()) {
				List<EObject> list = new ArrayList<EObject>();
				for (Object result : results) {
					if (result instanceof EObject) {
						list.add((EObject) result);
					}
				}
				editingDomain.getCommandStack().execute(new AddCommand(editingDomain, eObject, eReference, list));
			} else {
				if (results.length > 0 && results[0] instanceof EObject) {
					editingDomain.getCommandStack().execute(
						new SetCommand(editingDomain, eObject, eReference, results[0]));
				}
			}
		}
		composite.dispose();
	}

	/**
	 * Add a new {@link EObject} to the root of an {@link ECPProject}.
	 * 
	 * @param ecpProject the {@link ECPProject} to add the {@link EObject} to
	 * @param shell the {@link Shell} used to display the UI
	 * @param open whether to open the corresponding editor or not
	 */
	public static void addModelElement(final ECPProject ecpProject, final Shell shell, boolean open) {
		SelectModelClassComposite helper = new SelectModelClassComposite(ecpProject);
		NewModelElementWizard wizard = new NewModelElementWizard(Messages.NewModelElementWizardHandler_Title);
		wizard.setCompositeProvider(helper);
		WizardDialog wd = new WizardDialog(shell, wizard);

		int wizardResult = wd.open();
		if (wizardResult == WizardDialog.OK) {
			Object[] selection = helper.getSelection();
			if (selection == null || selection.length == 0) {
				return;
			}
			EClass newMEType = (EClass) selection[0];
			// TODO find childdescriptor

			if (ecpProject != null && newMEType != null) {
				// 1.create ME
				EPackage ePackage = newMEType.getEPackage();
				final EObject newMEInstance = ePackage.getEFactoryInstance().create(newMEType);
				ecpProject.getEditingDomain().getCommandStack().execute(new ChangeCommand(newMEInstance) {

					@Override
					protected void doExecute() {
						ecpProject.getElements().add(newMEInstance);
					}
				});
				if (open) {
					// 3.open the newly created ME
					ActionHelper.openModelElement(newMEInstance, HandlerHelper.class.getName(), ecpProject);
				}
			}
		}
	}

	/**
	 * This method allows the user to filter the visible packages and classes.
	 * 
	 * @param ecpProject the project to filter
	 * @param shell the {@link Shell} to use for UI
	 */
	public static void filterProjectPackages(final ECPProject ecpProject, final Shell shell) {
		Set<EPackage> ePackages = ECPUtil.getAllRegisteredEPackages();

		CheckedSelectModelClassComposite checkedModelComposite = new CheckedSelectModelClassComposite(ePackages,
			new HashSet<EPackage>(), ePackages, new HashSet<EClass>());
		Set<Object> initialSelectionSet = new HashSet<Object>();
		initialSelectionSet.addAll(ecpProject.getVisiblePackages());
		initialSelectionSet.addAll(ecpProject.getVisibleEClasses());
		checkedModelComposite.setInitialSelection(initialSelectionSet.toArray());

		FilterModelElementWizard wizard = new FilterModelElementWizard();
		wizard.setCompositeProvider(checkedModelComposite);
		WizardDialog wd = new WizardDialog(shell, wizard);

		int wizardResult = wd.open();
		if (wizardResult == WizardDialog.OK) {
			Object[] dialogSelection = checkedModelComposite.getChecked();
			Set<EPackage> filtererdPackages = new HashSet<EPackage>();
			Set<EClass> filtererdEClasses = new HashSet<EClass>();
			for (Object object : dialogSelection) {
				if (object instanceof EPackage) {
					filtererdPackages.add((EPackage) object);
				} else if (object instanceof EClass) {
					EClass eClass = (EClass) object;
					if (!filtererdPackages.contains(eClass.getEPackage())) {
						filtererdEClasses.add(eClass);
					}
				}
			}
			ecpProject.setVisiblePackages(filtererdPackages);
			ecpProject.setVisibleEClasses(filtererdEClasses);
		}
	}

	/**
	 * This method created a new Repository.
	 * 
	 * @param shell the shell for the Wizard
	 * @return the created {@link ECPRepository}
	 */
	public static ECPRepository createRepository(final Shell shell) {
		AddRepositoryComposite addRepositoryComposite = new AddRepositoryComposite();
		AddRepositoryWizard wizard = new AddRepositoryWizard();
		wizard.setCompositeProvider(addRepositoryComposite);
		WizardDialog wd = new WizardDialog(shell, wizard);

		int wizardResult = wd.open();
		if (wizardResult == WizardDialog.OK) {
			ECPRepository ecpRepository = ECPRepositoryManager.INSTANCE.addRepository(
				addRepositoryComposite.getProvider(), addRepositoryComposite.getRepositoryName(),
				addRepositoryComposite.getRepositoryLabel() == null ? "" : addRepositoryComposite.getRepositoryLabel(), //$NON-NLS-1$
				addRepositoryComposite.getRepositoryDescription() == null ? "" : addRepositoryComposite //$NON-NLS-1$
					.getRepositoryDescription(), addRepositoryComposite.getProperties());
			return ecpRepository;
		}
		return null;
	}

	/**
	 * This method closes/opens an array of closables.
	 * 
	 * @param closeables the {@link ECPCloseable}s to change the state for
	 * @param currentType the action to do
	 */
	public static void changeCloseState(ECPCloseable[] closeables, String currentType) {
		for (ECPCloseable closeable : closeables) {
			if ("open".equalsIgnoreCase(currentType)) { //$NON-NLS-1$
				closeable.open();
			} else if ("close".equalsIgnoreCase(currentType)) { //$NON-NLS-1$
				closeable.close();
			}
		}
	}

	/**
	 * Deletes the provided {@link ECPDeletable} elements.
	 * 
	 * @param deletables the List of {@link ECPDeletable}s to delete
	 * @param shell the shell to use for UI
	 */
	public static void deleteHandlerHelper(List<ECPDeletable> deletables, Shell shell) {

		if (!deletables.isEmpty()) {
			DeleteDialog dialog = new DeleteDialog(shell, deletables);
			if (dialog.open() == DeleteDialog.OK) {
				for (ECPDeletable deletable : deletables) {
					deletable.delete();
				}
			}
		}
	}

	/**
	 * Triggers the save on an {@link ECPProject}.
	 * 
	 * @param project the project to save the changes on
	 */
	public static void saveProject(ECPProject project) {
		project.saveModel();
	}
}

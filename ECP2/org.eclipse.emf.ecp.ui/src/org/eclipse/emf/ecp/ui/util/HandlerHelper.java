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
import org.eclipse.emf.ecore.EPackage.Registry;
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
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.ui.common.AbstractUICallback;
import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite;
import org.eclipse.emf.ecp.ui.common.CheckedSelectModelClassComposite;
import org.eclipse.emf.ecp.ui.common.CheckoutProjectComposite;
import org.eclipse.emf.ecp.ui.common.CreateProjectComposite;
import org.eclipse.emf.ecp.ui.common.SelectModelClassComposite;
import org.eclipse.emf.ecp.ui.common.SelectModelElementComposite;
import org.eclipse.emf.ecp.ui.dialogs.DeleteDialog;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eugen Neufeld
 */
public class HandlerHelper {
	public static void checkout(final List<ECPCheckoutSource> checkoutObjects, final AbstractUICallback callback) {
		for (ECPCheckoutSource checkoutSource : checkoutObjects) {
			CheckoutProjectComposite checkoutCompposite = new CheckoutProjectComposite(checkoutSource);
			callback.setCompositeUIProvider(checkoutCompposite);

			if (AbstractUICallback.OK == callback.open()) {
				String projectName = checkoutCompposite.getProjectName();
				ECPProperties projectProperties = checkoutCompposite.getProjectProperties();
				try {
					checkoutSource.checkout(projectName, projectProperties);
				} catch (ProjectWithNameExistsException ex) {
					callback.showError("Cannot checkout project", "A project with name " + projectName
						+ " already exists in the workspace.");
				}
			}
		}
	}

	public static void deleteModelElement(ECPProject project, Collection<EObject> eObjects) {
		project.delete(eObjects);
	}

	public static ECPProject createProject(final AbstractUICallback callback) {
		List<ECPProvider> providers = new ArrayList<ECPProvider>();
		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
			if (provider.hasUnsharedProjectSupport()) {
				providers.add(provider);
			}
		}
		if (providers.size() == 0) {
			callback.showError("No Provider", "Please check if a suitable provider is installed.");
			return null;
		}
		CreateProjectComposite createProjectComposite = new CreateProjectComposite(providers);
		callback.setCompositeUIProvider(createProjectComposite);

		if (AbstractUICallback.OK == callback.open()) {
			ECPProvider selectedProvider = createProjectComposite.getProvider();
			if (selectedProvider == null) {
				callback.showError("No project created", "Please check if a suitable provider is installed.");
				return null;
			}
			ECPProperties projectProperties = ECPUtil.createProperties();

			String projectName = createProjectComposite.getProjectName();
			ECPProject project = null;
			try {
				project = ECPProjectManager.INSTANCE.createProject(selectedProvider, projectName, projectProperties);
			} catch (ProjectWithNameExistsException ex) {
				callback.showError("No project created", "A project with name " + projectName
					+ " already exists in the workspace.");
				return null;
			}
			if (project == null) {
				callback.showError("No project created", "Please check the log.");
				return null;
			}
			((InternalProvider) selectedProvider).handleLifecycle(project, LifecycleEvent.CREATE);
			project.open();
			return project;
		}
		return null;
	}

	public static void createNewReferenceElement(EditingDomain editingDomain, EObject eObject, EReference eReference,
		Object input, AbstractUICallback callBack) {
		SelectModelElementComposite composite = new SelectModelElementComposite(new AdapterFactoryLabelProvider(
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE)), input);
		callBack.setCompositeUIProvider(composite);
		if (AbstractUICallback.OK == callBack.open()) {
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
	}

	/**
	 * Add a new {@link EObject} to the root of an {@link ECPProject}.
	 * 
	 * @param ecpProject the {@link ECPProject} to add the {@link EObject} to
	 * @param callback the {@link AbstractUICallback} to call for user input
	 */
	public static void addModelElement(final ECPProject ecpProject, final AbstractUICallback callback, boolean open) {
		SelectModelClassComposite helper = new SelectModelClassComposite(ecpProject);

		callback.setCompositeUIProvider(helper);

		if (AbstractUICallback.OK == callback.open()) {
			Object[] selection = helper.getSelection();
			if (selection == null || selection.length == 0) {
				return;
			}
			EClass newMEType = (EClass) selection[0];

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

	public static void filterProjectPackages(final ECPProject ecpProject, final AbstractUICallback callback) {
		Set<EPackage> ePackages = new HashSet<EPackage>();

		for (Object object : Registry.INSTANCE.values()) {
			if (object instanceof EPackage) {
				EPackage ePackage = (EPackage) object;
				ePackages.add(ePackage);
			}

		}
		CheckedSelectModelClassComposite checkedModelComposite = new CheckedSelectModelClassComposite(ePackages,
			new HashSet<EPackage>(), ePackages, new HashSet<EClass>());

		callback.setCompositeUIProvider(checkedModelComposite);

		Set<Object> initialSelectionSet = new HashSet<Object>();
		initialSelectionSet.addAll(ecpProject.getVisiblePackages());
		initialSelectionSet.addAll(ecpProject.getVisibleEClasses());
		checkedModelComposite.setInitialSelection(initialSelectionSet.toArray());

		if (AbstractUICallback.OK == callback.open()) {
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

	public static ECPRepository createRepository(AbstractUICallback callback) {
		AddRepositoryComposite addRepositoryComposite = new AddRepositoryComposite();
		callback.setCompositeUIProvider(addRepositoryComposite);
		if (AbstractUICallback.OK == callback.open()) {
			ECPRepository ecpRepository = ECPRepositoryManager.INSTANCE.addRepository(
				addRepositoryComposite.getProvider(), addRepositoryComposite.getRepositoryName(),
				addRepositoryComposite.getRepositoryLabel() == null ? "" : addRepositoryComposite.getRepositoryLabel(), //$NON-NLS-1$
				addRepositoryComposite.getRepositoryDescription() == null ? "" : addRepositoryComposite //$NON-NLS-1$
					.getRepositoryDescription(), addRepositoryComposite.getProperties());
			return ecpRepository;
		}
		return null;
	}

	public static void close(ECPCloseable[] closeables, String currentType) {
		for (ECPCloseable closeable : closeables) {
			if ("open".equalsIgnoreCase(currentType)) { //$NON-NLS-1$
				closeable.open();
			} else if ("close".equalsIgnoreCase(currentType)) { //$NON-NLS-1$
				closeable.close();
			}
		}
	}

	public static void deleteHandlerHelper(final List<ECPDeletable> deletables, final AbstractUICallback callback) {
		// TODO add DeleteComposite
		callback.setCompositeUIProvider(null);
		if (AbstractUICallback.OK == callback.open()) {
			for (ECPDeletable deletable : deletables) {
				deletable.delete();
			}
		}
	}

	public static void deleteHandlerHelper(IStructuredSelection selection, Shell shell) {
		List<ECPDeletable> deletables = new ArrayList<ECPDeletable>();
		for (Iterator<?> it = selection.iterator(); it.hasNext();) {
			Object element = it.next();
			if (element instanceof ECPDeletable) {
				deletables.add((ECPDeletable) element);
			}
		}

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
	 * @param project
	 */
	public static void saveProject(ECPProject project) {
		project.saveModel();
	}
}

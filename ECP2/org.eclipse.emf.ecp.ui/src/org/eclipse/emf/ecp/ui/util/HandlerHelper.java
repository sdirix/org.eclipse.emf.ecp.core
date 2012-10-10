/**
 * 
 */
package org.eclipse.emf.ecp.ui.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPCloseable;
import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.ui.common.AbstractUICallback;
import org.eclipse.emf.ecp.ui.common.AddRepositoryComposite;
import org.eclipse.emf.ecp.ui.common.CheckedModelElementHelper;
import org.eclipse.emf.ecp.ui.common.CheckoutProjectComposite;
import org.eclipse.emf.ecp.ui.common.SelectModelElementHelper;
import org.eclipse.emf.ecp.ui.common.UICreateProject;
import org.eclipse.emf.ecp.ui.dialogs.DeleteDialog;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
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
				checkoutSource.checkout(projectName, projectProperties);
			}
		}
	}

	public static void createProject(final AbstractUICallback callback) {
		List<ECPProvider> providers = new ArrayList<ECPProvider>();
		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
			if (provider.hasUnsharedProjectSupport()) {
				providers.add(provider);
			}
		}
		UICreateProject createProjectComposite = new UICreateProject(providers);
		callback.setCompositeUIProvider(createProjectComposite);

		if (AbstractUICallback.OK == callback.open()) {
			ECPProvider selectedProvider = createProjectComposite.getProvider();

			ECPProperties projectProperties = ECPUtil.createProperties();

			String projectName = createProjectComposite.getProjectName();
			ECPProject project = ECPProjectManager.INSTANCE.createProject(selectedProvider, projectName,
				projectProperties);

			((InternalProvider) selectedProvider).handleLifecycle(project, LifecycleEvent.CREATE);
			project.open();
		}
	}

	public static void addModelElement(final ECPProject ecpProject, final AbstractUICallback callback) {
		SelectModelElementHelper helper = new SelectModelElementHelper(ecpProject);

		callback.setCompositeUIProvider(helper);

		if (AbstractUICallback.OK == callback.open()) {
			Object[] selection = helper.getTreeSelection();
			if (selection == null || selection.length == 0) {
				return;
			}
			EClass newMEType = (EClass) selection[0];

			if (ecpProject != null && newMEType != null) {
				// 1.create ME
				EPackage ePackage = newMEType.getEPackage();
				EObject newMEInstance = ePackage.getEFactoryInstance().create(newMEType);

				ecpProject.getElements().add(newMEInstance);

				// 3.open the newly created ME
				ActionHelper.openModelElement(newMEInstance, HandlerHelper.class.getName(), ecpProject);
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
		CheckedModelElementHelper checkedModelComposite = new CheckedModelElementHelper(ePackages,
			new HashSet<EPackage>(), ePackages, new HashSet<EClass>());

		callback.setCompositeUIProvider(checkedModelComposite);

		Set<Object> initialSelectionSet = new HashSet<Object>();
		initialSelectionSet.addAll(ecpProject.getFilteredPackages());
		initialSelectionSet.addAll(ecpProject.getFilteredEClasses());
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
			ecpProject.setFilteredPackages(filtererdPackages);
			ecpProject.setFilteredEClasses(filtererdEClasses);
		}
	}

	public static void createRepository(AbstractUICallback callback) {
		AddRepositoryComposite addRepositoryComposite = new AddRepositoryComposite();
		callback.setCompositeUIProvider(addRepositoryComposite);
		if (AbstractUICallback.OK == callback.open()) {
			ECPRepositoryManager.INSTANCE.addRepository(
				addRepositoryComposite.getProvider(),
				addRepositoryComposite.getRepositoryName(),
				addRepositoryComposite.getRepositoryLabel() == null ? "" : addRepositoryComposite.getRepositoryLabel(),
				addRepositoryComposite.getRepositoryDescription() == null ? "" : addRepositoryComposite
					.getRepositoryDescription(), addRepositoryComposite.getProperties());
		}
	}

	public static void close(ECPCloseable[] closeables, String currentType) {
		for (ECPCloseable closeable : closeables) {
			if ("open".equalsIgnoreCase(currentType)) {
				closeable.open();
			} else if ("close".equalsIgnoreCase(currentType)) {
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
		project.doSave();
	}
}

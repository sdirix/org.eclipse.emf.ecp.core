package org.eclipse.emf.ecp.emfstore.internal.ui;

import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreCheckoutData;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProjectWrapper;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.ui.DefaultUIProvider;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.client.model.connectionmanager.KeyStoreManager;
import org.eclipse.emf.emfstore.client.model.exceptions.CertificateStoreException;
import org.eclipse.emf.emfstore.client.model.observers.CheckoutObserver;
import org.eclipse.emf.emfstore.client.ui.views.emfstorebrowser.views.CertificateSelectionDialog;
import org.eclipse.emf.emfstore.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;

/**
 * @author Eugen Neufeld
 */
public class EMFStoreUIProvider extends DefaultUIProvider {

	public EMFStoreUIProvider() {
		super(EMFStoreProvider.NAME);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Object adaptable, Class<T> adapterType) {
		if (EMFStoreCheckoutData.class.isInstance(adaptable) && adapterType.equals(ProjectSpace.class)) {
			EMFStoreCheckoutData checkoutData = (EMFStoreCheckoutData) adaptable;
			// final ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(PlatformUI.getWorkbench()
			// .getActiveWorkbenchWindow().getShell());
			try {
				// progressDialog.open();
				// progressDialog.getProgressMonitor().beginTask("Checkout project...", IProgressMonitor.UNKNOWN);
				ProjectSpace projectSpace = WorkspaceManager
					.getInstance()
					.getCurrentWorkspace()
					.checkout(checkoutData.getServerInfo().getLastUsersession(),
						ModelUtil.clone(checkoutData.getProjectInfo()));
				WorkspaceManager.getInstance().getCurrentWorkspace().save();
				WorkspaceManager.getObserverBus().notify(CheckoutObserver.class).checkoutDone(projectSpace);
				return (T) projectSpace;
			} catch (EmfStoreException e) {
				Activator.log(e);
				// BEGIN SUPRESS CATCH EXCEPTION
			} catch (RuntimeException e) {
				Activator.log(e);
				// END SUPRESS CATCH EXCEPTION
			} finally {
				// progressDialog.getProgressMonitor().done();
				// progressDialog.close();
			}
		}
		return super.getAdapter(adaptable, adapterType);
	}

	@Override
	public Control createCheckoutUI(Composite parent, ECPCheckoutSource checkoutSource, ECPProperties projectProperties) {
		return null;
	}

	@Override
	public Control createAddRepositoryUI(Composite parent, final ECPProperties repositoryProperties,
		final Text repositoryNameText, Text repositoryLabelText, Text repositoryDescriptionText) {

		GridLayout mainLayout = new GridLayout(3, false);
		mainLayout.marginWidth = 0;
		mainLayout.marginHeight = 0;

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(mainLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label url = new Label(composite, 0);
		url.setText("Url:");
		final Text urlText = new Text(composite, SWT.BORDER);
		urlText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		urlText.addModifyListener(new ModifyListener() {
			private String oldText = "";

			public void modifyText(ModifyEvent e) {
				if (oldText.equals(repositoryNameText.getText())) {
					oldText = urlText.getText();
					repositoryNameText.setText(oldText);
					repositoryProperties.addProperty(EMFStoreProvider.PROP_REPOSITORY_URL, oldText);
				}
			}
		});

		Label port = new Label(composite, 0);
		port.setText("Port:");
		final Text portText = new Text(composite, SWT.BORDER);
		portText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		portText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				repositoryProperties.addProperty(EMFStoreProvider.PROP_PORT, portText.getText());
			}

		});

		Label cert = new Label(composite, 0);
		cert.setText("Certificate:");
		final Text certificateText = new Text(composite, SWT.BORDER);
		certificateText.setEditable(false);
		certificateText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		Button bSelectCertificate = new Button(composite, SWT.PUSH);
		// TODO change text
		bSelectCertificate.setText("Select Certificate");
		bSelectCertificate.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				// open dialog
				String certificate = selectCertificate();
				certificateText.setText(certificate);
				repositoryProperties.addProperty(EMFStoreProvider.PROP_CERTIFICATE, certificateText.getText());

			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		urlText.setText("localhost");
		portText.setText("8080");
		certificateText.setText(KeyStoreManager.getInstance().getDefaultCertificate());
		return composite;
	}

	// @Override
	// public Control createCheckoutUI(Composite parent, final ECPCheckoutSource checkoutSource,
	// final ECPProperties projectProperties)
	// {
	// Composite composite = new Composite(parent, SWT.NONE);
	// composite.setLayout(new GridLayout(2, false));
	//
	// new Label(composite, SWT.NONE).setText("User");
	// final Text tUser = new Text(composite, SWT.SINGLE | SWT.BORDER);
	// tUser.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	//
	// new Label(composite, SWT.NONE).setText("Password");
	// final Text tPassword = new Text(composite, SWT.SINGLE | SWT.BORDER);
	// tPassword.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	//
	// Button bConnect = new Button(composite, SWT.PUSH);
	// bConnect.setText("Connect");
	// bConnect.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
	//
	// new Label(composite, SWT.NONE).setText("Select Project:");
	// final ComboViewer cvProjectInfos = new ComboViewer(composite, SWT.READ_ONLY);
	// cvProjectInfos.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	// cvProjectInfos.setLabelProvider(new LabelProvider()
	// {
	// @Override
	// public String getText(Object object)
	// {
	// if (object instanceof ProjectInfo)
	// {
	// ProjectInfo projectInfo = (ProjectInfo)object;
	// return projectInfo.getName();
	// }
	// return "";
	// }
	// });
	// cvProjectInfos.setContentProvider(new ArrayContentProvider());
	//
	// cvProjectInfos.addSelectionChangedListener(new ISelectionChangedListener()
	// {
	//
	// public void selectionChanged(SelectionChangedEvent event)
	// {
	// ProjectInfo projectInfo = (ProjectInfo)((IStructuredSelection)event.getSelection()).getFirstElement();
	// projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, projectInfo.getProjectId().getId());
	// }
	// });
	//
	// bConnect.addSelectionListener(new SelectionListener()
	// {
	//
	// public void widgetSelected(SelectionEvent e)
	// {
	// EList<ProjectInfo> projectInfos = EMFStoreProvider.INSTANCE.getAllProjects(checkoutSource.getRepository(),
	// tUser.getText(), tPassword.getText());
	// cvProjectInfos.setInput(projectInfos);
	// if (projectInfos.size() > 0)
	// {
	// cvProjectInfos.setSelection(new StructuredSelection(projectInfos.get(0)));
	// }
	// }
	//
	// public void widgetDefaultSelected(SelectionEvent e)
	// {
	// widgetSelected(e);
	// }
	// });
	//
	// tUser.setText("super");
	// tPassword.setText("super");
	// return composite;
	// }

	/**
	 * @return
	 */
	private String selectCertificate() {
		CertificateSelectionDialog csd = new CertificateSelectionDialog(Display.getCurrent().getActiveShell(),
			new LabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof String) {
						return element.toString();
					}

					return "";

				}
			});
		ArrayList<String> certificates;
		try {
			certificates = KeyStoreManager.getInstance().getCertificates();
			csd.setElements(certificates.toArray());
		} catch (CertificateStoreException e1) {
			csd.setErrorMessage(e1.getMessage());
		}
		csd.setBlockOnOpen(true);
		csd.setTitle("Certificate Selection Dialog");
		csd.open();
		if (csd.getReturnCode() == Window.OK) {
			return csd.getCertificateAlias();
		}
		return "";
	}

	@Override
	public String getText(Object element) {
		if (element instanceof EMFStoreProjectWrapper) {
			EMFStoreProjectWrapper emfStoreProjectWrapper = (EMFStoreProjectWrapper) element;
			return emfStoreProjectWrapper.getDefaultCheckoutName();
		}

		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof EMFStoreProjectWrapper) {
			return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/projectinfo.png").createImage();
		}

		return super.getImage(element);
	}

}

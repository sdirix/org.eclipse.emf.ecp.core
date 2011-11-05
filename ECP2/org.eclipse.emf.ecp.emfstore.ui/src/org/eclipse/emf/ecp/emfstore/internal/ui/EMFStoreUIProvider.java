package org.eclipse.emf.ecp.emfstore.internal.ui;

import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProjectWrapper;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.ui.DefaultUIProvider;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EMFStoreUIProvider extends DefaultUIProvider {
	public EMFStoreUIProvider() {
		super(EMFStoreProvider.NAME);
	}

	@Override
	public <T> T getAdapter(Object adaptable, Class<T> adapterType) {
		return super.getAdapter(adaptable, adapterType);
	}

	@Override
	public Control createAddRepositoryUI(Composite parent,
			final ECPProperties repositoryProperties,
			final Text repositoryNameText, Text repositoryLabelText,
			Text repositoryDescriptionText) {

		GridLayout mainLayout = new GridLayout(2, false);
		mainLayout.marginWidth = 0;
		mainLayout.marginHeight = 0;

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(mainLayout);
	    
		Label url = new Label(composite, 0);
		url.setText("Url:");
		final Text urlText = new Text(composite, SWT.BORDER);
		urlText.addModifyListener(new ModifyListener()
	    {
	      private String oldText = "";

	      public void modifyText(ModifyEvent e)
	      {
	        if (oldText.equals(repositoryNameText.getText()))
	        {
	          oldText = urlText.getText();
	          repositoryNameText.setText(oldText);
	          repositoryProperties.addProperty(EMFStoreProvider.PROP_REPOSITORY_URL, oldText);
	        }
	      }
	    });
		
		Label port = new Label(composite, 0);
		port.setText("Port:");
		final Text portText = new Text(composite, SWT.BORDER);
		
		portText.addModifyListener(new ModifyListener()
	    {
	      

	      public void modifyText(ModifyEvent e)
	      {
	          repositoryProperties.addProperty(EMFStoreProvider.PROP_PORT, portText.getText());
	        }
	      
	    });
		
		Label cert = new Label(composite, 0);
		cert.setText("Certificate:");
		Text certText = new Text(composite, SWT.BORDER);

		
		return composite;
	}
	
	 @Override
	  public String getText(Object element)
	  {
	    if (element instanceof EMFStoreProjectWrapper)
	    {
	    	EMFStoreProjectWrapper emfStoreProjectWrapper = (EMFStoreProjectWrapper) element;
	    	return emfStoreProjectWrapper.getDefaultCheckoutName();
	    }

	    return super.getText(element);
	  }

	  @Override
	  public Image getImage(Object element)
	  {
	    if (element instanceof EMFStoreProjectWrapper)
	    {
	      return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/projectinfo.png").createImage();
	    }

	    

	    return super.getImage(element);
	  }
}

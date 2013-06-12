package org.eclipse.emf.ecp.internal.ui.view;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.emf.ecp.ui.view"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    private BundleContext context;

    // private IValidationServiceProvider validationServiceProvider;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.context = context;
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Logs exception.
     * 
     * @param e
     *            the {@link Exception} to log
     */
    public static void log(Exception e) {
        getDefault().getLog().log(
                new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(), e
                        .getMessage(), e));
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        if (getDefault() == null)
            try {
                return ImageDescriptor.createFromURL(new File(path).toURI().toURL());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return ImageDescriptor.createFromURL(getDefault().getBundle().getResource(path));
    }

    private ServiceReference<ECPControlFactory> controlFactoryReference;

    public ECPControlFactory getECPControlFactory() {
        if (controlFactoryReference == null) {
            controlFactoryReference = plugin.getBundle().getBundleContext()
                    .getServiceReference(ECPControlFactory.class);
        }
        return plugin.getBundle().getBundleContext().getService(controlFactoryReference);
    }

    public void ungetECPControlFactory() {
        if (controlFactoryReference == null) {
            return;
        }
        plugin.getBundle().getBundleContext().ungetService(controlFactoryReference);
        controlFactoryReference = null;
    }

    // public IValidationService getValidationService(EObject rootObject) {
    // return getValidationServiceProvider().getValidationService(rootObject);
    // }
    //
    // private IValidationServiceProvider getValidationServiceProvider() {
    // if (validationServiceProvider == null) {
    // // Register directly with the service
    // ServiceReference<IValidationServiceProvider> reference = context
    // .getServiceReference(IValidationServiceProvider.class);
    // validationServiceProvider = (IValidationServiceProvider) context.getService(reference);
    // }
    // return validationServiceProvider;
    // }
}

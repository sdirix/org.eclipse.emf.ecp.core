/**
 * 
 */
package org.eclipse.emf.ecp.internal.edit.swt.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

// TODO: Review this class
/**
 * Label provider to shorten the getText Method.
 * 
 * @author helming
 */
public class ShortLabelProvider extends AdapterFactoryLabelProvider {

	/**
	 * Default constructor.
	 */
	public ShortLabelProvider(AdapterFactory adapterFactory) {

		super(adapterFactory);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemLabelProvider#getText(java.lang.Object)
	 * @override
	 */
	@Override
	public String getText(Object object) {
		int limit = 30;
		String name = super.getText(object);
		if (name == null) {
			name = ""; //$NON-NLS-1$
		}
		if (name.length() > limit + 5) {
			name = name.substring(0, limit).concat("[...]"); //$NON-NLS-1$
		}
		return name;
	}
}

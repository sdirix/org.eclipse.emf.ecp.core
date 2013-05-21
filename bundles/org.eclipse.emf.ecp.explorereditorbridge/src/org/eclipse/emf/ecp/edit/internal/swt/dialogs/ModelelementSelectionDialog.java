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
package org.eclipse.emf.ecp.edit.internal.swt.dialogs;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.explorereditorbridge.internal.Activator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import java.util.Collection;
import java.util.Comparator;

// TODO: Repair docu
/**
 * Abstract Dialog as pattern for all dialogs which show ModelElements for selection. A
 * 
 * @author mkagel
 */
public abstract class ModelelementSelectionDialog extends FilteredItemsSelectionDialog {

	private static final String DIALOG_SETTINGS = "STANDARD_DIALOG_SETTING"; //$NON-NLS-1$

	private static final String DIALOG_MESSAGE = "";// Messages.ModelelementSelectionDialog_DialogMessage_SearchPattern;

	private static final String DIALOG_TITLE = "";// Messages.ModelelementSelectionDialog_DialogTitle;

	private static final String DIALOG_INITIAL_PATTERN = "**"; //$NON-NLS-1$

	private ILabelProvider labelProvider;

	private Collection<EObject> modelElements;

	protected ComposedAdapterFactory composedAdapterFactory;

	protected AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	/**
	 * Constructor which calls another constructor with parameter false for multiple selection of elements.
	 */
	public ModelelementSelectionDialog(Shell shell) {
		this(false, shell);
	}

	/**
	 * Contructor which calls another constructor with project parameter and false for multiple selection of elements.
	 */
	public ModelelementSelectionDialog(Collection<EObject> modelElements, Shell shell) {
		this(modelElements, false, shell);
	}

	/**
	 * Constructor which calls another constructor with null for project paramter (modelelemnts will be set from
	 * outside)
	 * and the given multiSelection behaviour.
	 * 
	 * @param multiSelection
	 *            indicates whether dialog allows to select more than one item
	 */
	public ModelelementSelectionDialog(boolean multiSelection, Shell shell) {
		this(null, multiSelection, shell);
	}

	/**
	 * Default constructor, here will be done the main work.
	 * 
	 * @param multiSelection
	 *            indicates whether dialog allows to select more than one item
	 */
	public ModelelementSelectionDialog(Collection<EObject> modelElements, boolean multiSelection, Shell shell) {

		super(shell, multiSelection);

		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		;
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		if (modelElements != null) {
			this.modelElements = modelElements;
		}

		setLabelProvider(adapterFactoryLabelProvider);

		setListLabelProvider(getLabelProvider());
		setDetailsLabelProvider(getLabelProvider());

		setBlockOnOpen(true);
		setTitle(DIALOG_TITLE);
		setMessage(DIALOG_MESSAGE);
		this.setInitialPattern(DIALOG_INITIAL_PATTERN);
	}

	/**
	 * Sets the labelProvider.
	 * 
	 * @param labelProvider
	 *            is the labelProvider for this class
	 */
	public void setLabelProvider(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	/**
	 * Returns the LabelProvider for this class.
	 * 
	 * @return labelProvider
	 */
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * Returns the ModelElement-Collection which contains the modelelements shown in the dialog.
	 * 
	 * @return Collection of modelelements for the dialog
	 */
	public Collection<EObject> getModelElements() {
		return modelElements;
	}

	/**
	 * Set the ModelElement-Collection from outside. Necessary if the collection is not set by the dialog but coms from
	 * the caller.
	 * 
	 * @param modelElements
	 *            Collection which modelelements which will be shown in the dialog
	 */
	public void setModelElements(Collection<EObject> modelElements) {
		this.modelElements = modelElements;
	}

	/**
	 * Does nothing.
	 * 
	 * @param parent
	 *            the parent
	 * @return null
	 */
	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	/**
	 * Returns a new instance of class ModelElementFilter.
	 * 
	 * @return new instance of class ModelElementFilter
	 */
	@Override
	protected ItemsFilter createFilter() {
		return new ModelElementFilter();
	}

	/**
	 * Fills the content provider with all elements matching the items filter.
	 * 
	 * @param contentProvider
	 *            the content provider which gets added the items
	 * @param itemsFilter
	 *            the used items filter
	 * @param progressMonitor
	 *            a progress monitor stating the progress
	 */
	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
		IProgressMonitor progressMonitor) {
		// FIXME
		// progressMonitor.beginTask(Messages.ModelelementSelectionDialog_ProgressMonitor_Searching,
		// modelElements.size());
		for (EObject modelElement : modelElements) {
			contentProvider.add(modelElement, itemsFilter);
			progressMonitor.worked(1);
		}
		progressMonitor.done();
	}

	/**
	 * Gets the dialog settings.
	 * 
	 * @return the dialog settings
	 */
	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings().getSection(DIALOG_SETTINGS);
		if (settings == null) {
			settings = Activator.getDefault().getDialogSettings().addNewSection(DIALOG_SETTINGS);
		}
		return settings;
	}

	/**
	 * Gets the name of an element by asking the labelProvider.
	 * 
	 * @return the name as provided by the labelProvider
	 * @param item
	 *            the element to get the name from
	 */
	@Override
	public String getElementName(Object item) {
		if (item instanceof EObject) {
			return getLabelProvider().getText(item);
		}
		return item.toString();
	}

	/**
	 * Returns an alphabetical comparator.
	 * 
	 * @return an alphabetical comparator
	 */
	@Override
	protected Comparator<EObject> getItemsComparator() {
		return new Comparator<EObject>() {
			public int compare(EObject arg0, EObject arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
		};
	}

	/**
	 * Always returns Status.OK_STATUS.
	 * 
	 * @return Status.OK_STATUS
	 * @param item
	 *            an item
	 */
	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	/**
	 * A filter class for model elements.
	 * 
	 * @author mkagel
	 */
	public class ModelElementFilter extends ItemsFilter {

		/**
		 * @param item
		 *            the item
		 * @return true
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter#isConsistentItem(java.lang.Object)
		 */
		@Override
		public boolean isConsistentItem(Object item) {
			return true;
		}

		/**
		 * Matches ModelElement's toString Methods.
		 * 
		 * @param item
		 *            an WorkPackage
		 * @return bool
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter#matchItem(java.lang.Object)
		 */
		@Override
		public boolean matchItem(Object item) {
			String label = getLabelProvider().getText(item);

			if (!patternMatcher.getPattern().startsWith("*")) //$NON-NLS-1$
			{
				patternMatcher.setPattern("*" + patternMatcher.getPattern() + "*"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			return matches(label);
		}

	}

	@Override
	public boolean close() {
		composedAdapterFactory.dispose();
		adapterFactoryLabelProvider.dispose();
		return super.close();
	}

}

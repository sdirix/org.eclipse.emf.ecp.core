/**
 * 
 */
package org.eclipse.emf.ecp.ui.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.ui.common.CheckoutProjectComposite;
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.emf.ecp.wizards.CheckoutProjectWizard;
import org.eclipse.emf.ecp.wizards.WizardUICallback;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eugen Neufeld
 */
public class CheckoutHandler extends AbstractHandler {
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection ssel = (IStructuredSelection) selection;

		List<ECPCheckoutSource> checkouts = new ArrayList<ECPCheckoutSource>();
		for (Iterator<?> it = ssel.iterator(); it.hasNext();) {
			Object element = it.next();
			if (element instanceof ECPCheckoutSource) {
				ECPCheckoutSource checkoutSource = (ECPCheckoutSource) element;
				checkouts.add(checkoutSource);
			}
		}
		HandlerHelper.checkout(checkouts,
			new WizardUICallback<CheckoutProjectComposite>(HandlerUtil.getActiveShell(event),
				new CheckoutProjectWizard()));

		return null;
	}

}

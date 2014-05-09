/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Muenchen - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.view.editor.handler;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.editor.controls.Activator;
import org.eclipse.emf.ecp.view.editor.controls.Helper;
import org.eclipse.emf.ecp.view.spi.model.VContainer;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handler for generating controls on a {@link org.eclipse.emf.ecp.view.spi.model.VContainer VContainer} or
 * {@link org.eclipse.emf.ecp.view.spi.model.VView VView}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class GenerateControlsHandler extends MasterDetailAction {

	private final boolean enabled = true;

	private transient ListenerList listenerList;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Object selection = ((IStructuredSelection) HandlerUtil.getActiveMenuSelection(event)).getFirstElement();
		if (selection == null || !(selection instanceof EObject)) {
			return null;
		}
		final Object obj = getView((EObject) selection);
		if (obj == null) {
			return null;
		}
		final VView view = (VView) obj;
		final EClass rootClass = Helper.getRootEClass((EObject) selection);
		final SelectAttributesDialog sad = new SelectAttributesDialog(view, rootClass,
			HandlerUtil.getActiveShell(event));
		final int result = sad.open();
		if (result == Window.OK) {
			final Set<EStructuralFeature> featuresToAdd = getFeaturesToCreate(sad);
			final VElement compositeCollection = (VElement) selection;
			AdapterFactoryEditingDomain.getEditingDomainFor(compositeCollection).getCommandStack()
				.execute(new ChangeCommand(compositeCollection) {

					@Override
					protected void doExecute() {
						ControlGenerator.addControls(rootClass, compositeCollection, sad.getDataSegment(),
							featuresToAdd);
					}
				});
		}

		return null;
	}

	private Set<EStructuralFeature> getFeaturesToCreate(final SelectAttributesDialog sad) {
		final EClass datasegment = sad.getDataSegment();
		final Set<EStructuralFeature> features = sad.getSelectedFeatures();
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);
		final Set<EStructuralFeature> featuresToAdd = new HashSet<EStructuralFeature>();
		IItemPropertyDescriptor propertyDescriptor = null;
		for (final EStructuralFeature feature : features) {
			propertyDescriptor =
				adapterFactoryItemDelegator
					.getPropertyDescriptor(EcoreUtil.create(datasegment), feature);
			if (propertyDescriptor != null) {
				featuresToAdd.add(feature);
			}
			else {
				logInvalidFeature(feature.getName(), datasegment.getName());
			}
		}
		composedAdapterFactory.dispose();
		return featuresToAdd;
	}

	private void logInvalidFeature(String featureName, String eClassName) {
		final String infoMessage = "Feature " + featureName //$NON-NLS-1$
			+ " of the class " + eClassName + "could not be rendered because it has no property descriptor."; //$NON-NLS-1$ //$NON-NLS-2$
		final ILog log = Activator
			.getDefault()
			.getLog();
		log.log(
			new Status(
				IStatus.INFO,
				Activator.PLUGIN_ID, infoMessage));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction#execute(org.eclipse.emf.ecp.view.spi.model.VView)
	 */
	@Override
	public void execute(final EObject object) {
		if (!(VView.class.isInstance(object) || VContainer.class.isInstance(object))) {
			return;
		}

		final Object obj = getView(object);
		if (obj == null) {
			return;
		}

		final VView view = (VView) obj;
		final EClass rootEClass = view.getRootEClass();
		final VElement container = getContainer(object);

		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		final SelectAttributesDialog sad = new SelectAttributesDialog(view, rootEClass, shell);
		final int result = sad.open();
		if (result == Window.OK) {
			final Set<EStructuralFeature> featuresToAdd = getFeaturesToCreate(sad);
			AdapterFactoryEditingDomain.getEditingDomainFor(view).getCommandStack()
				.execute(new ChangeCommand(view) {
					@Override
					protected void doExecute() {
						ControlGenerator.addControls(rootEClass, container, sad.getDataSegment(),
							featuresToAdd);
					}
				});
		}
	}

	/**
	 * @param object
	 */
	private VView getView(EObject object) {

		if (object == null) {
			return null;
		}
		// TODO Create test cases for this method
		while (!(object instanceof VView)) {
			object = object.eContainer();
			if (object == null) {
				return null;
			}
		}
		return (VView) object;

	}

	private VElement getContainer(EObject object) {

		if (object == null) {
			return null;
		}
		// TODO Create test cases for this method
		while (!(object instanceof VView || object instanceof VContainer)) {
			object = object.eContainer();
			if (object == null) {
				return null;
			}
		}
		return (VElement) object;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction#shouldShow(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean shouldShow(EObject object) {
		if (object == null) {
			return false;
		}
		return object instanceof VView || object instanceof VContainer;
	}

	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core.commands.IHandlerListener)
	// */
	// @Override
	// public void addHandlerListener(IHandlerListener handlerListener) {
	// if (listenerList == null) {
	// listenerList = new ListenerList(ListenerList.IDENTITY);
	// }
	//
	// listenerList.add(handlerListener);
	// }
	//
	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.core.commands.IHandler#dispose()
	// */
	// @Override
	// public void dispose() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.core.commands.IHandler#isEnabled()
	// */
	// @Override
	// public boolean isEnabled() {
	// return enabled;
	// }
	//
	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.core.commands.IHandler#isHandled()
	// */
	// @Override
	// public boolean isHandled() {
	// return true;
	// }
	//
	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.core.commands.IHandlerListener)
	// */
	// @Override
	// public void removeHandlerListener(IHandlerListener handlerListener) {
	// if (listenerList != null) {
	// listenerList.remove(handlerListener);
	//
	// if (listenerList.isEmpty()) {
	// listenerList = null;
	// }
	// }
	// }
}

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
package org.eclipse.emf.ecp.ui.common.dnd;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the central drop adapter for ECP views. This class acts as a
 * dispatcher. It has a map of (EClass, MEDropAdapter) which contains a
 * reference to a specific drop adapter for each model element type.
 * 
 * @author Hodaie
 */
public class ComposedDropAdapter extends DropTargetAdapter {

	private StructuredViewer viewer;

	private List<Object> source;
	private Object target;
	private EObject targetContainer;
	private Object dropee;

	private Map<EClass, MEDropAdapter> dropAdapters;

	/**
	 * this is used for performance, so that drop method do not need to find the
	 * appropriate drop adapter again.
	 */
	private MEDropAdapter targetDropAdapter;

	/**
	 * Actually I should be able to get event feedback from event.feedback But
	 * the problem is, the event feedback is correctly set in dragOver() method,
	 * but in drop() method it is set to 1 (only selection). That's why I save
	 * event feedback at the end of dragOver() in a variable, and check this
	 * variable in drop() instead of event.feedback
	 */
	private int eventFeedback;

	private boolean targetIsRoot = false;
	private boolean sourceIsRoot = false;

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 *            viewer
	 */
	public ComposedDropAdapter(StructuredViewer viewer) {

		super();
		this.viewer = viewer;

		dropAdapters = new HashMap<EClass, MEDropAdapter>();
		IConfigurationElement[] confs = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.ecp.ui.dropadapter");
		for (IConfigurationElement element : confs) {
			try {
				MEDropAdapter dropAdapter = (MEDropAdapter) element.createExecutableExtension("class");
				dropAdapter.init(viewer);
				if (dropAdapter.isDropAdapterfor() instanceof EClass) {
					dropAdapters.put((EClass) dropAdapter.isDropAdapterfor(), dropAdapter);
				}

			} catch (CoreException e) {
				Activator.log(e.getMessage(), e);
			}
		}

	}

	/**
	 * @param event
	 *            DropTargetEvent
	 */
	@Override
	public void drop(final DropTargetEvent event) {

		if (targetIsRoot && sourceIsRoot) {
			ECPProject project = getProject(dropee);
			ECPProvider offlineProvider = null;
			for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
				if (provider.hasUnsharedProjectSupport()) {
					offlineProvider = provider;
				}
			}
			ECPProject projectCopy = ECPProjectManager.INSTANCE.createProject(offlineProvider, project.getName()
				+ " (Copy)", ECPUtil.createProperties());

			for (EObject eObject : project.getElements()) {
				projectCopy.addModelElement(EcoreUtil.copy(eObject));
			}
			project.open();

		} else if (targetIsRoot) {
			((ECPProject) target).addModelElement((EObject) dropee);
		} else {
			EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor((EObject) target);
			editingDomain.getCommandStack().execute(new ChangeCommand((EObject) target) {

				@Override
				protected void doExecute() {
					List<EObject> sourceObjects = sourceAsEObjectList();
					if (isInsertAfter(eventFeedback)) {
						targetDropAdapter.dropMove(targetContainer, (EObject) target, sourceObjects, true);
					} else if (isInsertBefore(eventFeedback)) {
						targetDropAdapter.dropMove(targetContainer, (EObject) target, sourceObjects, false);
					} else {
						targetDropAdapter.drop(event, (EObject) target, sourceObjects);
					}
				}
			});
		}

	}

	private List<EObject> sourceAsEObjectList() {
		List<EObject> sourceObjects = new ArrayList<EObject>();
		for (Object o : source) {
			sourceObjects.add((EObject) o);
		}
		return sourceObjects;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dragOver(DropTargetEvent event) {
		source = null;
		target = null;
		targetContainer = null;
		targetDropAdapter = null;
		targetIsRoot = false;
		sourceIsRoot = false;
		eventFeedback = 1;

		event.detail = DND.DROP_COPY;
		if (!extractDnDSourceAndTarget(event)) {
			event.detail = DND.DROP_NONE;
			return;
		}
		// // TODO remove this block
		// if (targetIsRoot && sourceIsRoot) {
		// event.detail = DND.DROP_NONE;
		// return;
		// }

		setInitialEventFeedback(event);
		eventFeedback = event.feedback;

		if (isInsertBefore(eventFeedback) || isInsertAfter(eventFeedback)) {
			// if (sourceIsRoot) {
			// event.detail = DND.DROP_NONE;
			// return;
			// }
			if (targetIsRoot) {
				event.detail = DND.DROP_COPY;
				return;
			}
			targetContainer = ((EObject) target).eContainer();

			targetDropAdapter = getTargetDropAdapter(targetContainer.eClass());

		} else {
			if (targetIsRoot) {
				event.detail = DND.DROP_COPY;
				return;
			}
			targetDropAdapter = getTargetDropAdapter(((EObject) target).eClass());

		}

		if (targetDropAdapter == null) {
			event.detail = DND.DROP_NONE;
		} else {
			List<EObject> sourceObjects = sourceAsEObjectList();
			boolean canDrop = targetDropAdapter.canDrop(eventFeedback, event, sourceObjects, (EObject) target,
				(EObject) dropee);
			if (!canDrop) {
				event.detail = DND.Drop;
			} else {
				event.detail = DND.DROP_COPY;
			}
		}

	}

	/**
	 * This is called continually from dragOver() event handler. This checks
	 * drop target and drop source to be not Null, and sets the target, source,
	 * and dropee fields.
	 * 
	 * @param event
	 *            DropTargetEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean extractDnDSourceAndTarget(DropTargetEvent event) {
		boolean result = true;
		source = (List<Object>) DragSourcePlaceHolder.getDragSource();
		if (source == null || source.isEmpty()) {
			result = false;
		}

		for (Object obj : source) {
			if (!(obj instanceof EObject || obj instanceof ECPProject)) {
				result = false;
			}
		}

		// source = (List<EObject>) DragSourcePlaceHolder.getDragSource();
		// if (source.size() == 0) {
		// return false;
		// }

		// take care that you cannot drop anything on project (project is not a
		// ModelElement)
		if (event.item == null || event.item.getData() == null
			|| !(event.item.getData() instanceof EObject || event.item.getData() instanceof ECPProject)) {
			result = false;
		}

		// TODO: ChainSaw - How to retrieve the ECPProject of target and dropee?
		// check if source and target are in the same project

		if (result) {
			dropee = source.get(0);
			target = event.item.getData();
			// ECPWorkspaceManager.getInstance().getWorkSpace().getProject(target);
			// dropeeProject = ECPWorkspaceManager.getInstance().getWorkSpace().getProject(dropee);

			ECPProject targetProject = getProject(target);
			ECPProject dropeeProject = getProject(dropee);
			if (targetProject == dropeeProject) {
				return false;
			}

			if (target instanceof ECPProject) {
				targetIsRoot = true;
			}
			if (dropee instanceof ECPProject) {
				sourceIsRoot = true;
			}

			if (sourceIsRoot && !targetIsRoot) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * @param target2
	 * @return
	 */
	private ECPProject getProject(Object object) {
		ECPModelContextProvider contextProvider = (ECPModelContextProvider) viewer.getContentProvider();

		InternalProject project = (InternalProject) contextProvider.getModelContext(object);
		return project;
	}

	/**
	 * This method searches drop adaptors map recursively to find the
	 * appropriate drop adapter for this model element type or one of its super
	 * types in model hierarchy.
	 * 
	 * @param targetEClass
	 * @return specific drop target for this model element type or one of its
	 *         super types in model hierarchy.
	 */
	private MEDropAdapter getTargetDropAdapter(EClass targetEClass) {

		MEDropAdapter ret = dropAdapters.get(targetEClass);
		if (ret == null) {
			EClass superTypeHavingADropAdapter = getSuperTypeHavingADropAdapter(targetEClass.getESuperTypes());
			if (superTypeHavingADropAdapter != null && superTypeHavingADropAdapter != targetEClass) {
				ret = getTargetDropAdapter(superTypeHavingADropAdapter);
			}
		}

		return ret;
	}

	/**
	 * This is used by getDropTarget() method. It takes super classes of
	 * targetEClass and tries to find a unique drop adapter that matches one of
	 * super types. If there are more than one matching drop adapters, an
	 * exception is thrown. If there is no matching drop adapter, this method
	 * searches recursively until it finds one, or throws the exception.
	 * 
	 * @param superClazz
	 *            super classes of targetEClass. If there is no match at the
	 *            first call of method, this will be a collection of super
	 *            classes of each input super class.
	 * @return an EClass that is both super class of targetEClass (directly of
	 *         indirectly) and has a drop adapter.
	 */
	private EClass getSuperTypeHavingADropAdapter(Collection<EClass> superClazz) {

		EClass ret = null;
		if (superClazz.size() == 0) {
			return EcorePackage.eINSTANCE.getEObject();
		}

		Set<EClass> intersection = new HashSet<EClass>(dropAdapters.keySet());
		intersection.retainAll(superClazz);

		// check if intersection contains many classes, but if they are in an
		// inheritance hierarchy keep only the
		// deepest class.
		// This must be discussed as a potential modeling problem.

		if (intersection.size() > 1) {
			Set<EClass> toBeRemoved = new HashSet<EClass>();
			for (EClass eClass1 : intersection) {
				for (EClass eClass2 : intersection) {
					if (!eClass2.equals(eClass1)
						&& (eClass2.isSuperTypeOf(eClass1) || eClass2.equals(EcorePackage.eINSTANCE.getEObject()))) {
						toBeRemoved.add(eClass2);
					}
				}
			}
			intersection.removeAll(toBeRemoved);
		}

		if (intersection.size() > 1) {
			throw new IllegalStateException("More than one drop adapter for this type found!");

		} else if (intersection.size() == 0) {
			Set<EClass> eclazz = new HashSet<EClass>();
			for (EClass superClass : superClazz) {
				eclazz.addAll(superClass.getESuperTypes());
			}
			ret = getSuperTypeHavingADropAdapter(eclazz);
		} else {
			ret = (EClass) intersection.toArray()[0];
		}

		return ret;
	}

	/**
	 * This sets the initial event feedback, and is also responsible for showing
	 * INSERT_AFTER and INSERT_BEFORE feedbacks according to mouse cursor
	 * position.
	 * 
	 * @param event
	 *            DropTargetEvent
	 */
	private void setInitialEventFeedback(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_SELECT;

		if (event.item != null) {
			Rectangle rect = ((TreeItem) event.item).getBounds();
			Point pt = viewer.getControl().toControl(event.x, event.y);
			if (pt.y < rect.y + 5) {
				event.feedback = DND.FEEDBACK_INSERT_BEFORE;
			}
			if (pt.y > rect.y + rect.height - 5) {
				event.feedback = DND.FEEDBACK_INSERT_AFTER;
			}

		}
		event.feedback |= DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;

	}

	/**
	 * Checks whether event feedback is a {@link DND#FEEDBACK_INSERT_BEFORE} event.
	 * 
	 * @param eventFeedback DropTarget drag under effect
	 * @return true if the event feedback is a {@value DND#FEEDBACK_INSERT_BEFORE} event, false otherwise
	 */
	public static boolean isInsertBefore(int eventFeedback) {
		return (eventFeedback & DND.FEEDBACK_INSERT_BEFORE) == DND.FEEDBACK_INSERT_BEFORE;
	}

	/**
	 * Checks whether event feedback is a {@link DND#FEEDBACK_INSERT_AFTER} event.
	 * 
	 * @param eventFeedback DropTarget drag under effect
	 * @return true if the event feedback is a {@value DND#FEEDBACK_INSERT_AFTER} event, false otherwise
	 */
	public static boolean isInsertAfter(int eventFeedback) {
		return (eventFeedback & DND.FEEDBACK_INSERT_AFTER) == DND.FEEDBACK_INSERT_AFTER;
	}
}

/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.diffmerge.internal.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.diffmerge.spi.context.ControlPair;
import org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext;
import org.eclipse.emf.ecp.view.internal.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.spi.context.ViewModelService;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelUtil;

/**
 * Implementation of the DiffMergeModelContext based on the {@link ViewModelContextImpl}.
 * As the {@link ViewModelContextImpl} is internal we suppress the restriction.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class DiffMergeModelContextImpl extends ViewModelContextImpl implements
	DiffMergeModelContext {

	private final EObject left;
	private final EObject right;
	private Map<VControl, ControlPair> controlDiffMap;
	private List<VControl> diffControls;
	private final Set<VControl> mergedControls = new LinkedHashSet<VControl>();

	/**
	 * Constructor for the {@link DiffMergeModelContextImpl}.
	 * 
	 * @param view the {@link VElement}
	 * @param domainObject the {@link EObject} which is editable
	 * @param left the first object
	 * @param right the second object
	 * @see ViewModelContextImpl#ViewModelContextImpl(VElement, EObject)
	 */
	public DiffMergeModelContextImpl(VElement view, EObject domainObject,
		EObject left, EObject right) {
		super(view, domainObject);
		this.left = left;
		this.right = right;

		initComparison();
	}

	/**
	 * Constructor for the {@link DiffMergeModelContextImpl}.
	 * 
	 * @param view the {@link VElement}
	 * @param domainObject the {@link EObject} which is editable
	 * @param left the first object
	 * @param right the second object
	 * @param mergedControls the set of controls which are already merged
	 * @see ViewModelContextImpl#ViewModelContextImpl(VElement, EObject)
	 */
	public DiffMergeModelContextImpl(VElement view, EObject domainObject,
		EObject left, EObject right, Set<VControl> mergedControls) {
		this(view, domainObject, left, right);
		diffControls.addAll(mergedControls);
	}

	/**
	 * Constructor for the {@link DiffMergeModelContextImpl}.
	 * 
	 * @param view the {@link VElement}
	 * @param domainObject the {@link EObject} which is editable
	 * @param origin1 the first object
	 * @param origin2 the second object
	 * @param modelServices the {@link ViewModelService ViewModelServices} to register
	 * @see ViewModelContextImpl#ViewModelContextImpl(VElement, EObject, ViewModelService...)
	 */
	public DiffMergeModelContextImpl(VElement view, EObject domainObject,
		EObject origin1, EObject origin2, ViewModelService... modelServices) {
		super(view, domainObject, modelServices);
		left = origin1;
		right = origin2;

		initComparison();
	}

	/**
	 * Constructor for the {@link DiffMergeModelContextImpl}.
	 * 
	 * @param view the {@link VElement}
	 * @param domainObject the {@link EObject} which is editable
	 * @param origin1 the first object
	 * @param origin2 the second object
	 * @param mergedControls the set of controls which are already merged
	 * @param modelServices the {@link ViewModelService ViewModelServices} to register
	 * @see ViewModelContextImpl#ViewModelContextImpl(VElement, EObject, ViewModelService...)
	 */
	public DiffMergeModelContextImpl(VElement view, EObject domainObject,
		EObject origin1, EObject origin2, Set<VControl> mergedControls, ViewModelService... modelServices) {
		this(view, domainObject, origin1, origin2, modelServices);
		diffControls.addAll(mergedControls);
	}

	private void initComparison() {

		final VElement viewModelLeft = EcoreUtil.copy(getViewModel());
		final VElement viewModelRight = EcoreUtil.copy(getViewModel());

		ViewModelUtil.resolveDomainReferences(viewModelLeft, getLeftModel());
		ViewModelUtil.resolveDomainReferences(viewModelRight, getRightModel());

		final TreeIterator<EObject> mainViewModel = getViewModel().eAllContents();
		final TreeIterator<EObject> leftViewModel = viewModelLeft.eAllContents();
		final TreeIterator<EObject> rightViewModel = viewModelRight.eAllContents();

		controlDiffMap = new LinkedHashMap<VControl, ControlPair>();
		diffControls = new ArrayList<VControl>();

		while (mainViewModel.hasNext()) {
			final EObject mainEObject = mainViewModel.next();
			final EObject leftEObject = leftViewModel.next();
			final EObject rightEObject = rightViewModel.next();
			if (VControl.class.isInstance(mainEObject)) {
				if (hasDiff((VControl) leftEObject, (VControl) rightEObject)) {
					controlDiffMap.put((VControl) mainEObject, new ControlPair((VControl) leftEObject,
						(VControl) rightEObject));
					diffControls.add((VControl) mainEObject);
				}
			}
		}
	}

	private boolean hasDiff(VControl leftEObject, VControl rightEObject) {
		return !CompareControls.areEqual(leftEObject, rightEObject);
	}

	/** {@inheritDoc} **/
	public EObject getLeftModel() {
		return left;
	}

	/** {@inheritDoc} **/
	public EObject getRightModel() {
		return right;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#hasDiff(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	public boolean hasDiff(VControl control) {
		return controlDiffMap.containsKey(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getPairWithDiff(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	public ControlPair getPairWithDiff(VControl control) {
		return controlDiffMap.get(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getTotalNumberOfDiffs()
	 */
	public int getTotalNumberOfDiffs() {
		return diffControls.size();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getIndexOf(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	public int getIndexOf(VControl control) {
		return diffControls.indexOf(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getControl(int)
	 */
	public VControl getControl(int diffIndex) throws IllegalArgumentException {
		if (diffIndex < 0) {
			throw new IllegalArgumentException("The index must be 0 or greater."); //$NON-NLS-1$
		}
		if (diffIndex >= getTotalNumberOfDiffs()) {
			throw new IllegalArgumentException(String.format(
				"The index %1$d is to high. There are only %2$d differences.", diffIndex, getTotalNumberOfDiffs())); //$NON-NLS-1$
		}
		return diffControls.get(diffIndex);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#isControlMerged(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	public boolean isControlMerged(VControl vControl) {
		if (!diffControls.contains(vControl)) {
			return true;
		}
		if (mergedControls.contains(vControl)) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#markControlAsMerged(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	public void markControlAsMerged(VControl vControl) {
		mergedControls.add(vControl);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getMergedControls()
	 */
	public Set<VControl> getMergedControls() {
		return mergedControls;
	}

}

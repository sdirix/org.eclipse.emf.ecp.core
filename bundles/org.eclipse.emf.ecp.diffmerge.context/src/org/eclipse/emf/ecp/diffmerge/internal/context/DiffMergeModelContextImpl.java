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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.diffmerge.spi.context.ControlPair;
import org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext;
import org.eclipse.emf.ecp.spi.diffmerge.model.VDiffAttachment;
import org.eclipse.emf.ecp.spi.diffmerge.model.VDiffmergeFactory;
import org.eclipse.emf.ecp.view.internal.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.spi.context.ViewModelService;
import org.eclipse.emf.ecp.view.spi.model.VAttachment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
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
	 * @param mergedReferences the set of already merged domain references
	 * @see ViewModelContextImpl#ViewModelContextImpl(VElement, EObject)
	 */
	public DiffMergeModelContextImpl(VElement view, EObject domainObject,
		EObject left, EObject right, Set<VDomainModelReference> mergedReferences) {
		this(view, domainObject, left, right);
		readAlreadyMerged(mergedReferences);
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
	 * @param mergedReferences the set of already merged domain references
	 * @param modelServices the {@link ViewModelService ViewModelServices} to register
	 * @see ViewModelContextImpl#ViewModelContextImpl(VElement, EObject, ViewModelService...)
	 */
	public DiffMergeModelContextImpl(VElement view, EObject domainObject,
		EObject origin1, EObject origin2, Set<VDomainModelReference> mergedReferences,
		ViewModelService... modelServices) {
		this(view, domainObject, origin1, origin2, modelServices);
		readAlreadyMerged(mergedReferences);
	}

	private void readAlreadyMerged(Set<VDomainModelReference> mergedReferences) {
		for (final VDomainModelReference domainModelReference : mergedReferences) {
			domainModelReference.resolve(getDomainModel());
			final Iterator<Setting> iterator = domainModelReference.getIterator();
			while (iterator.hasNext()) {
				final Setting setting = iterator.next();
				final Set<VControl> controls = getControlsFor(setting);
				if (controls == null) {
					continue;
				}
				for (final VControl vControl : controls) {
					markControl(vControl, true);
				}
			}
		}
	}

	private void initComparison() {

		final VElement viewModelLeft = EcoreUtil.copy(getViewModel());
		final VElement viewModelRight = EcoreUtil.copy(getViewModel());

		ViewModelUtil.resolveDomainReferences(this, viewModelLeft, getLeftModel());
		ViewModelUtil.resolveDomainReferences(this, viewModelRight, getRightModel());

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
				if (hasDiff((VControl) leftEObject, (VControl) rightEObject, (VControl) mainEObject)) {
					final VControl control = (VControl) mainEObject;
					controlDiffMap.put(control, new ControlPair((VControl) leftEObject,
						(VControl) rightEObject));
					diffControls.add(control);
				}
			}
		}
		for (final VControl control : diffControls) {

			final VDiffAttachment diffAttachment = getDiffAttachment(control);
			diffAttachment.setTotalNumberOfDiffs(1);
			diffAttachment.setMergedDiffs(mergedControls.contains(control) ? 1 : 0);
			propagateDiffAttachment(control, diffAttachment);
		}
	}

	private void propagateDiffAttachment(VControl control, VDiffAttachment childDiff) {

		EObject parent = control.eContainer();
		while (parent != null) {
			final EObject newParent = parent.eContainer();
			if (!VElement.class.isInstance(parent)) {
				parent = newParent;
				continue;
			}
			final VElement vElement = (VElement) parent;
			final VDiffAttachment attachment = getDiffAttachment(vElement);
			attachment.setMergedDiffs(0);
			attachment.setTotalNumberOfDiffs(0);

			for (final EObject eObject : vElement.eContents()) {
				if (!VElement.class.isInstance(eObject)) {
					continue;
				}
				final VElement childElement = (VElement) eObject;
				final VDiffAttachment childAttachment = getDiffAttachment(childElement);
				attachment.setMergedDiffs(attachment.getMergedDiffs() + childAttachment.getMergedDiffs());
				attachment.setTotalNumberOfDiffs(attachment.getTotalNumberOfDiffs()
					+ childAttachment.getTotalNumberOfDiffs());
			}

			parent = newParent;
			childDiff = attachment;
		}
	}

	private VDiffAttachment getDiffAttachment(VElement vElement) {
		VDiffAttachment attachment = null;
		boolean hasAttachment = false;
		for (final VAttachment vAttachment : vElement.getAttachments()) {
			if (VDiffAttachment.class.isInstance(vAttachment)) {
				attachment = (VDiffAttachment) vAttachment;
				hasAttachment = true;
				break;
			}
		}
		if (!hasAttachment) {
			final VDiffAttachment vDiffAttachment = VDiffmergeFactory.eINSTANCE.createDiffAttachment();
			vElement.getAttachments().add(vDiffAttachment);
			attachment = vDiffAttachment;
		}
		return attachment;
	}

	/**
	 * Checks whether the controls have a diff.
	 * 
	 * @param leftControl the left control to check
	 * @param rightControl the right control to check
	 * @param targetControl the target control to check
	 * @return true if there is a diff, false otherwise
	 */
	protected boolean hasDiff(VControl leftControl, VControl rightControl, VControl targetControl) {
		return !CompareControls.areEqual(leftControl, rightControl);
	}

	/** {@inheritDoc} **/
	@Override
	public EObject getLeftModel() {
		return left;
	}

	/** {@inheritDoc} **/
	@Override
	public EObject getRightModel() {
		return right;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#hasDiff(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	@Override
	public boolean hasDiff(VControl control) {
		return controlDiffMap.containsKey(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getPairWithDiff(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	@Override
	public ControlPair getPairWithDiff(VControl control) {
		return controlDiffMap.get(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getTotalNumberOfDiffs()
	 */
	@Override
	public int getTotalNumberOfDiffs() {
		return diffControls.size();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getIndexOf(org.eclipse.emf.ecp.view.spi.model.VControl)
	 */
	@Override
	public int getIndexOf(VControl control) {
		return diffControls.indexOf(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getControl(int)
	 */
	@Override
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
	@Override
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
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#markControl(org.eclipse.emf.ecp.view.spi.model.VControl,
	 *      boolean)
	 */
	@Override
	public void markControl(VControl vControl, boolean merged) {
		if (merged) {
			mergedControls.add(vControl);
		} else {
			mergedControls.remove(vControl);
		}
		final VDiffAttachment diffAttachment = getDiffAttachment(vControl);
		diffAttachment.setMergedDiffs(merged ? 1 : 0);
		propagateDiffAttachment(vControl, diffAttachment);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext#getMergedDomainObjects()
	 */
	@Override
	public Set<VDomainModelReference> getMergedDomainObjects() {
		final Set<VDomainModelReference> result = new LinkedHashSet<VDomainModelReference>();
		for (final VControl control : mergedControls) {
			result.add(EcoreUtil.copy(control.getDomainModelReference()));
		}
		return result;
	}

}

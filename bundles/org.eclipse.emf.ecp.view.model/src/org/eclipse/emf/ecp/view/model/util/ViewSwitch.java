/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Action;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.CompositeCollection;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VDiagnostic;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method
 * for each class of the model,
 * starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the
 * result of the switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage
 * @generated
 */
public class ViewSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ViewPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ViewSwitch() {
		if (modelPackage == null)
		{
			modelPackage = ViewPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that
	 * result.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID)
		{
		case ViewPackage.RENDERABLE: {
			Renderable renderable = (Renderable) theEObject;
			T result = caseRenderable(renderable);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.VDIAGNOSTIC: {
			VDiagnostic vDiagnostic = (VDiagnostic) theEObject;
			T result = caseVDiagnostic(vDiagnostic);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.ATTACHMENT: {
			Attachment attachment = (Attachment) theEObject;
			T result = caseAttachment(attachment);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.VIEW: {
			View view = (View) theEObject;
			T result = caseView(view);
			if (result == null)
				result = caseCategorization(view);
			if (result == null)
				result = caseAbstractCategorization(view);
			if (result == null)
				result = caseRenderable(view);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.ABSTRACT_CATEGORIZATION: {
			AbstractCategorization abstractCategorization = (AbstractCategorization) theEObject;
			T result = caseAbstractCategorization(abstractCategorization);
			if (result == null)
				result = caseRenderable(abstractCategorization);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.CATEGORIZATION: {
			Categorization categorization = (Categorization) theEObject;
			T result = caseCategorization(categorization);
			if (result == null)
				result = caseAbstractCategorization(categorization);
			if (result == null)
				result = caseRenderable(categorization);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.CATEGORY: {
			Category category = (Category) theEObject;
			T result = caseCategory(category);
			if (result == null)
				result = caseAbstractCategorization(category);
			if (result == null)
				result = caseRenderable(category);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.ACTION: {
			Action action = (Action) theEObject;
			T result = caseAction(action);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.COMPOSITE: {
			Composite composite = (Composite) theEObject;
			T result = caseComposite(composite);
			if (result == null)
				result = caseRenderable(composite);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.ABSTRACT_CONTROL: {
			AbstractControl abstractControl = (AbstractControl) theEObject;
			T result = caseAbstractControl(abstractControl);
			if (result == null)
				result = caseComposite(abstractControl);
			if (result == null)
				result = caseRenderable(abstractControl);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.VDOMAIN_MODEL_REFERENCE: {
			VDomainModelReference vDomainModelReference = (VDomainModelReference) theEObject;
			T result = caseVDomainModelReference(vDomainModelReference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.VFEATURE_PATH_DOMAIN_MODEL_REFERENCE: {
			VFeaturePathDomainModelReference vFeaturePathDomainModelReference = (VFeaturePathDomainModelReference) theEObject;
			T result = caseVFeaturePathDomainModelReference(vFeaturePathDomainModelReference);
			if (result == null)
				result = caseVDomainModelReference(vFeaturePathDomainModelReference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.CONTROL: {
			Control control = (Control) theEObject;
			T result = caseControl(control);
			if (result == null)
				result = caseAbstractControl(control);
			if (result == null)
				result = caseComposite(control);
			if (result == null)
				result = caseRenderable(control);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ViewPackage.COMPOSITE_COLLECTION: {
			CompositeCollection compositeCollection = (CompositeCollection) theEObject;
			T result = caseCompositeCollection(compositeCollection);
			if (result == null)
				result = caseComposite(compositeCollection);
			if (result == null)
				result = caseRenderable(compositeCollection);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of ' <em>View</em>'. <!-- begin-user-doc --> This
	 * implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc
	 * -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of ' <em>View</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseView(View object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of ' <em>Abstract Categorization</em>'. <!--
	 * begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of ' <em>Abstract Categorization</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractCategorization(AbstractCategorization object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Categorization</em>'.
	 * <!-- begin-user-doc
	 * --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Categorization</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCategorization(Categorization object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Category</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Category</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCategory(Category object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Composite</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Composite</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComposite(Composite object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Control</em>'.
	 * <!-- begin-user-doc --> This
	 * implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Control</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseControl(Control object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of ' <em>Composite Collection</em>'. <!--
	 * begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of ' <em>Composite Collection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCompositeCollection(CompositeCollection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Renderable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Renderable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRenderable(Renderable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Action</em>'.
	 * <!-- begin-user-doc --> This
	 * implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAction(Action object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Control</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Control</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractControl(AbstractControl object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>VDomain Model Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>VDomain Model Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVDomainModelReference(VDomainModelReference object)
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>VFeature Path Domain Model Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>VFeature Path Domain Model Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVFeaturePathDomainModelReference(VFeaturePathDomainModelReference object)
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attachment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attachment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttachment(Attachment object)
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>VDiagnostic</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>VDiagnostic</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVDiagnostic(VDiagnostic object)
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} // ViewSwitch

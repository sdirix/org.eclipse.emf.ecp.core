/**
 */
package org.eclipse.emf.ecp.ui.view.editor.test.model.test;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.ui.view.editor.test.model.test.Component2#getComposites <em>Composites</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.ui.view.editor.test.model.test.TestPackage#getComponent2()
 * @model
 * @generated
 */
public interface Component2 extends EObject {

	/**
	 * Returns the value of the '<em><b>Composites</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecp.ui.view.editor.test.model.test.Composite}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Composites</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Composites</em>' containment reference list.
	 * @see org.eclipse.emf.ecp.ui.view.editor.test.model.test.TestPackage#getComponent2_Composites()
	 * @model containment="true"
	 * @generated
	 */
	EList<Composite> getComposites();
} // Component2

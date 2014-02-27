/**
 */
package org.eclipse.emf.ecp.view.core.swt.test.model;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Test Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecp.view.core.swt.test.model.SimpleTestObject#getDate <em>Date</em>}</li>
 *   <li>{@link org.eclipse.emf.ecp.view.core.swt.test.model.SimpleTestObject#getXmlDate <em>Xml Date</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecp.view.core.swt.test.model.TestPackage#getSimpleTestObject()
 * @model
 * @generated
 */
public interface SimpleTestObject extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see org.eclipse.emf.ecp.view.core.swt.test.model.TestPackage#getSimpleTestObject_Date()
	 * @model
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.core.swt.test.model.SimpleTestObject#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

	/**
	 * Returns the value of the '<em><b>Xml Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Xml Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Xml Date</em>' attribute.
	 * @see #setXmlDate(XMLGregorianCalendar)
	 * @see org.eclipse.emf.ecp.view.core.swt.test.model.TestPackage#getSimpleTestObject_XmlDate()
	 * @model dataType="org.eclipse.emf.ecp.view.core.swt.test.model.XMLDate"
	 * @generated
	 */
	XMLGregorianCalendar getXmlDate();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.core.swt.test.model.SimpleTestObject#getXmlDate <em>Xml Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Xml Date</em>' attribute.
	 * @see #getXmlDate()
	 * @generated
	 */
	void setXmlDate(XMLGregorianCalendar value);

} // SimpleTestObject

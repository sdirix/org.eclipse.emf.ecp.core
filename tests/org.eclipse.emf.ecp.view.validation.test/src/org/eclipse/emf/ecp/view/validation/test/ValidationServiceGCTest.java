/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.view.common.test.GCCollectable;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.validation.test.model.Computer;
import org.eclipse.emf.ecp.view.validation.test.model.Mainboard;
import org.eclipse.emf.ecp.view.validation.test.model.TestFactory;
import org.eclipse.emf.ecp.view.validation.test.model.TestPackage;
import org.eclipse.emf.ecp.view.validation.test.model.Writer;
import org.junit.Test;

/**
 * Tests for checking whether the {@link org.eclipse.emf.ecp.view.validation.ValidationRegistry ValidationRegistry} &
 * the {@link org.eclipse.emf.ecp.view.validation.ValidationService ValidationService} correctly behave if domain
 * objects and/or {@link org.eclipse.emf.ecp.view.model.Renderable Renderable}s are removed.
 * 
 * @author emueller
 */
public class ValidationServiceGCTest extends CommonValidationTest {

	/** Convenience class for returning multiple values at once. */
	class Tuple<T, U> {

		private final T t;
		private final U u;

		public Tuple(T t, U u) {
			this.t = t;
			this.u = u;
		}

		public T first() {
			return t;
		}

		public U second() {
			return u;

		}
	}

	/**
	 * Creates a basic view with an column that contains a control
	 * that is bound to the mainboard name feature of a computer.
	 * Both the computer and the mainboard are also created.
	 * 
	 * @return a tuple containing the root of the view model
	 *         as well as the root of the domain model
	 */
	protected Tuple<View, Computer> createComputerView() {

		final Computer computer = TestFactory.eINSTANCE.createComputer();
		final View view = ViewFactory.eINSTANCE.createView();

		new ViewModelContextImpl(view, computer);

		final Control control = ViewFactory.eINSTANCE.createControl();
		final Column column = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(column);

		control.setDomainModelReference(getVFeaturePathDomainModelReference(TestPackage.eINSTANCE.getMainboard_Name(),
			TestPackage.eINSTANCE.getComputer_Mainboard()));
		column.getComposites().add(control);

		final Mainboard mainboard = TestFactory.eINSTANCE.createMainboard();
		computer.setMainboard(mainboard);

		assertEquals("Severity of mainboard name must be error", Diagnostic.ERROR, control.getDiagnostic()
			.getHighestSeverity());

		return new Tuple<View, Computer>(view, computer);
	}

	/**
	 * Creates a basic view with an two nested columns.
	 * The most inner column contains a control
	 * that is bound to the {@link TestPackage#getWriter_FirstName()} feature of a {@link Writer}.
	 * The writer will also be created by this method.
	 * 
	 * @return a tuple containing the root of the view model
	 *         as well as the root of the domain model
	 */
	protected Tuple<View, Writer> createWriterWithNestedColumnsView() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(writer.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		final Column column = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(column);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();

		final VFeaturePathDomainModelReference domainModelReference = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriter.setDomainModelReference(domainModelReference);

		column.getComposites().add(controlWriter);

		new ViewModelContextImpl(view, writer);

		return new Tuple<View, Writer>(view, writer);
	}

	/**
	 * Removes the direct column of the view. The nested column as
	 * well as the control should be removed and thus not referenced
	 * anymore.
	 */
	@Test
	public void testRemoveRenderableHierarchy() {

		final View view = createWriterWithNestedColumnsView().first();
		final GCCollectable parentColumnCollectable = new GCCollectable(
			view.getChildren().get(0));
		final GCCollectable columnCollectable = new GCCollectable(
			Column.class.cast(view.getChildren().get(0)).getComposites().get(0));
		final GCCollectable controlCollectable = new GCCollectable(
			Column.class.cast(
				Column.class.cast(view.getChildren().get(0))
					.getComposites().get(0)).getComposites()
				.get(0));

		view.getChildren().remove(0);

		assertTrue(parentColumnCollectable.isCollectable());
		assertTrue(columnCollectable.isCollectable());
		assertTrue(controlCollectable.isCollectable());
	}

	/**
	 * Make sure child domain object is actually referenced.
	 */
	@Test
	public void testDomainObjectIsReferenced() {

		final Tuple<View, Computer> t = createComputerView();

		final GCCollectable mainboardCollectable = new GCCollectable(
			t.second().getMainboard());

		assertFalse(mainboardCollectable.isCollectable());
	}

	/**
	 * Remove the {@link Mainboard}. It shouldn't be referenced anymore
	 * in contrast to the control.
	 */
	@Test
	public void testRemoveChildOfDomainObject() {

		final Tuple<View, Computer> t = createComputerView();

		final GCCollectable mainboardCollectable = new GCCollectable(
			t.second().getMainboard());
		final GCCollectable controlCollectable = new GCCollectable(
			Column.class.cast(t.first().getChildren().get(0)).getComposites().get(0));
		t.second().setMainboard(null);

		// control for mainboard should be removed from
		assertTrue(mainboardCollectable.isCollectable());
		assertFalse(controlCollectable.isCollectable());
	}

	/**
	 * Removes the {@link Mainboard} from the computer and also removes the control
	 * from the view model containment tree.
	 * Neither the mainboard nor the control should be referenced afterwards.
	 */
	@Test
	public void testRemoveChildOfDomainObjectWithCutOffControl() {

		final Tuple<View, Computer> t = createComputerView();

		final GCCollectable mainboardCollectable = new GCCollectable(
			t.second().getMainboard());
		t.second().setMainboard(null);

		final GCCollectable controlCollectable = new GCCollectable(
			Column.class.cast(t.first().getChildren().get(0)).getComposites().get(0));

		Column.class.cast(t.first().getChildren().get(0)).getComposites().clear();

		// control for mainboard shouldn't be referenced anymore by validation registry or service
		assertTrue(mainboardCollectable.isCollectable());
		assertTrue(controlCollectable.isCollectable());
	}

	/**
	 * Removes the direct column of the computer view.
	 */
	@Test
	public void testRemoveRenderable() {

		final View view = createComputerView().first();
		final GCCollectable collectable = new GCCollectable(view.getChildren().get(0));
		// removes the column
		view.getChildren().remove(0);

		assertTrue(collectable.isCollectable());
	}
}

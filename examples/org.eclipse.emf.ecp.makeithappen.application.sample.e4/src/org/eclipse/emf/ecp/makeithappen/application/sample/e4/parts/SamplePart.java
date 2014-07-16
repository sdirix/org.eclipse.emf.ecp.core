/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package org.eclipse.emf.ecp.makeithappen.application.sample.e4.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class SamplePart {

	@Inject
	private MDirtyable dirty;
	private Composite content;

	@PostConstruct
	public void createComposite(Composite parent) {

		final EObject dummyObject = getDummyEObject();

		try {

			content = new Composite(parent, SWT.NONE);
			content.setLayout(GridLayoutFactory.fillDefaults().create());
			content.setLayoutData(GridDataFactory.fillDefaults().create());

			ECPSWTViewRenderer.INSTANCE.render(content, dummyObject);

			content.layout();

		} catch (final ECPRendererException e) {
			e.printStackTrace();
		}
		parent.layout();
	}

	private EObject getDummyEObject() {
		final EClass eClass = TaskPackage.eINSTANCE.getUser();
		final EPackage ePackage = eClass.getEPackage();
		return ePackage.getEFactoryInstance().create(eClass);
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}
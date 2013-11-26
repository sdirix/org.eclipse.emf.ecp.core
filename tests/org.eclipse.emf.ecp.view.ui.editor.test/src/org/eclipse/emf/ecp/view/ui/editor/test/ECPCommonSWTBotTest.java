/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.ui.editor.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Edgar
 * 
 */
public abstract class ECPCommonSWTBotTest extends SWTBotTestCase {

	private static Shell shell;
	private Display display;

	private VView view;

	@Override
	@Before
	public void setUp() {
		display = Display.getDefault();
		shell = UIThreadRunnable.syncExec(display, new Result<Shell>() {
			public Shell run() {
				final Shell shell = new Shell(display);
				shell.setLayout(new FillLayout());
				return shell;
			}
		});
	}

	@Test
	public void test() throws ECPRendererException,
		InterruptedException {
		Realm.runWithDefault(SWTObservables.getRealm(display), new TestRunnable());
	}

	public abstract EObject createDomainObject();

	public abstract VView createView();

	public abstract void logic();

	// public ECPControlContext createContext(EObject domainObject, VView view) {
	// return new DefaultControlContext(domainObject, view);
	// }

	public VView getViewNode() {
		return view;
	}

	private class TestRunnable implements Runnable {

		public void run() {
			final List<ECPSWTView> holdingList = new ArrayList<ECPSWTView>();
			try {
				holdingList.add(UIThreadRunnable.syncExec(new Result<ECPSWTView>() {
					public ECPSWTView run() {
						try {
							view = createView();
							final EObject domainObject = createDomainObject();

							// final ViewModelContext viewContext = new ViewModelContextImpl(view, domainObject);

							final ECPSWTView swtView = ECPSWTViewRenderer.INSTANCE.render(shell, domainObject, view);
							final Composite composite = (Composite) swtView.getSWTControl();
							final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
							composite.setLayoutData(gridData);
							// final ECPSWTViewImpl swtView = new ECPSWTViewImpl(composite, rendererContext,
							// viewContext);
							shell.open();
							return swtView;
						} catch (final NoRendererFoundException e) {
							fail(e.getMessage());
						} catch (final NoPropertyDescriptorFoundExeption e) {
							fail(e.getMessage());
						} catch (final ECPRendererException ex) {
							fail(ex.getMessage());
						}
						return null;
					}
				}));

				logic();
			} finally {
				UIThreadRunnable.syncExec(new VoidResult() {
					public void run() {
						shell.close();
						if (holdingList.size() > 0) {
							holdingList.remove(0).dispose();
						}
						shell.dispose();
					}
				});
			}
		}
	}

}

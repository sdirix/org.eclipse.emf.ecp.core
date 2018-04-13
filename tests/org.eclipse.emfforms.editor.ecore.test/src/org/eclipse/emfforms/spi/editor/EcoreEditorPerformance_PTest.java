/*******************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Christian W. Damus - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.editor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailComposite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Performance tests for the <em>EMF Forms GenericEditor</em>, using the Ecore editor
 * as test subject.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("nls")
public class EcoreEditorPerformance_PTest {

	@Rule
	public final ProjectRule project = new ProjectRule();

	/**
	 * Initializes me.
	 */
	public EcoreEditorPerformance_PTest() {
		super();
	}

	/**
	 * Regression test for <a href="http://eclip.se/533568">bug 533568</a> in which the
	 * {@link GenericEditor} leaks an instance of the {@link TreeMasterDetailComposite} and
	 * its attendant "limbo" shell after the editor is closed, until workbench shutdown.
	 */
	@Test
	@TestResource
	public void treeMasterDetailCompositeDoesNotLeak() {
		final int expectedShellCount = Display.getDefault().getShells().length;

		final GenericEditor editor = open();
		close(editor);

		final int actualShellCount = Display.getDefault().getShells().length;
		assertThat("Limbo shell remains", actualShellCount, is(expectedShellCount));
	}

	//
	// Test framework
	//

	/**
	 * Import the test models indicated by the annotations on the {@code test} into the {@code project}.
	 */
	void importTestModels(Description test, IProject project, IProgressMonitor monitor)
		throws CoreException, IOException {

		final String[] paths = getTestResources(test);

		final SubMonitor sub = SubMonitor.convert(monitor, paths.length);
		final ResourceSet rset = new ResourceSetImpl();

		for (final String path : paths) {
			final URI uri = URI.createURI(
				"platform:/fragment/org.eclipse.emfforms.editor.ecore.test/data/" + path);

			final Resource resource = rset.getResource(uri, true);
			resource
				.setURI(URI.createPlatformResourceURI(String.format("%s/%s", project.getName(), path), true));
			resource.save(null);
			sub.worked(1);
		}

		for (final Resource next : rset.getResources()) {
			next.unload();
		}
		rset.getResources().clear();
		rset.eAdapters().clear();
	}

	/**
	 * Get the test resources indicated by the annotation on the given {@code test}.
	 *
	 * @param test a test
	 * @return its test resources
	 */
	static String[] getTestResources(Description test) {
		final TestResource testResource = test.getAnnotation(TestResource.class);
		return testResource.value();
	}

	@BeforeClass
	public static void closeIntroView() {
		final IViewPart introView = getActivePage().findView("org.eclipse.ui.internal.introview");
		if (introView != null) {
			introView.getSite().getPage().hideView(introView);
		}
	}

	@After
	public void closeAllEditors() {
		getActivePage().closeAllEditors(false);
	}

	GenericEditor open() {
		return open(project.getFiles().get(0));
	}

	GenericEditor open(String fileName) {
		return open(project.getProject().getFile(fileName));
	}

	GenericEditor open(IFile file) {
		try {
			final IEditorPart result = IDE.openEditor(getActivePage(), file, "EcoreEditor");
			return (GenericEditor) result;
		} catch (final PartInitException e) {
			e.printStackTrace();
			fail("Failed to open editor: " + e.getMessage());
			return null; // Unreachable
		} finally {
			flushUIEvents();
		}
	}

	void close(IEditorPart editor) {
		editor.getSite().getPage().closeEditor(editor, false);

		flushUIEvents();
	}

	static IWorkbenchPage getActivePage() {
		final IWorkbench bench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = bench.getActiveWorkbenchWindow();
		if (window == null) {
			window = bench.getWorkbenchWindows()[0];
		}
		return window.getActivePage();
	}

	static void flushUIEvents() {
		final Display display = Display.getCurrent();
		while (display.readAndDispatch()) {
			// Nothing to do
		}
	}

	//
	// Nested types
	//

	/**
	 * Annotates a test with the resources that it needs to import into the test project.
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface TestResource {
		/**
		 * Paths within the {@code data/} folder of resources to import into the test project.
		 */
		String[] value() default { "test.ecore" };
	}

	public final class ProjectRule extends TestWatcher {
		private IProject project;

		private List<IFile> files;

		/**
		 * @return the project
		 */
		public IProject getProject() {
			return project;
		}

		/**
		 * @return the files
		 */
		public List<IFile> getFiles() {
			return files;
		}

		@Override
		protected void starting(final Description description) {
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getMethodName());

			try {
				if (project.exists()) {
					flushUIEvents();
					project.delete(true, null);
				}
				flushUIEvents();
				project.create(null);
				project.open(null);

				project.getWorkspace().run(new IWorkspaceRunnable() {

					@Override
					public void run(IProgressMonitor monitor) throws CoreException {
						try {
							importTestModels(description, project, monitor);
						} catch (final IOException e) {
							final Bundle bundle = FrameworkUtil.getBundle(getClass());
							String message = e.getMessage();
							if (message == null || message.isEmpty()) {
								message = "Unknown I/O exception.";
							}
							throw new CoreException(
								new Status(IStatus.ERROR, bundle.getSymbolicName(), message, e));
						}
					}
				}, new NullProgressMonitor());

				flushUIEvents();
			} catch (final CoreException e) {
				e.printStackTrace();
				fail("Failed to create test project: " + e.getStatus().getMessage());
			} finally {
				final String[] paths = getTestResources(description);
				files = new ArrayList<IFile>(paths.length);
				for (final String next : paths) {
					files.add(project.getFile(next));
				}
			}
		}

		@Override
		protected void finished(Description description) {
			flushUIEvents();
			try {
				project.delete(true, null);
			} catch (final CoreException e) {
				e.printStackTrace();
			}
			flushUIEvents();
		}
	}
}

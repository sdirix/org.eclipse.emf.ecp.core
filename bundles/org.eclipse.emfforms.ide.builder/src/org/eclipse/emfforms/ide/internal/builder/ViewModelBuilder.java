/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.ide.internal.builder;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.ui.MarkerHelper;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.ide.spi.util.EcoreHelper;
import org.eclipse.emf.ecp.ide.spi.util.ViewModelHelper;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelPropertiesHelper;
import org.eclipse.emf.ecp.view.spi.model.util.ViewValidator;
import org.eclipse.emf.edit.ui.util.EditUIMarkerHelper;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.common.internal.validation.ValidationServiceImpl;
import org.eclipse.emfforms.common.spi.validation.ValidationService;
import org.eclipse.emfforms.common.spi.validation.exception.ValidationCanceledException;
import org.eclipse.emfforms.common.spi.validation.filter.AbstractComplexFilter;
import org.eclipse.emfforms.ide.internal.builder.messages.Messages;
import org.eclipse.osgi.util.NLS;

/**
 * Incremental builder that triggers validation on view models in the workspace.
 */
@SuppressWarnings("restriction")
public class ViewModelBuilder extends IncrementalProjectBuilder {

	/** View model file extension. */
	private static final String VIEW = "view"; //$NON-NLS-1$

	/** identifier of the builder, similar to plugin.xml value. */
	public static final String BUILDER_ID = "org.eclipse.emfforms.ide.builder.viewModelBuilder"; //$NON-NLS-1$

	/** identifier of the marker, similar to plugin.xml value. */
	public static final String MARKER_ID = "org.eclipse.emfforms.ide.builder.ViewModelProblem"; //$NON-NLS-1$

	private final EditUIMarkerHelper projectMarkerHelper = new EditUIMarkerHelper() {
		@Override
		protected String getMarkerID() {
			return MARKER_ID;
		}
	};

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
		throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			final IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		// delete markers set and files created
		getProject().deleteMarkers(MARKER_ID, true, IResource.DEPTH_INFINITE);
	}

	/**
	 * Check the content of the view file.
	 *
	 * @param iResource the {@link IResource} to check
	 * @param monitor the progress monitor
	 */
	void checkViewModelFile(IResource iResource, IProgressMonitor monitor) {
		if (isViewModelResource(iResource)) {
			final IFile file = IFile.class.cast(iResource);
			monitor.subTask(NLS.bind(Messages.ViewModelBuilder_Subtask_Label, file.getName()));
			projectMarkerHelper.deleteMarkers(file);

			try {
				final Optional<BasicDiagnostic> diagnostics = validateView(file);
				if (diagnostics.isPresent()) {
					final Diagnostic diag = diagnostics.get();
					// create markers only if severity >= Warning
					if (diag.getSeverity() >= IMarker.SEVERITY_WARNING) {
						final MarkerHelper markerHelper = new ViewModelMarkerHelper(file);
						markerHelper.createMarkers(diag);
					}
				}
			} catch (final CoreException ex) {
				Activator.log("Errors while creating markers on file " + file, ex);//$NON-NLS-1$
			}
		}
	}

	private static boolean isViewModelResource(IResource resource) {
		if (IFile.class.isInstance(resource)) {
			return VIEW.equals(resource.getFileExtension());
		}
		return false;
	}

	/**
	 * Runs a full build on the project.
	 *
	 * @param monitor the progress monitor to track the build
	 * @throws CoreException exception in case of issues
	 */
	protected void fullBuild(final IProgressMonitor monitor)
		throws CoreException {
		try {
			getProject().accept(new ViewModelResourceVisitor(monitor));
		} catch (final CoreException e) {
			Activator.log("Error running full build", e);//$NON-NLS-1$
		}
	}

	/**
	 * Runs an incremental build on the project.
	 *
	 * @param delta the delta on the resource that triggers this incremental build
	 * @param monitor the progress monitor to track the build
	 * @throws CoreException exception in case of issues
	 */
	protected void incrementalBuild(IResourceDelta delta,
		IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new ViewModelDeltaVisitor(monitor));
	}

	/**
	 * Basic resource visitor based on deltas.
	 */
	class ViewModelDeltaVisitor implements IResourceDeltaVisitor {

		private final IProgressMonitor monitor;

		/**
		 * Constructor.
		 *
		 * @param monitor the progress monitor
		 */
		ViewModelDeltaVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			final IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				checkViewModelFile(resource, monitor);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				checkViewModelFile(resource, monitor);
				break;
			default:
				break;
			}
			// return true to continue visiting children.
			return true;
		}
	}

	/**
	 * Visit a given resource.
	 */
	class ViewModelResourceVisitor implements IResourceVisitor {

		private final IProgressMonitor monitor;

		/**
		 * Constructor.
		 *
		 * @param monitor the progress monitor
		 */
		ViewModelResourceVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public boolean visit(IResource resource) {
			checkViewModelFile(resource, monitor);
			// return true to continue visiting children.
			return true;
		}
	}

	private Optional<BasicDiagnostic> validateView(IFile file) {
		final LinkedHashSet<String> ecores = new LinkedHashSet<String>();
		try {
			// load file thanks to ECP helpers to avoid missing Properties
			final VView view = ViewModelHelper.loadView(file, ecores);
			if (view != null) {
				final VViewModelProperties properties = ViewModelPropertiesHelper.getInhertitedPropertiesOrEmpty(view);
				view.setLoadingProperties(properties);

				// trigger validation
				final ValidationService service = new ValidationServiceImpl();
				service.registerValidationFilter(new ViewValidatorDuplicateFilter());
				final Diagnostic viewDiagnostic = service.validate(view);
				final Set<Diagnostic> subDiagnostics = service.validate(view.eAllContents());

				// create the final result, mainly extract the most relevant diagnostic among all of them
				final Diagnostician diagnostician = new Diagnostician();
				final BasicDiagnostic diagnostics = diagnostician.createDefaultDiagnostic(view);
				if (viewDiagnostic.getSeverity() >= Diagnostic.WARNING) {
					diagnostics.add(viewDiagnostic);
				}
				for (final Diagnostic sub : subDiagnostics) {
					if (sub.getSeverity() >= Diagnostic.WARNING) {
						diagnostics.add(sub);
					}
				}

				return Optional.of(diagnostics);
			}
		} catch (final ClassCastException e) {
			// can occur during validation
			return Optional.of((BasicDiagnostic) toDiagnostic(e));
		} catch (final IOException ex) {
			return Optional.of((BasicDiagnostic) toDiagnostic(ex));
			// BEGIN SUPRESS CATCH EXCEPTION
		} catch (final RuntimeException e) {
			// suppressing checkstyle warning to catch the runtime exception thrown when failing to load a resource
			// END SUPRESS CATCH EXCEPTION
			return Optional.of((BasicDiagnostic) toDiagnostic(e));
		} catch (final ValidationCanceledException ex) {
			// no diagnostic to return in this case
		} finally {
			for (final String registeredEcore : ecores) {
				EcoreHelper.unregisterEcore(registeredEcore);
			}
		}
		return Optional.empty();
	}

	private static Diagnostic toDiagnostic(Throwable throwable) {
		String message = throwable.getClass().getName();
		final int index = message.lastIndexOf('.');
		if (index >= 0) {
			message = message.substring(index + 1);
		}
		if (throwable.getLocalizedMessage() != null) {
			message = message + ": " + throwable.getLocalizedMessage();//$NON-NLS-1$
		}

		final BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR,
			ViewValidator.DIAGNOSTIC_SOURCE,
			0,
			message,
			new Object[] { throwable });

		if (throwable.getCause() != null && throwable.getCause() != throwable) {
			throwable = throwable.getCause();
			basicDiagnostic.add(toDiagnostic(throwable));
		}

		return basicDiagnostic;
	}

	/**
	 * Specific Marker Helper that directly knows the file.
	 */
	private class ViewModelMarkerHelper extends MarkerHelper {
		private final IFile file;

		@Override
		protected String getMarkerID() {
			return MARKER_ID;
		}

		/**
		 * Constructor.
		 *
		 * @param file file being validated
		 */
		ViewModelMarkerHelper(IFile file) {
			this.file = file;
		}

		@Override
		public void createMarkers(final Diagnostic diagnostic) throws CoreException {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					final int severity = diagnostic.getSeverity();
					if (severity < Diagnostic.WARNING) {
						// neither info nor ok markers
						return;
					}
					// no child and severity ok, directly create marker.
					if (diagnostic.getChildren().isEmpty()) {
						createMarkers(getFile(diagnostic), diagnostic, null);
					} else {
						for (final Diagnostic child : diagnostic.getChildren()) {
							// retrieve the most relevant child to get the right message: most relevant is a child
							// diagnostic with the ECP diagnostic specific source and the right severity
							final Diagnostic relevant = extractRelevantDiagnostic(child);
							createMarkers(getFile(diagnostic), relevant, diagnostic);
						}
					}

				}
			},
				null);
		}

		private Diagnostic extractRelevantDiagnostic(Diagnostic parentDiagnostic) {
			if (parentDiagnostic.getSeverity() == 0 || parentDiagnostic.getChildren().isEmpty()) {
				return parentDiagnostic;
			}

			// try to get the most relevant => the one with highest severity, and the ECP validation source among the
			// children with highest severity
			Diagnostic result = parentDiagnostic.getChildren().get(0);
			int currentHighest = result.getSeverity();
			for (final Diagnostic child : parentDiagnostic.getChildren()) {
				if (child.getSeverity() > currentHighest) {
					currentHighest = child.getSeverity();
					result = child;
				} else if (child.getSeverity() == currentHighest
					&& !ViewValidator.DIAGNOSTIC_SOURCE.equals(result.getSource())
					&& ViewValidator.DIAGNOSTIC_SOURCE.equals(child.getSource())) {
					currentHighest = child.getSeverity();
					result = child;
				}
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.common.ui.MarkerHelper#getFile(org.eclipse.emf.common.util.Diagnostic)
		 */
		@Override
		protected IFile getFile(Object datum) {
			if (file != null) {
				return file;
			}
			return super.getFile(datum);
		}
	}

	/**
	 * Filter to avoid duplications in validation from {@link ViewValidator}.
	 */
	private class ViewValidatorDuplicateFilter extends AbstractComplexFilter {
		@Override
		public boolean skipSubtree(EObject eObject, Optional<Diagnostic> diagnostic) {
			return false;
		}

		@Override
		public boolean skipValidation(EObject eObject) {
			return VDomainModelReference.class.isInstance(eObject);
		}

	}
}

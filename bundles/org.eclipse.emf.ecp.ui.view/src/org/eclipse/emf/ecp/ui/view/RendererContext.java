package org.eclipse.emf.ecp.ui.view;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.SelectedChildNodeListener;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RendererContext<CONTROL> implements SelectedChildNodeListener {

	final private Map<EStructuralFeature, Set<EObject>> categoryValidationMap = new HashMap<EStructuralFeature, Set<EObject>>();
	final private Map<EObject, Set<Diagnostic>> validationMap = new HashMap<EObject, Set<Diagnostic>>();

	private Node<? extends Renderable> node;
	private boolean alive = true;
	private Renderable renderable;
	private ECPControlContext context;
	private final Set<ValidationListener> listeners = new HashSet<RendererContext.ValidationListener>();
	private ValidationSeverityModifier validationSeverityHandler;

	private EContentAdapter contentAdapter;
	private CONTROL control;
	private List<SelectedNodeChangedListener> selectionChangedListeners;

	public RendererContext(final Node<? extends Renderable> node, final ECPControlContext context) {
		this.node = node;
		this.renderable = node.getRenderable();
		this.selectionChangedListeners = new ArrayList<SelectedNodeChangedListener>();
		this.context = context;
		analyseView();

		if (node.getRenderable() instanceof View) {
			node.addSelectedChildNodeListener(this);
		}
	}

	public void setValidationSeverityHandler(ValidationSeverityModifier validationSeverityHandler) {
		this.validationSeverityHandler = validationSeverityHandler;
	}

	public void triggerValidation() {
		validate();
		for (ValidationListener validationListener : listeners) {
			validationListener.validationChanged(validationMap);
		}
	}

	public void addListener(ValidationListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ValidationListener listener) {
		listeners.remove(listener);
	}

	private void validate() {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(context.getModelElement());
		validationMap.clear();
		for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
			if (childDiagnostic.getData().size() < 2) {
				continue;
			}

			EStructuralFeature feature = (EStructuralFeature) childDiagnostic.getData().get(1);

			Set<EObject> objectsToMark = categoryValidationMap.get(feature);
			if (objectsToMark == null) {
				continue;
			}
			for (EObject object : objectsToMark) {
				Set<Diagnostic> currentValues = validationMap.get(object);
				if (currentValues == null) {
					validationMap.put(object, new HashSet<Diagnostic>());
				}
				if (validationSeverityHandler != null) {
					int severityForDiagnostic = validationSeverityHandler.getSeverityForDiagnostic(childDiagnostic,
						feature);
					if (severityForDiagnostic != childDiagnostic.getSeverity()) {
						childDiagnostic = createDiagnosticWithSeverity(childDiagnostic, severityForDiagnostic);
					}
				}
				validationMap.get(object).add(childDiagnostic);
			}

		}
	}

	private static BasicDiagnostic createDiagnosticWithSeverity(Diagnostic diagnosticTemplate, int severity) {
		return new BasicDiagnostic(severity, diagnosticTemplate.getSource(), diagnosticTemplate.getCode(),
			diagnosticTemplate.getMessage(), diagnosticTemplate.getData().toArray());
	}

	private void analyseView() {
		categoryValidationMap.clear();
		TreeIterator<EObject> eAllContents = renderable.eAllContents();
		while (eAllContents.hasNext()) {
			EObject eObject = eAllContents.next();
			if (org.eclipse.emf.ecp.view.model.AbstractControl.class.isInstance(eObject)) {
				org.eclipse.emf.ecp.view.model.AbstractControl control = (org.eclipse.emf.ecp.view.model.AbstractControl) eObject;
				for (EStructuralFeature structuralFeature : control.getTargetFeatures()) {
					Set<EObject> controls = categoryValidationMap.get(structuralFeature);
					if (controls == null) {
						controls = new HashSet<EObject>();
						categoryValidationMap.put(structuralFeature, controls);
					}
					controls.add(control);
					if (structuralFeature.isMany() && EReference.class.isInstance(structuralFeature)) {
						EReference eReference = (EReference) structuralFeature;
						if (eReference.isContainment()) {
							if (TableControl.class.isInstance(control)) {
								TableControl tc = (TableControl) control;
								for (TableColumn column : tc.getColumns()) {
									Set<EObject> controls2 = categoryValidationMap.get(column.getAttribute());
									if (controls2 == null) {
										controls2 = new HashSet<EObject>();
										categoryValidationMap.put(column.getAttribute(), controls2);
									}
									controls2.add(control);
								}
							} else {
								for (EStructuralFeature feature : eReference.getEReferenceType()
									.getEAllStructuralFeatures()) {
									Set<EObject> controls2 = categoryValidationMap.get(feature);
									if (controls2 == null) {
										controls2 = new HashSet<EObject>();
										categoryValidationMap.put(feature, controls2);
									}
									controls2.add(control);
								}
							}
						}
					}
					EObject parent = control.eContainer();
					while (!View.class.isInstance(parent)) {
						controls.add(parent);
						parent = parent.eContainer();
					}
				}
			}
		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void dispose() {
		alive = false;
		listeners.clear();
		context.getModelElement().eAdapters().remove(contentAdapter);
		validationMap.clear();
		categoryValidationMap.clear();
		selectionChangedListeners.clear();
		node.dispose();
		context = null;
		renderable = null;
		contentAdapter = null;
	}

	public interface ValidationListener {
		void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects);
	}

	public interface ValidationSeverityModifier {
		int getSeverityForDiagnostic(Diagnostic diagnostic, EStructuralFeature feature);
	}

	public Integer getSeverity(EObject object) {
		if (validationMap.containsKey(object)) {
			int maxValue = Diagnostic.OK;
			for (Diagnostic diagnostic : validationMap.get(object)) {
				if (diagnostic.getSeverity() > maxValue) {
					maxValue = diagnostic.getSeverity();
				}
			}
			return maxValue;
		}
		return Diagnostic.OK;
	}

	public Map<EObject, Set<Diagnostic>> getValidationMap() {
		return Collections.unmodifiableMap(validationMap);
	}

	public void setRenderedResult(CONTROL control) {
		this.control = control;
		this.contentAdapter = new EContentAdapter() {

			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);

				// node is null, since render hasn't been called yet
				if (node != null) {
					node.checkEnable(notification);
					node.checkShow(notification);
				}

				triggerValidation();
			}

			@Override
			protected void addAdapter(Notifier notifier) {
				super.addAdapter(notifier);
				// FIXME HACK
				analyseView();
			}

		};

		this.context.getModelElement().eAdapters().add(contentAdapter);
	}

	public CONTROL getControl() {
		return control;
	}

	public void addSelectionChangedListener(SelectedNodeChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	public void removeSelectionChangedListener(SelectedNodeChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	private <T extends Renderable> void fireSelectionChanged(T selectedRenderable) {
		for (SelectedNodeChangedListener listener : selectionChangedListeners) {
			listener.selectionChanged(selectedRenderable);
		}
	}

	public void childSelected(Node<?> child) {
		// trigger validation in order to update validation status of controls
		triggerValidation();
		fireSelectionChanged(child.getRenderable());
	}
}

package org.eclipse.emf.ecp.ui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.SelectedChildNodeListener;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;

public class RendererContext<CONTROL> implements SelectedChildNodeListener {

	final private Map<EStructuralFeature, Set<EObject>> categoryValidationMap = new HashMap<EStructuralFeature, Set<EObject>>();
	final private Map<EObject, Set<Diagnostic>> validationMap = new HashMap<EObject, Set<Diagnostic>>();

	private final Node<? extends Renderable> node;
	private boolean alive = true;
	private Renderable renderable;
	private ECPControlContext context;
	private final Set<ValidationListener> listeners = new HashSet<RendererContext.ValidationListener>();
	private ValidationSeverityModifier validationSeverityHandler;

	private EContentAdapter contentAdapter;
	private CONTROL control;
	private final List<SelectedNodeChangedListener> selectionChangedListeners;

	public RendererContext(final Node<? extends Renderable> node, final ECPControlContext context) {
		this.node = node;
		this.renderable = node.getRenderable();
		this.selectionChangedListeners = new ArrayList<SelectedNodeChangedListener>();
		this.context = context;

		// analyseView();

		if (node.getRenderable() instanceof View) {
			node.addSelectedChildNodeListener(this);
		}
	}

	public void setValidationSeverityHandler(ValidationSeverityModifier validationSeverityHandler) {
		this.validationSeverityHandler = validationSeverityHandler;
	}

	public void addListener(ValidationListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ValidationListener listener) {
		listeners.remove(listener);
	}

	public boolean isAlive() {
		return alive;
	}

	public void dispose() {
		alive = false;
		listeners.clear();
		// context.getModelElement().eAdapters().remove(contentAdapter);
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
			for (final Diagnostic diagnostic : validationMap.get(object)) {
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
		// this.contentAdapter = new EContentAdapter() {
		//
		// @Override
		// public void notifyChanged(final Notification notification) {
		// super.notifyChanged(notification);
		//
		// // // node is null, since render hasn't been called yet
		// // if (node != null) {
		// // node.checkEnable(notification);
		// // node.checkShow(notification);
		// // }
		//
		// // triggerValidation();
		// }
		//
		// // @Override
		// // protected void addAdapter(Notifier notifier) {
		// // super.addAdapter(notifier);
		// // // FIXME HACK
		// // analyseView();
		// // }
		//
		// };
		//
		// this.context.getModelElement().eAdapters().add(contentAdapter);
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
		for (final SelectedNodeChangedListener listener : selectionChangedListeners) {
			listener.selectionChanged(selectedRenderable);
		}
	}

	public void childSelected(Node<?> child) {
		// // trigger validation in order to update validation status of controls
		// triggerValidation();
		fireSelectionChanged(child.getRenderable());
	}
}

package org.eclipse.emf.ecp.ui.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.View;

public class RendererContext<CONTROL> {

    final private Map<EStructuralFeature, Set<EObject>> categoryValidationMap = new HashMap<EStructuralFeature, Set<EObject>>();
    final private Map<EObject, Set<Diagnostic>> validationMap = new HashMap<EObject, Set<Diagnostic>>();

    private RendererNode<CONTROL> node;
    private boolean alive = true;
    private EObject article;
    private Renderable renderable;
    private final Set<ValidationListener> listeners = new HashSet<RendererContext.ValidationListener>();

    private EContentAdapter contentAdapter;

    public RendererContext(Renderable renderable, EObject article) {
        this.renderable = renderable;
        analyseView();
        this.article = article;
        this.contentAdapter = new EContentAdapter() {

            @Override
            public void notifyChanged(final Notification notification) {
                super.notifyChanged(notification);

                triggerValidation();
                
                // node is null, since render hasn't been called yet
                if (getNode() != null) {
                	getNode().checkEnable(notification);
                	getNode().checkShow(notification);
                }
            }

        };
        this.article.eAdapters().add(contentAdapter);
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
        Diagnostic diagnostic = Diagnostician.INSTANCE.validate(article);
        validationMap.clear();
        for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
        	if (childDiagnostic.getData().size() < 2) {
        		continue;
        	}
        	EStructuralFeature feature = (EStructuralFeature) childDiagnostic.getData().get(1);

            Set<EObject> objectsToMark = categoryValidationMap.get(feature);
            if (objectsToMark == null)
                continue;
            for (EObject object : objectsToMark) {
                Set<Diagnostic> currentValues = validationMap.get(object);
                if (currentValues == null){
                    validationMap.put(object, new HashSet<Diagnostic>());
                }
                validationMap.get(object).add(childDiagnostic);
            }

        }
    }

    private void analyseView() {
        TreeIterator<EObject> eAllContents = renderable.eAllContents();
        while (eAllContents.hasNext()) {
            EObject eObject = eAllContents.next();
            if (org.eclipse.emf.ecp.view.model.Control.class.isInstance(eObject)) {
                org.eclipse.emf.ecp.view.model.Control control = (org.eclipse.emf.ecp.view.model.Control) eObject;
                EStructuralFeature structuralFeature = control.getTargetFeature();
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
                            TableControl tc=(TableControl)control;
                            for(TableColumn column:tc.getColumns()){
                                Set<EObject> controls2 = categoryValidationMap.get(column.getAttribute());
                                if (controls2 == null) {
                                    controls2 = new HashSet<EObject>();
                                    categoryValidationMap.put(column.getAttribute(), controls2);
                                }
                                controls2.add(control);
                            }
                        } else {
                            for (EStructuralFeature feature : eReference.getEReferenceType().getEAllStructuralFeatures()) {
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

    public RendererNode<CONTROL> getNode() {
        return node;
    }

    public void setNode(RendererNode<CONTROL> node) {
        this.node = node;
    }

    public boolean isAlive() {
        return alive;
    }

    public void dispose() {
        alive = false;
        listeners.clear();
        article.eAdapters().remove(contentAdapter);
        validationMap.clear();
        categoryValidationMap.clear();
        node.dispose();
        article = null;
        renderable = null;
        contentAdapter = null;
    }

    public interface ValidationListener {
        void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects);
    }

    public Integer getSeverity(EObject object) {
        if (validationMap.containsKey(object)){
            int maxValue=Diagnostic.OK;
            for(Diagnostic diagnostic:validationMap.get(object)){
                if(diagnostic.getSeverity()>maxValue){
                    maxValue=diagnostic.getSeverity();
                }
            }
            return maxValue;
        }
        return Diagnostic.OK;
    }

    public Map<EObject, Set<Diagnostic>> getValidationMap() {
        return Collections.unmodifiableMap(validationMap);
    }
}

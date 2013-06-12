package org.eclipse.emf.ecp.internal.ui.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableColumnConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableControlConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.ui.view.CompositeFactory;
import org.eclipse.emf.ecp.view.model.AndCondition;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.OrCondition;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.ecp.view.model.ShowRule;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;

public class CompositeFactoryImpl implements CompositeFactory {

    // public static CompositeFactoryImpl INSTANCE = new CompositeFactoryImpl();
    // TODO dispose
	private static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";
    private final ComposedAdapterFactory composedAdapterFactory;
    private final AdapterFactoryItemDelegator adapterFactoryItemDelegator;

    private final Map<Control, ECPControl> controlToControlMap = new HashMap<Control, ECPControl>();
    private Map<Condition, ConditionEvaluator> evaluators;

    // RAP theming custom variant constants
    private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_control_column";
    private static final String CONTROL_COLUMN_COMPOSITE = "org_eclipse_emf_ecp_ui_control_column_composite";

    public CompositeFactoryImpl() {
        composedAdapterFactory = new ComposedAdapterFactory(
                ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
        evaluators = new LinkedHashMap<Condition, ConditionEvaluator>();
    }
    

    @Override
    public void dispose() {
        for (ECPControl control : controlToControlMap.values()) {
            control.dispose();
        }

        composedAdapterFactory.dispose();
        controlToControlMap.clear();
        for (ConditionEvaluator evaluator : evaluators.values()) {
            evaluator.dispose();
        }
    }

    @Override
    public Composite getComposite(final Composite composite,
            final org.eclipse.emf.ecp.view.model.Composite modelComposite, 
            final EObject article,
            ECPControlContext controlContext) {
        
        Composite resultComposite = null;

        
        if (ViewPackage.eINSTANCE.getColumnComposite().isInstance(modelComposite)) {
            resultComposite = createColumnComposite(composite, (ColumnComposite) modelComposite,
                    article, controlContext);
        } else if (ViewPackage.eINSTANCE.getColumn().isInstance(modelComposite)) {
            resultComposite = createColumn(composite, (Column) modelComposite, article, controlContext);
        } else if (ViewPackage.eINSTANCE.getGroup().isInstance(modelComposite)) {
            resultComposite = createGroup(composite, (Group) modelComposite, article, controlContext);
        } else if (ViewPackage.eINSTANCE.getTableControl().isInstance(modelComposite)) {
            resultComposite = createTableControl(composite, (TableControl) modelComposite, article, controlContext);
        } else if (ViewPackage.eINSTANCE.getControl().isInstance(modelComposite)) {
            resultComposite = createControl(composite, (Control) modelComposite, article, controlContext);
        } else if (ViewPackage.eINSTANCE.getSeperator().isInstance(modelComposite)) {
            resultComposite = createSeperator(composite, (Seperator) modelComposite, article, controlContext);
        } else if (ViewPackage.eINSTANCE.getCustomComposite().isInstance(modelComposite)) {
            resultComposite = createCustomComposite(composite, (CustomComposite) modelComposite,
                    article, controlContext);
        }
        
        
        if (resultComposite != null) {
            addControlToMap(resultComposite, modelComposite);
            checkEnableRule(resultComposite, modelComposite, article);
            checkShowRule(new org.eclipse.swt.widgets.Control[] { resultComposite }, modelComposite, article, controlContext);
        }
        
        
        return resultComposite;
    }

    private void checkEnableRule(final org.eclipse.swt.widgets.Control resultComposite,
            final org.eclipse.emf.ecp.view.model.Composite modelComposite, EObject article) {
        if (EnableRule.class.isInstance(modelComposite.getRule())) {
            Condition condition = modelComposite.getRule().getCondition();
            if (!evaluators.containsKey(condition)) {
                evaluators.put(condition, new ConditionEvaluator(article, condition, 
                        new IConditionEvalResult() {
                    @Override
                    public void evalFinished(boolean result) {
                        if (result == ((EnableRule) modelComposite.getRule()).isDisable()) {
                            resultComposite.setEnabled(false);
                        } else {
                            resultComposite.setEnabled(true);
                        }
                    }
                }));
            }
        }
    }
    
    private void checkShowRule(
    		final org.eclipse.swt.widgets.Control[] resultControl,
            final org.eclipse.emf.ecp.view.model.Composite modelComposite, 
            final EObject article, 
            final ECPControlContext controlContext) {
    	
        if (ShowRule.class.isInstance(modelComposite.getRule())) {    
            Condition condition = modelComposite.getRule().getCondition();
            if (!evaluators.containsKey(condition)) {
                evaluators.put(condition, new ConditionEvaluator(article, condition, new IConditionEvalResult() {
                    @Override
                    public void evalFinished(boolean result) {
                        if (result == ((ShowRule) modelComposite.getRule()).isHide()) {
                            for (org.eclipse.swt.widgets.Control control : resultControl) {
                                if (control != null) {
                                    GridData gridData = (GridData) control.getLayoutData();
                                    if (gridData != null) {
                                        gridData.exclude = true;
                                    }
                                    control.setVisible(false);
                                    
                                    TreeIterator<EObject> eAllContents = modelComposite.eAllContents();
                                    while (eAllContents.hasNext()) {
                                        EObject eObject = eAllContents.next();
                                        unset(article, eObject, controlContext);
                                    }
                                    unset(article, modelComposite, controlContext);
                                }
                            }
                        } else {
                            for (org.eclipse.swt.widgets.Control control : resultControl) {
                                if (control != null) {
                                    GridData gridData = (GridData) control.getLayoutData();
                                    if (gridData != null) {
                                        gridData.exclude = false;
                                    }
                                    control.setVisible(true);
                                } 
                            }
                        }
                        
                        for (org.eclipse.swt.widgets.Control control : resultControl) {
                            if (control != null) {
                                Composite parent = control.getParent();
                                parent.layout(true, true);
                                break;
                            }
                        }
                        
                    }
                }));
            }
        }
    }
    
    private void unset(final EObject article, EObject eObject, ECPControlContext controlContext) {
        if (eObject instanceof Control) {
            Control control = (Control) eObject;
            EStructuralFeature targetFeature = control.getTargetFeature();
            if (targetFeature.isMany()) {
                Collection<?> collection = (Collection<?>) controlContext.getModelElement().eGet(targetFeature);
                collection.clear();
            } else {
                controlContext.getModelElement().eSet(targetFeature, null);
            }
        } else if (eObject instanceof Group) {   
            Group group = (Group) eObject;
            EList<org.eclipse.emf.ecp.view.model.Composite> composites = group.getComposites();
            for (org.eclipse.emf.ecp.view.model.Composite composite : composites) {
                unset(article, composite, controlContext);
            }
        }
    }

    private Composite createColumnComposite(Composite composite,
            ColumnComposite modelColumnComposite, EObject article, ECPControlContext controlContext) {

        Composite columnComposite = new Composite(composite, SWT.NONE);
        columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);
        GridLayoutFactory.fillDefaults().numColumns(modelColumnComposite.getComposites().size())
                .equalWidth(true).applyTo(columnComposite);
        int numHiddenColumns = 0;
        for (org.eclipse.emf.ecp.view.model.Composite modelComposite : modelColumnComposite
                .getComposites()) {
            Composite column = new Composite(columnComposite, SWT.NONE);
            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true)
                    .applyTo(column);
            GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(column);
            Composite subComposite = getComposite(column, modelComposite, article, controlContext);
            if (subComposite != null) {
                GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false)
                        .span(2, 1).applyTo(subComposite);
            } else {
                column.dispose();
                numHiddenColumns++;
            }
        }
        ((GridLayout) columnComposite.getLayout()).numColumns = modelColumnComposite
                .getComposites().size() - numHiddenColumns;

        return columnComposite;
    }

    private Composite createColumn(Composite composite, Column column, EObject article, ECPControlContext controlContext) {

        Composite columnComposite = new Composite(composite, SWT.NONE);
        columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN);
        GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(columnComposite);
        for (org.eclipse.emf.ecp.view.model.Composite modelComposite : column.getComposites()) {
            Composite subComposite = getComposite(columnComposite, modelComposite, article, controlContext);
            if (subComposite != null) {
                GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                        .span(2, 1).applyTo(subComposite);
            }
        }
        return columnComposite;
    }

    private Composite createGroup(Composite composite, Group modelGroup, EObject article, ECPControlContext controlContext) {

        org.eclipse.swt.widgets.Group group = new org.eclipse.swt.widgets.Group(composite,
                SWT.TITLE);
        group.setText(modelGroup.getName());

        GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(group);
        for (org.eclipse.emf.ecp.view.model.Composite modelComposite : modelGroup.getComposites()) {
            Composite subComposite = getComposite(group, modelComposite, article, controlContext);
            if (subComposite != null) {
                GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                        .span(2, 1).applyTo(subComposite);
            }
        }

        return group;
    }

    private Composite createCustomComposite(Composite composite, CustomComposite customComposite,
            EObject article, ECPControlContext controlContext) {
        try {
            Class<?> clazz = getClass(customComposite.getBundle(), customComposite.getClassName());
            Constructor<?> constructor = clazz.getConstructor(Composite.class, EObject.class);
            Object obj = constructor.newInstance(composite, article);
            Composite categoryComposite = (Composite) obj;
            GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL)
                    .applyTo(categoryComposite);
            return categoryComposite;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Class<?> getClass(String pluginID, String className) throws ClassNotFoundException {
        Bundle bundle = Platform.getBundle(pluginID);
        if (bundle == null) {
            throw new ClassNotFoundException(className
                    + " cannot be loaded because because bundle " + pluginID
                    + " cannot be resolved");
        } else {
            return bundle.loadClass(className);
        }
    }

    private final Map<EAttribute, Map<org.eclipse.emf.ecp.view.model.Composite, org.eclipse.swt.widgets.Control>> controlsWithRule = 
    		new HashMap<EAttribute, Map<org.eclipse.emf.ecp.view.model.Composite, org.eclipse.swt.widgets.Control>>();

    @SuppressWarnings("restriction")
    private Composite createControl(Composite composite, Control modelControl, final EObject article, ECPControlContext controlContext) {
        EClass dataClass = modelControl.getTargetFeature().getEContainingClass();
        
        ECPControlContext subContext = createSubcontext(modelControl, controlContext);
        
        if (dataClass == null)
            return null;

        IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
                .getPropertyDescriptor(subContext.getModelElement(),
                        modelControl.getTargetFeature());
        if (itemPropertyDescriptor == null)
            return null;

        ECPControlFactory controlFactory = Activator.getDefault().getECPControlFactory();
        if (controlFactory == null) {
            Activator.getDefault().ungetECPControlFactory();
            return null;
        }
        SWTControl control = controlFactory.createControl(SWTControl.class, itemPropertyDescriptor,
                subContext);

        if (control != null) {
            int numControl = 2;
            Label label = null;
            if (control.showLabel()) {
                numControl = 1;
                label = new Label(composite, SWT.NONE);
                label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label");
                String extra = "";
                if (((EStructuralFeature) itemPropertyDescriptor.getFeature(null)).getLowerBound() > 0) {
                    extra = "*";
                }
                label.setText(itemPropertyDescriptor.getDisplayName(subContext.getModelElement())
                        + extra);
                label.setToolTipText(itemPropertyDescriptor.getDescription(subContext.getModelElement()));
                GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
            }
            controlToControlMap.put(modelControl, control);
            Composite controlComposite = control.createControl(composite);

            addControlToMap(controlComposite, modelControl);
            controlComposite.setEnabled(!modelControl.isReadonly());
            
            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false)
                    .span(numControl, 1).applyTo(controlComposite);
            
            checkEnableRule(controlComposite, modelControl, article);
            checkShowRule(new org.eclipse.swt.widgets.Control[] { label, controlComposite }, modelControl, article, subContext);
        }
        Activator.getDefault().ungetECPControlFactory();
        return null;
    }


	/**
	 * @param modelControl
	 * @param controlContext
	 * @return
	 */
	private ECPControlContext createSubcontext(Control modelControl,
			ECPControlContext controlContext) {
		EObject parent = controlContext.getModelElement();
        
        for (EReference eReference : modelControl.getPathToFeature()) {
            EObject child = (EObject) parent.eGet(eReference);
            if (child == null) {
                child = EcoreUtil.create(eReference.getEReferenceType());
                parent.eSet(eReference, child);
            }
            parent = child;
        }
        ECPControlContext subContext = controlContext.createSubContext(parent);
		return subContext;
	}

    private Composite createTableControl(Composite composite, TableControl tableControl,
            EObject article, ECPControlContext controlContext) {
    	
    	ECPControlContext subContext = createSubcontext(tableControl, controlContext);
    	
        EClass dataClass = tableControl.getTargetFeature().getEContainingClass();
        if (dataClass == null)
            return null;

        IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
                .getPropertyDescriptor(subContext.getModelElement(),
                        tableControl.getTargetFeature());
        if (itemPropertyDescriptor == null)
            return null;

        TableControlConfiguration tcc = new TableControlConfiguration();
        tcc.setAddRemoveDisabled(tableControl.isAddRemoveDisabled());
        for (TableColumn column : tableControl.getColumns()) {
            tcc.getColumns().add(
                    new TableColumnConfiguration(column.isReadOnly(), column.getAttribute()));
        }

        org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl control = new org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl(
                false, itemPropertyDescriptor,
                (EStructuralFeature) itemPropertyDescriptor.getFeature(subContext
                        .getModelElement()), subContext, false, tcc);

        if (control != null) {
            int numControl = 2;
            Label label = null;
            if (control.showLabel()) {
                numControl = 1;
                label = new Label(composite, SWT.NONE);
                label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label");
                String extra = "";
                if (((EStructuralFeature) itemPropertyDescriptor.getFeature(null)).getLowerBound() > 0) {
                    extra = "*";
                }
                label.setText(itemPropertyDescriptor.getDisplayName(subContext.getModelElement())
                        + extra);
                label.setToolTipText(itemPropertyDescriptor.getDescription(subContext
                        .getModelElement()));
                GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
            }
            controlToControlMap.put(tableControl, control);
            Composite controlComposite = control.createControl(composite);

            addControlToMap(controlComposite, tableControl);
            controlComposite.setEnabled(!tableControl.isReadonly());
            checkEnableRule(controlComposite, tableControl, article);
            checkShowRule(new org.eclipse.swt.widgets.Control[] { label, controlComposite }, tableControl, article, subContext);

            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false)
                    .span(numControl, 1).applyTo(controlComposite);
        }
        
        Activator.getDefault().ungetECPControlFactory();
        return null;
    }

    private void addControlToMap(org.eclipse.swt.widgets.Control controlComposite,
            org.eclipse.emf.ecp.view.model.Composite modelControl) {
        if (modelControl.getRule() != null && EnableRule.class.isInstance(modelControl.getRule())) {
            addControlToMap(controlComposite, modelControl, modelControl.getRule().getCondition());
        }

    }

    private void addControlToMap(org.eclipse.swt.widgets.Control controlComposite,
            org.eclipse.emf.ecp.view.model.Composite modelControl, Condition condition) {
        if (LeafCondition.class.isInstance(condition)) {
            LeafCondition leafCondition = (LeafCondition) condition;
            Map<org.eclipse.emf.ecp.view.model.Composite, org.eclipse.swt.widgets.Control> controlMap = controlsWithRule
                    .get(leafCondition.getAttribute());
            if (controlMap == null) {
                controlMap = new HashMap<org.eclipse.emf.ecp.view.model.Composite, org.eclipse.swt.widgets.Control>();
                controlsWithRule.put(leafCondition.getAttribute(), controlMap);
            }
            controlMap.put(modelControl, controlComposite);
        } else if (AndCondition.class.isInstance(condition)) {
            AndCondition andCondition = (AndCondition) condition;
            for (Condition con : andCondition.getConditions()) {
                addControlToMap(controlComposite, modelControl, con);
            }
        } else if (OrCondition.class.isInstance(condition)) {
            OrCondition orCondition = (OrCondition) condition;
            for (Condition con : orCondition.getConditions()) {
                addControlToMap(controlComposite, modelControl, con);
            }
        }
    }

    private Composite createSeperator(Composite composite, Seperator modelComposite, EObject article, ECPControlContext controlContext) {

        Label label = new Label(composite, SWT.NONE);
        label.setText(modelComposite.getName());
        label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_ui_seperator");
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).span(2, 1)
                .applyTo(label);
        addControlToMap(label, modelComposite);
        checkEnableRule(label, modelComposite, article);
        checkShowRule(new org.eclipse.swt.widgets.Control[] { label }, modelComposite, article, controlContext);
        return null;
    }

    @Override
    public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
        for (Control control : controlToControlMap.keySet()) {
            controlToControlMap.get(control).resetValidation();
            if (affectedObjects.containsKey(control)) {
                for(Diagnostic diagnostic:affectedObjects.get(control))
                    controlToControlMap.get(control).handleValidation(diagnostic);
            }
        }
    }
    

}

package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.CompositeFactoryImpl;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.ModelRenderer;
import org.eclipse.emf.ecp.ui.view.CompositeFactory;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public final class ModelRendererImpl implements ModelRenderer {

    public static ModelRendererImpl INSTANCE = new ModelRendererImpl();

    private ModelRendererImpl() {
    }

    @Override
    public RendererContext render(Composite composite, View view, ECPControlContext controlContext) {
    	EObject modelElement = controlContext.getModelElement();
        RendererContext context = new RendererContext(view, modelElement);
        Composite renderedComposite = renderCategorization(composite, view, modelElement, controlContext);
        context.setComposite(renderedComposite);
        return context;
    }

    private Composite renderCategorization(Composite composite, Categorization categorization,
            EObject article, ECPControlContext controlContext) {
        TabFolder folder = new TabFolder(composite, SWT.NONE);
        for (AbstractCategorization abstractCategorization : categorization.getCategorizations()) {
            TabItem item = new TabItem(folder, SWT.NONE);
            item.setText(abstractCategorization.getName());
            Composite categoryComposite = renderAbstractCategory(folder, abstractCategorization,
                    article, controlContext);
            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true)
                    .applyTo(categoryComposite);
            item.setControl(categoryComposite);
        }

        return folder;
    }

    private Composite renderAbstractCategory(TabFolder folder,
            AbstractCategorization abstractCategorization, EObject article, ECPControlContext controlContext) {
        if (ViewPackage.eINSTANCE.getCategorization().isInstance(abstractCategorization)) {
            return renderCategorization(folder, (Categorization) abstractCategorization, article, controlContext);
        } else if (ViewPackage.eINSTANCE.getCategory().isInstance(abstractCategorization)) {
            return renderCategory(folder, (Category) abstractCategorization, article, controlContext);
        }
        return null;
    }

    private Composite renderCategory(Composite composite, Category category, EObject article, ECPControlContext controlContext) {
        ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.V_SCROLL
                | SWT.H_SCROLL);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true)
                .applyTo(scrolledComposite);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setExpandHorizontal(true);

        Composite tabContent = createComposite(scrolledComposite, category.getComposite(), article, controlContext);
        scrolledComposite.setContent(tabContent);
        Point point = tabContent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        tabContent.setSize(point);
        scrolledComposite.setMinSize(point);
        return scrolledComposite;
    }

    private Composite createComposite(Composite composite,
            org.eclipse.emf.ecp.view.model.Composite modelComposite, EObject article, ECPControlContext controlContext) {
        CompositeFactory factory = new CompositeFactoryImpl();
        Composite result = factory.getComposite(composite, modelComposite, controlContext);
        // factory.updateLiveValidation(article);
        return result;
    }
}

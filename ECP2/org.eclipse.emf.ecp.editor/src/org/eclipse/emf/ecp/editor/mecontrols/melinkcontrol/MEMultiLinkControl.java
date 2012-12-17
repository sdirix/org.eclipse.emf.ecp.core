/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import java.util.ArrayList;

/**
 * GUI Control for the ME reference multilinks.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public class MEMultiLinkControl extends AbstractMEControl {

	/**
	 * Command to rebuild the links.
	 * 
	 * @author helming
	 */
	// private final class RebuildLinksCommand extends ChangeCommand {
	// private final int sizeLimit;
	//
	// public RebuildLinksCommand(EObject modelElement, int sizeLimit) {
	// super(modelElement);
	// this.sizeLimit = sizeLimit;
	// }
	//
	// private void doRun() {
	// Object objectList = getModelElement().eGet(getStructuralFeature());
	// if (objectList instanceof EList) {
	// EList<EObject> eList = (EList<EObject>) objectList;
	// if (eList.size() <= sizeLimit) {
	// linkArea = getToolkit().createComposite(composite, style);
	// linkArea.setLayout(tableLayout);
	// linkArea.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));// GridData.FILL_HORIZONTAL
	// } else {
	//
	// scrollPane = new ScrolledComposite(composite, SWT.V_SCROLL | SWT.H_SCROLL);
	// scrollPane.setBackgroundMode(SWT.INHERIT_FORCE);
	// scrollClient = new Composite(scrollPane, style);
	// scrollPane.setContent(scrollClient);
	// getToolkit().getColors().createColor("white", 255, 255, 255);
	// scrollClient.setBackground(getToolkit().getColors().getColor("white"));
	// scrollPane.setExpandVertical(true);
	// scrollPane.setExpandHorizontal(true);
	// RowLayout layout = new RowLayout(SWT.VERTICAL);
	// layout.wrap = true;
	// scrollClient.setLayout(layout);
	// GridData spec = new GridData(400, 150);
	// spec.horizontalAlignment = GridData.FILL;
	// spec.grabExcessHorizontalSpace = true;
	// scrollPane.setLayoutData(spec);
	// scrollPane.setMinSize(150, 150);
	// }
	//
	// for (EObject object : eList) {
	//
	// MELinkControlFactory controlFactory = MELinkControlFactory.getInstance();
	// MELinkControl meControl = controlFactory.createMELinkControl(getItemPropertyDescriptor(), object,
	// getModelElement(), getContext());
	// meControl.createControl(eList.size() <= sizeLimit ? linkArea : scrollClient, style,
	// getItemPropertyDescriptor(), object, getModelElement(), getToolkit(), getContext());
	// linkControls.add(meControl);
	//
	// }
	// if (scrollPane != null && !scrollPane.isDisposed()) {
	// scrollPane.setMinSize(scrollClient.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	// scrollClient.layout();
	// scrollPane.layout();
	// } else {
	// linkArea.layout();
	// }
	// if (eList.size() > 0) {
	// section.setExpanded(false);
	// section.setExpanded(true);
	// }
	// }
	// }
	//
	// /*
	// * (non-Javadoc)
	// * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
	// */
	// @Override
	// protected void doExecute() {
	// doRun();
	// }
	// }

	// private EReference eReference;
	final int sizeLimit = 5;
	private int style;

	private ScrolledComposite scrollPane;

	private Section section;

	private Composite linkArea;

	private Composite composite;

	private ArrayList<MELinkControl> linkControls;

	private GridLayout tableLayout;

	private Composite scrollClient;

	private DropTarget sectionDropTarget;

	// private static final int PRIORITY = 1;

	// private Object feature;

	private org.eclipse.emf.ecp.editor.ModelElementChangeListener modelElementChangeListener;

	private Cursor handCursor;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	// private RebuildLinksCommand rebuildLinksCommand;

	private void doRun() {
		Object objectList = getModelElement().eGet(getStructuralFeature());
		if (objectList instanceof EList) {
			EList<EObject> eList = (EList<EObject>) objectList;
			if (eList.size() <= sizeLimit) {
				linkArea = getToolkit().createComposite(composite, style);
				linkArea.setLayout(tableLayout);
				linkArea.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));// GridData.FILL_HORIZONTAL
			} else {

				scrollPane = new ScrolledComposite(composite, SWT.V_SCROLL | SWT.H_SCROLL);
				scrollPane.setBackgroundMode(SWT.INHERIT_FORCE);
				scrollClient = new Composite(scrollPane, style);
				scrollPane.setContent(scrollClient);
				getToolkit().getColors().createColor("white", 255, 255, 255);
				scrollClient.setBackground(getToolkit().getColors().getColor("white"));
				scrollPane.setExpandVertical(true);
				scrollPane.setExpandHorizontal(true);
				RowLayout layout = new RowLayout(SWT.VERTICAL);
				layout.wrap = true;
				scrollClient.setLayout(layout);
				GridData spec = new GridData(400, 150);
				spec.horizontalAlignment = GridData.FILL;
				spec.grabExcessHorizontalSpace = true;
				scrollPane.setLayoutData(spec);
				scrollPane.setMinSize(150, 150);
			}

			for (EObject object : eList) {

				MELinkControlFactory controlFactory = MELinkControlFactory.getInstance();
				MELinkControl meControl = controlFactory.createMELinkControl(getItemPropertyDescriptor(), object,
					getModelElement(), getContext());
				meControl.createControl(eList.size() <= sizeLimit ? linkArea : scrollClient, style,
					getItemPropertyDescriptor(), object, getModelElement(), getToolkit(), getContext());
				linkControls.add(meControl);

			}
			if (scrollPane != null && !scrollPane.isDisposed()) {
				scrollPane.setMinSize(scrollClient.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				scrollClient.layout();
				scrollPane.layout();
			} else {
				linkArea.layout();
			}
			if (eList.size() > 0) {
				section.setExpanded(false);
				section.setExpanded(true);
			}
		}
	}

	private void createSectionToolbar(Section section, FormToolkit toolkit) {
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(section);
		handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);
		// Cursor needs to be explicitly disposed
		toolbar.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (!handCursor.isDisposed()) {
					handCursor.dispose();
				}
			}
		});

		toolBarManager.add(new AddReferenceAction(getModelElement(), (EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), getContext(), getShell(), adapterFactoryLabelProvider));
		toolBarManager.add(new NewReferenceAction(getModelElement(), (EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), getContext(), getShell(), adapterFactoryLabelProvider));
		toolBarManager.update(true);
		section.setTextClient(toolbar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Control createControl(final Composite parent, int style) {
		linkControls = new ArrayList<MELinkControl>();
		// feature = getItemPropertyDescriptor().getFeature(getModelElement());
		// eReference = (EReference)feature;
		modelElementChangeListener = new org.eclipse.emf.ecp.editor.ModelElementChangeListener(getModelElement()) {

			@Override
			public void onChange(Notification notification) {
				if (notification.getEventType() != Notification.RESOLVE
					&& notification.getFeature().equals(getStructuralFeature())) {
					rebuildLinkSection();
				}

			}

		};
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);

		this.style = style;
		tableLayout = new GridLayout(1, false);
		section = getToolkit().createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		section.setText(getItemPropertyDescriptor().getDisplayName(getModelElement()));
		createSectionToolbar(section, getToolkit());
		composite = getToolkit().createComposite(section, style);
		composite.setLayout(tableLayout);

		rebuildLinkSection();

		section.setClient(composite);
		addDnDSupport();
		return section;
	}

	private void addDnDSupport() {
		sectionDropTarget = new DropTarget(section, DND.DROP_COPY);
		Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		sectionDropTarget.setTransfer(transfers);
		sectionDropTarget.addDropListener(new MEMultiLinkControlDropAdapter(getModelElement(),
			(EReference) getStructuralFeature(), getContext()));

	}

	/**
	 * Method for refreshing (rebuilding) the composite section.
	 */
	private void rebuildLinkSection() {
		final int sizeLimit = 5;

		for (MELinkControl link : linkControls) {
			link.dispose();
		}
		if (scrollPane != null) {
			scrollPane.dispose();
		}
		if (linkArea != null) {
			linkArea.dispose();
		}
		linkControls.clear();
		doRun();
		// JH: TransactionUtil.getEditingDomain(modelElement);
		// if (rebuildLinksCommand != null) {
		// rebuildLinksCommand.dispose();
		// rebuildLinksCommand = null;
		// }

		// getContext().getEditingDomain().getCommandStack()
		// .execute(new RebuildLinksCommand(getModelElement(), sizeLimit));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		modelElementChangeListener.remove();
		for (MELinkControl link : linkControls) {
			link.dispose();
		}
		if (sectionDropTarget != null) {
			sectionDropTarget.dispose();
		}
		handCursor.dispose();
		// if (rebuildLinksCommand != null) {
		// rebuildLinksCommand.dispose();
		// rebuildLinksCommand = null;
		// }
		section.dispose();
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
	}

	// @Override
	// public int canRender(IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement)
	// {
	// Object feature = itemPropertyDescriptor.getFeature(modelElement);
	// if (feature instanceof EReference
	// && EObject.class.isAssignableFrom(((EReference)feature).getEType().getInstanceClass())
	// && ((EReference)feature).isMany())
	// {
	//
	// return PRIORITY;
	// }
	// return AbstractMEControl.DO_NOT_RENDER;
	// }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getPriority()
	 */
	@Override
	protected int getPriority() {
		// TODO Auto-generated method stub
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getEStructuralFeatureType()
	 */
	@Override
	protected Class<? extends EStructuralFeature> getEStructuralFeatureType() {
		return EReference.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return EObject.class;
	}

	@Override
	protected boolean isMulti() {
		return true;
	}

}

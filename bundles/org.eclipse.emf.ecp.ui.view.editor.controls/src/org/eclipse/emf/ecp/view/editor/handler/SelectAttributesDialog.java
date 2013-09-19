package org.eclipse.emf.ecp.view.editor.handler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SelectAttributesDialog extends Dialog {

	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryLabelProvider labelProvider;
	private final ECPProject project;
	private final Set<EStructuralFeature> selectedFeatures = new LinkedHashSet<EStructuralFeature>();
	private EClass dataSegment;
	private final EClass rootClass;

	public SelectAttributesDialog(ECPProject project, EClass rootClass, Shell parentShell) {
		super(parentShell);
		this.project = project;
		this.rootClass = rootClass;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		((GridLayout) composite.getLayout()).numColumns = 2;

		final Label labelDatasegment = new Label(composite, SWT.NONE);
		labelDatasegment.setText("Select Datasegment");

		final ComboViewer cvDatasegment = new ComboViewer(composite, SWT.READ_ONLY);
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		labelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);

		cvDatasegment.setLabelProvider(labelProvider);
		cvDatasegment.setContentProvider(ArrayContentProvider.getInstance());

		final Button bUnreferenced = new Button(composite, SWT.CHECK);
		bUnreferenced.setText("Show only unreferenced Attributes?");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).span(2, 1)
			.applyTo(bUnreferenced);

		final Label labelAttributes = new Label(composite, SWT.NONE);
		labelAttributes.setText("Select Attributes");

		final CheckboxTableViewer tvAttributes = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
		tvAttributes.setLabelProvider(labelProvider);
		tvAttributes.setContentProvider(ArrayContentProvider.getInstance());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).hint(SWT.DEFAULT, 200)
			.applyTo(tvAttributes.getControl());

		tvAttributes.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				final EStructuralFeature object = (EStructuralFeature) event.getElement();
				if (event.getChecked()) {
					selectedFeatures.add(object);
				} else {
					selectedFeatures.remove(object);
				}

			}
		});

		cvDatasegment.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				dataSegment = (EClass) ((IStructuredSelection) event.getSelection()).getFirstElement();
				List<EStructuralFeature> attributes = null;
				if (!bUnreferenced.getSelection()) {
					attributes = dataSegment.getEAllStructuralFeatures();
				} else {
					attributes = getUnreferencedSegmentAttributes(dataSegment);
				}
				tvAttributes.setInput(attributes);
			}
		});

		bUnreferenced.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final EClass dataSegment = (EClass) ((IStructuredSelection) cvDatasegment.getSelection())
					.getFirstElement();
				List<EStructuralFeature> attributes = null;
				if (!bUnreferenced.getSelection()) {
					attributes = dataSegment.getEAllStructuralFeatures();
				} else {
					attributes = getUnreferencedSegmentAttributes(dataSegment);
				}
				tvAttributes.setInput(attributes);
			}

		});

		final Composite compositeButtons = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(compositeButtons);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).span(2, 1)
			.applyTo(compositeButtons);

		final Button bSelectAll = new Button(compositeButtons, SWT.PUSH);
		bSelectAll.setText("Select All");
		bSelectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final List<EStructuralFeature> segments = (List<EStructuralFeature>) tvAttributes.getInput();
				tvAttributes.setAllChecked(true);
				selectedFeatures.addAll(segments);
			}

		});
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(bSelectAll);
		final Button bDeSelectAll = new Button(compositeButtons, SWT.PUSH);
		bDeSelectAll.setText("Deselect All");
		bDeSelectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final List<EStructuralFeature> segments = (List<EStructuralFeature>) tvAttributes.getInput();
				tvAttributes.setAllChecked(false);
				selectedFeatures.removeAll(segments);
			}

		});
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(bDeSelectAll);

		final Set<EClass> datasegments = getDatasegmentSubclasses(rootClass);
		cvDatasegment.setInput(datasegments);
		if (datasegments.size() > 0) {
			cvDatasegment.setSelection(new StructuredSelection(datasegments.iterator().next()));
		}
		return composite;
	}

	@Override
	public boolean close() {
		labelProvider.dispose();
		composedAdapterFactory.dispose();
		return super.close();
	}

	private Set<EClass> getDatasegmentInput() {
		final EPackage customerPackage = rootClass.getEPackage();
		final Set<EClass> possibleSegments = new LinkedHashSet<EClass>();
		for (final EClassifier classifier : customerPackage.getEClassifiers()) {
			if (!EClass.class.isInstance(classifier)) {
				continue;
			}
			possibleSegments.add((EClass) classifier);

		}
		return possibleSegments;

	}

	private Set<EClass> getDatasegmentSubclasses(EClass root) {
		final Set<EClass> possibleSegments = new LinkedHashSet<EClass>();
		possibleSegments.add(root);
		for (final EReference eReference : root.getEAllContainments()) {
			if (eReference.getEReferenceType() != root) {
				possibleSegments.addAll(getDatasegmentSubclasses(eReference.getEReferenceType()));
			}
		}
		return possibleSegments;

	}

	private List<EStructuralFeature> getUnreferencedSegmentAttributes(EClass eClass) {
		final List<EStructuralFeature> result = new ArrayList<EStructuralFeature>();
		final List<EStructuralFeature> allStructuralFeatures = new ArrayList<EStructuralFeature>(
			eClass.getEAllStructuralFeatures());
		for (final Object rootElement : project.getContents()) {
			if (View.class.isInstance(rootElement)) {
				final View viewConfiguration = (View) rootElement;
				final TreeIterator<EObject> eAllContents = viewConfiguration.eAllContents();
				while (eAllContents.hasNext()) {
					final EObject eObject = eAllContents.next();
					if (org.eclipse.emf.ecp.view.model.Control.class.isInstance(eObject)) {
						final org.eclipse.emf.ecp.view.model.Control control = (org.eclipse.emf.ecp.view.model.Control) eObject;

						final EStructuralFeature feature = control.getDomainModelReference().getModelFeature();

						if (feature != null && feature.getEContainingClass().equals(eClass)) {
							result.add(control.getDomainModelReference().getModelFeature());
						}
					}
				}

			}
		}

		allStructuralFeatures.removeAll(result);
		return allStructuralFeatures;
	}

	public Set<EStructuralFeature> getSelectedFeatures() {
		return selectedFeatures;
	}

	public EClass getDataSegment() {
		return dataSegment;
	}

}

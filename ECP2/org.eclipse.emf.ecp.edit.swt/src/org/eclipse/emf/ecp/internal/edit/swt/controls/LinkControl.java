package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.swt.actions.AddReferenceAction;
import org.eclipse.emf.ecp.edit.swt.actions.DeleteReferenceAction;
import org.eclipse.emf.ecp.edit.swt.actions.NewReferenceAction;
import org.eclipse.emf.ecp.editor.util.ECPObservableValue;
import org.eclipse.emf.ecp.editor.util.ModelElementChangeListener;
import org.eclipse.emf.ecp.internal.edit.swt.provider.ShortLabelProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

public class LinkControl extends SingleControl {

	public LinkControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	private Composite linkComposite;

	private Link hyperlink;

	private Label imageHyperlink;

	private EObject linkModelElement;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	private ShortLabelProvider shortLabelProvider;

	private ModelElementChangeListener modelElementChangeListener;

	private Label unsetLabel;

	private StackLayout stackLayout;

	private Composite mainComposite;

	private ModelElementChangeListener linkedModelElementChangeListener;

	private Button setNullButton;

	private Button addButton;

	private Button newButton;
	
	@Override
	protected void fillInnerComposite(Composite composite) {
		GridLayoutFactory.fillDefaults().numColumns(4).spacing(0, 0).equalWidth(false).applyTo(composite);
		
		mainComposite = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(mainComposite);

		stackLayout = new StackLayout();
		mainComposite.setLayout(stackLayout);

		unsetLabel = new Label(mainComposite, SWT.NONE);
		unsetLabel.setText("(Not Set)");
		unsetLabel.setBackground(composite.getBackground());
		unsetLabel.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);

		linkComposite = new Composite(mainComposite, SWT.NONE);
		linkComposite.setLayout(new GridLayout(2, false));
		
		createHyperlink();
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(linkComposite);
		if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			stackLayout.topControl = linkComposite;
		} else {
			stackLayout.topControl = unsetLabel;
		}
		
		setNullButton = createButtonForAction(new DeleteReferenceAction(getModelElementContext(), getItemPropertyDescriptor(), getStructuralFeature()), composite);
		addButton = createButtonForAction(new AddReferenceAction(getModelElementContext(), getItemPropertyDescriptor(), getStructuralFeature()), composite);
		newButton = createButtonForAction(new NewReferenceAction(getModelElementContext(), getItemPropertyDescriptor(), getStructuralFeature()), composite);
	}

	private void createHyperlink() {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		shortLabelProvider = new ShortLabelProvider(composedAdapterFactory);
		modelElementChangeListener = new ModelElementChangeListener(getModelElementContext().getModelElement()) {

			@Override
			public void onChange(Notification notification) {
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						if (mainComposite.isDisposed()) {
							return;
						}
						if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
							stackLayout.topControl = linkComposite;
							setLinkModelElement();
							setLinkChangeListener();
						} else {
							stackLayout.topControl = unsetLabel;
							linkModelElement = null;
						}
						mainComposite.layout();
					}

				});

			}
		};

		imageHyperlink = new Label(linkComposite, SWT.NONE);

		// TODO: Reactivate
		// ModelElementClassTooltip.enableFor(imageHyperlink);
		hyperlink = new Link(linkComposite, SWT.NONE);
		String text = shortLabelProvider.getText(linkModelElement);
		hyperlink.setText("<a>" + text + "</a>");
		hyperlink.setToolTipText(text);
		hyperlink.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				getModelElementContext().openEditor(linkModelElement, "org.eclipse.emf.ecp.editor");
			}

		});
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(hyperlink);

	}
	
	private void updateValues() {
		if (linkModelElement != null) {
			Image image = shortLabelProvider.getImage(linkModelElement);
			imageHyperlink.setImage(image);
			imageHyperlink.setData(linkModelElement.eClass());
			String text = shortLabelProvider.getText(linkModelElement);
			hyperlink.setText("<a>" + text + "</a>");
			hyperlink.setToolTipText(text);
			hyperlink.update();
			// imageHyperlink.layout(true);

			// if (listener != null) {
			// hyperlink.removeHyperlinkListener(listener);
			// imageHyperlink.removeHyperlinkListener(listener);
			// listener = null;
			// }
			// if (listener == null) {
			// listener = new MEHyperLinkAdapter(linkModelElement, getModelElementContext());
			// hyperlink.addHyperlinkListener(listener);
			// imageHyperlink.addHyperlinkListener(listener);
			// }
		}
	}

	private void setLinkChangeListener() {

		if (linkedModelElementChangeListener != null) {
			linkedModelElementChangeListener.remove();
		}
		if (linkModelElement != null) {
			linkedModelElementChangeListener = new ModelElementChangeListener(linkModelElement) {

				@Override
				public void onChange(Notification notification) {
					Display.getDefault().syncExec(new Runnable() {

						public void run() {
							updateValues();

						}

					});

				}
			};
		}
	}

	private void setLinkModelElement() {
		if (!getStructuralFeature().isMany()) {
			linkModelElement = (EObject) getModelElementContext().getModelElement().eGet(getStructuralFeature());
		}
		updateValues();
	}
	
	@Override
	public void setEditable(boolean isEditable) {
		setNullButton.setVisible(isEditable);
		addButton.setVisible(isEditable);
		newButton.setVisible(isEditable);
		mainComposite.getParent().layout();
	}

	@Override
	public void bindValue() {
		if (ECPObservableValue.class.isInstance(getModelValue())) {
			linkModelElement = (EObject) ((ECPObservableValue) getModelValue()).getValue();
		}
		setLinkModelElement();
		setLinkChangeListener();
	}

	@Override
	public void dispose() {
		adapterFactoryLabelProvider.dispose();
		composedAdapterFactory.dispose();
		shortLabelProvider.dispose();
		modelElementChangeListener.remove();
		if (linkedModelElementChangeListener != null) {
			linkedModelElementChangeListener.remove();
		}
		linkModelElement = null;
		hyperlink.dispose();
		super.dispose();
	}
}

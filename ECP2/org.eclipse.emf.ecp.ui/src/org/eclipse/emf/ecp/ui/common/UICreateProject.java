package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.ui.model.ProvidersLabelProvider;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.List;

public class UICreateProject
{

  public interface CreateProjectChangeListener
  {
    public void projectNameChanged(String projectName);

    public void providerChanged(ECPProvider provider);
  }

  public UICreateProject(List<ECPProvider> providers)
  {
    this.providers = providers;
  }

  private CreateProjectChangeListener listener;

  private final List<ECPProvider> providers;

  private ComboViewer providersViewer;

  private ECPProvider provider;

  private String projectName;

  public Composite createUI(Composite parent)
  {

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    composite.setLayout(new GridLayout(2, false));
    if (providers.size() > 1)
    {
      Label label = new Label(composite, SWT.NONE);
      label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
      label.setText("Provider:");

      providersViewer = new ComboViewer(composite, SWT.NONE);
      Combo combo = providersViewer.getCombo();
      GridData gd_combo = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
      gd_combo.minimumWidth = 150;
      combo.setLayoutData(gd_combo);
      providersViewer.setContentProvider(new ArrayContentProvider());
      providersViewer.setLabelProvider(new ProvidersLabelProvider());
      providersViewer.setSorter(new ViewerSorter());
      providersViewer.setInput(ECPProviderRegistry.INSTANCE);
      providersViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection)event.getSelection();
          provider = (ECPProvider)selection.getFirstElement();
          if (listener != null)
          {
            listener.providerChanged(provider);
          }
        }
      });
    }
    else if (providers.size() == 1)
    {
      provider = providers.get(0);
    }
    Label labelName = new Label(composite, SWT.NONE);
    labelName.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
    labelName.setText("Projectname:");

    final Text textProjectName = new Text(composite, SWT.BORDER | SWT.SINGLE);
    textProjectName.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));
    textProjectName.addModifyListener(new ModifyListener()
    {

      public void modifyText(ModifyEvent e)
      {
        projectName = textProjectName.getText();
        if (projectName.equals(""))
        {
          projectName = null;
        }
        if (listener != null)
        {
          listener.projectNameChanged(projectName);
        }
      }
    });
    if (providers.size() > 1)
    {
      providersViewer.setSelection(new StructuredSelection(providers.get(0)));
    }
    return composite;
  }

  /**
   * @return the provider
   */
  public ECPProvider getProvider()
  {
    return provider;
  }

  /**
   * @return the projectName
   */
  public String getProjectName()
  {
    return projectName;
  }

  /**
   * @param listener
   *          the listener to set
   */
  public void setListener(CreateProjectChangeListener listener)
  {
    this.listener = listener;
  }
}

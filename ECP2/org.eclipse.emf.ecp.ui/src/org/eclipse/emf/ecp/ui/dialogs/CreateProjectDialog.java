/**
 * 
 */
package org.eclipse.emf.ecp.ui.dialogs;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.ecp.ui.model.ProvidersLabelProvider;
import org.eclipse.emf.ecp.ui.model.TreeContentProvider;

import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Eugen Neufeld
 */
public class CreateProjectDialog extends TitleAreaDialog
{

  private String projectName;

  private ECPProvider provider;

  /**
   * @param parentShell
   */
  public CreateProjectDialog(Shell parentShell)
  {
    super(parentShell);
    setShellStyle(SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);

    projectName = "";
  }

  public final ECPProvider getProvider()
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

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setTitle("Create Project");
    setTitleImage(Activator.getImage("icons/checkout_project_wiz.png"));
    setMessage("Select a provider and set the project name.");

    Composite area = (Composite)super.createDialogArea(parent);
    Composite composite = new Composite(area, SWT.NONE);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    composite.setLayout(new GridLayout(2, false));

    Label label = new Label(composite, SWT.NONE);
    label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
    label.setText("Provider:");

    ComboViewer providersViewer = new ComboViewer(composite, SWT.NONE);
    Combo combo = providersViewer.getCombo();
    GridData gd_combo = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
    gd_combo.minimumWidth = 150;
    combo.setLayoutData(gd_combo);
    providersViewer.setContentProvider(new CreateProjetContentProvider());
    providersViewer.setLabelProvider(new ProvidersLabelProvider());
    providersViewer.setSorter(new ViewerSorter());
    providersViewer.setInput(ECPProviderRegistry.INSTANCE);
    providersViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        provider = (ECPProvider)selection.getFirstElement();
      }
    });

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
      }
    });

    for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders())
    {
      if (provider.hasUnsharedProjectSupport())
      {
        providersViewer.setSelection(new StructuredSelection(provider));
        break;
      }
    }

    return area;
  }

  @Override
  protected void cancelPressed()
  {
    projectName = null;
    super.cancelPressed();
  }

  private class CreateProjetContentProvider extends TreeContentProvider<ECPProviderRegistry> implements
      ECPProviderRegistry.Listener
  {

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.emf.ecp.core.ECPProviderRegistry.Listener#providersChanged(org.eclipse.emf.ecp.core.ECPProvider[],
     * org.eclipse.emf.ecp.core.ECPProvider[])
     */
    public void providersChanged(ECPProvider[] oldProviders, ECPProvider[] newProviders) throws Exception
    {
      this.refreshViewer();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.emf.ecp.ui.model.TreeContentProvider#fillChildren(java.lang.Object,
     * org.eclipse.emf.ecp.spi.core.util.InternalChildrenList)
     */
    @Override
    protected void fillChildren(Object parent, InternalChildrenList childrenList)
    {
      if (parent == ECPProviderRegistry.INSTANCE)
      {
        ECPProvider[] providers = ECPProviderRegistry.INSTANCE.getProviders();

        for (ECPProvider provider : providers)
        {
          if (provider.hasUnsharedProjectSupport())
          {
            childrenList.addChild(provider);
          }
        }
      }
    }
  }
}

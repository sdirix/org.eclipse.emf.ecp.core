package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Eugen Neufeld
 */
// TODO: Revise
public class DynamicContainmentCommands extends CompoundContributionItem
{
  private static AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(
      new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));

  //
  private static final String COMMAND_ID = "org.eclipse.emf.ecp.navigator.createContaiment";

  /**
   * . {@inheritDoc}
   */
  @Override
  protected IContributionItem[] getContributionItems()
  {
    // 1. get selected EObject
    Object selection = getSelection();
    if (selection == null)
    {
      return new IContributionItem[0];
    }
    // if (selection instanceof ECPProject)
    // {
    // return createNewWizard();
    // }

    if (!(selection instanceof EObject))
    {
      return new IContributionItem[0];
    }

    EObject selectedME = (EObject)selection;

    AdapterFactoryItemDelegator delegator = new AdapterFactoryItemDelegator(new ComposedAdapterFactory(
        ComposedAdapterFactory.Descriptor.Registry.INSTANCE));

    Collection<?> collection = delegator.getNewChildDescriptors(selectedME,
        AdapterFactoryEditingDomain.getEditingDomainFor(selectedME), null);
    // if (collection.size() > 5)
    // {
    // return createNewWizard(selectedME.eClass());
    // }
    if (collection instanceof List<?>)
    {
      @SuppressWarnings("unchecked")
      List<CommandParameter> childDescriptors = (List<CommandParameter>)delegator.getNewChildDescriptors(selectedME,
          AdapterFactoryEditingDomain.getEditingDomainFor(selectedME), null);
      List<EReference> containments = new ArrayList<EReference>();
      for (CommandParameter cp : childDescriptors)
      {
        containments.add(cp.getEReference());
      }

      IContributionItem[] commands = createCommands(containments);
      return commands;
    }

    // 3. create commands for these containments
    List<EReference> containments = selectedME.eClass().getEAllContainments();
    IContributionItem[] commands = createCommands(containments);
    return commands;

    // return new IContributionItem[0];

  }

  private IContributionItem[] createNewWizard()
  {
    CommandContributionItemParameter commandParam = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
        null, "org.eclipse.emf.ecp.ui.wizards.newModelElementWizard", CommandContributionItem.STYLE_PUSH);
    List<IContributionItem> commands = new ArrayList<IContributionItem>();
    commandParam.label = "New Model Element";
    // TODO: Reactivate
    // Image image = labelProvider.getImage(eClass);
    // ImageDescriptor imageDescriptor = ImageDescriptor.createFromImage(image);
    // commandParam.icon = imageDescriptor;
    CommandContributionItem command = new CommandContributionItem(commandParam);
    commands.add(command);

    return commands.toArray(new IContributionItem[commands.size()]);
  }

  /**
   * .
   * 
   * @param containments
   *          a list of EReference of containments of selected ME
   * @return an array of IContributionsItem (commands) to create different types of containments.
   */
  private IContributionItem[] createCommands(List<EReference> containments)
  {

    List<IContributionItem> commands = new ArrayList<IContributionItem>();

    // every command takes its corresponding EClass type as parameter
    for (EReference containment : containments)
    {

      // do not create any commands for containments with multiplicity one
      if (!containment.isMany())
      {
        continue;
      }

      // try
      // {
      // if (ECPWorkspaceManager.getInstance().getWorkSpace().getProject(selectedME).getMetaModelElementContext()
      // .isNonDomainElement(containment.getEReferenceType()))
      // {
      // continue;
      // }
      // }
      // catch (NoWorkspaceException e)
      // {
      // Activator.getDefault().logException(e.getMessage(), e);
      // }

      // if containment type is abstract, create a list of
      // commands for its subclasses
      if (containment.getEReferenceType().isAbstract() || containment.getEReferenceType().isInterface())
      {

        // note that a reference of commands array is passed,
        // corresponding commands are created and added to it,
        // then continue
        // TODO: fix
        // addCommandsForSubTypes(containment.getEReferenceType(), commands);
        continue;
      }

      CommandContributionItemParameter commandParam = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
          null, COMMAND_ID, CommandContributionItem.STYLE_PUSH);

      Map<Object, Object> commandParams = new HashMap<Object, Object>();

      // commandParams.put(CreateContainmentHandler.COMMAND_ECLASS_PARAM, containment.getEReferenceType());
      commandParam.label = "New " + containment.getEReferenceType().getName();
      commandParam.icon = getImage(containment.getEReferenceType());

      // create command
      commandParam.parameters = commandParams;
      CommandContributionItem command = new CommandContributionItem(commandParam);
      commands.add(command);
    }

    return commands.toArray(new IContributionItem[commands.size()]);

  }

  private ImageDescriptor getImage(EClass eClass)
  {
    EObject instance = eClass.getEPackage().getEFactoryInstance().create(eClass);
    Image image = labelProvider.getImage(instance);
    ImageDescriptor imageDescriptor = ImageDescriptor.createFromImage(image);
    return imageDescriptor;
  }

  /**
   * If reference type is abstract create commands for its subclasses.
   * 
   * @param refClass
   * @param commands
   */
  private void addCommandsForSubTypes(EClass refClass, List<IContributionItem> commands)
  {

    // TODO EMFPlainObjectTransition: do not create commands for subclasses of ModelElement
    // if (refClass.equals(MetamodelPackage.eINSTANCE.getModelElement())) {
    // return;
    // }

    Set<EClass> eClazz = getAllSubEClasses(refClass);
    eClazz.remove(refClass);
    for (EClass eClass : eClazz)
    {
      CommandContributionItemParameter commandParam = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
          null, COMMAND_ID, CommandContributionItem.STYLE_PUSH);

      Map<Object, Object> commandParams = new HashMap<Object, Object>();
      // commandParams.put(CreateContainmentHandler.COMMAND_ECLASS_PARAM, eClass);
      commandParam.label = "New " + eClass.getName();
      commandParam.icon = getImage(eClass);

      // create command
      commandParam.parameters = commandParams;
      CommandContributionItem command = new CommandContributionItem(commandParam);
      commands.add(command);
    }

  }

  /**
   * Retrieve all EClasses from the Ecore package registry that are subclasses of the given EClass. Does not include
   * abstract classes or interfaces.
   * 
   * @param eClass
   *          the superClass of the subClasses to retrieve
   * @return a set of EClasses
   */
  private static Set<EClass> getAllSubEClasses(EClass eClass)
  {
    Set<EClass> allEClasses = getAllModelElementEClasses();
    if (EcorePackage.eINSTANCE.getEObject().equals(eClass))
    {
      return allEClasses;
    }
    Set<EClass> result = new HashSet<EClass>();
    for (EClass subClass : allEClasses)
    {
      boolean isSuperTypeOf = eClass.isSuperTypeOf(subClass) || eClass.equals(EcorePackage.eINSTANCE.getEObject());
      if (isSuperTypeOf && !subClass.isAbstract() && !subClass.isInterface())
      {
        result.add(subClass);
      }
    }
    return result;
  }

  /**
   * Retrieve all EClasses from the Ecore package registry that are model element subclasses.
   * 
   * @return a set of EClasses
   */
  private static Set<EClass> getAllModelElementEClasses()
  {
    // if (modelElementEClasses != null) {
    // return new HashSet<EClass>(modelElementEClasses);
    // }
    Set<EClass> result = new HashSet<EClass>();
    Registry registry = EPackage.Registry.INSTANCE;

    for (Entry<String, Object> entry : new HashSet<Entry<String, Object>>(registry.entrySet()))
    {
      try
      {
        EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(entry.getKey());
        result.addAll(getAllModelElementEClasses(ePackage));
      }
      // BEGIN SUPRESS CATCH EXCEPTION
      catch (RuntimeException exception)
      {
        Activator.log("Failed to load model package " + entry.getKey(), exception);
        // END SUPRESS CATCH EXCEPTION
      }

    }
    // modelElementEClasses = result;
    return result;
  }

  /**
   * Retrieve all EClasses from the Ecore package that are model element subclasses.
   * 
   * @param ePackage
   *          the package to get the classes from
   * @return a set of EClasses
   */
  private static Set<EClass> getAllModelElementEClasses(EPackage ePackage)
  {
    Set<EClass> result = new HashSet<EClass>();
    for (EPackage subPackage : ePackage.getESubpackages())
    {
      result.addAll(getAllModelElementEClasses(subPackage));
    }
    for (EClassifier classifier : ePackage.getEClassifiers())
    {
      if (classifier instanceof EClass)
      {
        EClass subEClass = (EClass)classifier;
        result.add(subEClass);
      }
    }
    return result;
  }

  /**
   * Extract the selected Object in navigator or other StructuredViewer. This method uses the general ISelectionService
   * of Workbench to extract the selection. Beware that the part providing the selection should have registered its
   * SelectionProvider.
   * 
   * @return the selected Object or null if selection is not an IStructuredSelection
   */
  public static Object getSelection()
  {
    ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();

    ISelection sel = selectionService.getSelection();
    if (!(sel instanceof IStructuredSelection))
    {
      return null;
    }

    IStructuredSelection ssel = (IStructuredSelection)sel;
    if (ssel.isEmpty())
    {
      return null;
    }

    Object o = ssel.getFirstElement();
    return o;
  }

}

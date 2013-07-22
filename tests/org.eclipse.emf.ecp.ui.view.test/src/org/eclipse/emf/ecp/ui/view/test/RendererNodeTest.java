package org.eclipse.emf.ecp.ui.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.util.ECPUtil;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.explorereditorbridge.internal.ECPControlContextImpl;
import org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl;
import org.eclipse.emf.ecp.internal.ui.view.IViewProvider;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.ModelRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegatorAdapter;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.provider.xmi.XMIViewModelProvider;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

// TODO: test case for filtering nodes
// TODO: remove EMFStore provider dependency
// TODO: how to retrieve ECPControlContext
public class RendererNodeTest {
	
	private static final String EXAMPLE_VIEW_MODEL = "ExampleViewModel";
	private static final String EXAMPLE_VIEW_MODEL_2 = "ExampleViewModel2";
	private static final String EXAMPLE_VIEW_MODEL_3 = "ExampleViewModel3";
	private static final String EXAMPLE_VIEW_MODEL_4 = "ExampleViewModel4";

	@Rule
	public MethodRule viewModelRule = new MethodRule() {
	    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
	        final String viewModelName = method.getAnnotation(ViewModel.class).value();
	        
	        return new Statement() {
	            @Override
	            public void evaluate() throws Throwable {
	                try{
	                	Display display = Display.getDefault();
	                    shell = new Shell(display);
	                    shell.setText("DirectoryDialog");		
	                    shell.setLayout(new GridLayout());
	                	Composite parent = new Composite(shell, SWT.NONE);
	               		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	               		parent.setLayout(new GridLayout());
	                	View view = getView(player, viewModelName);
	                	
	                	if (view == null) {
	                		throw new IllegalStateException("@ViewModel annotation missing or view model does not exist.");
	                	}
	                	
	            		ModelRenderer<Composite> renderer = ModelRenderer.INSTANCE.getRenderer();
	            		ECPProvider provider = ECPUtil.getECPProviderRegistry().getProvider(EMFStoreProvider.NAME);

	            		ECPProject p = ECPProjectManagerImpl.INSTANCE.getProject("hello");
	            		if (p == null){
	            			p = ECPProjectManagerImpl.INSTANCE.createProject(provider, "hello");
	            		}
	            		
	            		ECPControlContextImpl context = new ECPControlContextImpl(player, 
	            				p,
	            				shell);
	            		root = NodeBuilders.INSTANCE.build(view, context);
	            		
	            		rendererContext = renderer.render(root,parent);
	            		rendererContext.addListener(root);
	            		rendererContext.triggerValidation();
	            				 
	            		Composite c = rendererContext.getControl();
	            		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	            		base.evaluate();

	                } catch (Exception e) {
	                	throw e;
	                }
	            }
	        };
	    }
	};

	private static Player player;
	private RendererContext<Composite> rendererContext; 
	private Node<View> root;
	private Shell shell;

	/**
	 * 
	 */
	private static Player createPlayer() {
		Player player = BowlingFactory.eINSTANCE.createPlayer();
		player.setName("Gustav Gans");
		player.setDateOfBirth(new Date());
		player.setNumberOfVictories(3);
		player.setWinLossRatio(new BigDecimal("0.3"));
		return player;
	}
	
	@After
	public void after() {
		rendererContext.dispose();
		shell.dispose();	
	}
	
	@Before
	public void before() {
		player.setName("Gustav Gans");
		player.setDateOfBirth(new Date());
		player.setNumberOfVictories(3);
		player.setWinLossRatio(new BigDecimal("0.3"));
		player.getEMails().clear();
	}
	
	@BeforeClass
	public static void beforeClass() {
		player = createPlayer();
	}
	
	//@Test
	public void testFind() {
		Node<ColumnComposite> nodeColumn1 = findNodeByName(root, "column1");
		assertTrue(nodeColumn1.getRenderable() instanceof ColumnComposite);
		Node<Control> control = findNodeByName(root, "eMailsControl");
		assertTrue(control.getRenderable() instanceof Control);
		Node<? extends Renderable> findNodeBy = findNodeByName(nodeColumn1, "nameControl");
		assertTrue(findNodeBy.getRenderable() instanceof Control);
	}
	
	
	@Test
	@ViewModel(EXAMPLE_VIEW_MODEL)
	public void testEnablementOfColumnComposite() {

		// start value is 3 
		Node<ColumnComposite> row1 = findNodeByName(root, "Row1");
		Node<ColumnComposite> row2 = findNodeByName(root, "Row2");
		Node<ColumnComposite> control1 = findNodeByName(row1, "Control1");
		Node<ColumnComposite> control2 = findNodeByName(row2, "Control2");
		
		assertTrue(row1.isEnabled());
		assertTrue(control1.isEnabled());
		assertFalse(row2.isEnabled());
		assertFalse(control2.isEnabled());
		
		player.setNumberOfVictories(1);
		
		assertFalse(row1.isEnabled());
		assertFalse(control1.isEnabled());
		assertTrue(row2.isEnabled());
		assertTrue(control2.isEnabled());
		
		player.setNumberOfVictories(0);

		assertTrue(row1.isEnabled());
		assertTrue(control1.isEnabled());
		assertFalse(row2.isEnabled());
		assertFalse(control2.isEnabled());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL)
	@Test
	public void testShowOfColumnmposite() {

		// start value is 3 
		Node<ColumnComposite> row3 = findNodeByName(root, "Row3");
		Node<ColumnComposite> row4 = findNodeByName(root, "Row4");
		Node<ColumnComposite> control3 = findNodeByName(row3, "Control3");
		Node<ColumnComposite> control4 = findNodeByName(row4, "Control4");
		
		assertTrue(row3.isVisible());
		assertTrue(control3.isVisible());
		assertFalse(row4.isVisible());
		assertFalse(control4.isVisible());
		
		player.setNumberOfVictories(4);
		
		assertFalse(row3.isVisible());
		assertFalse(control3.isVisible());
		assertTrue(row4.isVisible());
		assertTrue(control4.isVisible());
		
		player.setNumberOfVictories(0);

		assertTrue(row3.isVisible());
		assertTrue(control3.isVisible());
		assertFalse(row4.isVisible());
		assertFalse(control4.isVisible());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL_2)
	@Test
	public void testShowRuleOverrideOfControl() {
		// start value is 3 
		Node<ColumnComposite> row1 = findNodeByName(root, "Row1");
		Node<ColumnComposite> control1 = findNodeByName(root, "Control1");

		assertTrue(row1.isVisible());
		assertFalse(control1.isVisible());
		
		player.setNumberOfVictories(1);
		
		assertFalse(row1.isVisible());
		assertTrue(control1.isVisible());
		
		player.setNumberOfVictories(3);
		
		assertTrue(row1.isVisible());
		assertFalse(control1.isVisible());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL_2)
	@Test
	public void testEnableRuleOverrideOfControl() {
		// start value is 3 
		Node<ColumnComposite> row2 = findNodeByName(root, "Row2");
		Node<ColumnComposite> control2 = findNodeByName(root, "Control2");

		assertTrue(row2.isEnabled());
		assertFalse(control2.isEnabled());
		
		player.setNumberOfVictories(2);
		
		assertFalse(row2.isEnabled());
		assertTrue(control2.isEnabled());
		
		player.setNumberOfVictories(3);
		
		assertTrue(row2.isEnabled());
		assertFalse(control2.isEnabled());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL)
	@Test
	public void testUnset() {
		// start value is 3, row 3 should be visible
		Node<ColumnComposite> row3 = findNodeByName(root, "Row3");
		
		// control3's target feature is height
		player.setHeight(4);
		
		// now hide row3
		player.setNumberOfVictories(4);
		
		assertFalse(row3.isVisible());
		
		player.setNumberOfVictories(1);
		
		assertTrue(row3.isVisible());
		
		// 0 is the default value
		assertEquals(new Double(0.0), new Double(player.getHeight()));
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL)
	@Test
	public void testUnsetMulti() {
		// start value is 3, row 3 should be visible
		Node<ColumnComposite> row3 = findNodeByName(root, "Row3");

		// control3_2's target feature is eMails
		player.getEMails().add("foo@bar.net");

		// now hide row3
		player.setNumberOfVictories(4);

		assertFalse(row3.isVisible());

		player.setNumberOfVictories(1);

		assertTrue(row3.isVisible());

		// 0 is the default value
		assertEquals(0, player.getEMails().size());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL)
	@Test
	public void testValidationOfEMailFails() {
		// TODO: root node is not marked?
		Node<Control> control3_2 = findNodeByName(root, "Control3_2");
		Node<Category> myCategory = findNodeByName(root, "MyCategory");
		assertEquals(Diagnostic.ERROR, control3_2.getSeverity());
		assertEquals(Diagnostic.ERROR, myCategory.getSeverity());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL_3)
	@Test
	public void testAddAndRemoveRenderingResultDelegator() {
		final boolean[] flag = new boolean[1];
		Node<Control> eMailsControl = findNodeByName(root, "eMailsControl");
		RenderingResultDelegator adapter = new RenderingResultDelegatorAdapter() {
			@Override
			public void validationChanged(
					Map<EObject, Set<Diagnostic>> affectedObjects) {
				flag[0] = !flag[0];
			}
		};
		eMailsControl.addRenderingResultDelegator(adapter);
		
		player.getEMails().add("foo@bar.net");
		// player now validates correctly, RenderingResultDelegator#validationChanged should have been triggered
		assertTrue(flag[0]);
		eMailsControl.removeRenderingResultDelegator(adapter);
		player.getEMails().clear();
		// still true, because adapter has been removed
		assertTrue(flag[0]);
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL_3)
	@Test
	public void testShowNested() {
		Node<ColumnComposite> row = findNodeByName(root, "Row1");
		Node<Control> control = findNodeByName(root, "eMailsControl");
		
		assertTrue(row.isVisible());
		assertTrue(control.isVisible());
		
		player.setName("abc");
		
		assertFalse(row.isVisible());
		assertFalse(control.isVisible());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL)
	@Test
	public void testValidationOfEMail() {
		// TODO: root node is not marked?
		Node<Control> control3_2 = findNodeByName(root, "Control3_2");
		Node<Category> myCategory = findNodeByName(root, "MyCategory");
		assertEquals(Diagnostic.ERROR, control3_2.getSeverity());
		assertEquals(Diagnostic.ERROR, myCategory.getSeverity());
		
		player.getEMails().add("foo@bar.net");
		
		assertEquals(Diagnostic.OK, control3_2.getSeverity());
		assertEquals(Diagnostic.OK, myCategory.getSeverity());
	}
	
	@ViewModel(EXAMPLE_VIEW_MODEL_4)
	@Test
	public void testHideCategoryWithUnset() {
		Node<Category> category4 = findNodeByName(root, "Category4");
		
		assertTrue(category4.isVisible());
		assertEquals(Diagnostic.ERROR, category4.getSeverity());
		player.getEMails().add("foo@bar.net");
		assertEquals(Diagnostic.OK, category4.getSeverity());
		
		player.setName("foo");
		
		assertFalse(category4.isVisible());
		
		player.setName("foo2");
		
		assertTrue(category4.isVisible());
		assertEquals(Diagnostic.ERROR, category4.getSeverity());
	}
		
	@ViewModel(EXAMPLE_VIEW_MODEL_4)
	//@Test
	public void testManually() {
		Display display = Display.getDefault(); 
		shell.open();
		
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	//@Test
	public void showControl_SourceAndTargetOnSameComposite() {
		Node<Control> isProfessionalControl = findNodeByName(root, "column5_isProfessionalControl");
		assertFalse(isProfessionalControl.isVisible());
		player.setHeight(42);
		assertTrue(isProfessionalControl.isVisible());
		player.setHeight(43);
		assertFalse(isProfessionalControl.isVisible());
	}
	
	@SuppressWarnings("unchecked")
	private static <R extends Renderable> Node<R> findNodeByName(Node<? extends Renderable> node, String name) {
		Renderable renderable = node.getRenderable();
		
		// TODO: add common name type to view ecore
		if (renderable instanceof org.eclipse.emf.ecp.view.model.Composite) {
			org.eclipse.emf.ecp.view.model.Composite composite = (org.eclipse.emf.ecp.view.model.Composite) renderable;
			String compositeName = composite.getName();
			if (compositeName != null && compositeName.equals(name)) {
				return (Node<R>) node;
			} 
		} else if (renderable instanceof Control) {
			Control control = (Control) renderable;
			String controlName = control.getName();
			if (controlName != null && controlName.equals(name)) {
				return (Node<R>) node;
			}
		} else if (renderable instanceof AbstractCategorization) {
			AbstractCategorization categorization = (AbstractCategorization) renderable;
			String categorizationName = categorization.getName();
			if (categorizationName != null && categorizationName.equals(name)) {
				return (Node<R>) node;
			}
		}
		
		for (Node<? extends Renderable> child : node.getChildren()) {
			Node<? extends Renderable> found = findNodeByName(child, name);
			if (found != null) {
				return (Node<R>) found;
			}
		}
		
		return null;
	}
	

	
	/**
	 * @param eObject 
	 * @return
	 */
	private static View getView(EObject eObject, final String viewModelName) {
		
		IViewProvider viewProvider = new XMIViewModelProvider() {
			
			@Override
			public int canRender(EObject eObject) {
				return 1;
			}
			
			@Override
			protected URI getXMIPath() {
				URL url = this.getClass().getResource("/" + viewModelName + ".xmi");
				try {
					return URI.createURI(url.toURI().toString());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		};
		
		return viewProvider.generate(eObject);
	}
}

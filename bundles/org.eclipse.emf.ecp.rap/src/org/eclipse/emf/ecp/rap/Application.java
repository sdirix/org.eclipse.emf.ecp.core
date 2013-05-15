package org.eclipse.emf.ecp.rap;


/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements EntryPoint {

	// public Object start(IApplicationContext context) throws Exception {
	// Display display = PlatformUI.createDisplay();
	// WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
	// return PlatformUI.createAndRunWorkbench(display, advisor);
	// }
	//
	// public void stop() {
	// // Do nothing
	// }

	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
		return PlatformUI.createAndRunWorkbench(display, advisor);
	}
}

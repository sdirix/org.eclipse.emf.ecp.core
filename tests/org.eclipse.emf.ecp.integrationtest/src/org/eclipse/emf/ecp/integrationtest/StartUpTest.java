package org.eclipse.emf.ecp.integrationtest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class StartUpTest {

	@Test
	public void test() {
		// get bundle instance via the OSGi Framework Util class
		final BundleContext ctx = Activator.getContext();
		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Bundle[] bundles = ctx.getBundles();
		final List<String> list = new ArrayList<String>();
		String result = "";

		for (final Bundle bundle : bundles) {
			if (bundle.getSymbolicName().contains("ecp")
				&& !bundle.getSymbolicName().contains(
					"org.eclipse.emf.ecp.integrationtest")) {
				final int state = bundle.getState();

				if ((state & (Bundle.RESOLVED | Bundle.STARTING)) == 0) {
					final String string = "Plug is not resolved: "
						+ bundle.getSymbolicName() + " state: "
						+ getBundleStateText(state);
					list.add(string);
					result += string + "\n";
				}

			}
		}

		assertEquals(result, 0, list.size());
	}

	private String getBundleStateText(int state) {
		switch (state) {
		case Bundle.RESOLVED:
			return "RESOLVED";
		case Bundle.INSTALLED:
			return "INSTALLED";
		case Bundle.STARTING:
			return "STARTING";
		case Bundle.ACTIVE:
			return "ACTIVE";
		case Bundle.START_TRANSIENT:
			return "START_TRANSIENT";
		case Bundle.STOPPING:
			return "STOPPING";

		default:
			return "No State";
		}

	}
}
/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.view.multisegment.tooling;

/**
 * Interface implemented by testable subclasses of strategy providers for multi dmrs or segments.
 *
 * @author Lucas Koehler
 *
 */
interface TestableStrategyProvider {
	void setSegmentToolingEnabled(boolean segmentToolingEnabled);
}

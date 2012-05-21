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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.ecp.ui.util.ActionHelper;

import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;

/**
 * A {@link HyperlinkAdapter} to the model elements.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public class MEHyperLinkAdapter extends HyperlinkAdapter implements IHyperlinkListener
{

  private EObject target;

  private final ECPProject ecpProject;

  /**
   * Default constructor.
   * 
   * @param source
   *          the source of the model link
   * @param target
   *          the target of the model link
   * @param featureName
   *          the feature of the model link
   * @param ecpProject
   * @param context
   *          the {@link EditorModelelementContext}
   */
  public MEHyperLinkAdapter(EObject target, EObject source, String featureName, ECPProject ecpProject)
  {
    super();
    this.target = target;
    this.ecpProject = ecpProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void linkActivated(HyperlinkEvent event)
  {
    ActionHelper.openModelElement(target, "org.eclipse.emf.ecp.editor", ecpProject);
    super.linkActivated(event);
  }
}

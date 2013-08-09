/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule;

import static org.eclipse.emf.ecp.view.rule.ReadOnlySetting.createSetting;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.context.AbstractViewService;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.Condition;
import org.eclipse.emf.ecp.view.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.ShowRule;

/**
 * The Class RuleService.
 * 
 * @author emueller
 * @author Eugen Neufeld
 */
public class RuleService extends AbstractViewService {

	private ViewModelContext context;
	private ModelChangeListener domainChangeListener;
	private ModelChangeListener viewChangeListener;

	private final RuleRegistry<EnableRule> enableRuleRegistry;
	private final RuleRegistry<ShowRule> showRuleRegistry;
	private SettingsRegistry settingsRegistry;

	/**
	 * Instantiates a new rule service.
	 */
	public RuleService() {
		enableRuleRegistry = new RuleRegistry<EnableRule>();
		showRuleRegistry = new RuleRegistry<ShowRule>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		this.context = context;
		domainChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {

				if (isAttributeNotification(notification)) {
					final EAttribute attribute = (EAttribute) notification.getStructuralFeature();
					eval(enableRuleRegistry, notification.getNotifier(), attribute);
					eval(showRuleRegistry, notification.getNotifier(), attribute);
				}
			}
		};
		context.registerDomainChangeListener(domainChangeListener);
		viewChangeListener = new ModelChangeListener() {
			public void notifyChange(ModelChangeNotification notification) {
				// do nothing for now, dynamic registration of rules isn't possible
			}
		};
		context.registerViewChangeListener(viewChangeListener);

		final View view = context.getViewModel();

		if (view == null) {
			throw new IllegalStateException("View model must not be null");
		}

		final EObject domainModel = context.getDomainModel();

		if (domainModel == null) {
			throw new IllegalStateException("Domain model must not be null");
		}

		settingsRegistry = createSettingsRegistry(domainModel);

		init(enableRuleRegistry, view, domainModel);
		init(showRuleRegistry, view, domainModel);

		eval(enableRuleRegistry, domainModel);
		eval(showRuleRegistry, domainModel);
	}

	private <T extends Rule> void eval(RuleRegistry<T> ruleHierarchy, EObject domainModel) {

		final Set<EAttribute> allEAttributes = getAllEAttributes(domainModel);

		for (final EAttribute eAttribute : allEAttributes) {
			eval(ruleHierarchy, domainModel, eAttribute);
		}
	}

	private static Set<EAttribute> getAllEAttributes(EObject eObject) {
		final Set<EAttribute> result = new LinkedHashSet<EAttribute>();
		final EList<EAttribute> eAllAttributes = eObject.eClass().getEAllAttributes();
		result.addAll(eAllAttributes);

		final TreeIterator<EObject> eAllContents = eObject.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject next = eAllContents.next();
			result.addAll(getAllEAttributes(next));
		}

		return result;
	}

	private static Rule getRule(Renderable renderable) {
		for (final Attachment attachment : renderable.getAttachments()) {
			if (Rule.class.isInstance(attachment)) {
				final Rule rule = (Rule) attachment;
				return rule;
			}
		}

		return null;
	}

	private static <T extends Rule> void eval(RuleRegistry<T> ruleHierarchy, EObject domainEObject, EAttribute attribute) {
		for (final Renderable renderable : ruleHierarchy.getAffectedRenderables(
			createSetting(domainEObject, attribute))) {

			final Rule rule = getRule(renderable);
			final boolean result = ConditionEvaluator.evaluate(domainEObject, rule.getCondition());

			if (isEnableRule(rule)) {
				if (((EnableRule) rule).isDisable()) {
					enable(renderable, !result);
				} else {
					enable(renderable, result);
				}
			} else {
				if (((ShowRule) rule).isHide()) {
					show(renderable, !result);
				} else {
					show(renderable, result);
				}
			}
		}
	}

	private static SettingsRegistry createSettingsRegistry(EObject domainModel) {

		final SettingsRegistry result = new SettingsRegistry();
		final TreeIterator<EObject> iterator = domainModel.eAllContents();

		while (iterator.hasNext()) {
			final EObject eObject = iterator.next();
			final EList<EAttribute> allAttributes = eObject.eClass().getEAllAttributes();
			for (final EAttribute attribute : allAttributes) {
				final Setting setting = createSetting(eObject, attribute);
				result.add(setting);
			}
			result.addAll(createSettingsRegistry(eObject));
		}

		final EList<EAttribute> allAttributes = domainModel.eClass().getEAllAttributes();
		for (final EAttribute attribute : allAttributes) {
			final Setting setting = createSetting(domainModel, attribute);
			result.add(setting);
		}

		return result;
	}

	private static void enable(Renderable renderable, boolean isEnabled) {
		renderable.setEnabled(isEnabled);
		final TreeIterator<EObject> iterator = renderable.eAllContents();

		while (iterator.hasNext()) {
			final EObject child = iterator.next();
			if (child instanceof Renderable) {
				enable((Renderable) child, isEnabled);
			}
		}
	}

	private static void show(Renderable renderable, boolean isVisible) {

		renderable.setVisible(isVisible);
		final TreeIterator<EObject> iterator = renderable.eAllContents();

		while (iterator.hasNext()) {
			final EObject child = iterator.next();
			if (child instanceof Renderable) {
				show((Renderable) child, isVisible);
			}
		}
	}

	private <T extends Rule> void init(RuleRegistry<T> registry, EObject eObject, EObject domainObject) {
		final TreeIterator<EObject> iterator = eObject.eAllContents();

		while (iterator.hasNext()) {
			final EObject next = iterator.next();

			if (hasRule(next)) {
				final Renderable renderable = (Renderable) next;
				registerLeafConditions(registry, renderable, getRule(renderable).getCondition(), domainObject);
			}
			init(registry, next, domainObject);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends Rule> void registerLeafConditions(RuleRegistry<T> registry, Renderable renderable,
		Condition condition,
		EObject domainObject) {

		if (condition instanceof LeafCondition) {
			final LeafCondition leafCondition = (LeafCondition) condition;

			final EObject target = getTargetEObject(leafCondition, domainObject);

			if (target == null) {
				return;
			}

			registry.register(renderable, (T) getRule(renderable), leafCondition, target);

		} else if (condition instanceof OrCondition) {
			final OrCondition orCondition = (OrCondition) condition;
			for (final Condition cond : orCondition.getConditions()) {
				registerLeafConditions(registry, renderable, cond, domainObject);
			}
		} else {
			final AndCondition andCondition = (AndCondition) condition;
			for (final Condition cond : andCondition.getConditions()) {
				registerLeafConditions(registry, renderable, cond, domainObject);
			}
		}
	}

	private static EObject getTargetEObject(LeafCondition condition, EObject parent) {
		final EList<EReference> pathToAttribute = condition.getPathToAttribute();
		for (final EReference eReference : pathToAttribute) {

			EObject child;
			if (eReference.isMany()) {
				final List<?> eObjects = (List<?>) parent.eGet(eReference);
				if (eObjects.size() == 0) {
					return null;
				}
				child = (EObject) eObjects.get(0);
			} else {
				child = (EObject) parent.eGet(eReference);
			}

			if (child == null) {
				child = EcoreUtil.create(eReference.getEReferenceType());
				parent.eSet(eReference, child);
			}
			parent = child;
		}

		return parent;
	}

	private static boolean hasRule(EObject eObject) {
		if (!(eObject instanceof Renderable)) {
			return false;
		}

		final Renderable renderable = (Renderable) eObject;
		return getRule(renderable) != null;
	}

	/**
	 * Gets the involved {@link org.eclipse.emf.ecore.EObject EObjects}.
	 * 
	 * @param s the setting
	 * @param newValue the new value
	 * @return the involved {@link Renderable}s
	 */
	public Set<Renderable> getInvolvedEObject(Setting s, Object newValue) {

		final EStructuralFeature feature = s.getEStructuralFeature();

		if (feature instanceof EAttribute) {

			final EAttribute attribute = (EAttribute) feature;
			final Object val = s.getEObject().eGet(attribute);
			final Setting setting = settingsRegistry.getByEObjectAndEAttribute(s.getEObject(),
				(EAttribute) s.getEStructuralFeature());

			if (val.equals(newValue) || setting == null) {
				return Collections.emptySet();
			}

			final Set<Renderable> renderables = new LinkedHashSet<Renderable>();
			final List<Renderable> affectedEnableRules = enableRuleRegistry.getAffectedRenderables(setting);
			final List<Renderable> affectedShowRules = showRuleRegistry.getAffectedRenderables(setting);

			for (final Renderable e : affectedEnableRules) {
				renderables.add(e);
			}

			for (final Renderable e : affectedShowRules) {
				renderables.add(e);
			}

			return renderables;
		}
		return Collections.emptySet();
	}

	/**
	 * Dispose.
	 */
	@Override
	public void dispose() {
		// dispose stuff
		context.unregisterDomainChangeListener(domainChangeListener);
		context.unregisterViewChangeListener(viewChangeListener);
	}

	private static boolean isEnableRule(Rule rule) {
		return EnableRule.class.isInstance(rule);
	}

	private static boolean isAttributeNotification(ModelChangeNotification notification) {
		if (notification.getStructuralFeature() instanceof EAttribute) {
			return true;
		}

		return false;
	}
}

/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.rule;

import static org.eclipse.emf.ecp.view.internal.rule.UniqueSetting.createSetting;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.context.AbstractViewService;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.ShowRule;

/**
 * Rule service that, once instantiated, maintains and synchronizes
 * the state of a rule with its {@link Renderable}.
 * 
 * @author emueller
 */
public class RuleService extends AbstractViewService {

	private ViewModelContext context;
	private ModelChangeListener domainChangeListener;
	private ModelChangeListener viewChangeListener;

	private final RuleRegistry<EnableRule> enableRuleRegistry;
	private final RuleRegistry<ShowRule> showRuleRegistry;
	private boolean isUnset;

	/**
	 * Instantiates the rule service.
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
					if (isUnset) {
						return;
					}
					final EAttribute attribute = (EAttribute) notification.getStructuralFeature();
					evalShow(notification.getNotifier(), attribute);
					evalEnable(notification.getNotifier(), attribute);
				}
			}
		};
		context.registerDomainChangeListener(domainChangeListener);
		viewChangeListener = new ModelChangeListener() {
			public void notifyChange(ModelChangeNotification notification) {
				// do nothing for now, dynamic registration of rules isn't possible yet
			}
		};
		context.registerViewChangeListener(viewChangeListener);

		final Renderable view = context.getViewModel();

		if (view == null) {
			throw new IllegalStateException("View model must not be null");
		}

		final EObject domainModel = context.getDomainModel();

		if (domainModel == null) {
			throw new IllegalStateException("Domain model must not be null");
		}

		init(enableRuleRegistry, EnableRule.class, view, domainModel);
		init(showRuleRegistry, ShowRule.class, view, domainModel);

		evalEnable(domainModel);
		evalShow(domainModel);
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

	private static void updateStateMap(Map<Renderable, Boolean> stateMap, Renderable renderable,
		boolean isOpposite, boolean evalResult) {

		if (!stateMap.containsKey(renderable)) {
			stateMap.put(renderable, isOpposite ? !evalResult : evalResult);
		} else {
			final Boolean currentState = stateMap.get(renderable).booleanValue();
			if (currentState) {
				stateMap.put(renderable, isOpposite ? !evalResult : evalResult);
			}
		}

		for (final EObject childContent : renderable.eContents()) {
			if (childContent instanceof Renderable) {
				updateStateMap(stateMap, (Renderable) childContent, isOpposite, evalResult);
			}
		}
	}

	private static <T extends Rule> boolean hasRule(Class<T> ruleType, EObject eObject) {

		if (!Renderable.class.isInstance(eObject)) {
			return false;
		}

		final Renderable renderable = (Renderable) eObject;
		final Rule rule = getRule(renderable);

		if (ruleType.isInstance(rule)) {
			return true;
		}

		return false;
	}

	private static <T extends Rule> Map<Renderable, Boolean> evalAffectedRenderables(RuleRegistry<T> registry,
		Class<T> ruleType, EObject domainEObject, EAttribute attribute, boolean dry, Object newValue) {

		final Map<Renderable, Boolean> map = new LinkedHashMap<Renderable, Boolean>();

		for (final Map.Entry<Rule, Renderable> ruleAndRenderable : registry.getAffectedRenderables(
			createSetting(domainEObject, attribute)).entrySet()) {

			final Rule rule = ruleAndRenderable.getKey();
			final Renderable renderable = ruleAndRenderable.getValue();
			// whether the value changed at all, if newValue has been provided
			boolean hasChanged = false;

			if (!ruleType.isInstance(rule)) {
				continue;
			}

			if (dry) {
				final Object currentValue = domainEObject.eGet(attribute);
				if (currentValue == null) {
					hasChanged = newValue == null ? false : true;
				} else {
					hasChanged = !currentValue.equals(newValue);
				}
			}

			if (hasChanged) {
				final boolean result = ConditionEvaluator.evaluate(newValue, rule.getCondition());
				updateStateMap(map, renderable, isDisableRule(rule) || isHideRule(rule), result);
			} else if (!dry) {
				final boolean result = ConditionEvaluator.evaluate(domainEObject, rule.getCondition());
				updateStateMap(map, renderable, isDisableRule(rule) || isHideRule(rule), result);
			}
		}

		return map;
	}

	private static <T extends Rule> Map<Renderable, Boolean> evalAffectedRenderables(RuleRegistry<T> registry,
		Class<T> ruleType, EObject domainEObject, EAttribute attribute, Object newValue) {
		return evalAffectedRenderables(registry, ruleType, domainEObject, attribute, true, newValue);
	}

	private static <T extends Rule> Map<Renderable, Boolean> evalAffectedRenderables(RuleRegistry<T> registry,
		Class<T> ruleType, EObject domainEObject, EAttribute attribute) {
		return evalAffectedRenderables(registry, ruleType, domainEObject, attribute, false, null);
	}

	private static boolean isDisableRule(Rule rule) {
		if (isEnableRule(rule)) {
			final EnableRule enableRule = (EnableRule) rule;
			return enableRule.isDisable();
		}

		return false;
	}

	private static boolean isHideRule(Rule rule) {
		if (isShowRule(rule)) {
			final ShowRule showRule = (ShowRule) rule;
			return showRule.isHide();
		}

		return false;
	}

	private static boolean isShowRule(Rule rule) {
		return rule instanceof ShowRule;
	}

	private <T extends Rule> void evalShow(EObject domainEObject, EAttribute attribute) {

		final Map<Renderable, Boolean> visibleMap = evalAffectedRenderables(showRuleRegistry, ShowRule.class,
			domainEObject, attribute);

		for (final Map.Entry<Renderable, Boolean> e : visibleMap.entrySet()) {
			final Boolean isVisible = e.getValue();
			final Renderable renderable = e.getKey();
			final boolean isCurrentlyVisible = renderable.isVisible();
			renderable.setVisible(isVisible);
			if (isCurrentlyVisible && !isVisible) {
				unset(renderable);
			}
		}
	}

	private <T extends Rule> void evalEnable(EObject domainEObject, EAttribute attribute) {

		final Map<Renderable, Boolean> enabledMap = evalAffectedRenderables(enableRuleRegistry, EnableRule.class,
			domainEObject, attribute);

		for (final Map.Entry<Renderable, Boolean> e : enabledMap.entrySet()) {
			e.getKey().setEnabled(e.getValue());
		}
	}

	private <T extends Rule> void evalShow(EObject domainModel) {
		for (final UniqueSetting setting : showRuleRegistry.getSettings()) {
			evalShow(setting.getEObject(), setting.getEAttribute());
		}
	}

	private <T extends Rule> void evalEnable(EObject domainModel) {
		for (final UniqueSetting setting : enableRuleRegistry.getSettings()) {
			evalEnable(setting.getEObject(), setting.getEAttribute());
		}
	}

	private <T extends Rule> void init(RuleRegistry<T> registry, Class<T> ruleType, EObject viewModel,
		EObject domainObject) {
		final TreeIterator<EObject> iterator = viewModel.eAllContents();

		register(registry, ruleType, domainObject, viewModel);

		while (iterator.hasNext()) {
			final EObject content = iterator.next();
			register(registry, ruleType, domainObject, content);
		}
	}

	private <T extends Rule> void register(RuleRegistry<T> registry, Class<T> ruleType, EObject domainObject,
		final EObject viewModel) {
		if (hasRule(ruleType, viewModel)) {
			final Renderable renderable = (Renderable) viewModel;
			@SuppressWarnings("unchecked")
			final T rule = (T) getRule(renderable);
			registry.register(renderable, rule, rule.getCondition(), domainObject);
		}
	}

	/**
	 * Returns all {@link Renderable}s, that would we disabled if {@code newValue} would be set for the given
	 * {@code setting}.
	 * 
	 * @param setting
	 *            the setting
	 * @param newValue
	 *            the new value of the setting
	 * @return the disabled {@link Renderable}s and their new state if newValue would be set
	 */
	public Map<Renderable, Boolean> getDisabledRenderables(Setting setting, Object newValue) {

		final EStructuralFeature feature = setting.getEStructuralFeature();

		if (feature instanceof EAttribute) {

			final EAttribute attribute = (EAttribute) feature;

			return evalAffectedRenderables(enableRuleRegistry,
				EnableRule.class, setting.getEObject(), attribute, newValue);
		}

		return Collections.emptyMap();
	}

	/**
	 * Returns all {@link Renderable}s, that would we hidden if {@code newValue} would be set for the given
	 * {@code setting}.
	 * 
	 * @param setting
	 *            the setting
	 * @param newValue
	 *            the new value of the setting
	 * @return the hidden {@link Renderable}s and their new state if newValue would be set
	 */
	public Map<Renderable, Boolean> getHiddenRenderables(Setting setting, Object newValue) {
		final EStructuralFeature feature = setting.getEStructuralFeature();

		if (feature instanceof EAttribute) {

			final EAttribute attribute = (EAttribute) feature;

			return evalAffectedRenderables(showRuleRegistry,
				ShowRule.class, setting.getEObject(), attribute, newValue);
		}

		return Collections.emptyMap();
	}

	private void unset(Renderable renderable) {

		if (renderable instanceof Control) {
			final Control control = (Control) renderable;
			EObject parent = context.getDomainModel();
			final EStructuralFeature targetFeature = control.getTargetFeature();
			final Class<?> containerClass = targetFeature.getContainerClass();

			if (!containerClass.isInstance(parent)) {
				for (final EReference eReference : control.getPathToFeature()) {
					if (parent.eGet(eReference) instanceof EObject) {
						parent = (EObject) parent.eGet(eReference);
					}
				}
			}

			isUnset = true;
			if (containerClass.isInstance(parent)) {
				parent.eUnset(targetFeature);
			}
			isUnset = false;
		}

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

package com.olyno.skent.util.skript;

import java.util.ArrayList;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.SyntaxElementInfo;

public abstract class PriorityPropertyCondition<T> extends PropertyCondition<T> {

	public static void registerCondition(final Class<? extends Condition> c, final String property, final String type) {
		register(c, PropertyType.BE, property, type);
		ArrayList<SyntaxElementInfo<? extends Condition>> conditons = new ArrayList<>(Skript.getConditions());
		conditons.add(0, conditons.get(conditons.size() - 1));
		conditons.remove(conditons.size() - 1);
		Skript.getConditions().clear();
		Skript.getConditions().addAll(conditons);
	}

}
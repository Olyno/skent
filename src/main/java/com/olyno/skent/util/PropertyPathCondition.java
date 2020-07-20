package com.olyno.skent.util;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.Condition;

public abstract class PropertyPathCondition<T> extends PropertyCondition<T> {
 
    public static void registerCondition(final Class<? extends Condition> c, final String property, final String type) {
		register(c, "[a[n]] " + property + " (file|dir[ectory]|path)", type);
	}

}
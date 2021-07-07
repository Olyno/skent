package com.olyno.skent.util.skript;

import com.olyno.skent.util.scope.EffectSection;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;

public abstract class AsyncEffect extends EffectSection implements Runnable {

    private Event event;
    private Object localVars;
	protected boolean needExecuteCode = false; 

    protected abstract void executeAsync(Event e);
    protected abstract boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult);

    protected static <E extends Condition> void registerAsyncEffect(final Class<E> effect, final String... patterns) {
        Skript.registerCondition(effect, patterns);
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        needExecuteCode = hasSection();
        if (checkIfCondition()) return false;
        boolean result = this.initAsync(expr, matchedPattern, isDelayed, parseResult);
        this.loadSection(result);
        return result;
    }

    @Override
    protected void execute(Event e) {
        this.event = e;
        localVars = Variables.removeLocals(e);
        if (localVars != null) {
            Variables.setLocalVariables(e, localVars);
        }
        Thread effect = new Thread(this);
        effect.setName(this.toString());
        effect.start();
    }

    @Override
    public void run() {
        if (localVars != null) {
            Variables.setLocalVariables(this.event, localVars);
        }
        this.executeAsync(this.event);
        if (this.needExecuteCode) {
            this.runSection(this.event);
        }
        Variables.removeLocals(this.event);
    }
    
}

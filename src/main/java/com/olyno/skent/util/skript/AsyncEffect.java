package com.olyno.skent.util.skript;

import java.util.stream.Stream;

import org.bukkit.event.Event;

import com.olyno.skent.Skent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;

public abstract class AsyncEffect extends Effect implements Runnable {

    enum AsyncKeyword {
        NONE,
        SYNC,
        ASYNC;
    }

    private Event event;
    private AsyncKeyword asyncKeyword;
    private boolean isDefaultAsync = Skent.config.getBoolean("is_default_async");

    private Object variables;

    protected abstract void executeAsync(Event e);
    protected abstract boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult);

    protected static <E extends Effect> void registerAsyncEffect(final Class<E> effect, final String... patterns) {
        Skript.registerEffect(
            effect,
            Stream.of(patterns)
                .map(pattern -> "[(1¦sync|2¦async)] " + pattern)
                .toArray(String[]::new)
        );
    }    

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        asyncKeyword = AsyncKeyword.values()[parseResult.mark];
        return initAsync(expr, matchedPattern, isDelayed, parseResult);
    }

    @Override
    protected void execute(Event e) {
        this.event = e;
        boolean isAsync = asyncKeyword.equals(AsyncKeyword.ASYNC)
            || (asyncKeyword.equals(AsyncKeyword.NONE) && isDefaultAsync);
        if (isAsync) {
            variables = Variables.removeLocals(this.event);
            Thread effect = new Thread(this);
            effect.setName(this.toString());
            effect.start();
        } else {
            run();
        }
    }

    @Override
    public void run() {
        if (variables != null) {
            Variables.setLocalVariables(this.event, variables);
        }
        executeAsync(this.event);
    }
  
}

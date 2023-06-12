/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package com.olyno.skent.util.skript;

import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import com.olyno.skent.Skent;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;

/**
 * Effects that extend this class are ran asynchronously. Next trigger item will be ran
 * in main server thread, as if there had been a delay before.
 * <p>
 * Majority of Skript and Minecraft APIs are not thread-safe, so be careful.
 *
 * Make sure to add set {@link ch.njol.skript.ScriptLoader#hasDelayBefore} to
 * {@link ch.njol.util.Kleenean#TRUE} in the {@code init} method.
 * 
 * This version is modified to be flexible with "async" and "sync" keywords.
 */
public abstract class AsyncEffect extends Effect {

    enum AsyncKeyword {
        NONE,
        SYNC,
        ASYNC;
    }

    private AsyncKeyword asyncKeyword;
    private boolean isDefaultAsync = Skent.config.getBoolean("is_default_async");

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
	protected TriggerItem walk(Event e) {
		debug(e, true);
		
		Delay.addDelayedEvent(e); // Mark this event as delayed
        boolean isAsync = asyncKeyword.equals(AsyncKeyword.ASYNC) // Check if we should run asynchronously
            || (asyncKeyword.equals(AsyncKeyword.NONE) && isDefaultAsync);

		if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
			return null;

        if (isAsync) {
            Object localVars = Variables.removeLocals(e); // Back up local variables
            Bukkit.getScheduler().runTaskAsynchronously(Skent.instance, () -> {
                // Re-set local variables
                if (localVars != null)
                    Variables.setLocalVariables(e, localVars);
                
                execute(e); // Execute this effect
                
                if (getNext() != null) {
                    Bukkit.getScheduler().runTask(Skent.instance, () -> { // Walk to next item synchronously
                        Object timing = null;
                        if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                            Trigger trigger = getTrigger();
                            if (trigger != null) {
                                timing = SkriptTimings.start(trigger.getDebugLabel());
                            }
                        }
                        
                        TriggerItem.walk(getNext(), e);
                        
                        Variables.removeLocals(e); // Clean up local vars, we may be exiting now
                        
                        SkriptTimings.stop(timing); // Stop timing if it was even started
                    });
                } else {
                    Variables.removeLocals(e);
                }
            });
        } else {
           this.execute(e);
        }
		
		return null;
	}
  
}

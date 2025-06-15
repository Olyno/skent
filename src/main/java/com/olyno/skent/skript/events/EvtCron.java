package com.olyno.skent.skript.events;

import java.util.UUID;

import org.bukkit.event.Event;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.cron.CronExpressionSchedule;
import com.olyno.skent.skript.events.bukkit.CronEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptEventHandler;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;

public class EvtCron extends SkriptEvent {

    private static final Scheduler SCHEDULER;

    static {
        Skript.registerEvent("Cron task", EvtCron.class, CronEvent.class,
            "cron %string% start"
        )
            .description("Peridically execute a task (cron).")
            .examples(
                "on cron \"0 0/1 * * *\" start:\n" +
                    "\tbroadcast \"New time yeay!\""
            )
            .since("3.2.0");

        SCHEDULER = new Scheduler();
    }

    private String schedulerId;
	private String period;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        period = ((Literal<String>) args[0]).getSingle();
        return true;
    }

    private void execute() {
		final Trigger trigger = this.trigger;
		if (trigger == null) {
			assert false;
			return;
		}
		final CronEvent event = new CronEvent();
		SkriptEventHandler.logEventStart(event);
		SkriptEventHandler.logTriggerStart(trigger);
		trigger.execute(event);
		SkriptEventHandler.logTriggerEnd(trigger);
		SkriptEventHandler.logEventEnd();
	}

    @Override
    public boolean check(Event event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean postLoad() {
        schedulerId = UUID.randomUUID().toString();
        final CronExpressionSchedule schedule = period.split(" ").length == 6 
            ? CronExpressionSchedule.parseWithSeconds(period)
            : CronExpressionSchedule.parse(period);
        SCHEDULER.schedule(schedulerId, () -> execute(), schedule);
        return true;
    }

    @Override
    public void unload() {
        SCHEDULER.cancel(schedulerId);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "cron '" + period + "'' start";
    }
}

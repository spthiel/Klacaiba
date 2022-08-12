package me.spthiel.klacaiba.module.actions.information.counter;

import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class TimestampToDate extends BaseScriptAction {
	
	public TimestampToDate() {
		
		super("timestamptodate");
	}
	
	@Override
	public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		if (params.length == 0) {
			return new ReturnValue("See https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html for a full date format reference.");
		}
		
		String  dateformat     = "dd.MMM.yyyy";
		boolean isMilliseconds = false;
		long    timestamp      = 0;
		
		String current = provider.expand(macro, params[0], false);
		if (!current.matches("\\d+")) {
			return new ReturnValue("Invalid timestamp. [0]");
		} else {
			try {
				timestamp = Long.parseLong(current);
			} catch (NumberFormatException e) {
				return new ReturnValue("Invalid timestamp. [1]");
			}
		}
		
		if (params.length > 1) {
			current = provider.expand(macro, params[1], false);
			if (current.matches("(?:1|true|false|0)")) {
				isMilliseconds = current.matches("(?:1|true)");
			} else {
				dateformat = current;
				if (params.length > 2) {
					isMilliseconds = provider.expand(macro, params[2], false).matches("(?:1|true)");
				}
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return new ReturnValue(format.format(new Date(timestamp)));
	}
	
	@Nonnull
	@Override
	public String getUsage() {
		
		return "&date = timestamptodate(<timestamp>,[in milliseconds|date format],[in milliseconds])";
	}
	
	@Nonnull
	@Override
	public String getDescription() {
		
		return "Format a timestamp in seconds or optionally in milliseconds. See https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html for possible date formats";
	}
	
	@Nonnull
	@Override
	public String getReturnType() {
		
		return "Date";
	}
}

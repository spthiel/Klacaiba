package me.spthiel.klacaiba;

import net.eq2online.macros.scripting.api.IScriptAction;

import me.spthiel.klacaiba.module.actions.information.*;
import me.spthiel.klacaiba.module.actions.information.counter.*;
import me.spthiel.klacaiba.module.actions.information.external.*;
import me.spthiel.klacaiba.module.actions.language.*;
import me.spthiel.klacaiba.module.actions.mod.*;
import me.spthiel.klacaiba.module.actions.mod.exec.Exec;
import me.spthiel.klacaiba.module.actions.player.*;
import me.spthiel.klacaiba.module.actions.player.inventory.*;
import me.spthiel.klacaiba.module.actions.world.*;
import me.spthiel.klacaiba.module.events.PollEvent;

public class Actions {
	
	public static IScriptAction[] ACTIONS = {
		new CalcYawTo(),
		new For(),
		new GetJsonAsArray(),
		new GetJsonKeys(),
		new GetSlotItem(),
		new Gui(),
		new Http(),
		new IfFileExists(),
		new JsonGet(),
		new LogTo(),
		new Look(),
		new Looks(),
		new MkDir(),
		new PlaceSign(),
		new Readfile(),
		new ShowGui(),
		new WriteFile(),
		new Sort(),
		new Teammembers(),
		new Score(),
		new Exec(),
		new Countdownto(),
		new Countdownfrom(),
		new Counter(),
		new Countup(),
		new LeftPad(),
		new TimeToSec(),
		new SecToTime(),
		new GetSlotItemInventory(),
		new GetSlotInventory(),
		new IfInvIsFull(),
		new IfEnchanted(),
		new GetSlotItemInventory(),
		new GetMouseItem(),
		new GetEmptySlots(),
		new GetChestName(),
		new GetFishHook(),
		new SetSlotItem(),
		new GetId(),
		new GetIdRel(),
		new Particle(),
		new Map(),
		new CountInvItem(),
		new CountItem(),
		new Pexec(),
		new Mexec(),
		new CreateControl(),
		new DeleteControl(),
		new GetItemInfo(),
		new TimestampToDate(),
		new Put(),
		new Push(),
		new Do(),
		new IfInInv(),
		new Strlen(),
		new IfCanHarvestBlock(),
		new PollEvent(),
		new GetBreakTime(),
		new Restart(),
		new Char(),
		new Unix(),
		new Calc(),
		new Mod(),
		new OldName(),
		new GetPlayerUUID(),
		new FormatNumber(),
		new Trace(),
		new Pick(),
	};
	
}

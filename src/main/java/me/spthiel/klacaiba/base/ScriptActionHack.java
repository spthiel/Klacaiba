package me.spthiel.klacaiba.base;

import me.spthiel.documentor.main.DocumentorAPI;
import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.module.actions.auto.ElseIfBase;
import me.spthiel.klacaiba.module.actions.auto.IfNotBase;
import me.spthiel.klacaiba.module.actions.auto.UntilBase;
import me.spthiel.klacaiba.module.actions.auto.WhileBase;
import me.spthiel.klacaiba.module.actions.information.*;
import me.spthiel.klacaiba.module.actions.information.counter.*;
import me.spthiel.klacaiba.module.actions.information.external.*;
import me.spthiel.klacaiba.module.actions.player.*;
import me.spthiel.klacaiba.module.actions.player.inventory.*;
import me.spthiel.klacaiba.module.actions.language.*;
import me.spthiel.klacaiba.module.actions.mod.*;
import me.spthiel.klacaiba.module.actions.world.*;
import me.spthiel.klacaiba.module.events.PollEvent;
import me.spthiel.klacaiba.module.iterators.*;
import me.spthiel.klacaiba.module.actions.information.external.WriteFile;
import me.spthiel.klacaiba.module.actions.information.external.Readfile;
import me.spthiel.klacaiba.module.actions.mod.exec.Exec;
import me.spthiel.klacaiba.utils.HackObject;
import me.spthiel.klacaiba.utils.PacketListener;

import net.eq2online.macros.compatibility.AllowedCharacters;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map;
import java.util.regex.Pattern;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionHack extends ScriptAction {
	
	public static  boolean             hacked     = false;
	private static boolean             documented = false;
	public static  List<IScriptAction> actions    = new ArrayList<>();
	
	public ScriptActionHack() {
		
		super(ScriptContext.MAIN, "hack");
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
		
		return hack();
	}
	
	public static ReturnValue hack() {
		
		if (!hacked) {
			HackObject object = getHackObject();
			
			addActions(object);
			hacked = true;
			
			if (!documented) {
				
				try {
					ITextComponent line1 = new TextComponentString("[WARN] Klacaiba\n").setStyle(new Style().setColor(
						TextFormatting.RED));
					ITextComponent line2 = new TextComponentString(
						"[WARN] Documentor not found but highly advised for ingame syntax lookup\n")
						.setStyle(new Style().setColor(TextFormatting.RED));
					ITextComponent line3 = new TextComponentString("[WARN] Click ").setStyle(new Style().setColor(
						TextFormatting.RED));
					ITextComponent line32 = new TextComponentString(" to get to the download page for the module.").setStyle(
						new Style()
							.setColor(TextFormatting.RED));
					Style clickStyle = new Style();
					clickStyle.setColor(TextFormatting.GRAY)
							  .setClickEvent(new ClickEvent(
								  ClickEvent.Action.OPEN_URL,
								  "https://spthiel.github.io/Modules/?name=Documentor"
							  ))
							  .setUnderlined(true);
					ITextComponent click = new TextComponentString("here").setStyle(clickStyle);
					
					Minecraft.getMinecraft().player.sendMessage(line1
																	.appendSibling(line2)
																	.appendSibling(line3)
																	.appendSibling(click)
																	.appendSibling(line32)
					);
				} catch (StackOverflowError e) {
					for (StackTraceElement element : e.getStackTrace()) {
						System.out.println(element.getClassName() + " " + element.getFileName() + " #" + element.getLineNumber());
					}
				}
			}
			return new ReturnValue("Success");
		} else {
			return new ReturnValue("Already hacked");
		}
		
	}
	
	private void addDocument() throws ClassNotFoundException {
		
		DocumentorAPI api = new DocumentorAPI("klacaiba");
		
		actions.stream()
			   .filter(action -> action instanceof IDocumentable)
			   .map(action -> (IDocumentable) action)
			   .forEach(
				   iDocumentable ->
					   api.addDocumentation(
						   (IScriptAction) iDocumentable,
						   iDocumentable.getUsage(),
						   iDocumentable.getDescription(),
						   iDocumentable.getReturnType()
					   )
			   );
		
	}
	
	private static void addDocument(List<IScriptAction> actions) throws ClassNotFoundException {
		
		DocumentorAPI api = new DocumentorAPI("klacaiba");
		
		
		actions.stream()
			   .filter(action -> action instanceof IDocumentable)
			   .map(action -> (IDocumentable) action)
			   .forEach(
				   iDocumentable ->
					   api.addDocumentation(
						   (IScriptAction) iDocumentable,
						   iDocumentable.getUsage(),
						   iDocumentable.getDescription(),
						   iDocumentable.getReturnType()
					   )
			   );
		
	}
	
	private static void addActions(HackObject object) {
		
		actions.forEach(object :: addOrPut);
		
		object.addOrPut("players", IteratorPlayers.class);
		object.addOrPut("teams", IteratorTeams.class);
		object.addOrPut("objectives", IteratorObjectives.class);
		object.addOrPut("scores", IteratorScores.class);
		object.addOrPut("trades", IteratorTrades.class);
		object.addOrPut("inventory", IteratorInventory.class);
		
		List<IScriptAction> toPut = new LinkedList<>();
		object.actionsList.stream()
						  .filter(action -> action instanceof ScriptActionIf)
						  .filter(action -> !action.getClass()
												   .equals(ScriptActionIf.class))
						  .map(action -> (ScriptActionIf) action)
						  .forEach(
							  action -> {
								  if (!object.actions.containsKey("else" + action.getName())) {
									  toPut.add(new ElseIfBase(action));
								  }
								  IfNotBase ifNotBase = new IfNotBase(action);
								  toPut.add(ifNotBase);
								  toPut.add(new ElseIfBase(ifNotBase));
								  toPut.add(new UntilBase(action));
								  toPut.add(new WhileBase(action));
							  }
						  );
		
		
		try {
			addDocument(toPut);
			System.out.println("Documentation for while, until and else actions loaded");
		} catch (NoClassDefFoundError | ClassNotFoundException ignored) {
		}
		
		toPut.forEach(object :: addOrPut);
		
		try {
			setFinalStatic(AllowedCharacters.class.getField("CHARACTERS"), buildUnicodeString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static String unicodeString = null;
	
	private static String buildUnicodeString() {
		
		if (unicodeString != null) {
			return unicodeString;
		}
		StringBuilder out = new StringBuilder();
		out.append(AllowedCharacters.CHARACTERS);
		for (int i = 256 ; i <= 0xFFFF ; i++) {
			out.append((char) i);
		}
		return (unicodeString = out.toString());
	}
	
	private static void setFinalStatic(Field field, Object newValue) throws Exception {
		
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		
		field.set(null, newValue);
	}
	
	@SuppressWarnings("unchecked")
	private static HackObject getHackObject() {
		
		
		HackObject out = new HackObject();
		
		ScriptCore core   = ScriptContext.MAIN.getCore();
		Field[]    fields = ScriptCore.class.getDeclaredFields();
		
		for (Field f : fields) {
			try {
				f.setAccessible(true);
				String name  = f.getName();
				Object value = f.get(core);
				if (name.equalsIgnoreCase("actions")) {
					out.actions = (HashMap<String, IScriptAction>) value;
				} else if (name.equalsIgnoreCase("iterators")) {
					out.iterators = (Map<String, Class<? extends IScriptedIterator>>) value;
				} else if (name.equalsIgnoreCase("actionsList")) {
					out.actionsList = (List<IScriptAction>) value;
				} else if (name.equalsIgnoreCase("actionRegex")) {
					out.pattern = (Pattern) value;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return out;
	}
	
	@Override
	public void onInit() {
		
		this.context.getCore()
					.registerScriptAction(this);
		actions.add(new CalcYawTo());
		actions.add(new For());
		actions.add(new GetJsonAsArray());
		actions.add(new GetJsonKeys());
		actions.add(new GetSlotItem());
		actions.add(new Gui());
		actions.add(new Http());
		actions.add(new IfFileExists());
		actions.add(new JsonGet());
		actions.add(new LogTo());
		actions.add(new Look());
		actions.add(new Looks());
		actions.add(new MkDir());
		actions.add(new PlaceSign());
		actions.add(new Readfile());
		actions.add(new ShowGui());
		actions.add(new WriteFile());
		actions.add(new Sort());
		actions.add(new Teammembers());
		actions.add(new Score());
		actions.add(new Exec());
		actions.add(new Countdownto());
		actions.add(new Countdownfrom());
		actions.add(new Counter());
		actions.add(new Countup());
		actions.add(new LeftPad());
		actions.add(new TimeToSec());
		actions.add(new SecToTime());
		actions.add(new GetSlotItemInventory());
		actions.add(new GetSlotInventory());
		actions.add(new IfInvIsFull());
		actions.add(new IfEnchanted());
		actions.add(new GetSlotItemInventory());
		actions.add(new GetMouseItem());
		actions.add(new GetEmptySlots());
		actions.add(new GetChestName());
		actions.add(new GetFishHook());
		actions.add(new SetSlotItem());
		actions.add(new GetId());
		actions.add(new GetIdRel());
		actions.add(new Particle());
		actions.add(new me.spthiel.klacaiba.module.actions.language.Map());
		actions.add(new CountInvItem());
		actions.add(new CountItem());
		actions.add(new Pexec());
		actions.add(new Mexec());
		actions.add(new CreateControl());
		actions.add(new DeleteControl());
		actions.add(new GetItemInfo());
		actions.add(new TimestampToDate());
		actions.add(new Put());
		actions.add(new Push());
		actions.add(new Do());
		actions.add(new IfInInv());
		actions.add(new Strlen());
		actions.add(new IfCanHarvestBlock());
		actions.add(new PollEvent());
		actions.add(new GetBreakTime());
		actions.add(new Restart());
		actions.add(new Char());
		actions.add(new Unix());
		actions.add(new Calc());
		actions.add(new Mod());
		actions.add(new OldName());
		actions.add(new GetPlayerUUID());
		actions.add(new FormatNumber());
		actions.add(new Trace());
		
		LinkedList<IScriptAction> toAdd = new LinkedList<>();
		
		actions.stream()
			   .filter(action -> action instanceof IMultipleScriptAction)
			   .map(action -> (IMultipleScriptAction) action)
			   .forEach(action -> action.registerAdditionalActions(toAdd :: add));
		actions.addAll(toAdd);
		try {
			addDocument();
			documented = true;
			System.out.println("Documentation loaded");
		} catch (NoClassDefFoundError | ClassNotFoundException e) {
			System.out.println("Documentor not found, documentation not loaded");
		}
	}
	
}

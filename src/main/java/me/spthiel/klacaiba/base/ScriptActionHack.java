package me.spthiel.klacaiba.base;

import me.spthiel.documentor.main.DocumentorAPI;
import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.module.actions.auto.ElseIfBase;
import me.spthiel.klacaiba.module.actions.auto.IfNotBase;
import me.spthiel.klacaiba.module.actions.auto.UntilBase;
import me.spthiel.klacaiba.module.actions.auto.WhileBase;
import me.spthiel.klacaiba.module.actions.base.IMultipleScriptAction;
import me.spthiel.klacaiba.module.actions.information.*;
import me.spthiel.klacaiba.module.actions.information.counter.*;
import me.spthiel.klacaiba.module.actions.information.external.*;
import me.spthiel.klacaiba.module.actions.player.*;
import me.spthiel.klacaiba.module.actions.player.inventory.*;
import me.spthiel.klacaiba.module.actions.language.*;
import me.spthiel.klacaiba.module.actions.mod.*;
import me.spthiel.klacaiba.module.actions.world.*;
import me.spthiel.klacaiba.module.actions.base.IDocumentable;
import me.spthiel.klacaiba.module.events.PollEvent;
import me.spthiel.klacaiba.module.iterators.*;
import me.spthiel.klacaiba.module.actions.information.external.WriteFile;
import me.spthiel.klacaiba.module.actions.information.external.Readfile;
import me.spthiel.klacaiba.module.actions.mod.exec.Exec;
import me.spthiel.klacaiba.utils.HackObject;

import net.eq2online.macros.compatibility.AllowedCharacters;
import net.eq2online.macros.scripting.ModuleLoader;
import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
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
		CrashReport
	}
	
	private static void addActions(HackObject object) {
		
		actions.forEach(object :: addOrPut);
		
		
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
		
	}
	
	@Override
	public void onInit() {
		
		this.context.getCore()
					.registerScriptAction(this);
//		try {
//			addDocument();
//			documented = true;
//			System.out.println("Documentation loaded");
//		} catch (NoClassDefFoundError | ClassNotFoundException e) {
//			System.out.println("Documentor not found, documentation not loaded");
//		}
	}
	
}

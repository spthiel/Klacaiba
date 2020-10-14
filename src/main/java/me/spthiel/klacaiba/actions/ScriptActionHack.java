package me.spthiel.klacaiba.actions;

import me.spthiel.documentor.main.DocumentorAPI;
import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.newactions.with.WriteFile;
import me.spthiel.klacaiba.newactions.with.Readfile;
import me.spthiel.klacaiba.newactions.with.*;
import me.spthiel.klacaiba.newactions.without.*;
import me.spthiel.klacaiba.newactions.without.exec.Exec;
import me.spthiel.klacaiba.utils.HackObject;

import net.eq2online.macros.compatibility.AllowedCharacters;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	           
            if(!documented) {
            	
                try {
                    ITextComponent line1 = new TextComponentString("[WARN] Klacaiba\n").setStyle(new Style().setColor(TextFormatting.RED));
                    ITextComponent line2 = new TextComponentString("[WARN] Documentor not found but highly advised for ingame syntax lookup\n").setStyle(new Style().setColor(TextFormatting.RED));
                    ITextComponent line3 = new TextComponentString("[WARN] Click ").setStyle(new Style().setColor(TextFormatting.RED));
                    ITextComponent line32 = new TextComponentString(" to get to the download page for the module.").setStyle(new Style().setColor(TextFormatting.RED));
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
                } catch(StackOverflowError e) {
                    for(StackTraceElement element : e.getStackTrace()) {
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
               .forEach(iDocumentable ->
                                api.addDocumentation(
                                        (IScriptAction) iDocumentable,
                                        iDocumentable.getUsage(),
                                        iDocumentable.getDescription(),
                                        iDocumentable.getReturnType()
                                                    ));
        
    }
    
    private static void addActions(HackObject object) {
        
        actions.forEach(object :: addOrPut);
        
        object.addOrPut("players", IteratorPlayers.class);
        object.addOrPut("teams", IteratorTeams.class);
        object.addOrPut("objectives", IteratorObjectives.class);
        object.addOrPut("scores", IteratorScores.class);
        object.addOrPut("trades", IteratorTrades.class);
        
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
        
        this.context.getCore().registerScriptAction(this);
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
        actions.add(new me.spthiel.klacaiba.newactions.with.Map());
        actions.add(new CountInvItem());
        actions.add(new CountItem());
        actions.add(new Pexec());
        actions.add(new Mexec());
        actions.add(new CreateControl());
        actions.add(new DeleteControl());
        actions.add(new GetItemInfo());
        
        try {
            addDocument();
            documented = true;
            System.out.println("Documentation loaded");
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            System.out.println("Documentor not found, documentation not loaded");
        }
    }
    
}

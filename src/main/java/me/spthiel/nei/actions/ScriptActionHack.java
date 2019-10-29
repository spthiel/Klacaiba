package me.spthiel.nei.actions;

import me.spthiel.documentor.main.DocumentorAPI;
import me.spthiel.nei.ModuleInfo;
import me.spthiel.nei.newactions.WriteFile;
import me.spthiel.nei.newactions.Readfile;
import me.spthiel.nei.newactions.*;
import me.spthiel.nei.utils.HackObject;

import net.eq2online.macros.compatibility.AllowedCharacters;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionHack extends ScriptAction {

	public static boolean             hacked  = false;
	public static List<IScriptAction> actions = new ArrayList<>();

    public ScriptActionHack() {
        super(ScriptContext.MAIN, "hack");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

		return hack();
    }
    
    public static ReturnValue hack() {
	
		if(!hacked) {
			HackObject object = getHackObject();
			addActions(object);
			hacked = true;
			return new ReturnValue("Success");
		} else {
			return new ReturnValue("Already hacked");
		}
		
	}

    private void addDocument() throws ClassNotFoundException {
    
		DocumentorAPI api = new DocumentorAPI("notEnoughInformation");
		
		actions.stream()
			   .filter(action -> action instanceof IDocumentable)
			   .map(action -> (IDocumentable)action)
			   .forEach(iDocumentable ->
								 api.addDocumentation((IScriptAction) iDocumentable,
													  iDocumentable.getUsage(),
													  iDocumentable.getDescription(),
													  iDocumentable.getReturnType()));
		
	}
    
    private static void addActions(HackObject object) {

	    actions.forEach(object::addOrPut);
	    
		object.addOrPut("players", IteratorPlayers.class);
		object.addOrPut("teams", IteratorTeams.class);
		object.addOrPut("objectives", IteratorObjectives.class);
		object.addOrPut("scores", IteratorScores.class);
	
		try {
			setFinalStatic(AllowedCharacters.class.getField("CHARACTERS"), buildUnicodeString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
    
    private static String unicodeString = null;
    
    private static String buildUnicodeString() {
    	if(unicodeString != null) {
    		return unicodeString;
		}
    	StringBuilder out = new StringBuilder();
    	out.append(AllowedCharacters.CHARACTERS);
    	for(int i = 256; i <= 0xFFFF; i++) {
    		out.append((char)i);
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

	    ScriptCore core = ScriptContext.MAIN.getCore();
	    Field[] fields = ScriptCore.class.getDeclaredFields();

	    for (Field f : fields) {
		    try {
			    f.setAccessible(true);
			    String name = f.getName();
			    Object value = f.get(core);
			    if(name.equalsIgnoreCase("actions")) {
					out.actions = (HashMap<String, IScriptAction>) value;
				} else if(name.equalsIgnoreCase("iterators")) {
			    	out.iterators = (Map<String, Class<? extends IScriptedIterator>>) value;
			    } else if(name.equalsIgnoreCase("actionsList")) {
				    out.actionsList = (List<IScriptAction>)value;
                } else if(name.equalsIgnoreCase("actionRegex")) {
				    out.pattern = (Pattern)value;
                }

		    } catch(Exception e) {
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
		try {
			addDocument();
			System.out.println("Documentation loaded");
		} catch(NoClassDefFoundError | ClassNotFoundException e) {
			System.out.println("Documentor not found, documentation not loaded");
		}
    }

}

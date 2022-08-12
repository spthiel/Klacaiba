package me.spthiel.klacaiba.module.actions.mod.exec;

import java.io.File;
import net.eq2online.macros.core.MacroIncludeProcessor;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroTemplate;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IVariableProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class Exec extends ScriptAction {
    
    public Exec() {
        super(ScriptContext.MAIN, "exec");
    }
    
    public boolean isThreadSafe() {
        return false;
    }
    
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if (params.length > 0) {
            String paramCompiled = provider.expand(macro, params[0], false);
            String macroName = null;
            if (params.length > 1) {
                macroName = provider.expand(macro, params[1], false);
            }
            
            IVariableProvider contextProvider = null;
            if (params.length > 2) {
                contextProvider = new ExecVariableProvider(params, 2, provider, macro);
            }
            
            if (MacroIncludeProcessor.PATTERN_FILE_NAME.matcher(paramCompiled).matches()) {
                if (instance.getState() != null) {
                    IMacroTemplate tpl = (IMacroTemplate)instance.getState();
                    provider.getMacroEngine().playMacro(tpl, false, ScriptContext.MAIN, contextProvider);
                } else {
                    File playFile = provider.getMacroEngine().getFile(paramCompiled);
                    if (playFile.isFile()) {
                        IMacroTemplate tpl = provider.getMacroEngine().createFloatingTemplate("$${$$<" + paramCompiled + ">}$$", macroName);
                        instance.setState(tpl);
                        provider.getMacroEngine().playMacro(tpl, false, ScriptContext.MAIN, contextProvider);
                    }
                }
            }
        }
        
        return null;
    }
}


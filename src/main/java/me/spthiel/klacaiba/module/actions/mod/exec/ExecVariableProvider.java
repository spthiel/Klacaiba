package me.spthiel.klacaiba.module.actions.mod.exec;

import net.eq2online.macros.core.MacroExecVariableProvider;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class ExecVariableProvider extends MacroExecVariableProvider {
    
    private final List<Object> parameters;
    
    public ExecVariableProvider(String[] parameters, int ignore, IScriptActionProvider provider, IMacro macro) {
        super(new String[0], 0, provider, macro);
        this.parameters = new ArrayList<>(parameters.length - ignore);
        
        for(int paramIndex = ignore; paramIndex < parameters.length; ++paramIndex) {
            String parameter = Matcher.quoteReplacement(provider.expand(macro, parameters[paramIndex], false));
            if (parameter.matches("^\\-?[0-9]{7}$")) {
                try {
                    Integer paramValue = Integer.parseInt(parameter);
                    this.parameters.add(paramValue);
                } catch (NumberFormatException e) {
                    this.parameters.add(parameter);
                }
            } else if ("true".equalsIgnoreCase(parameter)) {
                this.parameters.add(Boolean.TRUE);
            } else if ("false".equalsIgnoreCase(parameter)) {
                this.parameters.add(Boolean.FALSE);
            } else {
                this.parameters.add(parameter);
            }
        }
        
        this.storeParametersAsVariables();
    }
    
    private void storeParametersAsVariables() {
        int variableIndex = 1;
        Iterator var2 = this.parameters.iterator();
        
        while(var2.hasNext()) {
            Object parameter = var2.next();
            if (parameter instanceof Integer) {
                this.storeVariable(String.format("#var%d", variableIndex++), (Integer)parameter);
            } else if (parameter instanceof Boolean) {
                this.storeVariable(String.format("var%d", variableIndex++), (Boolean)parameter);
            } else {
                this.storeVariable(String.format("&var%d", variableIndex++), parameter.toString());
            }
        }
        
    }
    
    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }
    
    public String provideParameters(String macro) {
        if (this.parameters != null) {
            int variableIndex = 1;
            
            Object parameter;
            for(Iterator var3 = this.parameters.iterator(); var3.hasNext(); macro = macro.replaceAll("\\x24\\x24\\[" + variableIndex++ + "\\]", parameter.toString())) {
                parameter = var3.next();
            }
        }
        
        return macro.replaceAll("\\x24\\x24\\[[0-9]+\\]", "");
    }
}

package me.spthiel.klacaiba.module.actions.language;

import net.eq2online.macros.scripting.ReturnValueLog;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class Sort extends BaseScriptAction {
    
    public Sort() {
    
        super("sort");
    }
    
    public IReturnValue run(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        if(params.length > 0) {
            int idx = 0;
            boolean asc = true;
            if(params[0].toLowerCase().matches("^asc|dsc$")) {
                idx++;
                if(params[0].equalsIgnoreCase("dsc")) {
                    asc = false;
                }
            }
            
            List<String> listnames = new ArrayList<>();
            List<Object[]> lists = new ArrayList<>();
            
            boolean first = true;
            int size = -1;
            
            for(;idx < params.length; idx++) {
                String arrayName;
                if ((arrayName = Variable.getValidVariableOrArraySpecifier(params[idx])) != null) {
                    if (provider.getArrayExists(macro, arrayName)) {
                        listnames.add(arrayName);
                        if(size == -1) {
                            size = provider.getArraySize(macro, arrayName);
                        } else if(size != provider.getArraySize(macro, arrayName)) {
                            return new ReturnValueLog("§cIllegal arguments in sort, all arrays have to be the same size.");
                        }
                    }
                }
            }
            
            for(int i = 0; i < size; i++) {
                Object[] array = new Object[listnames.size()];
                for(int j = 0; j < listnames.size(); j++) {
                    array[j] = provider.getArrayElement(macro, listnames.get(j), i);
                }
                lists.add(array);
            }
            
            sort(asc, lists);
    
            for(int i = 0; i < listnames.size(); i++) {
                provider.clearArray(macro, listnames.get(i));
            }
    
            for (Object[] item : lists) {
                for(int j = 0; j < item.length; j++) {
                    String arrayname = listnames.get(j);
                    provider.pushValueToArray(macro, arrayname, item[j].toString());
                }
            }
            
        }
        
        return null;
    }
    
    private void sort(boolean asc, List<Object[]> lists) {
        Object[] first = lists.get(0);
        Object o = first[0];
        if(o instanceof Integer) {
            sortInt(asc, lists);
        } else if(o instanceof String) {
            sortString(asc, lists);
        } else if(o instanceof Boolean) {
            sortBoolean(asc, lists);
        }
    }
    
    private void sortInt(boolean asc, List<Object[]> lists) {
        
        lists.sort((o1, o2) -> {
            int i1 = (int)o1[0];
            int i2 = (int)o2[0];
            
            return (asc ? 1 : -1) * Integer.compare(i1, i2);
        });
        
    }
    
    private void sortString(boolean asc, List<Object[]> lists) {
    
        lists.sort((o1, o2) -> {
            String s1 = (String)o1[0];
            String s2 = (String)o2[0];
        
            return (asc ? 1 : -1) * String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
        });
    }
    
    // I have no clue why anyone would ever do this
    private void sortBoolean(boolean asc, List<Object[]> lists) {
        lists.sort((o1, o2) -> {
            boolean b1 = (boolean)o1[0];
            boolean b2 = (boolean)o2[0];
        
            return (asc ? 1 : -1) * Boolean.compare(b1, b2);
        });
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "sort([ASC|DSC],<#array[]|&array[]|array[]>,[#array[]|&array[]|array[]],...)";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Sorts array ASCending or DeSCending and copies the steps to sort ontop all additional given arrays";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "";
    }
}

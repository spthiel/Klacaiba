package me.spthiel.klacaiba.module.iterators;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class IteratorScores extends ScriptedIterator {
    
    public IteratorScores(IScriptActionProvider provider, IMacro macro) {
        
        super(provider, macro);
        
        this.mc.world.getScoreboard().getScores()
                     .forEach(
                             score -> {
                                 this.begin();
                                 this.add("SCOREOBJECTIVENAME", score.getObjective().getName());
                                 this.add("SCOREPLAYERNAME", score.getPlayerName());
                                 this.add("SCOREVALUE", score.getScorePoints());
                                 this.end();
                             }
                             );
    }
}

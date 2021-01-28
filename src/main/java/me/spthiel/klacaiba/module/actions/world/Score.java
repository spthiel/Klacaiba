package me.spthiel.klacaiba.module.actions.world;

import net.eq2online.macros.scripting.api.*;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.Collectors;

import me.spthiel.klacaiba.base.BaseScriptAction;

public class Score extends BaseScriptAction {
    
    public Score() {
    
        super("score");
    }
    
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    
        Scoreboard     scoreboard = this.mc.world.getScoreboard();
        if(params.length > 0) {
            String objectivename = provider.expand(macro, params[0], false);
            ScoreObjective o          = scoreboard.getObjective(objectivename);
            
            if(params.length > 1) {
                String playername = provider.expand(macro, params[1], false);
                net.minecraft.scoreboard.Score s = scoreboard.getScores()
                                                             .stream()
                                                             .filter(score -> score.getObjective().equals(o))
                                                             .filter(score -> score.getPlayerName().equalsIgnoreCase(playername))
                                                             .findFirst().orElse(null);
                if(s != null) {
                    return new ReturnValue(s.getScorePoints());
                } else {
                    return new ReturnValue(0);
                }
            } else {
                ReturnValueArray out = new ReturnValueArray(false);
                out.putStrings(scoreboard.getScores()
                          .stream()
                          .filter(score -> score.getObjective().equals(o))
                          .map(score -> score.getPlayerName() + ":" + score.getScorePoints())
                          .collect(Collectors.toList()));
                return out;
            }
        } else {
    
            ReturnValueArray out = new ReturnValueArray(false);
            out.putStrings(new ArrayList<>(scoreboard.getObjectiveNames()));
            return out;
        }
        
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "score([objective],[playername])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Retuns the score of the player for the objective or all scores for the objective or the list of objective names.";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "Int or 0 when playername is present. String[] (as Playername:Value) when playername is not present. If neither objectivename or playername present returns list of objectivenames";
    }
}

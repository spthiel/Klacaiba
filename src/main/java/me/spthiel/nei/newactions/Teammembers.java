package me.spthiel.nei.newactions;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.Collectors;

import me.spthiel.nei.actions.IDocumentable;

public class Teammembers extends ScriptAction implements IDocumentable {
    
    public Teammembers() {
    
        super(ScriptContext.MAIN, "teammembers");
    }
    
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        
        Scoreboard     scoreboard = this.mc.world.getScoreboard();
            
        if(params.length > 0) {
            String          teamname = provider.expand(macro, params[0], false);
            ScorePlayerTeam team        = scoreboard.getTeam(teamname);
            if(team != null) {
                ReturnValueArray out = new ReturnValueArray(false);
                out.putStrings(new ArrayList<>(team.getMembershipCollection()));
                return out;
            } else {
                return new ReturnValueArray(false);
            }
        } else {
            ReturnValueArray out = new ReturnValueArray(false);
            out.putStrings(new ArrayList<>(scoreboard.getTeamNames()));
            return out;
        }
        
    }
    
    @Nonnull
    @Override
    public String getUsage() {
        
        return "teammembers([team])";
    }
    
    @Nonnull
    @Override
    public String getDescription() {
        
        return "Retuns the list of players in the scoreboard team or the list of teams.";
    }
    
    @Nonnull
    @Override
    public String getReturnType() {
        
        return "List of playernames when team is present or and empty list if the team doesn't exist. List of teamnames when team isn't present";
    }
}

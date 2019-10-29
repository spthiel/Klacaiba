package me.spthiel.nei.newactions;

import com.mojang.authlib.GameProfile;
import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardSaveData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class IteratorTeams extends ScriptedIterator {
    
    public IteratorTeams(IScriptActionProvider provider, IMacro macro) {
        
        super(provider, macro);
        
        this.mc.world.getScoreboard().getTeams()
                     .forEach(
                             team -> {
                                 this.begin();
                                 this.add("TEAMALLOWFRIENDLYFIRE", team.getAllowFriendlyFire());
                                 this.add("TEAMCOLLISIONRULE", team.getCollisionRule().name);
                                 this.add("TEAMCOLOR", team.getColor().getFriendlyName());
                                 this.add("TEAMDEATHMESSAGEVISIBILITY", team.getDeathMessageVisibility().internalName);
                                 this.add("TEAMDISPLAYNAME", team.getDisplayName());
                                 this.add("TEAMNAME", team.getName());
                                 this.add("TEAMNAMETAGVISIBILITY", team.getNameTagVisibility().internalName);
                                 this.add("TEAMSEEFRIENDLYINVISIBLES", team.getSeeFriendlyInvisiblesEnabled());
                                 this.add("TEAMPREFIX", team.getPrefix());
                                 this.add("TEAMSUFFIX", team.getSuffix());
                                 this.add("TEAMMEMBERS", team.getMembershipCollection());
                                 this.end();
                             }
                             );
    }
}

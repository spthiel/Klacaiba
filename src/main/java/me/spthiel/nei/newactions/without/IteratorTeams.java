package me.spthiel.nei.newactions.without;

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
        try {
            this.mc.world.getScoreboard().getTeams()
                 .forEach(
                     team -> {
                         try {
                             this.begin();
                             this.add("TEAMALLOWFRIENDLYFIRE", team.getAllowFriendlyFire());
                             this.add("TEAMCOLLISIONRULE", team.getCollisionRule().name);
                             this.add("TEAMCOLOR", team.getColor() != null ? team.getColor().getFriendlyName() : "reset");
                             this.add("TEAMDEATHMESSAGEVISIBILITY", team.getDeathMessageVisibility().internalName);
                             this.add("TEAMDISPLAYNAME", team.getDisplayName());
                             this.add("TEAMNAME", team.getName());
                             this.add("TEAMNAMETAGVISIBILITY", team.getNameTagVisibility().internalName);
                             this.add("TEAMSEEFRIENDLYINVISIBLES", team.getSeeFriendlyInvisiblesEnabled());
                             this.add("TEAMPREFIX", team.getPrefix());
                             this.add("TEAMSUFFIX", team.getSuffix());
                             this.add("TEAMMEMBERS", team.getMembershipCollection());
                             this.end();
                         } catch (NullPointerException e) {
                             System.out.println("ERROR DEBUG START ----------------------------------------------");
    
                             System.out.println("FriendlyFire: " + team.getAllowFriendlyFire());
                             System.out.println("Collision: " + team.getCollisionRule());
                             System.out.println("Color: " + team.getColor());
                             System.out.println("DeathMessage: " + team.getDeathMessageVisibility());
                             System.out.println("DisplayName: " + team.getDisplayName());
                             System.out.println("Name: " + team.getName());
                             System.out.println("NameTagVisibility: " + team.getNameTagVisibility());
                             System.out.println("SeeFriendlyInvisibles: " + team.getSeeFriendlyInvisiblesEnabled());
                             System.out.println("Prefix: " + team.getPrefix());
                             System.out.println("Suffix: " + team.getSuffix());
                             
                             System.out.println("ERROR DEBUG END ------------------------------------------------");
                             e.printStackTrace();
                             throw new RuntimeException("NPE in teaminner", e);
                         }
                     }
                 );
        } catch (NullPointerException e) {
            throw new RuntimeException("NPE in teams", e);
        }
    }
}

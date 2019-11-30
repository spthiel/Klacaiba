package me.spthiel.nei.newactions.without;

import com.mojang.authlib.GameProfile;
import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class IteratorPlayers extends ScriptedIterator {
    public IteratorPlayers(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);
        EntityPlayerSP thePlayer = this.mc.player;
    
        HashMap<String, NetworkPlayerInfo> entries = new HashMap<>();
        thePlayer.connection.getPlayerInfoMap()
                            .stream()
                            .sorted((u1,u2) -> String.CASE_INSENSITIVE_ORDER.compare(u1.getGameProfile().getName(),u2.getGameProfile().getName()))
                .forEach(
                        playerEntry -> {
                            GameProfile profile = playerEntry.getGameProfile();
                            ITextComponent displayname = playerEntry.getDisplayName();
                            this.begin();
                            this.add("PLAYERNAME", profile.getName());
                            this.add("PLAYERUUID", profile.getId().toString());
                            this.add("PLAYERDISPLAYNAME", (displayname != null) ? displayname.getFormattedText() : profile.getName());
                            ScorePlayerTeam team = playerEntry.getPlayerTeam();
                            this.add("PLAYERTEAM", team == null ? "{}" : jsonify(team));
                            this.add("PLAYERPING", playerEntry.getResponseTime());
                            this.add("PLAYERISLEGACY", profile.isLegacy());
                            this.end();
                        }
                );
        
    }
    
    private String jsonify(ScorePlayerTeam team) {
        if(team == null) {
            return "{}";
        }
        return String.format("{\"allowFriendFire\":%b,\"collisionRule\":\"%s\",\"color\":\"%s\",\"deathMessageVisibility\":\"%s\",\"displayName\":\"%s\",\"name\":\"%s\",\"nameTagVisibility\":\"%s\",\"seeFriendlyInvisibles\":%b,\"prefix\":\"%s\",\"suffix\":\"%s\"}",
                             team.getAllowFriendlyFire(),
                             team.getCollisionRule().name,
                             team.getColor() == null ? "reset" : team.getColor().getFriendlyName(),
                             team.getDeathMessageVisibility().internalName,
                             team.getDisplayName(),
                             team.getName(),
                             team.getNameTagVisibility().internalName,
                             team.getSeeFriendlyInvisiblesEnabled(),
                             team.getPrefix(),
                             team.getSuffix());
    }
}

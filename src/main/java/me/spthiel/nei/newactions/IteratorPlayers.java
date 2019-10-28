package me.spthiel.nei.newactions;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.Iterator;

public class IteratorPlayers extends ScriptedIterator {
    public IteratorPlayers(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);
        EntityPlayerSP thePlayer = this.mc.player;
        
        for(NetworkPlayerInfo playerEntry : thePlayer.connection.getPlayerInfoMap()) {
            this.begin();
            this.add("PLAYERNAME", playerEntry.getGameProfile().getName());
            this.add("PLAYERUUID", playerEntry.getGameProfile().getId().toString());
            this.end();
        }
        
    }
}

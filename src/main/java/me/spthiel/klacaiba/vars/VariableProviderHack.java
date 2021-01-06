package me.spthiel.klacaiba.vars;

import com.mumfrey.liteloader.core.LiteLoader;
import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.actions.ScriptActionHack;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

import java.io.File;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderHack extends VariableCache {
    
    private World lastWorld = null;
    
    @Override
    public void updateVariables(boolean clock) {
        
        if (!clock) {
            return;
        }
    
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        NetworkPlayerInfo playerConnection = player.connection.getPlayerInfo(player.getUniqueID());
        
        this.storeVariable("LATENCY", playerConnection != null ? playerConnection.getResponseTime() : 0);
        this.storeVariable("HACKED", ScriptActionHack.hacked);
        this.storeVariable("MODULENEI", true);
        this.storeVariable("MODULEKLACAIBA", true);
        this.storeVariable("MINECRAFTDIR", LiteLoader.getGameDirectory().getAbsolutePath() + File.separator);
        this.storeVariable("MACROSCONFIGDIR", (Macros.getInstance().getMacrosDirectory().getAbsolutePath()));
        this.storeVariable("FILESEPARATOR", File.separator);
        
        World theWorld = Minecraft.getMinecraft().world;
        if(!(lastWorld == null && theWorld == null) && ((lastWorld == null || theWorld == null) || !lastWorld.getWorldInfo().getWorldName().equalsIgnoreCase(theWorld.getWorldInfo().getWorldName()))) {
        
            ScriptActionHack.hack();
            lastWorld = theWorld;
        }
        
    }

    @Override
    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }

    @Override
    public void onInit() {

        ScriptContext.MAIN.getCore().registerVariableProvider(this);
    }

}

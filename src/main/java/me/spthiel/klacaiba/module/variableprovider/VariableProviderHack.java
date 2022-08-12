package me.spthiel.klacaiba.module.variableprovider;

import com.mumfrey.liteloader.core.LiteLoader;
import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.base.ScriptActionHack;
import me.spthiel.klacaiba.config.gui.GuiKlacaibaConfig;
import me.spthiel.klacaiba.module.events.IWorldChangeListener;

import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.world.World;

import java.io.File;
import java.util.LinkedList;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderHack extends VariableCache {
    
    private              World                            lastWorld            = null;
    private static final LinkedList<IWorldChangeListener> worldChangeListeners = new LinkedList<>();
    
    public static void registerWorldChangeListener(IWorldChangeListener listener) {
        worldChangeListeners.add(listener);
    }
    
    public static void removeWorldChangeListener(IWorldChangeListener listener) {
        worldChangeListeners.remove(listener);
    }
    
    @Override
    public void updateVariables(boolean clock) {
        
        if (!clock) {
            return;
        }
    
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        NetworkPlayerInfo playerConnection = player.connection.getPlayerInfo(player.getUniqueID());
    
        //noinspection ConstantConditions
        this.storeVariable("LATENCY", playerConnection != null ? playerConnection.getResponseTime() : 0);
        this.storeVariable("HACKED", ScriptActionHack.hacked);
        this.storeVariable("MODULENEI", true);
        this.storeVariable("MODULEKLACAIBA", true);
        this.storeVariable("KLACAIBAVERSION", ModuleInfo.VERSION);
        this.storeVariable("MINECRAFTDIR", LiteLoader.getGameDirectory().getAbsolutePath() + File.separator);
        this.storeVariable("MACROSCONFIGDIR", (Macros.getInstance().getMacrosDirectory().getAbsolutePath()));
        this.storeVariable("FILESEPARATOR", File.separator);
        
        World theWorld = Minecraft.getMinecraft().world;
        if(!(lastWorld == null && theWorld == null) && ((lastWorld == null || theWorld == null) || !lastWorld.getWorldInfo().getWorldName().equalsIgnoreCase(theWorld.getWorldInfo().getWorldName()))) {
        
            worldChangeListeners.forEach(listener -> {
                listener.onWorldChange(lastWorld, theWorld);
            });
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
        Macros.getInstance().getCustomScreenManager().registerCustomScreen("Klacaiba Settings", GuiKlacaibaConfig.class);
    }

}

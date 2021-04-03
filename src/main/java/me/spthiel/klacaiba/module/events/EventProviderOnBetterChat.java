package me.spthiel.klacaiba.module.events;

import com.mumfrey.liteloader.ChatListener;
import com.mumfrey.liteloader.client.PacketEventsClient;
import com.mumfrey.liteloader.core.PacketEvents;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import me.spthiel.klacaiba.ModuleInfo;

@APIVersion(ModuleInfo.API_VERSION)
public class EventProviderOnBetterChat implements IMacroEventProvider, IMacroEventVariableProvider, ChatListener {
    
    private IMacroEvent         onBetterChat;
    private static IMacroEventManager manager = null;
    private Map<String, Object> vars = new HashMap<>();
    
    private IMacroEventDefinition definitionFromString(final String name) {
        return new IMacroEventDefinition() {
            @Override
            public String getName() {
                
                return name;
            }
            
            @Override
            public String getPermissionGroup() {
                
                return null;
            }
        };
    }
    
    public EventProviderOnBetterChat() {
    
    }
    
    public EventProviderOnBetterChat(IMacroEvent e) {
    
    }
    
    @Override
    public IMacroEventDispatcher getDispatcher() {
        
        return null;
    }
    
    @Override
    public void registerEvents(IMacroEventManager manager) {
        EventProviderOnBetterChat.manager = manager;
        this.onBetterChat = manager.registerEvent(this, definitionFromString("onBetterChat"));
        this.onBetterChat.setVariableProviderClass(getClass());
    }
    
    static {
        List<String> helpList = new ArrayList<>();
        helpList.add("This event is raised whenever a new chat message arrives from");
        helpList.add("the server. You can access the message that was received using");
        helpList.add("the CHAT variable within the event. CHATJSON gives the json");
        helpList.add("of the chatmessage and CHATCLEAN returns the stripped chat");
        help = Collections.unmodifiableList(helpList);
    }
    
    private static final List<String> help;
    
    @Override
    public List<String> getHelp(IMacroEvent macroEvent) {
        
        return help;
    }
    
    @Override
    public void initInstance(String[] instanceVariables) {
    
        vars.put("CHAT", instanceVariables[0]);
        vars.put("CHATCLEAN", StringUtils.stripControlCodes(instanceVariables[0]));
        vars.put("CHATJSON", instanceVariables[1]);
    }
    
    @Override
    public void updateVariables(boolean clock) {
    
    }
    
    @Override
    public Object getVariable(String variableName) {
        return this.vars.get(variableName);
    }
    
    @Override
    public Set<String> getVariables() {
        return this.vars.keySet();
    }
    
    @Override
    public void onInit() {
        
        try {
            Field packetEventsField = PacketEvents.class.getDeclaredField("instance");
            packetEventsField.setAccessible(true);
            PacketEventsClient packetEventsClient = (PacketEventsClient) packetEventsField.get(null);
            packetEventsClient.registerChatListener(this);
    
            ScriptCore core = ScriptContext.MAIN.getCore();
            core.registerEventProvider(this);
        } catch(NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onChat(ITextComponent chat, String message) {
    
        String json = ITextComponent.Serializer.componentToJson(chat);
        
        manager.sendEvent(onBetterChat, message, json);
    }
    
    @Override public String getVersion() {return null;}
    @Override public void init(File configPath) {}
    @Override public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
    @Override public String getName() {return null;}
}

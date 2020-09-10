package me.spthiel.nei.events;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

import me.spthiel.nei.ModuleInfo;

@APIVersion(ModuleInfo.API_VERSION)
public class EventProviderOnSound implements IMacroEventProvider, IMacroEventVariableProvider, ISoundEventListener {
    
    private IMacroEvent               onBetterSound;
    private static IMacroEventManager manager = null;
    private Map<String, Object>       vars = new HashMap<>();
    private static HashMap<UUID, ISound> sounds = new HashMap<>();
    
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
    
    public EventProviderOnSound() {
    
    }
    
    public EventProviderOnSound(IMacroEvent e) {
    
    }
    
    @Override
    public IMacroEventDispatcher getDispatcher() {
        
        return null;
    }
    
    @Override
    public void registerEvents(IMacroEventManager manager) {
        EventProviderOnSound.manager = manager;
        this.onBetterSound = manager.registerEvent(this, definitionFromString("onSound"));
        this.onBetterSound.setVariableProviderClass(getClass());
    }
    
    static {
        List<String> helpList = new ArrayList<>();
        helpList.add("This event is raised whenever a sound is played");
        help = Collections.unmodifiableList(helpList);
    }
    
    private static final List<String> help;
    
    @Override
    public List<String> getHelp(IMacroEvent macroEvent) {
        
        return help;
    }
    
    @Override
    public void initInstance(String[] instanceVariables) {
    
        UUID id = UUID.fromString(instanceVariables[0]);
        
        ISound sound = sounds.get(id);
        sounds.remove(id);
        
        vars.put("SOUNDXPOSF", sound.getXPosF());
        vars.put("SOUNDYPOSF", sound.getYPosF());
        vars.put("SOUNDZPOSF", sound.getZPosF());
        vars.put("SOUNDXPOS", (int)sound.getXPosF());
        vars.put("SOUNDYPOS", (int)sound.getYPosF());
        vars.put("SOUNDZPOS", (int)sound.getZPosF());
        vars.put("SOUNDCANREPEAT", sound.canRepeat());
        vars.put("SOUNDATTENUATIONTYPE", sound.getAttenuationType().name());
        vars.put("SOUNDCATEGORY", sound.getCategory().name());
        vars.put("SOUNDPITCH", sound.getPitch());
        vars.put("SOUNDVOLUME", sound.getVolume());
        vars.put("SOUNDRESOURCE", sound.getSound().getSoundLocation().toString());
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
        
        ScriptCore core = ScriptContext.MAIN.getCore();
        core.registerEventProvider(this);
    
        Minecraft.getMinecraft().getSoundHandler().addListener(this);
    }
    
    @Override
    public void soundPlay(ISound soundIn, SoundEventAccessor accessor) {
        UUID id = UUID.randomUUID();
        sounds.put(id, soundIn);
        manager.sendEvent(onBetterSound, id.toString());
    }
}

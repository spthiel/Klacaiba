package me.spthiel.klacaiba.module.events;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.SoundEventAccessor;

import java.util.*;

import me.spthiel.klacaiba.ModuleInfo;
import me.spthiel.klacaiba.base.BaseCustomEvent;

@APIVersion(ModuleInfo.API_VERSION)
public class EventOnSound extends BaseCustomEvent<ISound> implements ISoundEventListener {
    
    public EventOnSound() {
        
        super("sound", "sound");
    }
    
    public EventOnSound(String[] params) {
        this();
    }
    
    @Override
    protected void registerVariablesFor(ISound sound) {
    
        registerVariable("XPOSF", sound.getXPosF());
        registerVariable("YPOSF", sound.getYPosF());
        registerVariable("ZPOSF", sound.getZPosF());
        registerVariable("XPOS", (int)sound.getXPosF());
        registerVariable("YPOS", (int)sound.getYPosF());
        registerVariable("ZPOS", (int)sound.getZPosF());
        registerVariable("CANREPEAT", sound.canRepeat());
        registerVariable("ATTENUATIONTYPE", sound.getAttenuationType().name());
        registerVariable("CATEGORY", sound.getCategory().name());
        registerVariable("PITCH", sound.getPitch());
        registerVariable("VOLUME", sound.getVolume());
        registerVariable("RESOURCE", sound.getSound().getSoundLocation().toString());
    }
    
    @Override
    protected void init() {
    
        Minecraft.getMinecraft().getSoundHandler().addListener(this);
    }
    
    @Override
    public void soundPlay(ISound soundIn, SoundEventAccessor accessor) {
        populate(soundIn);
    }
}

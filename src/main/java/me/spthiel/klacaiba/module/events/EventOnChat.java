package me.spthiel.klacaiba.module.events;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;

import me.spthiel.klacaiba.module.actions.base.BaseCustomEvent;
import me.spthiel.klacaiba.module.events.base.PacketListeningEvent;
import me.spthiel.klacaiba.utils.PacketListener;

public class EventOnChat extends BaseCustomEvent<ITextComponent> implements PacketListeningEvent {
	
	public EventOnChat() {
		
		super("chat", "chat");
	}
	
	@Override
	protected void registerVariablesFor(ITextComponent packet) {
	
		this.registerVariable("", packet.getFormattedText());
		this.registerVariable("CLEAN", StringUtils.stripControlCodes(packet.getFormattedText()));
		this.registerVariable("JSON", ITextComponent.Serializer.componentToJson(packet));
	
	}
	
	@Override
	protected void init() {
		PacketListener.getInstance().registerListener(this);
	}
	
	@Override
	public void terminate() {
		PacketListener.getInstance().unregisterListener(this);
	}
	
	@Override
	public Class<? extends Packet<?>>[] getListenedPackets() {
		
		return new Class[]{SPacketChat.class};
	}
	
	@Override
	public void onPacket(Packet<?> packet) {
		
		if (packet instanceof SPacketChat) {
			onPacket((SPacketChat)packet);
		}
	}
	
	public void onPacket(SPacketChat packet) {
		
		this.populate(packet.getChatComponent());
	}
}

package me.spthiel.klacaiba.module.events;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.text.ITextComponent;

import me.spthiel.klacaiba.module.actions.base.BaseCustomEvent;
import me.spthiel.klacaiba.module.events.base.PacketListeningEvent;
import me.spthiel.klacaiba.utils.PacketListener;

public class EventOnBossBar extends BaseCustomEvent<SPacketUpdateBossInfo> implements PacketListeningEvent {
	
	public EventOnBossBar() {
		
		super("bossbar", "boss");
	}
	
	@Override
	protected void registerVariablesFor(SPacketUpdateBossInfo packet) {
	
		this.registerVariable("UUID", packet.getUniqueId().toString());
		this.registerVariable("PACKETOPERATION", packet.getOperation().ordinal());
		this.registerVariable("PACKETOPERATIONNAME", packet.getOperation().name());
		
		switch (packet.getOperation()) {
			case UPDATE_PCT:
				this.registerVariable("PERCENT", packet.getPercent() * 100);
				break;
			case UPDATE_NAME:
				this.registerVariable("NAME", ITextComponent.Serializer.componentToJson(packet.getName()));
				this.registerVariable("NAMEJSON", packet.getName().getFormattedText());
				break;
			case UPDATE_PROPERTIES:
				this.registerVariable("SHOULDDARKENSKY", packet.shouldDarkenSky());
				this.registerVariable("SHOULDPLAYMUSIC", packet.shouldPlayEndBossMusic());
				break;
			case UPDATE_STYLE:
				this.registerVariable("COLOR", packet.getColor());
				this.registerVariable("OVERLAY", packet.getOverlay());
				break;
			case ADD:
				this.registerVariable("PERCENT", packet.getPercent() * 100);
				this.registerVariable("NAME", ITextComponent.Serializer.componentToJson(packet.getName()));
				this.registerVariable("NAMEJSON", packet.getName().getFormattedText());
				this.registerVariable("SHOULDDARKENSKY", packet.shouldDarkenSky());
				this.registerVariable("SHOULDPLAYMUSIC", packet.shouldPlayEndBossMusic());
				this.registerVariable("COLOR", packet.getColor());
				this.registerVariable("OVERLAY", packet.getOverlay());
		}
	
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
		
		return new Class[]{SPacketUpdateBossInfo.class};
	}
	
	@Override
	public void onPacket(Packet<?> packet) {
		
		if (packet instanceof SPacketUpdateBossInfo) {
			onPacket((SPacketUpdateBossInfo)packet);
		}
	}
	
	public void onPacket(SPacketUpdateBossInfo packet) {
		
		this.populate(packet);
	}
}

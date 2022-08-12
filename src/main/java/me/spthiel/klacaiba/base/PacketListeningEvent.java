package me.spthiel.klacaiba.base;

import net.minecraft.network.Packet;

import java.util.List;

public interface PacketListeningEvent {
	
	public Class<? extends Packet<?>>[] getListenedPackets();
	public void onPacket(Packet<?> packet);
	
}

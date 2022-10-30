package me.spthiel.klacaiba.module.events;

import net.eq2online.macros.core.Macro;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketPlayerListItem;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import me.spthiel.klacaiba.base.BaseCustomEvent;
import me.spthiel.klacaiba.base.PacketListeningEvent;
import me.spthiel.klacaiba.utils.FilterRecentDuplicates;
import me.spthiel.klacaiba.utils.PacketListener;

public class EventOnPlayerJoined extends BaseCustomEvent<SPacketPlayerListItem.AddPlayerData> implements PacketListeningEvent {
	
	private final FilterRecentDuplicates<UUID> recentDuplicates;
	
	public EventOnPlayerJoined() {
		
		super("playerjoin", "joinedplayer");
		this.recentDuplicates = new FilterRecentDuplicates<>(1000);
	}
	
	@Override
	protected void registerVariablesFor(SPacketPlayerListItem.AddPlayerData playerInfo) {
	
		this.registerVariable("", playerInfo.getProfile().getName());
		if (playerInfo.getDisplayName() != null) {
			this.registerVariable("DISPLAYNAME", playerInfo.getDisplayName().getFormattedText());
		}
		this.registerVariable("UUID", playerInfo.getProfile().getId());
		this.registerVariable("PING", playerInfo.getPing());
		this.registerVariable("GAMETYPE", playerInfo.getGameMode());
	
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
		
		return new Class[]{SPacketPlayerListItem.class};
	}
	
	@Override
	public void onPacket(Packet<?> packet) {
		
		if (packet instanceof SPacketPlayerListItem) {
			onPacket((SPacketPlayerListItem)packet);
		}
	}
	
	public void onPacket(SPacketPlayerListItem packet) {
		if (packet.getAction().equals(SPacketPlayerListItem.Action.ADD_PLAYER)) {
			packet.getEntries().forEach(object -> {
				
				if (this.recentDuplicates.add(object.getProfile().getId())) {
					populate(object);
				}
			});
		}
	}
}

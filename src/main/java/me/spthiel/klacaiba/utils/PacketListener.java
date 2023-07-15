package me.spthiel.klacaiba.utils;

import com.mumfrey.liteloader.ChatListener;
import com.mumfrey.liteloader.PacketHandler;
import com.mumfrey.liteloader.client.PacketEventsClient;
import com.mumfrey.liteloader.core.PacketEvents;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import me.spthiel.klacaiba.module.events.base.PacketListeningEvent;

public class PacketListener implements PacketHandler, ChatListener {
	
	private static PacketListener instance = new PacketListener();
	private List<Class<? extends Packet<?>>> handledPackets = new ArrayList<>();
	private List<PacketListeningEvent> listeners = new LinkedList<>();
	private boolean locked = false;
	
	public static PacketListener getInstance() {
		
		return instance;
	}
	
	public void start() {
		try {
			Field packetEventsField = PacketEvents.class.getDeclaredField("instance");
			packetEventsField.setAccessible(true);
			PacketEventsClient packetEventsClient = (PacketEventsClient) packetEventsField.get(null);
			packetEventsClient.registerPacketHandler(this);
			packetEventsClient.registerChatListener(this);
			this.locked = true;
		} catch(NoSuchFieldException | IllegalAccessException | ClassCastException e) {
			e.printStackTrace();
		}
	}
	
	public void registerOwnEvents(Collection<?> classes) {
		if (locked) {
			throw new RuntimeException("Registering of custom events was too late: " + classes);
		}
		
		handledPackets.addAll(classes.stream()
			.filter(object -> object instanceof PacketListeningEvent)
			.map(object -> (PacketListeningEvent)object)
			.flatMap(event -> Arrays.stream(event.getListenedPackets()))
			.filter(packet -> !this.handledPackets.contains(packet))
			.distinct()
		    .collect(Collectors.toList()));
	}
	
	public void registerListener(PacketListeningEvent event) {
		listeners.add(event);
	}
	
	public void unregisterListener(PacketListeningEvent event) {
		listeners.remove(event);
	}
	
	@Override
	public List<Class<? extends Packet<?>>> getHandledPackets() {
		this.handledPackets.remove(SPacketChat.class);
		return this.handledPackets;
	}
	
	@Override
	public boolean handlePacket(INetHandler netHandler, Packet<?> packet) {
		listeners.stream()
				 .filter(listener -> Arrays.stream(listener.getListenedPackets()).anyMatch(clazz -> clazz.isInstance(packet)))
				 .forEach(listener -> listener.onPacket(packet));
		return true;
	}
	
	@Override
	public void onChat(ITextComponent chat, String message) {
		
		SPacketChat packet = new SPacketChat(chat);
		listeners.stream()
				 .filter(listener -> Arrays.stream(listener.getListenedPackets()).anyMatch(clazz -> clazz.isInstance(packet)))
				 .forEach(listener -> listener.onPacket(packet));
		
	}
	
	@Override
	public String getVersion() {return null;}
	@Override
	public void init(File configPath) {}
	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
	@Override
	public String getName() {return null;}
}

package com.jredfox.menulib.eventhandler;

import java.util.ArrayList;
import java.util.List;

import com.evilnotch.lib.minecraft.capability.registry.CapabilityRegistry;
import com.evilnotch.lib.minecraft.tick.ITick;
import com.evilnotch.lib.minecraft.tick.TickRegistry;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.cap.CapReg;
import com.jredfox.menulib.cap.CapabilityMusic;
import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.event.MusicEvent.MusicState;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.sound.IMusicPlayer;
import com.jredfox.menulib.sound.IMusicPlayerHolder;
import com.jredfox.menulib.sound.MusicEmpty;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class MusicPlayerHandler implements ITick{

	public Minecraft mc = Minecraft.getMinecraft();
	public ISound currentMusic;
	public MusicState previous = MusicState.NONE;
	
	@Override
	public void tick()
	{
		if(TickRegistry.isRightTickClient(20 * 10) && TickRegistry.tickCountClient != 0)
		{
			MenuRegistry.INSTANCE.registry.get(0).setEnabled(true);
		}
		MusicState state = this.getState();
		this.updateState(state);
		if(state == MusicState.GAME)
		{
			IMusicPlayerHolder player = (IMusicPlayerHolder) CapabilityRegistry.getCapability(this.mc.world, CapReg.musicPlayerWorld);
			player.getMusicPlayer().tick();
		}
		else if(state == MusicState.MENU)
		{
			MenuRegistry.INSTANCE.getMenu().getMusicPlayer().tick();
		}
		else if(state == MusicState.GUI)
		{
			((IMusicPlayerHolder)this.mc.currentScreen).getMusicPlayer().tick();
		}
	}
	
	public void updateState(MusicState state) 
	{
		if(this.previous != state)
		{
			this.stop();//stop if the state of the music has changed
		}
		this.previous = state;
	}

	public void play(IMusicPlayer player, ISound sound)
	{
		if(MusicEvent.canPlay(player, sound))
		{
			this.stop();
			this.currentMusic = sound;
			this.mc.getSoundHandler().playSound(sound);
		}
	}
	
	public void stop()
	{
		if(this.currentMusic != null)
		{
			this.mc.getSoundHandler().stopSound(this.currentMusic);
			this.currentMusic = null;
		}
	}

	/**
	 * grab the current MusicState
	 */
	public MusicState getState() 
	{
		GuiScreen gui = this.mc.currentScreen;
		return this.mc.world != null ? (gui instanceof IMusicPlayerHolder ? MusicState.GUI : MusicState.GAME) : (gui instanceof IMusicPlayerHolder && gui != MenuRegistry.INSTANCE.getGui() ? MusicState.GUI : MusicState.MENU);
	}

	@Override
	public void garbageCollect() 
	{
		
	}

	@Override
	public Phase getPhase() 
	{
		return Phase.START;
	}

}

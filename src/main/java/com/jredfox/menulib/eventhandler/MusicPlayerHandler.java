package com.jredfox.menulib.eventhandler;

import java.util.ArrayList;
import java.util.List;

import com.evilnotch.lib.minecraft.tick.ITick;
import com.jredfox.menulib.event.MusicEvent;
import com.jredfox.menulib.event.MusicEvent.MusicState;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.sound.IMusicPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class MusicPlayerHandler implements ITick{

	public Minecraft mc = Minecraft.getMinecraft();
	public List<IMusicPlayer> gameMusic = new ArrayList();
	public ISound currentMusic;
	
	@Override
	public void tick()
	{
		MusicState state = this.getState();
		if(state == MusicState.GAME)
		{
			for(IMusicPlayer p : gameMusic)
				p.tick();
		}
		else if(state == MusicState.MENU)
		{
			MenuRegistry.getCurrentMenu().getMusicPlayer().tick();
		}
		
		GuiScreen gui = this.mc.currentScreen;
		if(gui instanceof IMusicPlayer && gui != MenuRegistry.getCurrentGui())
		{
			((IMusicPlayer)gui).tick();
		}
	}
	
	public void play(IMusicPlayer player, ISound sound)
	{
		if(!player.replaceMusic() && this.currentMusic != null)
		{
			return;
		}
		if(MusicEvent.fire(player, sound))
		{
			if(this.currentMusic != null)
				this.mc.getSoundHandler().stopSound(this.currentMusic);
			this.currentMusic = sound;
			this.mc.getSoundHandler().playSound(sound);
		}
	}

	/**
	 * grab the current MusicState
	 */
	public MusicState getState() 
	{
		return this.mc.world != null ? MusicState.GAME : MusicState.MENU;
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

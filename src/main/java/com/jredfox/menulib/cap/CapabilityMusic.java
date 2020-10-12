package com.jredfox.menulib.cap;

import com.evilnotch.lib.minecraft.capability.CapContainer;
import com.evilnotch.lib.minecraft.capability.CapWorld;
import com.evilnotch.lib.minecraft.capability.ICapability;
import com.jredfox.menulib.sound.IMusicPlayer;
import com.jredfox.menulib.sound.IMusicPlayerHolder;
import com.jredfox.menulib.sound.MusicPlayerEmpty;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CapabilityMusic extends CapWorld implements IMusicPlayerHolder{

	public IMusicPlayer music;
	
	public CapabilityMusic(World w)
	{
		int dimId = w.provider.getDimension();
		this.music = MusicPlayerEmpty.musicPlayer;
	}
	
	@Override
	public void readFromNBT(World world, NBTTagCompound nbt, CapContainer container)
	{
		
	}

	@Override
	public void writeToNBT(World world, NBTTagCompound nbt, CapContainer container) 
	{
		
	}

	@Override
	public IMusicPlayer getMusicPlayer() 
	{
		return this.music;
	}

}

package com.jredfox.menulib.sound.category;

import java.util.List;

import com.jredfox.menulib.sound.IMusicPlayer;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICategoryWorld extends ICategory {
	
	/**
	 * return whether or not the category canTick when MusicState is MusicState.GAME
	*/
	public boolean canTick(EntityPlayerSP player, World w, BlockPos pos);
	public List<IMusicPlayer> getPlayList(EntityPlayerSP player, World w, BlockPos pos);

}

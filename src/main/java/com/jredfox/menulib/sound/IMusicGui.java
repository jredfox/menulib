package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;
import com.jredfox.menulib.misc.GameState;

public interface IMusicGui extends IMusicGuiHolder{
	
	@Override
	public default GameState getGameState()
	{
		return GameState.GUI;
	}

}

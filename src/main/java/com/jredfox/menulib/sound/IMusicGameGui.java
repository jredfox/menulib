package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;

public interface IMusicGameGui extends IMusicGui{
	
	@Override
	public default MusicState getMusicState()
	{
		return MusicState.GAMEGUI;
	}

}

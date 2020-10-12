package com.jredfox.menulib.sound;

import com.jredfox.menulib.event.MusicEvent.MusicState;

public interface IMusicGui extends IMusicGuiHolder{
	
	@Override
	public default MusicState getMusicState()
	{
		return MusicState.GUI;
	}

}

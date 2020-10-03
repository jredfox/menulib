package com.jredfox.menulib.coremod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class MLFrameRate {
	
	public static int getFrames()
	{
		return Minecraft.getMinecraft().world == null && Minecraft.getMinecraft().currentScreen != null && MLCoreConfig.lockFrames ? MLCoreConfig.vMenuFrames : Minecraft.getMinecraft().gameSettings.limitFramerate;
	}
}

package com.jredfox.menulib.proxy;

import net.minecraftforge.fml.common.Loader;

/**
 * WIP right now all it does is cache Loader.isModLoaded(modid)
 */
public class ModProxy {
	
	public boolean isLoaded;
	public String modid;
	
	public ModProxy(String id)
	{
		this.modid = id;
		this.isLoaded = Loader.isModLoaded(id);
	}
	
	public void run(Object... params)
	{
		//TODO:
	}

}

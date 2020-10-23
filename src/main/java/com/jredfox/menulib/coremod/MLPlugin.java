package com.jredfox.menulib.coremod;

import java.util.Map;

import com.evilnotch.lib.asm.ConfigCore;
import com.evilnotch.lib.asm.FMLCorePlugin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@IFMLLoadingPlugin.Name("menulib-transformer_fixes")
@IFMLLoadingPlugin.SortingIndex(1003)
@IFMLLoadingPlugin.MCVersion("1.12.2")
@TransformerExclusions("com.jredfox.menulib.coremod.")
public class MLPlugin implements IFMLLoadingPlugin
{
	static
	{
		MLConfigCore.loadConfig();
	}
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]
		{
				"com.jredfox.menulib.coremod.MLTransformer",
				"com.jredfox.menulib.compat.coremod.MLTransformerCompat"
		};
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass() 
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) 
	{
		
	}

	@Override
	public String getAccessTransformerClass() 
	{
		return null;
	}
	
}
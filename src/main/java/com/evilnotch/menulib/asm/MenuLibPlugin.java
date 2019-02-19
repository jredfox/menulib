package com.evilnotch.menulib.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@IFMLLoadingPlugin.Name("menulib-transformer_fixes")
@IFMLLoadingPlugin.SortingIndex(1002)
@IFMLLoadingPlugin.MCVersion("1.12.2")
@TransformerExclusions("com.evilnotch.menulib.asm.")
public class MenuLibPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{"com.evilnotch.menulib.asm.MenuLibTransformer"};
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
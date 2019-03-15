package com.evilnotch.menulib.compat;

import java.io.File;
import java.lang.reflect.Method;

import org.ralleytn.simple.json.JSONObject;

import com.evilnotch.lib.api.ReflectionUtil;
import com.evilnotch.lib.util.JavaUtil;
import com.evilnotch.menulib.ConfigMenu;
import com.evilnotch.menulib.compat.menu.MenuCMM;
import com.evilnotch.menulib.menu.MenuRegistry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class ProxyMod {
	
	public static boolean cmm;
	public static boolean thebetweenlands;
	
	//cmm
	public static boolean flagCMMJson;
	public static File cmmJson = null;
	
	//thebetweenlands
	public static Class tbl_musicHandler = null;
	public static Object tbl_instance = null;
	
	public static void preInit() 
	{
		ProxyMod.isModsLoaded();
		ProxyMod.register();
	}
	
	public static void isModsLoaded()
	{
		cmm = Loader.isModLoaded("custommainmenu");
		thebetweenlands = Loader.isModLoaded("thebetweenlands");
		cacheData();
	}

	public static void register()
	{	
		if(cmm)
		{
			MenuRegistry.registerIMenu(new MenuCMM());
			
			if(flagCMMJson)
			{
				JSONObject json = JavaUtil.getJson(cmmJson);
				JSONObject buttonElements = json.getJSONObject("buttons");
				JSONObject left = new JSONObject();
		    	
				left.put("text", ConfigMenu.fancyPage ? "previous" : "<");
		    	left.put("posX", 5);
		    	left.put("posY", 5);
		    	left.put("width", 20);
		    	left.put("height", 20);
		    	left.put("alignment", "top_left");
		    	left.put("wrappedButton", ConfigMenu.leftButtonId);
		    	buttonElements.put("menulibLeft", left);
		    	
		    	JSONObject right = new JSONObject();
		    	right.put("text", ConfigMenu.fancyPage ? "next" : ">");
		    	right.put("posX", 30);
		    	right.put("posY", 5);
		    	right.put("width", 20);
		    	right.put("height", 20);
		    	right.put("alignment", "top_left");
		    	right.put("wrappedButton", ConfigMenu.rightButtonId);
		    	buttonElements.put("menulibRight", right);
		    	
				JavaUtil.saveJSON(json, cmmJson, false);
				refreshCMM();
				
				System.out.println("Done Hooking CMM Auto JSON Support for first time use");
			}
		}
		
		if(thebetweenlands)
		{
			MenuRegistry.registerGuiMenu(ReflectionUtil.classForName("thebetweenlands.client.gui.menu.GuiBLMainMenu"), new ResourceLocation("thebetweenlands:mainmenu"));
		}
	}
	
	/**
	 * yes I am fixing mods music
	 */
	public static void menuChange()
	{
		if(thebetweenlands)
		{
			ReflectionUtil.setObject(tbl_instance, false, tbl_musicHandler, "hasBlMainMenu");//sets it to false to garentee it will not play till the next cik
		}
	}
	
	private static void cacheData() 
	{
		if(thebetweenlands)
		{
			tbl_musicHandler =  ReflectionUtil.classForName("thebetweenlands.client.handler.MusicHandler");
			tbl_instance = ReflectionUtil.getObject(null, tbl_musicHandler, "INSTANCE");
		}
	}

	public static void flagCMMJson(File rootdir) 
	{
		cmmJson = new File(rootdir, "config/CustomMainMenu/mainmenu.json");
		flagCMMJson = !cmmJson.exists();
	}
	
	public static void refreshCMM()
	{
		try 
		{
			Method refresh = MenuCMM.cmm.getDeclaredMethod("reload");
			refresh.setAccessible(true);
			refresh.invoke(MenuCMM.modInstance);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}

package com.jredfox.menulib.menu;

import java.util.Set;

import com.jredfox.menulib.sound.IMusicPlayer;
import com.jredfox.menulib.sound.IMusicPlayerHolder;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMenu extends IMusicPlayerHolder{
	/**
	 * this is the resource location to identify your main menu
	 */
	public ResourceLocation getId();
	/**
	 * get the gui class
	 */
	public Class<? extends GuiScreen> getGuiClass();
	/**
	 * grabs current gui populated from createGui()
	 */
	public GuiScreen get();
	/**
	 * this creates a new gui and sets the gui field
	 */
	public GuiScreen create();
	/**
	 * called when the menu opens
	 */
	public void open();
	/**
	 * called when the menu closes. be aware of if your menu isEnabled() or not when checking for MenuRegistry.index
	 */
	public void close();
	/**
	 * called when the menu switches off to another menu use MenuRegistry.menu to get the new menu. be aware of if your menu isEnabled() or not when checking for MenuRegistry.index
	 */
	public void switchMenu();
	/**
	 * clear your gui field
	 */
	public void clear();
	/**
	 * is your menu enabled
	 */
	public boolean isEnabled();
	/**
	 * set your enabled field and data then call MenuRegistry.update(this) when done
	 */
	public void setEnabled(boolean enabled);
	/**
	 * setting this to above -1 will result in a fixed framerate rather then whatever the game values are set to
	 */
	public int getFrames();
	/**
	 * return MusicPlayerEmpty.musicPlayer for no music
	 */
	public IMusicPlayer getMusicPlayer();
	/**
	 * the button going to to the next menu in reverse order
	 * if you return a new button each call you need to manually call MenuRegistry.syncButton() before returning it
	 */
	public GuiButton getPrevious();
	/**
	 * the button going to the next menu
	 * if you return a new button each call you need to manually call MenuRegistry.syncButton() before returning it
	 */
	public GuiButton getNext();
}

package com.evilnotch.menulib.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMenu {
	/**
	 * called right after menu closes
	 */
	public void onClose();
	/**
	 * called when the menu closes and goes into a sub menu
	 */
	public void onCloseFromSub();
	/**
	 * called right after gui is set to main menu
	 */
	public void onOpen();
	/**
	 * fires when your gui opens up from a sub menu without re-creating the whole gui
	 */
	public void onOpenFromSub();
	/**
	 * this creates a new gui called when a new main menu gets created(opened)
	 */
	public GuiScreen createGui();
	/**
	 * grabs current gui generated from createGui()
	 */
	public GuiScreen getGui();
	/**
	 * get the gui class
	 */
	public Class<? extends GuiScreen> getGuiClass();
	/**
	 * this is the resource loction to identify your main menu
	 */
	public ResourceLocation getId();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getLeftButton();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getRightButton();
	/**
	 * this allows for custom button textures
	 */
	public ResourceLocation getButtonTexture();
	/**
	 * this for Menu(IMenu implementation) sets the button texture field but, for other objects might not do anything
	 */
	public void setButtonTexture(ResourceLocation loc);

}

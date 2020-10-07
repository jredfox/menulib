package com.jredfox.menulib.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface IMenu {
	/**
	 * called right after menu closes
	 */
	public void close();
	/**
	 * called when the menu opens
	 */
	public void open();
	/**
	 * this creates a new gui called when a new main menu gets created(opened)
	 */
	public GuiScreen create();
	/**
	 * grabs current gui generated from createGui()
	 */
	public GuiScreen get();
	/**
	 * get the gui class
	 */
	public Class<? extends GuiScreen> getGuiClass();
	/**
	 * this is the resource location to identify your main menu
	 */
	public ResourceLocation getId();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getLeft();
	/**
	 * if this is null it won't be displayed
	 */
	public GuiButton getRight();

}

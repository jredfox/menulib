package com.jredfox.menulib.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * warning this is a dangerous event if it errors in a loop it will prevent the game from shutting down.
 * obviously this fires when the application is trying to close.
 * to prevent shutdown set Minecraft.getminecraft().running to true
 * canceling the event won't prevent the shutdown it's to stop other events from firing
 */
@Cancelable
public class ShutdownEvent extends Event {

}

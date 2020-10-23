package com.jredfox.menulib.compat.coremod;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.evilnotch.lib.asm.FMLCorePlugin;
import com.evilnotch.lib.asm.classwriter.MCWriter;
import com.evilnotch.lib.asm.util.ASMHelper;
import com.jredfox.menulib.coremod.MLTransformer;

import net.minecraft.launchwrapper.IClassTransformer;

public class MLTransformerCompat implements IClassTransformer{

	
	public static void patchGuiCustom(ClassNode classNode, String inputBase)
	{
		ASMHelper.replaceMethod(classNode, inputBase + "GuiCustom", "initGui", "()V", "func_73866_w_");
		ASMHelper.replaceMethod(classNode, inputBase + "GuiCustom","actionPerformed", "(Lnet/minecraft/client/gui/GuiButton;)V", "func_146284_a");
	}

	@Override
	public byte[] transform(String ob, String name, byte[] bytes) 
	{
		try
		{
		if(name.equals("lumien.custommainmenu.handler.CMMEventHandler"))
		{
			System.out.println("transforming cmm:" + name);
			return ASMHelper.replaceClass(MLTransformer.getInputBase() + "CMMEventHandler");
		}
		else if(name.equals("lumien.custommainmenu.gui.GuiCustom"))
		{
			System.out.println("transforming cmm:" + name);
			ClassNode node = ASMHelper.getClassNode(bytes);
			patchGuiCustom(node, MLTransformer.getInputBase());
			return MLTransformer.writeClass(node, name);
		}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		return bytes;
	}
}

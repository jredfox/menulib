package com.jredfox.menulib.compat.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.evilnotch.lib.asm.util.ASMHelper;
import com.jredfox.menulib.coremod.MLTransformer;

import net.minecraft.launchwrapper.IClassTransformer;
/**
 * made to fix some CMM bugs & add a json hook
 * A: remove it from overriding all other mods
 * B: fix several issues in GuiCustom one with wrapped button ids, the other with firing button events so it shows the current gui instead of a fake
 * C: add a json hook so modders can add their wrapped button for cmm main menu fires everytime the json file is deleted creating a new default
 */
public class MLTransformerCompat implements IClassTransformer{

	@Override
	public byte[] transform(String ob, String name, byte[] bytes) 
	{
		try
		{
		if(name.equals("lumien.custommainmenu.handler.CMMEventHandler"))
		{
			System.out.println("transforming cmm:" + name);
			return ASMHelper.replaceClass(MLTransformer.getInputBase() + "CMMEventHandler");//replaced as I re-wrote most of it
		}
		else if(name.equals("lumien.custommainmenu.gui.GuiCustom"))
		{
			System.out.println("transforming cmm:" + name);
			ClassNode node = ASMHelper.getClassNode(bytes);
			patchGuiCustom(node, MLTransformer.getInputBase());
			return MLTransformer.writeClass(node, name);
		}
		else if(name.equals("lumien.custommainmenu.configuration.ConfigurationLoader"))
		{
			ClassNode node = ASMHelper.getClassNode(bytes);
			hookConfigurationLoader(node);
			return MLTransformer.writeClass(node, name);
		}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		return bytes;
	}
	
	public static void patchGuiCustom(ClassNode classNode, String inputBase)
	{
		ASMHelper.replaceMethod(classNode, inputBase + "GuiCustom", "initGui", "()V", "func_73866_w_");
		ASMHelper.replaceMethod(classNode, inputBase + "GuiCustom","actionPerformed", "(Lnet/minecraft/client/gui/GuiButton;)V", "func_146284_a");
	}

	public static void hookConfigurationLoader(ClassNode classNode)
	{
		MethodNode method = ASMHelper.getMethodNode(classNode, "load", "()V");
		AbstractInsnNode spot = null;
		MethodInsnNode m = new MethodInsnNode(Opcodes.INVOKESTATIC, "org/apache/commons/io/IOUtils", "closeQuietly", "(Ljava/io/InputStream;)V", false);
		for(AbstractInsnNode ab : method.instructions.toArray())
		{
			if(ab instanceof MethodInsnNode && ASMHelper.equals(m, (MethodInsnNode) ab) && ab.getPrevious() instanceof VarInsnNode)
			{
				VarInsnNode var = (VarInsnNode) ab.getPrevious();
				if(var.var == 4)
				{
					MethodInsnNode i = (MethodInsnNode) ab;
					spot = ab;
					break;
				}
			}
		}
		InsnList list = new InsnList();
		list.add(new VarInsnNode(Opcodes.ALOAD, 3));
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/jredfox/menulib/compat/event/CMMJsonRegistry", "fire", "(Ljava/io/File;)V", false));
		method.instructions.insert(spot, list);
	}
}

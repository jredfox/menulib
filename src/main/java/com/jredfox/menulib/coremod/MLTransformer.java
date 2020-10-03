package com.jredfox.menulib.coremod;

import static org.objectweb.asm.Opcodes.ALOAD;

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.evilnotch.lib.api.mcp.MCPSidedString;
import com.evilnotch.lib.asm.ConfigCore;
import com.evilnotch.lib.asm.FMLCorePlugin;
import com.evilnotch.lib.asm.classwriter.MCWriter;
import com.evilnotch.lib.asm.util.ASMHelper;
import com.evilnotch.lib.util.JavaUtil;

import net.minecraft.launchwrapper.IClassTransformer;

public class MLTransformer implements IClassTransformer{
	
    public static final List<String> clazzes = (List<String>)JavaUtil.<String>asArray(new Object[]
    {
    	"net.minecraft.client.Minecraft",
   		"net.minecraft.client.audio.MusicTicker",
    	"lumien.custommainmenu.handler.CMMEventHandler",
    	"lumien.custommainmenu.gui.GuiCustom",
    });
    
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) 
	{
		if(bytes == null)
		{
			return null;
		}
		
		int index = clazzes.indexOf(transformedName);
		
        try 
        {
			return index != -1 ? transform(index, bytes, FMLCorePlugin.isObf) : bytes;
		} 
        catch (Throwable e) 
        {
			e.printStackTrace();
		}
        return bytes;
	}

	public byte[] transform(int index, byte[] clazz, boolean isObf) throws IOException 
	{
		String name =  clazzes.get(index);
		ClassNode classNode = ASMHelper.getClassNode(clazz);
		String inputBase = "assets/menulib/asm/" + (isObf ? "srg/" : "deob/");
		
		System.out.println("MenuLib Transforming:" + name);
		
		switch (index)
		{
			case 0:
				patchFrameRate(classNode, inputBase, name);
			break;
			
			case 1:
				ASMHelper.replaceMethod(classNode, inputBase + "MusicTicker", "update", "()V", "func_73660_a");
				ASMHelper.addMethod(classNode, inputBase + "MusicTicker", "isMenu", "(Lnet/minecraft/client/gui/GuiScreen;)Z");
			break;
			
			case 2:
				return ASMHelper.replaceClass(inputBase + "CMMEventHandler");//99% re-coded edited or removed as his ideas where wrong
			
			case 3:
				MethodNode node = ASMHelper.replaceMethod(classNode, inputBase + "GuiCustom", "initGui", "()V", "func_73866_w_");
				ASMHelper.replaceMethod(classNode, inputBase + "GuiCustom","actionPerformed", "(Lnet/minecraft/client/gui/GuiButton;)V", "func_146284_a");
			break;
		}
		
		ASMHelper.clearCacheNodes();
        
		ClassWriter classWriter = new MCWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        
        byte[] bytes = classWriter.toByteArray();
        if(ConfigCore.dumpASM)
        {
        	ASMHelper.dumpFile(name, bytes);
        }
		return bytes;
	}
	
	/**
	 * patch the framerate of main menus to allow for higher fps then 30 for advanced main menus and simply looks
	 */
	private static void patchFrameRate(ClassNode classNode, String inputBase, String name) throws IOException 
	{
		//add the method before changing the methods exicution
		MethodNode mainmenu = ASMHelper.addMethod(classNode, inputBase + "Minecraft", "getMainMenuFrameRate", "()I");
		
		AbstractInsnNode spot = null;
		FieldInsnNode tocheck = new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", new MCPSidedString("currentScreen","field_71462_r").toString(), "Lnet/minecraft/client/gui/GuiScreen;");
		MethodNode node = ASMHelper.getMethodNode(classNode, new MCPSidedString("getLimitFramerate", "func_90020_K").toString(), "()I");
		for(AbstractInsnNode ab : node.instructions.toArray())
		{
			if(ab.getOpcode() == Opcodes.GETFIELD)
			{
				FieldInsnNode field = (FieldInsnNode)ab;
				if(ASMHelper.equals(field, tocheck) && ab.getNext().getOpcode() == Opcodes.IFNULL)
				{
					spot = ab.getNext().getNext();
					break;
				}
			}
		}
		
		//remove the intial 30 and replace it with a method call
		InsnList toInsert = new InsnList();
        toInsert.add(new VarInsnNode(ALOAD, 0));
        toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getMainMenuFrameRate", "()I", false));
        node.instructions.insertBefore(spot, toInsert);
        node.instructions.remove(spot);
	}

}

package com.jredfox.menulib.coremod;

import static org.objectweb.asm.Opcodes.ALOAD;

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.evilnotch.lib.api.mcp.MCPSidedString;
import com.evilnotch.lib.asm.ConfigCore;
import com.evilnotch.lib.asm.FMLCorePlugin;
import com.evilnotch.lib.asm.classwriter.MCWriter;
import com.evilnotch.lib.asm.util.ASMHelper;
import com.evilnotch.lib.util.JavaUtil;
import com.jredfox.menulib.menu.IMenu;
import com.jredfox.menulib.menu.MenuRegistry;
import com.jredfox.menulib.misc.MLUtil;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.launchwrapper.IClassTransformer;

public class MLTransformer implements IClassTransformer{
	
    public static final List<String> clazzes = (List<String>)JavaUtil.<String>asArray(new Object[]
    {
    	"net.minecraft.client.Minecraft",
   		"net.minecraft.client.audio.MusicTicker"
    });
    
    //since I don't have my asm system I use a slightly better version for forge's switch case is gross but, it's better then if else
	@Override
	public byte[] transform(String ob, String name, byte[] bytes) 
	{
		if(bytes == null)
			return null;
		try
		{
			int index = clazzes.indexOf(name);
			if(index != -1)
			{
				ClassNode node = ASMHelper.getClassNode(bytes);
				switch (index)
				{
					case 0:
						transformFramerate(node, name);
					break;
					
					case 1:
						transformMusic(node);
					break;
				}
				
		        return writeClass(node, name);
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		return bytes;
	}

	/**
	 * patches the framerate to be equal to the game instead of locking it at 30 always
	 * @throws IOException 
	 */
	public void transformFramerate(ClassNode classNode, String name) throws IOException 
	{
		//add getMenuFrames so minecraft can use them later
		String methods = "com/jredfox/menulib/coremod/gen/" + new MCPSidedString("Methods", "MethodsOb").toString();
		MethodNode mainmenu = ASMHelper.addMethod(classNode, methods + ".class", "getMenuFrames", "()I");
		ASMHelper.patchMethod(mainmenu, name, methods);
		
		//start finding the injection point to change the 30 return value to a method call "getMenuFrames"
		MethodNode node = ASMHelper.getMethodNode(classNode, new MCPSidedString("getLimitFramerate", "func_90020_K").toString(), "()I");
		AbstractInsnNode spot = null;
		FieldInsnNode tocheck = new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", new MCPSidedString("currentScreen","field_71462_r").toString(), "Lnet/minecraft/client/gui/GuiScreen;");
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
		
		//remove the initial 30 and replace it with a method call
		InsnList toInsert = new InsnList();
        toInsert.add(new VarInsnNode(ALOAD, 0));
        toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getMenuFrames", "()I", false));
        node.instructions.insertBefore(spot, toInsert);
        node.instructions.remove(spot);
	}
	
	public void transformMusic(ClassNode node)
	{
		
	}
	
	public static byte[] writeClass(ClassNode node, String name) throws IOException
	{
		ASMHelper.clearCacheNodes();
		MCWriter classWriter = new MCWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(classWriter);
        byte[] transformed = classWriter.toByteArray();
        if(ConfigCore.dumpASM)
        {
        	ASMHelper.dumpFile(name, transformed);
        }
        return transformed;
	}

	public static String getInputBase()
	{
		return "assets/menulib/asm/" + (FMLCorePlugin.isObf ? "srg/" : "deob/");
	}

}

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

import net.minecraft.launchwrapper.IClassTransformer;

public class MLTransformer implements IClassTransformer{
	
    public static final List<String> clazzes = (List<String>)JavaUtil.<String>asArray(new Object[]
    {
    	"net.minecraft.client.Minecraft",
   		"net.minecraft.client.audio.MusicTicker"
    });
    
    //since I don't have my asm system I use a slightly better version for forge's switch case is gross but, it's better then if else
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) 
	{
		if(bytes == null)
			return null;
		try
		{
			int index = clazzes.indexOf(name);
			if(index != -1)
			{
				ClassNode node = ASMHelper.getClassNode(bytes);
				String base = "assets/menulib/asm/" + (FMLCorePlugin.isObf ? "srg/" : "deob/");
				switch (index)
				{
					case 0:
						transformFramerate(node, base);
					break;
					
					case 1:
//						transformMusic(node);
					break;
				}
				
				//legacy mess I have to manually clear the cache of classnodes, manually do classwriter sh*t then return and or dump the file
				ASMHelper.clearCacheNodes();
				MCWriter classWriter = new MCWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		        node.accept(classWriter);
		        byte[] transformed = classWriter.toByteArray();
		        if(ConfigCore.dumpASM)
		        {
		        	ASMHelper.dumpFile(name + "-test", transformed);
		        }
		        return transformed;
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
	public void transformFramerate(ClassNode classNode, String input) throws IOException 
	{
		//add getMenuFrames so minecraft can use them later
		MethodNode mainmenu = ASMHelper.addMethod(classNode, input + "Minecraft", "getMenuFrames", "()I");
		
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
		MethodNode play = ASMHelper.getMethodNode(node, new MCPSidedString("playMusic", "func_181558_a").toString(), "(Lnet/minecraft/client/audio/MusicTicker$MusicType;)V");
		AbstractInsnNode spot = ASMHelper.getFirstInstruction(play, Opcodes.PUTFIELD);
		
		//if(!MusicEvent.fire(MusicHandler.musicTicker, this.currentMusic) return
		InsnList list = new InsnList();
		list.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/jredfox/menulib/event/MusicEvent", "musicTicker", "Lnet/minecraft/util/ResourceLocation;"));
		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/audio/MusicTicker", new MCPSidedString("currentMusic", "field_147678_c").toString(), "Lnet/minecraft/client/audio/ISound;"));
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/jredfox/menulib/event/MusicEvent", "fire", "(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/client/audio/ISound;)Z", false));
		LabelNode l2 = new LabelNode();
		list.add(new JumpInsnNode(Opcodes.IFNE, l2));
		LabelNode l3 = new LabelNode();
		list.add(l3);
		list.add(new InsnNode(Opcodes.RETURN));
		list.add(l2);
		
		play.instructions.insert(spot, list);
	}

}

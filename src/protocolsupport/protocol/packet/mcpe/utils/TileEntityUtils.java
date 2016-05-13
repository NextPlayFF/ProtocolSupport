package protocolsupport.protocol.packet.mcpe.utils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.EncoderException;

import net.minecraft.server.v1_9_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import protocolsupport.utils.netty.Allocator;
import protocolsupport.utils.netty.ChannelUtils;

public class TileEntityUtils {

	public static boolean isTileEntity(int blockId) {
		return getTileId(blockId) != null;
	}

	public static NBTTagCompound getInitTileEntityTag(int x, int y, int z, int blockId) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInt("x", x);
		tag.setInt("y", y);
		tag.setInt("z", z);
		tag.setString("id", getTileId(blockId));
		return tag;
	}

	//TODO: more ids
	private static String getTileId(int blockId) {
		switch (blockId) {
			case 54: {
				return "Chest";
			}
			case 63:
			case 68: {
				return "Sign";
			}
		}
		return null;
	}

	public static byte[] toNoLengthPrefixBuf(NBTTagCompound tag) {
		ByteBuf buf = Allocator.allocateBuffer();
		try {
			NBTCompressedStreamTools.a(tag, new PEDataOutput(buf));
			return ChannelUtils.toArray(buf);
		} catch (Throwable ioexception) {
			throw new EncoderException(ioexception);
		} finally {
			buf.release();
		}
	}

}
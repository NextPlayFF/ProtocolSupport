package protocolsupport.protocol.pipeline.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import protocolsupport.utils.netty.ChannelUtils;
import protocolsupport.utils.netty.Compressor;

public class PacketCompressor extends net.minecraft.server.v1_10_R1.PacketCompressor {

	private final Compressor compressor = Compressor.create();
	private final int threshold;

	public PacketCompressor(int threshold) {
		super(threshold);
		this.threshold = threshold;
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		compressor.recycle();
	}

	@Override
	protected void a(ChannelHandlerContext ctx, ByteBuf from, ByteBuf to)  {
		int readable = from.readableBytes();
		if (readable == 0) {
			return;
		}
		if (readable < this.threshold) {
			ChannelUtils.writeVarInt(to, 0);
			to.writeBytes(from);
		} else {
			ChannelUtils.writeVarInt(to, readable);
			to.writeBytes(compressor.compress(ChannelUtils.toArray(from)));
		}
	}

}

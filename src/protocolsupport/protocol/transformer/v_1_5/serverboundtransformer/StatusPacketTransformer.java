package protocolsupport.protocol.transformer.v_1_5.serverboundtransformer;

import java.util.Collection;

import io.netty.channel.Channel;

import net.minecraft.server.v1_8_R3.Packet;

import protocolsupport.protocol.PacketDataSerializer;

public class StatusPacketTransformer implements PacketTransformer {

	@Override
	public Collection<Packet<?>> transform(Channel channel, int packetId, PacketDataSerializer serializer) throws Exception {
		return null;
	}

}

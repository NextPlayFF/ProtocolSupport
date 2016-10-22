package protocolsupport.protocol.packet.middleimpl.serverbound.play.v_1_4__1_5__1_6__1_7__1_8__1_9_r1__1_9_r2__1_10;

import protocolsupport.protocol.packet.middle.serverbound.play.MiddleInventoryClose;
import protocolsupport.protocol.serializer.ProtocolSupportPacketDataSerializer;

public class InventoryClose extends MiddleInventoryClose {

	@Override
	public void readFromClientData(ProtocolSupportPacketDataSerializer serializer) {
		windowId = serializer.readUnsignedByte();
	}

}

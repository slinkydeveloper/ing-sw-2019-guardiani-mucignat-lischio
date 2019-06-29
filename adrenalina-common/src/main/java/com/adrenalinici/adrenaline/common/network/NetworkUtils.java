package com.adrenalinici.adrenaline.common.network;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkUtils {

  public static final int PACKET_SIZE = 1024;

  public static List<ByteBuffer> prepareSerializedBuffer(byte[] serialized) {
    if (serialized.length <= PACKET_SIZE - 4) {
      // Only one packet needed

      ByteBuffer buf = ByteBuffer.allocate(serialized.length + 4);
      buf.putInt(serialized.length);
      buf.put(serialized);
      buf.rewind();

      return Collections.singletonList(buf);
    } else {
      int requiredPackets = (serialized.length + 4) / PACKET_SIZE;
      int lastPacketLength = (serialized.length + 4) % PACKET_SIZE;

      List<ByteBuffer> buffers = new ArrayList<>(requiredPackets + 1);

      ByteBuffer firstBuf = ByteBuffer.allocate(PACKET_SIZE);
      firstBuf.putInt(serialized.length);
      firstBuf.put(serialized, 0, PACKET_SIZE - 4);
      firstBuf.rewind();
      buffers.add(firstBuf);

      for (int i = 1; i < requiredPackets; i++) {
        ByteBuffer buf = ByteBuffer.allocate(PACKET_SIZE);
        buf.put(serialized, (PACKET_SIZE * i) - 4, PACKET_SIZE);
        buf.rewind();
        buffers.add(buf);
      }

      if (lastPacketLength != 0) {
        ByteBuffer lastBuf = ByteBuffer.allocate(lastPacketLength);
        lastBuf.put(serialized, serialized.length - lastPacketLength, lastPacketLength);
        lastBuf.rewind();
        buffers.add(lastBuf);
      }

      return buffers;
    }
  }

}

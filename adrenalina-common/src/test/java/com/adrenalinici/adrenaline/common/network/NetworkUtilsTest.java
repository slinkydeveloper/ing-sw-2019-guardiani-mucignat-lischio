package com.adrenalinici.adrenaline.common.network;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NetworkUtilsTest {

  @Test
  public void testOneChunk() {
    testBufferChunks(
      createByteArrayOf(
        1020,
        (byte) 255
      ),
      1
    );
  }

  @Test
  public void testTwoChunk() {
    testBufferChunks(
      createByteArrayOf(
        1024,
        (byte) 255
      ),
      2
    );
  }

  @Test
  public void testTwoExactChunk() {
    testBufferChunks(
      createByteArrayOf(
        2044,
        (byte) 255
      ),
      2
    );
  }

  @Test
  public void testThreeChunk() {
    testBufferChunks(
      createByteArrayOf(
        2048,
        (byte) 255
      ),
      3
    );
  }

  @Test
  public void testFourChunk() {
    testBufferChunks(
      createByteArrayOf(
        3500,
        (byte) 255
      ),
      4
    );
  }

  private void testBufferChunks(byte[] serialized, int chunksNumber) {
    List<ByteBuffer> chunks = NetworkUtils.prepareSerializedBuffer(serialized);

    assertThat(chunks).hasSize(chunksNumber);

    ByteBuffer resultBuf = ByteBuffer.allocate(serialized.length + 4);
    chunks.forEach(resultBuf::put);
    resultBuf.rewind();

    int size = resultBuf.getInt();

    assertThat(size)
      .isEqualTo(serialized.length);

    byte[] res = new byte[size];
    resultBuf.get(res);

    assertThat(res).isEqualTo(serialized);
  }

  private byte[] createByteArrayOf(int size, byte b) {
    byte[] newArr = new byte[size];
    for (int i = 0; i < size; i++) {
      newArr[i] = b;
    }
    return newArr;
  }

}

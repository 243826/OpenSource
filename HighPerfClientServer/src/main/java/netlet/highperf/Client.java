/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netlet.highperf;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class Client extends malhar.netlet.Client implements Runnable
{
  SelectionKey key;
  volatile boolean disconnected;
  volatile boolean done;

  public void run()
  {
    try {
      for (int i = 0; i < 16 * 1024 * 1024; i++) {
        byte[] array = new byte[64];
        Arrays.fill(array, (byte)i);
        send(array, 0, 64);
      }
    }
    catch (InterruptedException ie) {
      logger.debug("stopped sending since interrupted", ie);
    }

    if (key != null) {
      key.interestOps(SelectionKey.OP_WRITE);
    }
  }

  @Override
  public void connected(SelectionKey key)
  {
    key.interestOps(SelectionKey.OP_WRITE);
    this.key = key;
  }

  @Override
  public ByteBuffer buffer()
  {
    return bb;
  }

  @Override
  public void read(int len)
  {
  }

  private ByteBuffer bb = ByteBuffer.allocate(4096);
  private static final Logger logger = LoggerFactory.getLogger(Client.class);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netlet.highperf;

import static java.lang.Thread.sleep;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class Client extends com.malhartech.netlet.Client implements Runnable
{
  public Client(int sendBufferSize)
  {
    super(sendBufferSize);
  }

  @SuppressWarnings("SleepWhileInLoop")
  public void run()
  {
    try {
      for (int i = 0; i < 16 * 1024 * 1024; i++) {
        while (!send(new byte[64], 0, 64)) {
          sleep(5);
        }
      }
    }
    catch (InterruptedException ie) {
    }
  }

  @Override
  public ByteBuffer buffer()
  {
    return null;
  }

  @Override
  public void read(int len)
  {
  }

  @Override
  public String toString()
  {
    return "Client";
  }

  private static final Logger logger = LoggerFactory.getLogger(Client.class);
}

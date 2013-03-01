/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectlet.highperf;

import com.googlecode.connectlet.Connection;
import com.googlecode.connectlet.Connector;
import com.googlecode.connectlet.Connector.Listener;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class Client extends Connection implements Listener, Runnable
{
  private static final Logger logger = LoggerFactory.getLogger(Client.class);
  private String host;
  private int port;

  public Client(String host, int port)
  {
    this.host = host;
    this.port = port;
  }

  @SuppressWarnings("SleepWhileInLoop")
  public void run()
  {
    Connector connector = new Connector();
    try {
      connector.connect(this, host, port);
    }
    catch (IOException ie) {
      throw new RuntimeException(ie);
    }

    connector.setBufferSize(64 * 1024);
    for (int i = 0; i < 16 * 1024; i++) {
      if (connector.doEvents()) {
        if (Thread.interrupted()) {
          logger.debug("Interrupted... so aborting!");
          break;
        }
      }
      else {
//        try {
//          sleep(10);
//        }
//        catch (InterruptedException ie) {
//          logger.debug("Interrupted... so aborting!", ie);
//          break;
//        }
      }
    }

    connector.close();
  }

  public void onEvent()
  {
    for (int i = 0; i < 1024; i++) {
      send(new byte[64], 0, 64);
    }
  }

}

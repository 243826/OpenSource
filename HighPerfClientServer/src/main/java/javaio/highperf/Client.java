/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaio.highperf;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class Client
{
  private static final Logger logger = LoggerFactory.getLogger(Client.class);
  private String host;
  private int port;

  public Client(String host, int port)
  {
    this.host = host;
    this.port = port;
  }

  public void run() throws IOException
  {
    Socket socket = new Socket(host, port);
    OutputStream stream = socket.getOutputStream();

    for (int i = 0; i < 16 * 1024 * 1024; i++) {
      stream.write(new byte[64]);
    }

    stream.flush();
    stream.close();
  }
}

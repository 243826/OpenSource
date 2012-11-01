/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaio.highperf;

import java.io.IOException;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class ClientTest extends TestCase
{
  private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);
  public ClientTest(String testName)
  {
    super(testName);
  }

  public void testSomeMethod()
  {
    Client client = new Client("cnlindes", 5033);
    try {
      client.run();
    }
    catch (IOException io) {
      logger.debug("", io);
    }
  }
}

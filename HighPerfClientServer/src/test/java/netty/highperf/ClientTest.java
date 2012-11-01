package netty.highperf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import netty.highperf.Client;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ClientTest extends TestCase
{
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public ClientTest(String testName)
  {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite(ClientTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testApp() throws IOException, InterruptedException
  {
    Process proc = Runtime.getRuntime().exec(new String[] {
              "/bin/sh",
              "-c",
              "nc -l 5033 2>/tmp/nc | dd bs=4096 of=/dev/null 2>/tmp/dd"
            });

    Client c = new Client("cnlindes", 5033);
    c.run();

    BufferedInputStream buffer = new BufferedInputStream(proc.getInputStream());
    BufferedReader commandOutput = new BufferedReader(new InputStreamReader(buffer));
    try {
      String line;
      while ((line = commandOutput.readLine()) != null) {
        System.out.println("command output: " + line);
      }//end while

      commandOutput.close();
    }
    catch (IOException e) {
      //log and/or handle it
    }//end catch
  }
}

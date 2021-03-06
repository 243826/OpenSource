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
              "nc -l 5033 | dd bs=4096 of=/dev/null"
            });

    Client c = new Client("localhost", 5033);
    c.run();

    BufferedInputStream buffer = new BufferedInputStream(proc.getErrorStream());
    BufferedReader commandOutput = new BufferedReader(new InputStreamReader(buffer));
    String line;
    while ((line = commandOutput.readLine()) != null) {
      System.out.println("command output: " + line);
    }
    commandOutput.close();
  }
}
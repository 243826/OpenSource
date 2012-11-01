package netty.highperf;

import netty.highperf.Client;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//      Server s = new Server(0);
//      s.run();
//
//      Client c = new Client(s.getAddress().getHostName(), s.getAddress().getPort());
      Client c = new Client("127.0.0.1", 5033);
      c.run();
    }
}

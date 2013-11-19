/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import junit.framework.Assert;
import junit.framework.TestCase;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import gnu.trove.strategy.HashingStrategy;
import java.io.FileNotFoundException;

/**
 *
 * @author Chetan Narsude <chetan@datatorrent.com>
 */
public class TroveHashMapTest extends TestCase
{
  public TroveHashMapTest(String testName)
  {
    super(testName);
  }

  public void testSerialization() throws FileNotFoundException
  {
    MyHashingStrategy strategy = new MyHashingStrategy();
    TroveHashMap pre = new TroveHashMap(strategy);

    Assert.assertNotNull(pre.mystrategy);

    Kryo kryo = new Kryo();
    Output output = new Output(new FileOutputStream("target/file.bin"));
    kryo.writeObject(output, pre);
    output.close();

    Input input = new Input(new FileInputStream("target/file.bin"));
    @SuppressWarnings("unchecked")
    TroveHashMap post = kryo.readObject(input, TroveHashMap.class);
    input.close();

    Assert.assertNotNull(post.mystrategy);
  }

  public static class MyHashingStrategy implements HashingStrategy<Object>
  {
    public int computeHashCode(Object object)
    {
      return 0;
    }

    public boolean equals(Object o1, Object o2)
    {
      return true;
    }

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    private static final long serialVersionUID = 1L;
  }

}


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

public class TroveHashMap extends TCustomHashMap<Object, Object> implements KryoSerializable
{
  public Object mystrategy;

  public TroveHashMap(HashingStrategy<Object> strategy)
  {
    super(strategy);
    mystrategy = strategy;
  }

  private TroveHashMap()
  {
    /* provided for Kryo */
  }

  private static final long serialVersionUID = 1L;

  public void write(Kryo kryo, Output output)
  {
    try {
      ObjectOutputStream stream;
      super.writeExternal(stream = new ObjectOutputStream(output));
      stream.flush();
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public void read(Kryo kryo, Input input)
  {
    try {
      super.readExternal(new ObjectInputStream(input));
      mystrategy = super.strategy;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}

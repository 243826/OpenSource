/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netty.highperf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class Client implements Runnable
{
  private static final Logger logger = LoggerFactory.getLogger(Client.class);
  private Channel channel;
  private String host;
  private int port;
  boolean futureListenerAdded;

  public Client(String host, int port)
  {
    this.host = host;
    this.port = port;
  }

  public void run()
  {
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(new NioEventLoopGroup(8))
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, false)
            .remoteAddress(host, port)
            .handler(
            new ChannelInitializer<SocketChannel>()
            {
              @Override
              public void initChannel(SocketChannel ch) throws Exception
              {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("ob", new ChannelOutboundMessageHandlerAdapter<byte[]>()
                {

                  @Override
                  protected void flush(ChannelHandlerContext ctx, byte[] msg) throws Exception
                  {
                    ctx.nextOutboundByteBuffer().writeBytes(msg);
                  }
//                  @Override
//                  public void flush(ChannelHandlerContext ctx, ChannelFuture future) throws Exception
//                  {
//                    for (Iterator<Object> it = ctx.outboundMessageBuffer().iterator(); it.hasNext();) {
//                      byte[] b = (byte[])it.next();
//                      ctx.nextOutboundByteBuffer().writeBytes(b);
//                    }
//                    ctx.outboundMessageBuffer().clear();
//                    ctx.flush(future);
//                  }
                });
              }
            });
    try {
      channel = bootstrap.connect().sync().channel();
    }
    catch (InterruptedException ex) {
      logger.debug("exception while connection", ex);
    }

    ChannelFutureListener cfl = new ChannelFutureListener()
    {
      public synchronized void operationComplete(ChannelFuture future) throws Exception
      {
        futureListenerAdded = false;
        notify();
      }
    };

    for (int i = 0; i < 32 * 1024; i++) {
      for (int j = 0; j < 511; j++) {
        channel.write(new byte[64]);
      }

      synchronized (cfl) {
        if (futureListenerAdded) {
          try {
            channel.write(new byte[64]);
            cfl.wait();
          }
          catch (InterruptedException ex) {
          }
        }
        else {
          channel.write(new byte[64]).addListener(cfl);
          futureListenerAdded = true;
        }
      }
    }

    channel.flush().awaitUninterruptibly();
    channel.close().awaitUninterruptibly();
    bootstrap.shutdown();
  }
}

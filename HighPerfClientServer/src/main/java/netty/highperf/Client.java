/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netty.highperf;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ChannelBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.netty.buffer.Unpooled.wrappedBuffer;


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
            .remoteAddress(host, port)
            .handler(new ChannelInitializer<SocketChannel>()
    {
      @Override
      public void initChannel(SocketChannel ch) throws Exception
      {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("ob", new ChannelOutboundMessageHandlerAdapter()
        {
          @Override
          public void flush(ChannelHandlerContext ctx, ChannelFuture future) throws Exception
          {
            for (Iterator<Object> it = ctx.outboundMessageBuffer().iterator(); it.hasNext();) {
              byte[] b = (byte[])it.next();
              ctx.nextOutboundByteBuffer().writeBytes(b);
            }
//            logger.debug("total messages = {}", ctx.nextOutboundByteBuffer().writableBytes());
            ctx.flush(future);
          }

        });
      }
    });
    try {
      channel = bootstrap.connect().sync().channel();
    }
    catch (InterruptedException ex) {
      logger.debug("exception while connection", ex);
    }

    for (int i = 0; i < 16 * 1024 * 1024; i++) {
      channel.write(new byte[64]);
//      if (++i == 10) {
//        try {
//          Thread.sleep(100);
//        }
//        catch (InterruptedException ex) {
//          Thread.currentThread().interrupt();
//        }
//        i = 0;
//      }
    }

    bootstrap.shutdown();
  }
}

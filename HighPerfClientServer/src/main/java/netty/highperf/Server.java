/*
 * Copyright (c) 2012 Malhar, Inc.
 * All Rights Reserved.
 */
package netty.highperf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The buffer server application<p>
 * <br>
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class Server
{
  private static final Logger logger = LoggerFactory.getLogger(Server.class);
  public static final int DEFAULT_PORT = 9080;
  private final int port;
  private ServerBootstrap bootstrap;
  private InetSocketAddress address;

  /**
   * @param port - port number to bind to or 0 to auto select a free port
   */
  public Server(int port)
  {
    this.port = port;
  }

  /**
   *
   * @return {@link java.net.SocketAddress}
   * @throws Exception
   */
  public void run()
  {
    // Configure the server.
    bootstrap = new ServerBootstrap();

    bootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(8))
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 100)
            .localAddress(port)
            //.childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ChannelInitializer<SocketChannel>()
    {
      @Override
      public void initChannel(SocketChannel ch) throws Exception
      {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ChannelInboundByteHandlerAdapter()
        {
          @Override
          public void inboundBufferUpdated(ChannelHandlerContext ctx, ByteBuf in) throws Exception
          {
            logger.debug("msg = {}", in.readableBytes());
            in.clear(); // discard the data
          }
        });
      }
    });

    ChannelFuture f = bootstrap.bind().syncUninterruptibly();
    address = (InetSocketAddress)f.channel().localAddress();
    logger.info("Server instance bound to: {}", getAddress());
  }

  /**
   *
   */
  public void shutdown()
  {
    logger.info("Server instance {} being shutdown", getAddress());
    bootstrap.shutdown();
  }

  /**
   * @return the address
   */
  public InetSocketAddress getAddress()
  {
    return address;
  }
}

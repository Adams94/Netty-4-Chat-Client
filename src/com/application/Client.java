package com.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * The main class for the client.
 * 
 * @author Chad Adams <https://github.com/Adams94>
 */
public class Client {

	/**
	 * The single logger for this class.
	 */
	public static final Logger logger = Logger.getLogger(Client.class.getName());

	/**
	 * The main-entry way into the application.
	 * 
	 * @param args
	 * 		The command-line arguments.
	 */
	public static void main(String[] args) throws InterruptedException {
		connect(Configuration.ADDRESS, Configuration.PORT);
	}

	public static void connect(String address, int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new NetworkChannelInitializer());

			Channel ch = b.connect(address, port).sync().channel();
			logger.log(Level.INFO, "Connected to: {0} on port: " + port, address);

			ChannelFuture lastWriteFuture = null;

			// The reader that will read the users input.
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			// Declaring the message
			String message = "";

			try {
				while (!message.contains("exit")) {
					message = reader.readLine();
					if (message == null) {
						continue;
					}

					lastWriteFuture = ch.writeAndFlush(message);

					if (message.equalsIgnoreCase("exit")) {
						logger.log(Level.INFO, "Client is now shutting down..");
					}

				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "An error occured while trying to write a message.", ex);
			}

			if (lastWriteFuture != null) {
				lastWriteFuture.sync();
			}

		} finally {
			group.shutdownGracefully();
		}
	}

}

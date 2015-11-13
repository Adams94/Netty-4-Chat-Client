package com.application;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * The channel handler that handles all upstream {@link String} messages from registered channels.
 * 
 * @author Chad Adams <https://github.com/Adams94>
 */
public class NetworkChannelHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
		System.out.println("[" + ctx.channel().remoteAddress() + "] Message - " + message);
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

package com.hbdrawn.push;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PushClient extends IoHandlerAdapter implements Runnable {
    public Logger logger = LoggerFactory.getLogger(getClass());

    // 重试次数
    // public static final int tryCount = 3;

    private String ipAddress = null;

    private int port = 80;

    // connector只包含一个session
    public IoSession pcSession = null;

    private IoConnector connector = null;

    public PushClient() {
    }

    public PushClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public final void sessionClosed(IoSession session) throws Exception {
        session.close(true);
        pcSession = null;
        startService();
    }

    public final void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warn(">>>>{}", cause);
        session.close(true);
        pcSession = null;
        startService();
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("客户端向服务端发送消息:{}", message);
    }

    // 心跳不會到handler层
    public final void messageReceived(IoSession session, Object message) throws Exception {
        // 将试用次数复原，心跳发送异常会更改次数
        // if (session.getAttribute("tryCount") != null) {
        // int count = (Integer) session.getAttribute("tryCount");
        // if (count < tryCount) {
        // session.setAttribute("tryCount", tryCount);
        // }
        // }
        // 位置信息处理
        String msg = message.toString();
        logger.info(">>>>客户端接收数据：{}", msg);
        if (msg.startsWith("position:")) {
            logger.info("客户端接受到服务端命令请求:{}", msg);
            String string = sendLocationListener(Integer.valueOf(msg.substring(9).trim()));
            StringBuilder sb = new StringBuilder("position:");
            session.write(sb.append(string));
        }
    };

    // 客户端实现此接口，将位置信息报文发给给服务端
    public abstract String sendLocationListener(int coordtype);

    // 客户端实现此方法，发送teleNo和simNo给服务端
    public final void login(String teleNo, String simNo) throws Exception {
        if (teleNo == null || simNo == null || teleNo.trim().equals("") || simNo.trim().equals("")) {
            throw new Exception("参数teleNo或simNo不能为空");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("add:{\"simNo\":\"").append(simNo).append("\",\"teleNo\":\"").append(teleNo).append("\"}");
        pcSession.write(sb.toString());
    }

    // 客户端实现此方法，发送teleNo和simNo给服务端,进行注销
    public final void logout(String teleNo, String simNo) throws Exception {
        if (teleNo == null || simNo == null || teleNo.trim().equals("") || simNo.trim().equals("")) {
            throw new Exception("参数teleNo或simNo不能为空");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("remove:{\"simNo\":\"").append(simNo).append("\",\"teleNo\":\"").append(teleNo).append("\"}");
        pcSession.write(sb.toString());
    }

    public synchronized final void startService() throws Exception {
        logger.debug("开始创建长连接");
        if (pcSession == null) {
            // 建立连接前，先关闭已有的session
            close();
            connector = new NioSocketConnector();
            connector.setHandler(this);
            DefaultIoFilterChainBuilder chain = connector.getFilterChain();
            chain.addLast("logger", new LoggingFilter());
            // 心跳
            chain.addLast("keep-alive", new MyKeepAliveFilter());
            chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

            SocketSessionConfig config = (SocketSessionConfig) connector.getSessionConfig();
            config.setReadBufferSize(2048);
            config.setReuseAddress(true);

            ConnectFuture connFuture = connector.connect(new InetSocketAddress(ipAddress, port));
            connFuture.awaitUninterruptibly();
            pcSession = connFuture.getSession();
            logger.info(">>>长连接建立成功");
            login("18701585849", "18701585849");
        }
    }

    public final void close() {
        if (pcSession != null) {
            pcSession.close(true);
            pcSession = null;
        }
        // if (connector != null) {
        // connector.dispose(true);
        // }
    }

    // 重连策略
    @Override
    public void run() {
        while (true) {
            if (pcSession == null) {
                try {
                    preStartService();
                    startService();
                    afterSuccess();
                } catch (Exception e) {
                    logger.warn("建立长连接失败，正在重试:", e);
                    sleep();
                }
            } else {
                sleep();
                logger.debug("后台保护线程运行中");
            }
        }
    }

    public void sleep() {
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e1) {
        }
    }

    // 长连接建立成功后的相关处理逻辑
    public abstract void afterSuccess() throws Exception;

    // 修改ip和port，连接前预处理
    public abstract void preStartService() throws Exception;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
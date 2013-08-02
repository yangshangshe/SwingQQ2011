package org.fw.qq.plugins.udp;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 
 * <br/>
 * Title: UDPServer.java<br/>
 * E-Mail: 176291935@qq.com<br/>
 * QQ: 176291935<br/>
 * Http: iaiai.iteye.com<br/>
 * Create time: 2013-1-29 上午11:11:56<br/>
 * <br/>
 * @author 丸子
 * @version 0.0.1
 */
public class UDPServer {

    public static void main(String[] args) {
        try {
            DatagramSocket server = new DatagramSocket(2008);
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            String sendMessage132 = "";
            String sendMessage129 = "";
            int port132 = 0;
            int port129 = 0;
            InetAddress address132 = null;
            InetAddress address129 = null;
            for (;;) {
                server.receive(packet);

                String receiveMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println(receiveMessage);
                //接收到clientA
                if (receiveMessage.contains("132")) {
                    port132 = packet.getPort();
                    address132 = packet.getAddress();
                    sendMessage132 = "host:" + address132.getHostAddress() + ",port:" + port132;
                }
                //接收到clientB
                if (receiveMessage.contains("129")) {
                    port129 = packet.getPort();
                    address129 = packet.getAddress();
                    sendMessage129 = "host:" + address129.getHostAddress() + ",port:" + port129;
                }
                //两个都接收到后分别A、B址地交换互发
                if (!sendMessage132.equals("") && !sendMessage129.equals("")) {
                    send132(sendMessage129, port132, address132, server);
                    send129(sendMessage132, port129, address129, server);
                    sendMessage132 = "";
                    sendMessage129 = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void send129(String sendMessage132, int port132, InetAddress address132, DatagramSocket server) {
        try {
            byte[] sendBuf = sendMessage132.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address132, port132);
            server.send(sendPacket);
            System.out.println("消息发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void send132(String sendMessage129, int port129, InetAddress address129, DatagramSocket server) {
        try {
            byte[] sendBuf = sendMessage129.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address129, port129);
            server.send(sendPacket);
            System.out.println("消息发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

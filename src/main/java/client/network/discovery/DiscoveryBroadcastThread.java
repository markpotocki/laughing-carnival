package client.network.discovery;

import java.io.IOException;
import java.net.*;

public class DiscoveryBroadcastThread extends Thread {

    private final int PORT;

    public DiscoveryBroadcastThread(int port) {
        this.PORT = port;
    }

    @Override
    public void run() {
        DatagramSocket d = null;
        try {
            d = constructDiscoverySocket();
            while (true) {
                try {
                    System.out.println("Sending datagram packet");
                    d.send(constructDiscoveryDatagram());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.sleep(20000);
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }finally {
            d.close();
        }
    }

    private DatagramPacket constructDiscoveryDatagram() throws UnknownHostException {
        byte[] discoveryData = new byte[256];
        DatagramPacket packet = new DatagramPacket(discoveryData, discoveryData.length, InetAddress.getLocalHost(), 9010);
        return packet;
    }

    private DatagramSocket constructDiscoverySocket() {
        try {
            DatagramSocket d = new DatagramSocket();
            d.setBroadcast(true);
            return d;
        }
        catch(SocketException e) {
            throw new RuntimeException("Failed to bind to port " + PORT);
        }
    }


}

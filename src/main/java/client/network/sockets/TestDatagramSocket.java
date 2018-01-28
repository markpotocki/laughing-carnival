package client.network.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class TestDatagramSocket{

    private DatagramSocket socket;

    public TestDatagramSocket() throws SocketException {
        this.socket = new DatagramSocket();
    }

    public void broadcast(DatagramPacket packet) throws IOException {
        while(true) {
            this.socket.send(packet);
            try {
                Thread.sleep(10000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

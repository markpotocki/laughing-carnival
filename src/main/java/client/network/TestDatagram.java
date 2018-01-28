package client.network;

import java.net.DatagramPacket;

public class TestDatagram {

    private DatagramPacket packet;

    public TestDatagram(byte[] data) {
        this.packet = new DatagramPacket(data, data.length);
    }
}

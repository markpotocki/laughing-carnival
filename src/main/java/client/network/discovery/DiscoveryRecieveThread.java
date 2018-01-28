package client.network.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryRecieveThread extends Thread {

    private final int PORT;

    public boolean complete = false;

    private List<InetSocketAddress> discoveredPeers;

    private final int neededPeers;

    public DiscoveryRecieveThread(int port, int neededPeers) {
        PORT = port;
        discoveredPeers = new ArrayList<>();
        this.neededPeers = neededPeers;
    }

    @Override
    public void run() {
        DatagramSocket d = constructDiscoverySocket();
        try {
            for (int i = 0; i < neededPeers; i++) {
                DatagramPacket packet = constructDiscoveryDatagram();
                System.out.println("Awaiting discovery packet");
                d.receive(packet);
                discoveredPeers.add(new InetSocketAddress(packet.getAddress(), packet.getPort()));
                System.out.println("Discovered peer");
                printPeer(packet);
            }
            complete = true;
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void printPeer(DatagramPacket inPacket) {
        System.out.println("Peer\t" + "IP " + inPacket.getAddress() + "\tPort " +inPacket.getPort() );
    }

    private DatagramPacket constructDiscoveryDatagram() {
        byte[] discoveryData = new byte[256];
        DatagramPacket packet = new DatagramPacket(discoveryData, discoveryData.length);
        return packet;
    }

    private DatagramSocket constructDiscoverySocket() {
        try {
            DatagramSocket d = new DatagramSocket(null);
            d.setBroadcast(true);
            d.bind(new InetSocketAddress(InetAddress.getLocalHost(), PORT));
            return d;
        }
        catch(Exception e) {
            throw new RuntimeException("Failed to bind to port " + PORT);
        }
    }

    public List<InetSocketAddress> getDiscoveredPeers() {
        return discoveredPeers;
    }
}

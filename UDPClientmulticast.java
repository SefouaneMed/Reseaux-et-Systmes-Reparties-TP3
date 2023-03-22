import java.io.*;
import java.net.*;

public class UDPClientmulticast {
    public static void main(String[] args) throws Exception {
        MulticastSocket socket = null;
        try{

        socket = new MulticastSocket(1234);
        InetAddress group = InetAddress.getByName ("224.0.0.1");
        socket.joinGroup(group);

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        File file = new File("put your path");
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (true) {
            socket.receive(packet);
            fos.write(packet.getData(), 0, packet.getLength());
            if (packet.getLength() < buffer.length) {
                break;
            }
        }
        System.out.println("Received multicast !");
        fos.close();
        socket.leaveGroup(group);
        socket.close();

    }
        catch (IOException e) {
            System.out.println("IOException occurred");
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}

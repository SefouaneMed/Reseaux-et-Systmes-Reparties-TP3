import java.net.*;
import java.io.*;

public class UDPServer {
    private static final int PACKET_SIZE = 1024;
    private static final int PORT = 5000;
    private static final String path = "path data of server";


    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(PORT);

            
            while (true) {

                
                byte[] receiveBuffer = new byte[PACKET_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, PACKET_SIZE);
                socket.receive(receivePacket);

                String filename = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received request for file: " + filename);

                File file = new File(path +"/"+ filename);
                if (!file.exists()) {
                    byte[] failureBuffer = "FAILURE".getBytes();
                    DatagramPacket failurePacket = new DatagramPacket(failureBuffer, failureBuffer.length,
                            receivePacket.getAddress(), receivePacket.getPort());
                    socket.send(failurePacket);
                    System.out.println("File not found, sent failure message to client");
                    continue;
                }

                FileInputStream fileIn = new FileInputStream(file);
                
                byte[] sendBuffer = new byte[PACKET_SIZE];
                int numBytesRead;
                while ((numBytesRead = fileIn.read(sendBuffer)) > 0) {
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, numBytesRead,
                            receivePacket.getAddress(), receivePacket.getPort());
                        System.out.println(sendPacket);

                    socket.send(sendPacket);
                }

                byte[] successBuffer = "SUCCESS".getBytes();
                DatagramPacket successPacket = new DatagramPacket(successBuffer, successBuffer.length,
                        receivePacket.getAddress(), receivePacket.getPort());
                socket.send(successPacket);
                System.out.println("File transfer successful");
            }
        } catch (IOException e) {
            System.out.println("IOException occurred");
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}

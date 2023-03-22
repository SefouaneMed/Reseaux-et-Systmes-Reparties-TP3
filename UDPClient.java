import java.net.*;
import java.io.*;

public class UDPClient {
    private static final int PACKET_SIZE = 1024;
    private static final int PORT = 5000;
    private static final String SERVER_ADDRESS = "localhost";
    private static final String path2 = "path of data client";


    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);



            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Enter the name of the file to download (QUIT to exit): ");
                String filename = userInput.readLine();

                if (filename.equals("QUIT")) {
                    System.out.println("Client terminated by user");
                    break;
                }

                byte[] requestBuffer = filename.getBytes();
                DatagramPacket requestPacket = new DatagramPacket(requestBuffer, requestBuffer.length, serverAddress,
                        PORT);
                socket.send(requestPacket);

                byte[] receiveBuffer = new byte[PACKET_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, PACKET_SIZE);


                try (FileOutputStream fileOut = new FileOutputStream(path2+ "/downloaded_" + filename)) {
                    boolean success = false;
                    boolean failure = false;

                    while (true) {
                        try {
                            socket.receive(receivePacket);
                           // System.out.println(new String(receivePacket.getData(), 0, receivePacket.getLength()));

                        } catch (SocketTimeoutException e) {
                            System.out.println("Timeout occurred while waiting for server response");
                            break;
                        }
                
                        int numBytesReceived = receivePacket.getLength();
                        if (numBytesReceived == 0) {
                            break;
                        }
                
                        byte[] receivedData = receivePacket.getData();
                
                        // Check if the received data is a success message
                        String receivedMessage = new String(receivedData, 0, numBytesReceived);
                        if (receivedMessage.equals("SUCCESS")) {
                            success = true;
                            break;
                        }
                        if (receivedMessage.equals("FAILURE")) {
                            failure = true;
                            break;
                        }

                
                        fileOut.write(receivedData, 0, numBytesReceived);
                    }
                
                    if (success) {
                        System.out.println("Transfer is successful");
                    }
                    if (failure) {
                        System.out.println("File does not existe !");
                    }
                    
                } catch (IOException e) {
                    System.out.println("IOException occurred while receiving file");
                    e.printStackTrace();
                }
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

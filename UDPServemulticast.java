import java.io.*;
import java.net.*;

public class UDPServermulticast {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = null;
        int port =1234;
        try{

        socket  = new DatagramSocket();
        InetAddress adresseIP = InetAddress.getByName("224.0.0.1");

        String cheminFichier = "path of data";
        File fichier = new File(cheminFichier);
        FileInputStream fis = new FileInputStream(fichier);
        byte[] buffer = new byte[(int) fichier.length()];
        fis.read(buffer);

        DatagramPacket paquet = new DatagramPacket(buffer, buffer.length, adresseIP, port);
        socket.send(paquet);
        socket.close();
        fis.close();
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

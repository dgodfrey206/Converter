import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
    public static Socket aSocket;
    public static DataOutputStream out;
    public static DataInputStream in;

    public static void connect() {
        String hostName = "localhost";//"127.0.0.1";
        int portNumber = 8081;//must match the server's port number. use a number greater than 1023
        System.out.println("Creating a client socket to connect on port " + portNumber + " to host " + hostName);
        try {
            aSocket = new Socket(hostName, portNumber);
            out = new DataOutputStream(aSocket.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(aSocket.getInputStream()));

        } catch (UnknownHostException e) {
            System.out.println("Can't Find Host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    public static void close() throws IOException {
        out.writeUTF("exit");
        out.close();
        aSocket.close();
    }
}

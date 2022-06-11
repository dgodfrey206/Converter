import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ConnectionController {
    private static ConnectionController controller = null;
    private static ApplicationUI appUI;
    // A thread in which client keeps receiving messages from server
    private static Thread communicationThread = null;

    private ConnectionController() {
        Connection.connect();
    }

    public static ConnectionController getInstance() {
        if (controller == null) {
            controller = new ConnectionController();
        }
        return controller;
    }

    public void stopChat(){
        if(communicationThread !=null)
            communicationThread.stop();
    }
    public void disconnect() throws IOException {
        stopChat();
        Connection.close();
    }

    public static void main(String[] args) {
        ConnectionController.getInstance();
        appUI = new ApplicationUI();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    appUI.setVisible(true);
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        communicationThread = new Thread(() -> appUI.startChat());
        communicationThread.start();
    }
}
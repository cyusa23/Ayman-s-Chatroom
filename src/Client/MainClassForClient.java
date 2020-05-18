package Client;
/**
 * MainClassForClient
 */
import javax.swing.JFrame;
public class MainClassForClient {
    public static void main(String[]args ){
        Client Cyusa;
        Cyusa = new Client("127.0.0.1");
        Cyusa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Cyusa.startRunning();
    }
    
}
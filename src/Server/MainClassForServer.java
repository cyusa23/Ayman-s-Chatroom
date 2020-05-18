package Server;
import javax.swing.JFrame;
public class MainClassForServer {
     public static void main(String[] args) {
        Server Ayman = new Server();
        Ayman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Ayman.startRunning();
    }
        
    
}
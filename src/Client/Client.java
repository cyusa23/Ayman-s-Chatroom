package Client;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Client extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String message = "";
    private String serverIp;
    private Socket connection;

    
    //construct
    public Client(String host){
        super("Ayman's Whatsapp");
        serverIp = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER );
        setSize(300,150);
        setVisible(true);    
    }

    //connect to server
    public void startRunning(){
        try {
            connectToServer();
            setUpStreams();
            whileChatting();
        } catch (EOFException eofException) {
            //TODO: handle exception
            showMessage("\n Client terminated connnection");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }finally{
            closeCrap();
        }
    }



    //connect to server
    private void connectToServer() throws IOException{
        showMessage("Attempting connection ...\n");
        connection = new Socket(InetAddress.getByName(serverIp), 6789);
        showMessage("Connect to: "+connection.getInetAddress().getHostName());

    }


    //set up Streams to send and recieve messages
    private void setUpStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Dude your streams are now good to go");

    }

    //while chatting with server
    private void whileChatting() throws IOException{
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n"+message);
            } catch (ClassNotFoundException classNotFoundException) {
                //TODO: handle exception
                showMessage("\n i dont lnow that object type");
            }
        } while (!message.equals("SERVER - END"));
    }

    //close the streams and sockets
    private void closeCrap(){
        showMessage("\n closing crap down ...");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            //TODO: handle exception
            ioException.printStackTrace();
        }
    }

    //send message to server
    private void sendMessage(String message){
        try {
            output.writeObject("CLIENT - "+message);
            output.flush();
            showMessage("\nCLIENT - "+message);
        }catch (IOException ioException) {
            //TODO: handle exception
            chatWindow.append("\n something messed up sending message Ayman");
        }
    }

    //change/update chatwindow
    private void showMessage(final String message){
        SwingUtilities.invokeLater(
            new Runnable(){
            
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    chatWindow.append(message);       
                }
            }
        );
    }

    //gives user permission to type crap into the text box
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
            
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    userText.setEditable(tof);      
                }
            }
        );

    }

}
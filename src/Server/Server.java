package Server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Server extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ServerSocket server;
    private Socket connection;

    //constructor
    public Server(){
        super("Ayman's whatsapp");
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
        chatWindow=new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);
    }

    //set up and run the server
    public void startRunning(){
        try {
            server = new ServerSocket(6789,100);
            while(true){
                try {
                    //connect and have conversation
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eofException) {
                    //TODO: handle exception
                    showMessage("\n Server end the connectio! ");
                }finally{
                    closeCrap();
                }
            }
        } catch (IOException ioException) {
            //TODO: handle exception
            ioException.printStackTrace();
        }
    }

    //wait for conncetion,then display coonection information
    private void waitForConnection() throws IOException{
        showMessage(" waiting for someone to connect...\n");
        connection = server.accept();
        showMessage(" Now connected to "+connection.getInetAddress().getHostName());

    }


    //get stream to send and recieve data
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now Setup \n");
    }

    //during the chat conversation
    private void whileChatting() throws IOException{
        String message = " you are now connected! ";
        sendMessage(message);
        ableToType(true);
        do{
            //have a conversation
            try {
                message =(String) input.readObject();
                showMessage("\n"+message);
            } catch (ClassNotFoundException classNotFoundException){
                //TODO: handle exception
                //when the user try to hack us
                showMessage("\n idk wtf that user send!");
            }

        }while(!message.equals("CLIENT - END"));
    }

    //close stream and sockets after you are done chatting
    private void closeCrap(){
        showMessage("\n Closing connections ...\n");
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

    //send a message to client
    private void sendMessage(String message){
        try {
            output.writeObject("SERVER - "+message);
            output.flush();
            showMessage("\nSERVER - "+message);
        } catch (IOException ioException) {
            //TODO: handle exception
            chatWindow.append("\n ERROR: DUDE I CANT SEND THAT MESSAGE");
        }
    }

    //updates chatWindow
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
            
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    chatWindow.append(text);
                }
            }
        );
    }
    //let the user type stuff into their box
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
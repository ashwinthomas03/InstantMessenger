import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    //Uses BufferedReader to create an InputStream and creates an output stream
    public Client(Socket socket){
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessagetoServer() {
        //Creates a Scanner for user input
        Scanner scan = new Scanner(System.in);
        //While the client is connected to a socket outputs any message the user inputs to the server
        while (this.socket.isConnected()){
            String message = scan.nextLine();
            this.out.println(message);
        }
    }
    public void listenforServerMessage(){
        //Thread created to check for server outputs
        new Thread(new Runnable() {
            @Override
            public void run(){
                //When the server sends a message it is recieved and outputted to the client
                String msgfromServer;
                while(socket.isConnected()) {
                    try {
                        msgfromServer = in.readLine();
                        System.out.println(msgfromServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*
    Sets server address
    Sets server socket
    Sets client
    Begins checking for messages from the server using listenforServerMessage()
    Begins allowing the user to send messages using sendMessagetoServer()
    */
    public static void main(String[] args) throws UnknownHostException, IOException {
        String SERVER_ADDRESS = "127.0.0.1"; 
        int SERVER_PORT = 3000;
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        Client client = new Client(socket);
        client.listenforServerMessage();
        client.sendMessagetoServer();

    }
}

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 3000; //Port used by the server, this is the port that the Client needs to access
    private static HashMap<String, String> usernames = new HashMap<String, String>(); //A hashmap used for storing usernames and passwords
    private static List<ClientHandler> clients = new ArrayList<>(); //An arraylist used to hold all clients that interact with the server

    public static void main(String[] args) {
        /*
        The basic usernames an passowrds are added to the hashmap
        If another username and password pair were to be adde it would be done in the format:
                        usernames.put("USERNAME", "PASSWORD");
        The server then runs then loops until an error occurs, adding new clients when necessary
        */
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            usernames.put("user1", "pass1");
            usernames.put("user2", "pass2");
            System.out.println("Server started. Listening on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private String password;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket; //Establishes the socket of the given client
            try {
                out = new PrintWriter(this.clientSocket.getOutputStream(), true); //Instantiates an output stream for the client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Instantiates an input stream for the client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private boolean authenticate(){
            try {
                //Checks to see if the client's username is registered in the server
                //If the user is not recognized by the server the client is rejected
                this.out.println("Enter your username:");
                this.username = in.readLine();
                if (this.username == null|| this.username.trim().isEmpty()) {
                    out.println("Invalid username. Connection closed.");
                    clientSocket.close();
                    return false;
                }
                //The user is then asked to verify their passoword
                //And subsequently rejected if their given passowrd is incorrect
                this.out.println("Enter your password:");
                this.password = in.readLine();
                if (this.password == null|| this.password.trim().isEmpty()) {
                    out.println("Invalid username. Connection closed.");
                    clientSocket.close();
                    return false;
                }

                
                String clientPassword = usernames.get(this.username);

                //If either the username or passowrd are incorrect this is where the user is rejected
                if (!clientPassword.equals(this.password)) {
                    out.println("Incorrect username or Password. Connection closed.");
                    clientSocket.close();
                    return false;
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            //If all is done correctly the authenicate() function returns a boolean value of true
            return true;
        }
        @Override
        public void run() {
            try {
                //The authenitcate function is called
                if (authenticate()){
                    //The user is welcomed to the system
                    out.println("Welcome, " + username + "!");
                    String inputLine;
                    //If the user inputs "exit" the program will terminate
                    //Otherwise their message will be processed and sent
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.equalsIgnoreCase("exit")) {
                            break;
                        }
                        System.out.println(username + ": " + inputLine);
                        for (ClientHandler client : clients) {
                            if(!client.username.equals(username)){
                                //System.out.println("Sending Message");
                                client.out.println(username + ": " + inputLine);
                            }
                        }
                        //System.out.println("Message Sent to All");
                    }
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            //In the end the client will be removed from the thread, and the socket will be closed
            } finally {
                try {
                    clients.remove(this);
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
}

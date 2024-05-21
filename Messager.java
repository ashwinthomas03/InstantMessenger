import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Messager {
    private JFrame frame;
    private JTextField inputText;
    private JTextArea chatTextArea;

    private File selectedFile;

    //The main function used to build the GUI
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Messager window = new Messager();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Messager() {
        initialize();
    }

    private void initialize() {
        /*
        Sets the bounds for the frame
        Sets the title fo the frame
        Sets the operation for closing the frame
        */
        frame = new JFrame();
        frame.setBounds(100, 100, 493, 441);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("Chat Messenger");

        //Creates the send button using the addActionListener
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        //Sets the size of the send button
        sendButton.setBounds(411, 376, 76, 29);
        frame.getContentPane().add(sendButton);

        //Creates the area for sending messages
        inputText = new JTextField();
        inputText.setBounds(18, 375, 358, 29);
        frame.getContentPane().add(inputText);
        inputText.setColumns(10);

        //Creates the area for recieving messages
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setBounds(19, 18, 454, 347);
        frame.getContentPane().add(chatTextArea);

        //Creates the button for uploading a file
        //Uses a fileChooser to allow the selection of a file to send
        JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    chatTextArea.append("File Selected: " + selectedFile.getName() + "\n");
                }
            }
        });
        uploadButton.setBounds(388, 376, 23, 29);
        frame.getContentPane().add(uploadButton);
    }

   //Adds the text formatting for the GUI for sending and recieving messages/files
    private void sendMessage() {
        String message = inputText.getText();
        if (selectedFile != null) {
            chatTextArea.append("You: " + message + "\n");
            chatTextArea.append("File Sent: " + selectedFile.getName() + "\n");
            sendFile(selectedFile);
            selectedFile = null; 
        } else {
            chatTextArea.append("You: " + message + "\n");
        }
        inputText.setText(""); 
    }

    //Uses a fileInputStream to allow the input of a file to a subsequent output
    private void sendFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            // sendfiles code by Pranaav
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

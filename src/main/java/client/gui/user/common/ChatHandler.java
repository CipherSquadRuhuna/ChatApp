package client.gui.user.common;

import models.ChatMessage;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChatHandler implements Runnable {
    JTextPane chatArea;
    JLabel userMessageLabel;

    public ChatHandler(JTextPane chatArea, JLabel userMessageLabel) {
        this.chatArea = chatArea;
        this.userMessageLabel = userMessageLabel;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 3001);
            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                // we have message object now
                ChatMessage message = (ChatMessage) in.readObject();

                String userLine = message.getUser().getNickName() + ": ";
                String messageLine = message.getMessage() + "\n";
                ImageIcon imageIcon = new ImageIcon(message.getUser().getProfilePicturePath());
                Image image = imageIcon.getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH); // scale height to 20px
                ImageIcon scaledIcon = new ImageIcon(image);

                try {
                    StyledDocument doc;
                    doc = chatArea.getStyledDocument();

                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet boldAttr = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Bold, Boolean.TRUE);

                    // Create a style for the image
                    Style imageStyle = chatArea.addStyle("ImageStyle", null);
                    StyleConstants.setIcon(imageStyle, scaledIcon);

                    doc.insertString(doc.getLength(), "  ", imageStyle); // two spaces as placeholder for image

                    // Apply bold to nickname
                    doc.insertString(doc.getLength(), userLine, boldAttr);

                    // Normal style for message
                    doc.insertString(doc.getLength(), messageLine, SimpleAttributeSet.EMPTY);

                    userMessageLabel.setText("New message received");
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            userMessageLabel.setText("Connection error");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

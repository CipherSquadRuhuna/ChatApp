package client.gui.utility;

import models.Chat;
import models.ChatMessage;
import models.UserChat;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class ChatUtility extends JFrame {
    private final JTextPane chatArea;
    private final StyledDocument doc;

    private final SimpleAttributeSet boldAttr = new SimpleAttributeSet();
    public final SimpleAttributeSet infoMessageAttr = new SimpleAttributeSet();


    public ChatUtility(JTextPane chatArea) {
        this.chatArea = chatArea;
        doc = chatArea.getStyledDocument();

        // set attributes
        StyleConstants.setBold(boldAttr, true);
        StyleConstants.setAlignment(infoMessageAttr, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(infoMessageAttr, Color.BLUE);
    }

    /**
     * display message in chat area
     */
    public void printMessage(ChatMessage message) {
        String userLine = message.getUser().getNickName() + ": ";
        String messageLine = message.getMessage() + "\n";
        try {
            if (message.getUser().getProfilePicturePath() != null) {
                ImageIcon imageIcon = new ImageIcon(message.getUser().getProfilePicturePath());
                Image image = imageIcon.getImage().getScaledInstance(20, -1, Image.SCALE_SMOOTH); // scale height to 20px
                ImageIcon scaledIcon = new ImageIcon(image);

                // Create a style for the image
                Style imageStyle = chatArea.addStyle("ImageStyle", null);
                StyleConstants.setIcon(imageStyle, scaledIcon);
                doc.insertString(doc.getLength(), "  ", imageStyle); // two spaces as placeholder for image
            }

            // Apply bold to nickname
            doc.insertString(doc.getLength(), userLine, boldAttr);

            // Normal style for message
            doc.insertString(doc.getLength(), messageLine, SimpleAttributeSet.EMPTY);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * display subscriber join message for given subscribers
     */
    public void showSubscriberJoinMessage(List<UserChat> subscribers) {
        try {

            for (UserChat subscriber : subscribers) {
                String userJoinTextLine = subscriber.getUser().getNickName() + " has joined " + subscriber.getSubscribedAt() + " \n";
                doc.insertString(doc.getLength(), userJoinTextLine, infoMessageAttr);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * display chat started time in given chat in blue color
     */
    public void displayChatStartedMessage(Chat chat) {
        try {
            String startTimeText = "Chat started at: " + chat.getStartTime() + "\n";
            doc.insertString(doc.getLength(), startTimeText, infoMessageAttr);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * display subscriber join message
     */
    public void displaySubscriberJoinMessage(UserChat subscriber) throws BadLocationException {
        String userJoinTextLine = subscriber.getUser().getNickName() + "has joined " + subscriber.getSubscribedAt() + " \n";
        doc.insertString(doc.getLength(), userJoinTextLine, infoMessageAttr);
    }

}

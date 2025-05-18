package client.gui.utility;

import models.Chat;
import models.ChatMessage;
import models.UserChat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatUtility extends JFrame {
    public final SimpleAttributeSet infoMessageAttr = new SimpleAttributeSet();
    private final JPanel chatArea;

    private final SimpleAttributeSet boldAttr = new SimpleAttributeSet();


    public ChatUtility(JPanel chatArea) {
        this.chatArea = chatArea;
    }

    /**
     * Convert the image to rounded one
     * @param imagePath
     * @param diameter
     * @return ImageIcon
     */
    public static ImageIcon makeRoundedImage(String imagePath, int diameter) {
        try {
            BufferedImage original;
            File file = new File(imagePath);

            if (file.exists()) {
                original = ImageIO.read(file);
            } else {
                original = ImageIO.read(ChatUtility.class.getResourceAsStream(imagePath));
            }

            BufferedImage rounded = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = rounded.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Clip to round shape
            g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
            g2.drawImage(original.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH), 0, 0, null);
            g2.dispose();

            return new ImageIcon(rounded);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * display message in chat area
     */
    public void displayUserMessage(ChatMessage message) {

        Instant sendTime = message.getSentAt();
        ZoneId zoneId = ZoneId.systemDefault();
        String formattedTime = sendTime.atZone(zoneId).format(DateTimeFormatter.ofPattern("hh:mm a"));

        try {
            // Add profile picture if available
            String profileURL = message.getUser().getProfilePicturePath();
            if (message.getUser().getProfilePicturePath() == null) {
                profileURL = "/no-profile.png";
            }

            ImageIcon imageIcon = makeRoundedImage(profileURL, 40);

            ChatBubble bubble = new ChatBubble(message.getUser().getNickName(), message.getMessage(), imageIcon, formattedTime);


            JPanel bubbleWrapper = new JPanel();
            bubbleWrapper.setLayout(new BorderLayout());
            bubbleWrapper.setOpaque(false); // Let background of chatArea show

            //  Add horizontal margin via empty border
            bubbleWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Left & right padding

            // Add bubble into the wrapper
            bubbleWrapper.add(bubble, BorderLayout.CENTER);

            chatArea.add(bubbleWrapper);
            chatArea.add(Box.createVerticalStrut(20));

            chatArea.revalidate();
            chatArea.repaint();

            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = ((JScrollPane) chatArea.getParent().getParent()).getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display custom info message
     */
    public void displayInfoMessage(String message) {
        try {
//            doc.insertString(doc.getLength(), message, boldAttr);
            JLabel label = new JLabel(message);
            chatArea.add(label);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * display subscriber join message for given subscribers
     */
    public void showSubscriberJoinMessage(List<UserChat> subscribers) {
        try {

            for (UserChat subscriber : subscribers) {
                String userJoinTextLine = subscriber.getUser().getNickName() + " has joined " + subscriber.getSubscribedAt() + " \n";

//                doc.insertString(doc.getLength(), userJoinTextLine, infoMessageAttr);
                JLabel label = new JLabel(userJoinTextLine);
                chatArea.add(label);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Format time date to readable
     */

    private String formatDateTimetoReadable(Instant instant) {
        ZoneId zoneId = ZoneId.systemDefault();
        String formatedDateTime = instant.atZone(zoneId).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
        return formatedDateTime;
    }

    /**
     * display chat started time in given chat in blue color
     */
    public void displayChatStartedMessage(Chat chat) {
        try {
            String startTimeText = "Chat started at: " + formatDateTimetoReadable(chat.getStartTime()) + "\n";
            JLabel startTimeLabel = new JLabel(startTimeText);
            chatArea.add(startTimeLabel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * display subscriber join message
     */
    public void displaySubscriberJoinMessage(UserChat subscriber) throws BadLocationException {
        String userJoinTextLine = subscriber.getUser().getNickName() + " has joined " + subscriber.getSubscribedAt() + " \n";
        JLabel label = new JLabel(userJoinTextLine);
        chatArea.add(label);
    }

}

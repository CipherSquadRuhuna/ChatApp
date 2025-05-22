package client.gui.utility;

import javax.swing.*;
import java.awt.*;

public class ChatBubble extends JPanel {
    public ChatBubble(String username, String message, ImageIcon profilePic, String time) {
        setLayout(new BorderLayout());
        setOpaque(false); // We will paint background manually

        // Profile pic
        JLabel picLabel = new JLabel(profilePic);
        picLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel bubbleContent = new JPanel();
        bubbleContent.setLayout(new BoxLayout(bubbleContent, BoxLayout.Y_AXIS));
        bubbleContent.setOpaque(false); // So bubble background can show

        // Label for username + time
        JLabel headerLabel = new JLabel(username + " :" + time );
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        headerLabel.setForeground(new Color(25, 118, 210)); // Nice blue

// Label for the message body
        JLabel messageLabel = new JLabel("<html>" + message.replaceAll("\n", "<br>") + "</html>");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.DARK_GRAY);

// Add padding/margin if needed
        messageLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0)); // space between name and message

// Add both to the bubble content
        bubbleContent.add(headerLabel);
        bubbleContent.add(messageLabel);

        add(picLabel, BorderLayout.WEST);
        add(bubbleContent, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint rounded bubble background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(230, 240, 255)); // light blue bubble
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}

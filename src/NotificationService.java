
import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class NotificationService {
    private SystemTray tray;
    private TrayIcon trayIcon;

    public NotificationService() {
        if (SystemTray.isSupported()) {
            this.tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png"); // We'll just use a default or empty one for now as we might not have an asset
            // Since we don't have a real icon file yet, this might fail to render a nice icon, 
            // but the text notification is the key. 
            // In a real app, load a valid image resource.
            // For now, let's try to creating a simple buffered image or just be resilient if image fails.
            
            // Re-using a simple method to get an image to avoid null pointer on icon creation
            // if we really wanted to be fancy. For now, simple standard usage.
            this.trayIcon = new TrayIcon(new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB), "PNR Tracker");
            this.trayIcon.setImageAutoSize(true);
            this.trayIcon.setToolTip("PNR Status Tracker Running");
            
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        }
    }

    public void notifyUser(String pnr, String newStatus) {
        String message = "PNR " + pnr + " Updated!\nNew Status: " + newStatus;
        System.out.println("\n[NOTIFICATION] " + message); // Console backup
        
        if (trayIcon != null) {
            trayIcon.displayMessage("PNR Update", message, MessageType.INFO);
        }
    }
}

package sturesy.core.ui;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Assists in displaying multiple notifications.
 * Created by henrik on 6/30/14.
 */
public class NotificationService implements NotificationExpirationListener {

    // List of notifications
    private List<NotificationWindow> notifications;

    private GraphicsDevice screen;
    private NotificationWindow.Position position = NotificationWindow.Position.TOP_RIGHT;

    private static NotificationService instance = new NotificationService();

    public static NotificationService getInstance() {
        return instance;
    }

    public NotificationService() {
        notifications = new LinkedList<>();
    }

    /**
     * Called on expiration of a notification
     *
     * @param notification expired notification
     */
    @Override
    public void onExpiration(NotificationWindow notification) {
        if (notifications.contains(notification))
            notifications.remove(notification);
    }

    public void reset() {
        // decouple from window
        for(NotificationWindow w : notifications)
            w.setExpirationListener(null);

        notifications.clear();
    }

    /**
     * Set screen according to number
     * @param number screen number
     */
    public void setScreen(int number) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevices = ge.getScreenDevices();

        if(number >= 0 && number < screenDevices.length)
            screen = screenDevices[number];
        else
            screen = ge.getDefaultScreenDevice();
    }

    /**
     * Sets the position according to the settings string
     * @param position position in string form
     */
    public void setPosition(String position) {
        switch (position) {
            case "Top Left":
                this.position = NotificationWindow.Position.TOP_LEFT;
                break;
            case "Top Right":
                this.position = NotificationWindow.Position.TOP_RIGHT;
                break;
            case "Bottom Left":
                this.position = NotificationWindow.Position.BOTTOM_LEFT;
                break;
            case "Bottom Right":
                this.position = NotificationWindow.Position.BOTTOM_RIGHT;
                break;
            default:
                this.position = NotificationWindow.Position.TOP_RIGHT;
        }
    }


    public NotificationWindow.Position getPosition() {
        return position;
    }

    public void setPosition(NotificationWindow.Position position) {
        this.position = position;
    }

    public GraphicsDevice getScreen() {
        return screen;
    }

    public void setScreen(GraphicsDevice screen) {
        this.screen = screen;
    }

    /**
     * Displays a new notification
     * @param title Notification title
     * @param message Notification message
     * @param duration Duration of visibility
     */
    public synchronized void addNotification(String title, String message, int duration) {
        NotificationWindow w = new NotificationWindow(title, message);
        w.setExpirationListener(this);

        if(notifications.size() > 0) {
            NotificationWindow previousWindow = notifications.get(notifications.size()-1);
            NotificationWindow.Position p = previousWindow.getPosition();
            w.setPosition(p);

            int x = previousWindow.getX();
            int y = previousWindow.getY();

            if(p == NotificationWindow.Position.TOP_LEFT || p == NotificationWindow.Position.TOP_RIGHT)
                y += previousWindow.getHeight() + 2;
            else
                y -= w.getHeight() + 2;

            w.setLocation(x, y);
        }
        else {
            w.setScreen(screen);
            w.setPosition(position);
        }

        w.display(duration);
        notifications.add(w);
    }
}

package sturesy.core.ui;

import java.util.LinkedList;
import java.util.List;

/**
 * Assists in displaying multiple notifications.
 * Created by henrik on 6/30/14.
 */
public class NotificationService implements NotificationExpirationListener {

    // List of
    private List<NotificationWindow> notifications;

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

    public synchronized void addNotification(String title, String message, int duration) {
        NotificationWindow w = new NotificationWindow(title, message);
        w.setExpirationListener(this);

        if(notifications.size() > 0) {
            NotificationWindow previousWindow = notifications.get(notifications.size()-1);
            NotificationWindow.Position p = previousWindow.getPosition();
            int x = previousWindow.getX();
            int y = previousWindow.getY();

            if(p == NotificationWindow.Position.TOP_LEFT || p == NotificationWindow.Position.TOP_RIGHT)
                y += previousWindow.getHeight() + 2;
            else
                y -= w.getHeight() + 2;

            w.setLocation(x, y);
        }

        w.display(duration);
        notifications.add(w);
    }
}

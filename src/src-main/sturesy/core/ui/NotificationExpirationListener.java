package sturesy.core.ui;

/**
 * Listener that is called when a notification expires.
 * Created by henrik on 6/30/14.
 */
public interface NotificationExpirationListener {
    /**
     * Called on expiration of a notification
     * @param notification expired notification
     */
    public void onExpiration(NotificationWindow notification);
}

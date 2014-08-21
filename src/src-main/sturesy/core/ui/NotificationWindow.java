package sturesy.core.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * The window displaying a notification
 *
 * Created by henrik on 6/30/14.
 */
public class NotificationWindow extends JFrame {
    private Position position;

    public enum Position {
        TOP_LEFT, TOP_RIGHT,
        BOTTOM_LEFT, BOTTOM_RIGHT
    }

    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 80;
    private static final Position DEFAULT_POS = Position.TOP_RIGHT;

    private GraphicsDevice defaultScreen;
    private boolean translucencySupported = true, shapesSupported = true;
    private NotificationExpirationListener expirationListener;

    public NotificationWindow(String title, String message)
    {
        super(title);

        setFocusableWindowState(false);
        setDefaultLookAndFeelDecorated(false);
        setUndecorated(true);
        setAlwaysOnTop(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (shapesSupported)
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        });

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        defaultScreen = ge.getDefaultScreenDevice();

        if(!defaultScreen.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
            translucencySupported = false;
            System.err.println("Warning: Window translucency is not supported.");
        }
        if(!defaultScreen.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT)) {
            shapesSupported = false;
            System.err.println("Warning: Rounded Windows are not supported on this platform.");
        }

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();

        JLabel titleLabel = new JLabel(title);
        Font titleFont = titleLabel.getFont();
        titleLabel.setFont(titleFont.deriveFont(titleFont.getStyle() | Font.BOLD));

        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JPanel centerPanel = new JPanel();
        centerPanel.add(new JLabel(message));

        add(titlePanel, BorderLayout.PAGE_START);
        add(centerPanel, BorderLayout.CENTER);

        setPosition(DEFAULT_POS);
    }

    /**
     * Positions the notification window in one of the four screen borders.
     * Possible options are defined in the {@link Position} enum.
     *
     * @param position Position of window
     */
    public void setPosition(Position position)
    {
        this.position = position;
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();

        switch (position) {
            case TOP_LEFT:
                setLocation(0, 0);
                break;
            case TOP_RIGHT:
                setLocation((int)rect.getMaxX() - getWidth(), 0);
                break;
            case BOTTOM_LEFT:
                setLocation(0, (int)rect.getMaxY() - getHeight());
                break;
            case BOTTOM_RIGHT:
                setLocation((int)rect.getMaxX() - getWidth(), (int)rect.getMaxY() - getHeight());
        }
    }

    /**
     * Displays the notification window for a given amount of time
     * If <code>duration</code> is set to 0, the window will not be closed automatically.
     *
     * @param duration Duration of window visibility in seconds
     */
    public void display(int duration)
    {
        if(translucencySupported)
            setOpacity(0.8f);

        setVisible(true);

        if(duration > 0) {
            Timer timer = new Timer(duration*1000, e -> {
                // notify NotificationService
                if(expirationListener != null)
                    expirationListener.onExpiration(NotificationWindow.this);

                setVisible(false);
                dispose();
            });
            timer.start();
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setExpirationListener(NotificationExpirationListener expirationListener) {
        this.expirationListener = expirationListener;
    }
}

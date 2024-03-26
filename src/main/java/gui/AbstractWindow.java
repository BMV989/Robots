package gui;

import java.beans.PropertyVetoException;
import java.util.prefs.Preferences;
import javax.swing.JInternalFrame;

public abstract class AbstractWindow extends JInternalFrame {
    private static final String prefixWindowPreferences = formatTitle("window preferences");
    private static final String prefixWindowPositionX = formatTitle("position x");
    private static final String prefixWindowPositionY = formatTitle("position y");
    private static final String prefixWindowSizeWidth = formatTitle("size width");
    private static final String prefixWindowSizeHeight = formatTitle("size height");
    private static final String prefixWindowIsMinimised = formatTitle("is minimised");
    private final JInternalFrame frame = this;

    public AbstractWindow(String string, boolean b, boolean b1, boolean b2, boolean b3) {
        super(string, b, b1, b2, b3);
    }


    private static Preferences getPreferences() {
        return Preferences.userRoot().node(prefixWindowPreferences);
    }
    private static String formatTitle(String title) {
        String cased = title.toUpperCase();

        return cased.replaceAll(" +", "_");
    }

    public void saveWindow() {
        Preferences preferences = getPreferences();

        String title = formatTitle(frame.getTitle());

        preferences.putInt(prefixWindowPositionX + title, frame.getX());
        preferences.putInt(prefixWindowPositionY + title, frame.getY());
        preferences.putInt(prefixWindowSizeWidth + title, frame.getWidth());
        preferences.putInt(prefixWindowSizeHeight + title, frame.getHeight());
        preferences.putBoolean(prefixWindowIsMinimised + title, frame.isIcon());
    }

    public  void restoreWindow() {
        Preferences preferences = getPreferences();
        final int missing = -1;

        String title = formatTitle(frame.getTitle());

        int x = preferences.getInt(prefixWindowPositionX + title, missing);
        int y = preferences.getInt(prefixWindowPositionY + title, missing);
        int width = preferences.getInt(prefixWindowSizeWidth + title, missing);
        int height = preferences.getInt(prefixWindowSizeHeight + title, missing);
        boolean isMinimised = preferences.getBoolean(prefixWindowIsMinimised + title, false);

        if (x == -1 || y == -1 || width == -1 || height == -1)
            return;

        frame.setBounds(x, y, width, height);
        try {
            frame.setIcon(isMinimised);
        } catch (PropertyVetoException e) {
            e.printStackTrace(System.out);
        }
    }
}

package config;

import java.util.prefs.Preferences;
import javax.swing.JInternalFrame;
public class PreferenceHandler {
    private static final String prefixWindowPreferences = formatTitle("window preferences");
    private static final String prefixWindowPositionX = formatTitle("position x");
    private static final String prefixWindowPositionY = formatTitle("position y");
    private static final String prefixWindowSizeWidth = formatTitle("size width");
    private static final String prefixWindowSizeHeight = formatTitle("size height");


    private static Preferences getPreferences() {
        return Preferences.userRoot().node(prefixWindowPreferences);
    }
    private static String formatTitle(String title) {
        String cased = title.toUpperCase();

        return cased.replaceAll(" +", "_");
    }

    public static void saveWindow(JInternalFrame frame) {
        Preferences preferences = getPreferences();

        String title = formatTitle(frame.getTitle());

        preferences.putInt(prefixWindowPositionX + title, frame.getX());
        preferences.putInt(prefixWindowPositionY + title, frame.getY());
        preferences.putInt(prefixWindowSizeWidth + title, frame.getWidth());
        preferences.putInt(prefixWindowSizeHeight + title, frame.getHeight());
    }

    public  static void restoreWindow(JInternalFrame frame) {
        Preferences preferences = getPreferences();
        final int missing = -1;

        String title = formatTitle(frame.getTitle());

        int x = preferences.getInt(prefixWindowPositionX + title, missing);
        int y = preferences.getInt(prefixWindowPositionY + title, missing);
        int width = preferences.getInt(prefixWindowSizeWidth + title, missing);
        int height = preferences.getInt(prefixWindowSizeHeight + title, missing);

        if (x == -1 || y == -1 || width == -1 || height == -1)
            return;

        frame.setBounds(x, y, width, height);
    }
}
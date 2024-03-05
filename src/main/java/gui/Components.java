package gui;

import java.util.Locale;

public class Components {
    public static void translateComponents(Locale locale) {
        if (locale.equals(new Locale("ru", "RU"))) {
            RuLocalization.setup();
        }
    }
}

package gui;

import java.util.Locale;
import localizations.RuLocalization;

public class Components {
    public static void translateComponents(Locale locale) {
        if (locale.equals(new Locale("ru", "RU"))) {
            RuLocalization.setup();
        }
    }
}

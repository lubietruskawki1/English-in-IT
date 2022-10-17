package com.technetapps.english_in_it;

import com.technetapps.english_in_it.R;

/**
 * Class provides method getTheme used for setting a theme.
 */

public class Utils {
    public static int getTheme(String theme) {
        switch (theme) {
            case "LightTheme":
                return R.style.LightTheme;
            case "DarkTheme":
                return R.style.DarkTheme;
        }
        return 0;
    }
}

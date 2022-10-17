package com.example.english_in_it;

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

package com.craftelix.weatherviewer.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsernameUtils {

    public static String normalize(String username) {
        if (username == null) {
            return null;
        }
        return username.trim().toLowerCase();
    }
}

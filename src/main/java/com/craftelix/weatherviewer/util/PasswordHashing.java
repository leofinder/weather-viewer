package com.craftelix.weatherviewer.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordHashing {

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean checkPassword(String password, String bcryptHashString) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        return result.verified;
    }

}

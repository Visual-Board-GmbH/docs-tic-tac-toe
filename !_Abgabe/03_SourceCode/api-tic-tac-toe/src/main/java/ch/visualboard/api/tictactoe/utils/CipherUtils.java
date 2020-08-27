package ch.visualboard.api.tictactoe.utils;

import java.security.SecureRandom;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component("f55aacd8-8b5f-4a02-b56a-a22fdf5997bd")
public class CipherUtils
{
    public static String cryptPasswordWithSHA256(String rawPassword, String salt)
    {
        String cryptedPasswordLevel1 = DigestUtils.sha256Hex(salt + rawPassword);
        return DigestUtils.sha256Hex(cryptedPasswordLevel1 + salt);
    }

    public static String generateRandomToken()
    {
        SecureRandom rnd = new SecureRandom();
        String value = String.valueOf(rnd.nextLong());
        return DigestUtils.sha1Hex(value);
    }
}

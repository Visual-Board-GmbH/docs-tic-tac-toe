package ch.visualboard.api.tictactoe.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CipherUtilsTest
{
    @Test
    void cryptPasswordCase1()
    {
        String cryptedPasswordLevel1 = CipherUtils.cryptPasswordWithSHA256("Passwort", "Salt");
        String cryptedPasswordLevel2 = CipherUtils.cryptPasswordWithSHA256(cryptedPasswordLevel1, "Salt");

        assertEquals("536ceeadb9dd2b68283b227cdfa1b69919ffba866467d26c90e00801772582f1", cryptedPasswordLevel2);
    }

    @Test
    void generateRandomToken()
    {
        String randomToken = CipherUtils.generateRandomToken();

        Assertions.assertTrue(randomToken.length() > 20);
        Assertions.assertFalse(CipherUtils.generateRandomToken().isEmpty());
    }
}

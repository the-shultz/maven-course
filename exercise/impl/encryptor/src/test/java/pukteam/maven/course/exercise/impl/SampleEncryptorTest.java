package pukteam.maven.course.exercise.impl;

import com.sun.tools.internal.xjc.model.nav.EagerNClass;
import org.junit.Assert;
import org.junit.Test;
import pukteam.maven.course.exercise.provided.api.encrypt.EncryptorMethod;

public class SampleEncryptorTest {

    private final String PLAIN_TEXT = "this is a test".toUpperCase();

    @Test
    public void testSampleEncryptorForEnigma() {
        SampleEncryptor encryptor = new SampleEncryptor();
        String encryptedText = encryptor.encrypt(PLAIN_TEXT, EncryptorMethod.ENIGMA);
        String decryptText = encryptor.decrypt(encryptedText, EncryptorMethod.ENIGMA);
        Assert.assertEquals("Error ! original text " + PLAIN_TEXT + " is different than decrypted text: " + decryptText,
                PLAIN_TEXT, decryptText);
    }
}

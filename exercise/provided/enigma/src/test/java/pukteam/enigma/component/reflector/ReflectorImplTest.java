package pukteam.enigma.component.reflector;

import org.junit.Assert;
import org.junit.Test;
import pukteam.enigma.exceptions.input.InvalidReflectorInputException;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class ReflectorImplTest {

    @Test
    public void testReflector() {
        Reflector reflector = EnigmaComponentFactory.INSTANCE.createReflector(1, new byte[]{1,2,3}, new byte[]{4,5,6});
        testReflectorAnswers(reflector, new byte[] {0,1,2,3,4,5}, new byte [] {3,4,5,0,1,2});
        testReflectorAnswers(reflector, new byte[] {5,4,3,2,1,0}, new byte [] {2,1,0,5,4,3});
        testReflectorAnswers(reflector, new byte[] {1,4,2,0,3,5}, new byte [] {4,1,5,3,0,2});
    }

    @Test(expected = InvalidReflectorInputException.class)
    public void testInvalidInputReflector() {
        Reflector reflector = EnigmaComponentFactory.INSTANCE.createReflector(1, new byte[]{1,2,3}, new byte[]{4,5,6});
        reflector.reflect((byte) 8);
    }

    private void testReflectorAnswers(Reflector reflector, byte[] inputs, byte[] outputs) {
        for (int i = 0; i < inputs.length; i++) {
            byte output = reflector.reflect(inputs[i]);
            Assert.assertEquals(outputs[i], output);
        }
    }
}

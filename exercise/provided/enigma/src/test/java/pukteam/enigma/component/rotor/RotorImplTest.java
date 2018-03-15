package pukteam.enigma.component.rotor;

import org.junit.Assert;
import org.junit.Test;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class RotorImplTest {

    @Test
    public void testRotorTranslation() {
        Rotor rotor = EnigmaComponentFactory.INSTANCE.createFromString(1, "ABCDE", "EBACD");

        assertTranslations(rotor, new byte[]{3,1,0,4,2}, new byte[] {4,1,2,0,3}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,1,0,2,3}, Direction.BACKWARD);
    }

    @Test
    public void testTranslateWithAdvance() {
        Rotor rotor = EnigmaComponentFactory.INSTANCE.createFromString(1, "ABCDE", "EBACD");

        // pace = 0
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,2,1,3,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,1,0,2,3}, Direction.BACKWARD);

        // pace = 1
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,0,2,3,1}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {0,4,1,2,3}, Direction.BACKWARD);

        // pace = 2
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {0,1,2,3,4}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {3,0,1,2,4}, Direction.BACKWARD);

        // pace = 3
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {3,1,2,4,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,1,3,2}, Direction.BACKWARD);

        // pace = 4
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,1,3,2,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,2,1,3}, Direction.BACKWARD);

        // pace = 5 (% => 0)
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,2,1,3,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,1,0,2,3}, Direction.BACKWARD);

        // pace = 6 (% => 1)
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,0,2,3,1}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {0,4,1,2,3}, Direction.BACKWARD);

        // pace = 7 (% => 2)
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {0,1,2,3,4}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {3,0,1,2,4}, Direction.BACKWARD);

        // pace = 8 (% => 3)
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {3,1,2,4,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,1,3,2}, Direction.BACKWARD);

        // pace = 9 (% => 4)
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,1,3,2,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,2,1,3}, Direction.BACKWARD);

        // pace = 10 (% => 0)
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,2,1,3,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,1,0,2,3}, Direction.BACKWARD);

        // pace = 11 (% => 1)
        rotor.advance();
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,0,2,3,1}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {0,4,1,2,3}, Direction.BACKWARD);
        
    }

    @Test
    public void testNotchFiresWhenExpected() {
        Rotor rotor = EnigmaComponentFactory.INSTANCE.createFromString(1, "ABCDE", "EBACD", 2);
        final boolean[] notchFired = {false};
        rotor.setNextRotor(() -> notchFired[0] = true);

        rotor.advance(); // notch = 0. needs to fire
        Assert.assertTrue(notchFired[0]);

        notchFired[0] = false; // resetToInitialPosition tester
        rotor.advance(); // notch = 4 (back to end of rotor)
        Assert.assertFalse(notchFired[0]);

        rotor.advance(); // notch = 3
        Assert.assertFalse(notchFired[0]);

        rotor.advance(); // notch = 2
        Assert.assertFalse(notchFired[0]);

        rotor.advance(); // notch = 1
        Assert.assertFalse(notchFired[0]);

        rotor.advance(); // notch = 0
        Assert.assertTrue(notchFired[0]);

        notchFired[0] = false; // resetToInitialPosition tester
        rotor.advance(); // notch = 4 (back to end of rotor)
        Assert.assertFalse(notchFired[0]);

        rotor = EnigmaComponentFactory.INSTANCE.createFromString(1, "ABCDE", "EBACD", 1);
        notchFired[0] = false; // resetToInitialPosition tester
        rotor.advance(); // notch = 4 (back to end of rotor)
        Assert.assertFalse(notchFired[0]);
    }

    @Test
    public void testSetRotorToDirectPosition() {
        Rotor rotor = EnigmaComponentFactory.INSTANCE.createFromString(1, "ABCDE", "EBACD", 2);

        // equivalent to pace = 3
        rotor.setTo((byte)4);
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {3,1,2,4,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,1,3,2}, Direction.BACKWARD);

        // equivalent to pace = 0
        rotor.setTo((byte)1);
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,2,1,3,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,1,0,2,3}, Direction.BACKWARD);

        // equivalent to pace = 2
        rotor.setTo((byte)3);
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {0,1,2,3,4}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {3,0,1,2,4}, Direction.BACKWARD);

        // equivalent to pace = 4
        rotor.setTo((byte)5);
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,1,3,2,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,2,1,3}, Direction.BACKWARD);

        // equivalent to pace = 1
        rotor.setTo((byte)2);
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,0,2,3,1}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {0,4,1,2,3}, Direction.BACKWARD);

        // equivalent to pace = 9 (% => 4)
        rotor.setTo((byte)10);
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,1,3,2,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,2,1,3}, Direction.BACKWARD);

    }

    @Test
    public void testSetRotorToDirectPositionByAlphabetLetter() {
        Rotor rotor = EnigmaComponentFactory.INSTANCE.createFromString(1, "ABCDE", "EBACD", 2);

        // equivalent to pace = 3
        rotor.setTo('D');
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {3,1,2,4,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,1,3,2}, Direction.BACKWARD);

        // equivalent to pace = 0
        rotor.setTo('A');
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,2,1,3,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,1,0,2,3}, Direction.BACKWARD);

        // equivalent to pace = 2
        rotor.setTo('C');
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {0,1,2,3,4}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {3,0,1,2,4}, Direction.BACKWARD);

        // equivalent to pace = 4
        rotor.setTo('E');
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,1,3,2,0}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {4,0,2,1,3}, Direction.BACKWARD);

        // equivalent to pace = 1
        rotor.setTo('B');
        assertTranslations(rotor, new byte[]{3,0,1,2,4}, new byte[] {4,0,2,3,1}, Direction.FORWARD);
        assertTranslations(rotor, new byte[]{0,1,2,3,4}, new byte[] {0,4,1,2,3}, Direction.BACKWARD);

    }

    @Test
    public void testNotchPositionAfterDirectPositionSet() {
        Rotor rotor = EnigmaComponentFactory.INSTANCE.createFromString(1, "ABCDE", "EBACD", 3);
        final boolean[] notchFired = {false};
        rotor.setNextRotor(() -> notchFired[0] = true);

        // before setting position manually, verify proper behaviour of the notch for a full cycle
        rotor.advance(); // notch = 2
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 1 -> fires
        Assert.assertTrue(notchFired[0]);
        notchFired[0] = false;
        rotor.advance(); // notch = 5
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 4
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 3
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 2
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 1 -> fires
        Assert.assertTrue(notchFired[0]);
        notchFired[0] = false;
        
        rotor.setTo((byte)4); // notch at 5
        rotor.advance(); // notch = 4
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 3
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 2
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 1 -> fires
        Assert.assertTrue(notchFired[0]);
        notchFired[0] = false;

        rotor.setTo((byte)1); // notch at 3 (original position)
        rotor.advance(); // notch = 2
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 1 -> fires
        Assert.assertTrue(notchFired[0]);
        notchFired[0] = false;

        rotor.setTo((byte)10); // notch at 4 (= 3 - (9%5) + 5)
        rotor.advance(); // notch = 3
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 2
        Assert.assertFalse(notchFired[0]);
        rotor.advance(); // notch = 1 -> fires
        Assert.assertTrue(notchFired[0]);


    }

    private void assertTranslations(Rotor rotor, byte incoming[], byte expected[], Direction direction) {
        for (int i=0; i<incoming.length; i++) {
            Assert.assertEquals("Failed on translation of " + incoming[i], expected[i], rotor.translate(incoming[i], direction));
        }

    }
}

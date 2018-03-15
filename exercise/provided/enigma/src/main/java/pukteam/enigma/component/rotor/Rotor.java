package pukteam.enigma.component.rotor;

/**
 * This class represents a single rotor in the enigma machine.
 * The rotor holds a mapping (wiring) between 'input's and 'output's, given and treated in the form of pairs of numbers
 * 'input' and 'output' are arbitrary terms since the 'wiring' is bi-directional and is managed through the
 * enum Direction.FORWARD (from input to output) or by Direction.BACKWARD (from output to input)
 *
 * the rotor as a 'size' which is actually the size of it's input\output wiring. the rotor's size must match the machine's Alphabet length
 * (where it is due to serve it's duty). the input and output wiring must both not to have duplicate characters within them, and they
 * have to have a 1:1 relationship both between the input and output, and both between the machine alphabet (that is every char in the alphabet
 * must exists exactly once in both the input and output wiring (the order doesn't matter), and the length of all of them must be equal)
 *
 * when initially created the rotor is said to be in it's initial position, and the wiring are the original as given,
 * but rotors can also 'pace' steps (simulating they actually rotating), which makes the wiring to be matched to different
 * inputs and by that produce different output
 * for example, if a rotor's wiring is given with the below mapping (I = Input wiring ; O = Output wiring)
 *          I O
 *      0   A D
 *      1   B A
 *      2   C C
 *      3   D E
 *      4   E B
 * the numbers 0-4 are the actual inputs arrives to the rotor. they are fixed in their position and cannot be changed.
 * When an input arrives at number 3, it get's the D character, which it's wiring corresponds to the first (0) output (denoting a mapping of 3-0)
 * after this rotor will pace one step forward, the wiring comparing the input scheme will look like that:
 *      0   B A
 *      1   C C
 *      2   D E
 *      3   E B
 *      4   A D
 * so now the very same input 3, falls on E character, which the wiring (after the rotor moved one step a'head) is now setting it to be 2
 *
 * The first input (0) can also be described as a window slot, as if it's a tiny window enables the user to see what is the status\position of the rotor currently.
 *
 * Each rotor also has a notch, that can be located at any of the inputs available. as function of the pace the rotor advances, and the
 * notch location gets to be nearer and nearer to the window slot (0).
 * when the notch reaches the window slot, it triggers a callback that on the enigma machine uses to actually advance the next rotor in line (if there is such) by one step
 * this in turn will change the next rotor's wiring and it's notch position on it's own, which eventually trigger the next rotor advancing etc.
 *
 * the window slot is also used to set the rotor to a certain position manually, directly. setting the rotor directly can be done explicitly by selecting
 * a certain position for it, or implicitly by selecting a character from it input wiring, and it's location on the original input is the actual numeric
 * position to which this rotor will be advances
 * for example, if a rotor's wiring is given with the below mapping:
 *      0   A D
 *      1   B A
 *      2   C C
 *      3   D E
 *      4   E B
 * then setting it explicitly to position 3 will shift the rotor (illustration) to this position:
 *      0   C C
 *      1   D E
 *      2   E B
 *      3   A D
 *      4   B A
 * Note that:
 * 1. the position is given in 1'based manner, so position 3 actually means the 3rd slot, which is equivalent to entry 2 in 0'based manner
 * 2. this explicit positioning is equivalent for the implicit positioning by using the character C (C is the 3rd character on the input wiring string)
 * 3. setting the position manually to 3 is equivalent to perform 3 advances (rotating it 3 times), but NO NOTCH-FIRING is taken while doing manual positioning
 *    (notch-triggering is done ONLY when performing standard 'advance' of the rotor)
 */
public interface Rotor extends AdvanceSelf {

    /**
     * gets the id of the current rotor
     * @return the id of the current rotor
     */
    int getId();

    /**
     * gets the rotor size, which is the total amount of inputs (or outputs) this rotor is wired to
     * @return rotor size
     */
    byte getRotorSize();

    /**
     * translates a certain input according to the wiring set in this rotor.
     * @param input byte the input entrance to the rotor
     * @param direction states weather to use the FORWARD or BACKWARD wiring of the rotor
     * @return byte the output wired to the given input (according to the specified direction)
     */
    byte translate(byte input, Direction direction);

    /**
     * sets the next rotor after this one.
     * The next rotor, if exist will be notified when the current notch of this instance reaches it's initial position and needs to trigger the next rotor in line
     * @param nextRotor
     */
    void setNextRotor(AdvanceSelf nextRotor);

    /**
     * creates a clone of this rotor and returns a newly distinguished instance of this rotor.
     * @return
     */
    Rotor createClone();

    /**
     * Stes this rotor position to move to a certain position.
     * after this action, the notch position will be changed as well, but it WON'T trigger the next rotor ln line (if exists) to advance
     * the given value is expected to be given in 1'based mode (that is  0 < position <= rotor size
     * @param position byte the requested position
     */
    void setTo(byte position);

    /**
     * Sets this rotor position to move to a certain position, denoted by a character
     * the numeric position selected for this character is determined by the index of this character in the input wiring string of this rotor
     * if the character not found in the input wiring string throws InvalidRotorInputException exception
     * the position is determined effectively by the method void setTo(byte position), with all it's side effects
     * @param character char to set the position according to
     * @return the matching numeric input for the given character (1 based)
     */
    byte setTo(char character);

    /**
     * calculates numerical position matching a given character
     * the numeric position selected for this character is determined by the index of this character in the input wiring string of this rotor
     * the returned position is 1'based (0 < returned position <= rotor size).
     * in case not found will return the number 0 (which is not a valid position)
     * @param rotorPosition the requested character to find it's numerical position
     * @return the matching numerical position for the given character. in case character not found, returns 0 (not a valid position)
     */
    int calculatePositionFromChar(char rotorPosition);

    /**
     * calculates the matching character for a given numerical position, in either FORWARD or BACKWARD direction
     * the given positoin is 1'based and can be bigger than the rotor size (it will be modulu'ed)
     * @param position byte the numeric input
     * @param direction direction of wiring to perform the matching according to
     * @return the character matched to the given numerical position, according to the requested direction
     */
    char calculateCharFromPosition(byte position, Direction direction);

    /**
     * gets the wiring string used to construct this rotor
     * @return the wiring string used to construct this rotor
     */
    String getInputWiring();

}

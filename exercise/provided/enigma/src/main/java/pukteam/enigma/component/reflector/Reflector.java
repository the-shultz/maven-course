package pukteam.enigma.component.reflector;

public interface Reflector {

    /**
     * gets the id of this reflector
     * @return the id of this reflector
     */
    int getId();

    /**
     * gets the reflector size.
     * the reflector size must by even and is twice the length of the input\output byte array used to construct this reflector
     * @return the reflector size
     */
    int getReflectorSize();

    /**
     * translates (reflects) a certain input to the corresponding, matching output
     * the input is given in 1'based manner
     * @param input the input to reflect
     * @return byte the output reflected by (matched to) the given input
     */
    byte reflect(byte input);

}

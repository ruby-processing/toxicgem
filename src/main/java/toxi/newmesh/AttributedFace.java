package toxi.newmesh;

import java.util.HashMap;

/**
 *
 * @author tux
 */
public class AttributedFace {

    public int a,

    /**
     *
     */

    /**
     *
     */
    b,

    /**
     *
     */

    /**
     *
     */
    c;

    /**
     *
     */
    public int normal = -1;

    /**
     *
     */
    public HashMap<String, int[]> attribs;

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param attribs
     */
    public AttributedFace(int a, int b, int c, HashMap<String, int[]> attribs) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.attribs = attribs;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("a=%d,b=%d,c=%d,n=%d", a, b, c, normal);
    }
}

package toxi.newmesh;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tux
 */
public class AttributedEdge {

    public final int a,

    /**
     *
     */

    /**
     *
     */
    b;

    /**
     *
     */
    public List<AttributedFace> faces;

    /**
     *
     * @param a
     * @param b
     */
    public AttributedEdge(int a, int b) {
        this.a = a;
        this.b = b;
    }

    /**
     *
     * @param f
     */
    public void addFace(AttributedFace f) {
        if (faces == null) {
            faces = new ArrayList<>(2);
        }
        faces.add(f);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    /**
     *
     * @param obj
     * @return
     */
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AttributedEdge other = (AttributedEdge) obj;
        if (a != other.a) {
            return false;
        }
        return (b != other.b) ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    /**
     *
     * @return
     */
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + a;
        result = prime * result + b;
        return result;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("a=%d, b=%d", a, b);
    }
}

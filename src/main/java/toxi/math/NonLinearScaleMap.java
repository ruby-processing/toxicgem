package toxi.math;

import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author tux
 */
public class NonLinearScaleMap {

    /**
     *
     */
    public class Sample implements Comparable<Sample> {

        public final double x,

        /**
         *
         */
        y;

        /**
         *
         * @param x
         * @param y
         */
        public Sample(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         *
         * @param b
         * @return
         */
        @Override
        public int compareTo(Sample b) {
            return (int) Math.signum(x - b.x);
        }
    }

    private final TreeSet<Sample> samples;

    private double rangeMin = Float.MAX_VALUE;
    private double rangeMax = Float.MIN_VALUE;

    /**
     *
     */
    public NonLinearScaleMap() {
        samples = new TreeSet<>();
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public NonLinearScaleMap addSample(double x, double y) {
        samples.add(new Sample(x, y));
        rangeMin = MathUtils.min(y, rangeMin);
        rangeMax = MathUtils.max(y, rangeMax);
        return this;
    }

    /**
     *
     * @return
     */
    public NavigableSet<Sample> getSamples() {
        return samples;
    }

    /**
     *
     * @param x
     * @return
     */
    public double map(double x) {
        Sample t = new Sample(x, 0);
        SortedSet<Sample> aset = samples.headSet(t);
        SortedSet<Sample> bset = samples.tailSet(t);
        if (aset.isEmpty()) {
            return bset.first().y;
        } else {
            if (bset.isEmpty()) {
                return aset.last().y;
            } else {
                Sample a = aset.last();
                Sample b = bset.first();
                return MathUtils.lerp(a.y, b.y, (x - a.x) / (b.x - a.x));
            }
        }
    }

}

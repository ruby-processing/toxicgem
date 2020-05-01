package toxi.processing;

import toxi.color.ReadonlyTColor;
import toxi.color.ToneMap;
import toxi.geom.Vec3D;

/**
 *
 * @author tux
 */
public class DeltaOrientationMapper implements NormalMapper {

    /**
     *
     */
    protected Vec3D dir;

    /**
     *
     */
    protected ToneMap toneMap;

    /**
     *
     */
    protected double toneScale;

    /**
     *
     * @param dir
     * @param toneMap
     */
    public DeltaOrientationMapper(Vec3D dir, ToneMap toneMap) {
        this.dir = dir;
        setToneMap(toneMap);
    }

    /**
     *
     * @param normal
     * @return
     */
    @Override
    public ReadonlyTColor getRGBForNormal(Vec3D normal) {
        float dot = (float) (dir.dot(normal) * toneScale + toneScale);
        return toneMap.getToneFor(dot);
    }

    /**
     *
     * @param toneMap
     */
    public final void setToneMap(ToneMap toneMap) {
        this.toneMap = toneMap;
        this.toneScale = toneMap.map.getInputMedian();
    }
}

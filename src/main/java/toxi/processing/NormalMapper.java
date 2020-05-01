package toxi.processing;

import toxi.color.ReadonlyTColor;
import toxi.geom.Vec3D;

/**
 *
 * @author tux
 */
public interface NormalMapper {

    /**
     *
     * @param normal
     * @return
     */
    public ReadonlyTColor getRGBForNormal(Vec3D normal);
}

/*
 * This library adds PovRAY export facility to toxiclibscore
 * Copyright (c) 2012 Martin Prout
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * http://creativecommons.org/licenses/LGPL/2.1/
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */
package toxi.processing;

/**
 * Provides some textures
 *
 * @author Martin Prout
 */
public class TextureBuilder {

    static int count = 0;
    static final String DPHONG = "#declare Phong1 = finish{emission 0.1"
            + " phong 0.5 phong_size 10.0}";
    static final String DFINISH5 = "#declare F_Glass5 = "
            + "finish {\n"
            + "\tspecular 0.7\n"
            + "\troughness 0.001\n"
            + "\tambient 0\n"
            + "\tdiffuse 0\n"
            + "\treflection {\n"
            + "\t0.2, 1.0\n"
            + "\tfresnel on\n"
            + "\t}\n"
            + "\tconserve_energy\n"
            + "}";
    static final String DMFINISHE = "#declare F_MetalE  = "
            + "finish {\n"
            + "\tambient 0.1\n"
            + "\tbrilliance 6\n"
            + "\tdiffuse 0.7\n"
            + "\tmetallic\n"
            + "\tspecular 0.80\n"
            + "\troughness 1/120\n"
            + "\treflection 0.8\n"
            + "}";
    

    
/**
 * pink marble
 */    
static final String MARBLE_TEXTURE = "#declare T_Grnt28 = "
+ "texture {\n"
+ "pigment\n"
 + "{marble\n"
 + " turbulence 0.7\n"
 + " color_map\n"
 + "{[0.000, 0.155   color rgbt <0.686, 0.235, 0.282, 0.000>\n"
 + "                 color rgbt <0.686, 0.235, 0.282, 0.000>]\n"
 + " [0.155, 0.328   color rgbt <0.686, 0.235, 0.282, 0.000>\n"
 + "                 color rgbt <0.494, 0.243, 0.294, 0.000>]\n"
 + " [0.328, 0.474   color rgbt <0.494, 0.243, 0.294, 0.000>\n"
 + "                color rgbt <0.769, 0.329, 0.373, 0.000>]\n"
 + " [0.474, 0.647   color rgbt <0.769, 0.329, 0.373, 0.000>\n"
 + "                 color rgbt <0.769, 0.329, 0.373, 0.000>]\n"
 + " [0.647, 0.810   color rgbt <0.769, 0.329, 0.373, 0.000>\n"
 + "                color rgbt <0.686, 0.235, 0.282, 0.000>]\n"
 + " [0.810, 0.922   color rgbt <0.686, 0.235, 0.282, 0.000>\n"
 + "                 color rgbt <0.792, 0.388, 0.427, 0.000>]\n"
 + " [0.922, 1.001   color rgbt <0.792, 0.388, 0.427, 0.000>\n"
 + "                 color rgbt <0.686, 0.235, 0.282, 0.000>]\n"
 + "  }\n"
 + "}\n"
 + "finish {\n"
 + "crand 0.03\n"
 + "}\n"
 + " }\n" ;      
            
    static final String FINISH5 = "finish{ F_Glass5 }";
    static final String MFINISHE = "finish{ F_MetalE }";
    static final String PHONG = "finish{ Phong1 }";
    static final String MIRROR = "finish{ reflection 1}";
    static final String RED_MARBLE = "texture{ T_Grnt28 }";
    /**
     *
     * @return random rainbow color (with transparency)
     */
    static StringBuilder getRandRCTrans() {
        RCTransp[] vals = RCTransp.values();
        StringBuilder rainbowColor = new StringBuilder(100);
        rainbowColor.append("pigment{ ");
        rainbowColor.append(vals[(int) Math.round((Math.random() * 9))].getColor());
        return rainbowColor.append(" }");
    }
    
   

    /**
     *
     * @return transparent rainbow color (sequence)
     */
    static StringBuilder getRCTrans(int modulus) {
        RCTransp[] vals = RCTransp.values();
        StringBuilder rainbowColor = new StringBuilder(100);
        rainbowColor.append("pigment{ ");
        rainbowColor.append(vals[count % modulus].getColor());
        count++;
        return rainbowColor.append(" }");
    }

    /**
     *
     * @return transparent rainbow color declaration
     */
    static StringBuilder declareRCTrans() {
        StringBuilder declare = new StringBuilder(200);
        for (RCTransp rc : RCTransp.values()) {
            declare.append("#declare ");
            declare.append(rc.getColor());
            declare.append('=');
            declare.append(rc.getDeclare());
            declare.append('\n');
        }
        return declare;
    }
    
    
    /**
     *
     * @return pink marble declaration
     */
    static String declareMarble() {
        return MARBLE_TEXTURE;
    }
    /**
     *
     * @return opaque rainbow color (sequence)
     */
    static StringBuilder getRCOpaque(int modulus) {
        RCOpaque[] vals = RCOpaque.values();
        StringBuilder rainbowColor = new StringBuilder(100);
        rainbowColor.append("pigment{ ");
        rainbowColor.append(vals[count % modulus].getColor());
        count++;
        return rainbowColor.append(" }");
    }

    /**
     *
     * @return White
     */
    static String getWhite() {
        return "pigment{ color rgb<1, 1, 1> }";
    }
    
    /**
     *
     * @return Black
     */
    static String getBlack() {
        
        return "pigment{ color rgb <0, 0, 0> }";
    }
    
    /**
     *
     * @return Chrome
     */
    static StringBuilder getChrome() {
        StringBuilder col  = new StringBuilder();
        col.append("texture{\npigment {color rgb <0.95, 0.95, 0.95>}\n");
        col.append(TextureBuilder.MFINISHE);
        return col.append("\n}\n");
    }
    
    /**
     *
     * @return Pink Marble
     */
    static StringBuilder getMarble() {
        StringBuilder col  = new StringBuilder(TextureBuilder.RED_MARBLE);
        return col;
    }

    /**
     *
     * @return TwoTone
     */
    static String getTwoTone() {
        String col;
        if (count % 2 == 0) {
            col = getWhite();
        } else {
            col = "pigment{ color rgbf<0, 0, 0, 0.5> }";
        }
        count++;
        return col;
    }

    /**
     *
     * @return Red
     */
    static String getRed() {
        return "pigment{ color rgb<1, 0, 0> }";
    }

    /**
     *
     * @return opaque rainbow color declaration
     */
    static StringBuilder declareRCOpaque() {
        StringBuilder declare = new StringBuilder(200);
        for (RCOpaque rc : RCOpaque.values()) {
            declare.append("#declare ");
            declare.append(rc.getColor());
            declare.append('=');
            declare.append(rc.getDeclare());
            declare.append('\n');
        }
        return declare;
    }
}

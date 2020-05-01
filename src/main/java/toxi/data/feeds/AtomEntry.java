/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.data.feeds;

import java.util.ArrayList;
import java.util.List;


import javax.xml.datatype.XMLGregorianCalendar;


/**
 *
 * @author tux
 */
public class AtomEntry {

  
    public String title,

    /**
     *
     */

    /**
     *
     */
    id;

    /**
     *
     */
 
    public AtomAuthor author;

    /**
     *
     */
 
    public AtomContent content;

    /**
     *
     */
 
    public XMLGregorianCalendar timePublished;

    /**
     *
     */

    public XMLGregorianCalendar timeUpdated;

    /**
     *
     */
 
    public List<AtomLink> links = new ArrayList<>();

    /**
     * Returns a list of {@link AtomLink}s to enclosed items of the given MIME
     * type.
     * 
     * @param type
     *            MIME type or null to retrieve all enclosures
     * @return list
     */
    public List<AtomLink> getEnclosuresForType(String type) {
        List<AtomLink> enc = null;
        for (AtomLink l : links) {
            if (l.rel.equalsIgnoreCase("enclosure")) {
                if (type == null || l.type.equalsIgnoreCase(type)) {
                    if (enc == null) {
                        enc = new ArrayList<>();
                    }
                    enc.add(l);
                }
            }
        }
        return enc;
    }
}

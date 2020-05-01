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
import java.util.Iterator;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 *
 * @author tux
 */

public class AtomFeed implements Iterable<AtomEntry> {

    /**
     *
     */
    public static final String NS = "http://www.w3.org/2005/Atom";

    /**
     *
     * @param stream
     * @return
     */


    public String id,

    /**
     *
     */

    /**
     *
     */
    title;

    /**
     *
     */
  
    public XMLGregorianCalendar lastUpdated;

    /**
     *
     */
    public ArrayList<AtomLink> links = new ArrayList<>();

    /**
     *
     */

    public ArrayList<AtomEntry> entries = new ArrayList<>();

    /**
     *
     * @return
     */
    @Override
    public Iterator<AtomEntry> iterator() {
        return entries.iterator();
    }
}

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

package toxi.util.events;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author tux
 * @param <T>
 */
public class EventDispatcher<T> implements Iterable<T> {

    /**
     *
     */
    protected List<T> listeners = new LinkedList<>();

    /**
     *
     */
    public EventDispatcher() {
    }

    /**
     *
     * @param listener
     */
    public void addListener(T listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     *
     * @return
     */
    public List<T> getListeners() {
        return listeners;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator<T> iterator() {
        return listeners.iterator();
    }

    /**
     *
     * @param listener
     */
    public void removeListener(T listener) {
        listeners.remove(listener);
    }
}
/*
 * Copyright (c) 2000, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 * Specifically tailored/modified for Negative Harmony Inverter by Patrick Park 
 * aka Vrezerino.
 */

package com.vrezerino.negativeharmonyinverter;

import java.util.List;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.AbstractSpinnerModel;

@SuppressWarnings("serial") // Superclass is not serializable across versions
public class SpinnerListModel extends AbstractSpinnerModel implements Serializable
{
    private List<?> list;
    private int index;

    public int getIndex() {
        return index;
    }
    
    public SpinnerListModel(Object[] values) { // Modified so that one can get next value in list with the down-arrow button instead
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("SpinnerListModel(Object[]) expects non-null non-empty Object[]");
        }
        this.list = Arrays.asList(values);
        Collections.reverse(list); 
        this.index = values.length-1;
    }

    public Object getValue() {
        return list.get(index);
    }

    public void setValue(Object elt) {
        int index = list.indexOf(elt);
        if (index == -1) {
            throw new IllegalArgumentException("invalid sequence element");
        }
        else if (index != this.index) {
            this.index = index;
            fireStateChanged();
        }
    }

    public Object getNextValue() {
        return (index >= (list.size() - 1)) ? list.get(0) : list.get(index + 1); // Mofidied to start going through list over and over
    }

    public Object getPreviousValue() { // Mofidied to start going through list over and over
        return (index <= 0) ? list.get(list.size() -1) : list.get(index - 1); 
    }

    Object findNextMatch(String substring) {
        int max = list.size();

        if (max == 0) {
            return null;
        }
        int counter = index;

        do {
            Object value = list.get(counter);
            String string = value.toString();

            if (string != null && string.startsWith(substring)) {
                return value;
            }
            counter = (counter + 1) % max;
        } while (counter != index);
        return null;
    }
}
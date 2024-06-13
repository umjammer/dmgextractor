/*-
 * Copyright (C) 2006 Erik Larsson
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.dmgextractor;

import java.io.File;
import java.util.Vector;


public class SimpleFileFilter extends javax.swing.filechooser.FileFilter {

    private final Vector<String> extensions;
    private String description;

    public SimpleFileFilter() {
        extensions = new Vector<>();
        description = "";
    }

    public void addExtension(String extension) {
        extensions.add(extension);
    }

    public void setDescription(String idescription) {
        description = idescription;
    }

    public void removeExtension(String iextension) {
        for (int i = 0; i < extensions.size(); i++) {
            if (extensions.get(i).equals(iextension))
                extensions.remove(i);
        }
    }

    @Override
    public boolean accept(File f) {

        if (f.isDirectory()) return true;

        for (String extension : extensions) {
            if (f.getName().endsWith(extension))
                return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

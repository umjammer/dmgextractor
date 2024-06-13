/*-
 * Copyright (C) 2008 Erik Larsson
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
import javax.swing.filechooser.FileFilter;


/**
 * Even simpler file filter as it only allows one extension.<br>
 * Directories are always accepted.
 *
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public class SimplerFileFilter extends FileFilter {

    private final String extension;
    private final String description;

    public SimplerFileFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory())
            return true;
        else if (f.getName().endsWith(extension))
            return true;
        else
            return false;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the extension that this file filter matches.
     *
     * @return the extension that this file filter matches.
     */
    public String getExtension() {
        return extension;
    }
}

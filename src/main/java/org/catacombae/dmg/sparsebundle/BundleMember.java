/*-
 * Copyright (C) 2011-2014 Erik Larsson
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

package org.catacombae.dmg.sparsebundle;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import org.catacombae.io.ReadableRandomAccessStream;
import org.catacombae.io.RuntimeIOException;

import static java.lang.System.getLogger;


/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
abstract class BundleMember {

    private static final Logger logger = getLogger(BundleMember.class.getName());

    // Backing store.
    private final FileAccessor file;
    private final boolean fileLocked;
    protected final ReadableRandomAccessStream stream;

    public BundleMember(FileAccessor file, boolean fileLocked) {
        this.file = file;
        this.fileLocked = fileLocked;
        this.stream = file.createReadableStream();
    }

    public void close() {
        stream.close();

        try {
            if(fileLocked) {
                file.unlock();
            }
        } catch(RuntimeIOException ex) {
            IOException cause = ex.getIOCause();

            if(cause != null) {
                logger.log(Level.ERROR, ex.getMessage(), ex);
            }
            else {
                throw ex;
            }
        }

        try {
            file.close();
        } catch(RuntimeIOException ex) {
            IOException cause = ex.getIOCause();
            if(cause != null) {
                logger.log(Level.ERROR, ex.getMessage(), ex);
            }
            else {
                throw ex;
            }
        }
    }

}

/*-
 * Copyright (C) 2007 Erik Larsson
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

package org.catacombae.dmgextractor.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import static java.lang.System.getLogger;


public class CharByCharReader extends Reader {

    private static final Logger logger = getLogger(CharByCharReader.class.getName());

    private final InputStream is;
    private final Charset cs;
    private final CharsetDecoder cdec;
    private final byte[] tempBuffer;
    private int tempBufferPtr = 0;

//    private CharArrayBuilder cab = new CharArrayBuilder();

    /** The assumption we make here is that a number of bytes define a Unicode character. */
    public CharByCharReader(InputStream is, Charset cs) {
        this.is = is;
        this.cs = cs;
        this.cdec = cs.newDecoder();
        tempBuffer = new byte[(int) Math.ceil(cs.newEncoder().maxBytesPerChar())];
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int curByte;
        int charsRead = 0;

        while (charsRead < len) {
            while (true) {
                curByte = is.read();
                if (curByte >= 0) {
                    tempBuffer[tempBufferPtr++] = (byte) curByte;
                    ByteBuffer bb = ByteBuffer.wrap(tempBuffer, 0, tempBufferPtr);
                    CharBuffer out = CharBuffer.allocate(1);

                    CoderResult res = cdec.decode(bb, out, true);
                    if (!res.isError()) {
                        cbuf[off + charsRead] = out.get(0); // cab.put(out.get(0));
                        break;
                    } else if (tempBufferPtr == tempBuffer.length) {
                        logger.log(Level.DEBUG, res);
                        throw new RuntimeException("error while decoding");
                    }
                } else
                    return charsRead;
            }
            ++charsRead;
            tempBufferPtr = 0;
        }

        return charsRead;
    }
}

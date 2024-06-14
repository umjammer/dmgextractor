/*-
 * Copyright (C) 2007-2008 Erik Larsson
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

package org.catacombae.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.CharBuffer;

import static java.lang.System.getLogger;


public class MutableInputStreamReader extends Reader {

    private static final Logger logger = getLogger(MutableInputStreamReader.class.getName());

    private static final String PREFIX = "---->MutableInputStreamReader: ";
    private final InputStream iStream;
    private InputStreamReader isReader;

    public MutableInputStreamReader(InputStream iStream, String charset) throws UnsupportedEncodingException {
        this.iStream = iStream;
        this.isReader = new InputStreamReader(iStream, charset);
    }

    @Override
    public void close() throws IOException {
        try {
            logger.log(Level.TRACE, PREFIX + "isReader.close()");
            isReader.close();
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        try {
            logger.log(Level.TRACE, PREFIX + "isReader.mark(" + readAheadLimit + ")");
            isReader.mark(readAheadLimit);
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean markSupported() {
        try {
            boolean result = isReader.markSupported();
            logger.log(Level.TRACE, PREFIX + "isReader.markSupported() == " + result);
            return result;
        } catch (RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int read() throws IOException {
        try {
            int result = isReader.read();
            logger.log(Level.TRACE, PREFIX + "isReader.read() == " + result);
            return result;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        try {
            int result = isReader.read(cbuf);
            logger.log(Level.TRACE, PREFIX + "isReader.read(" + cbuf.length + " bytes...) == " + result);
            return result;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        try {
            int result = isReader.read(cbuf, off, len);
            logger.log(Level.TRACE, PREFIX + "isReader.read(" + cbuf.length + " bytes..., " + off + ", " + len + ") == " + result);
            return result;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        try {
            int result = isReader.read(target);
            logger.log(Level.TRACE, PREFIX + "isReader.read(CharBuffer with length " + target.length() + ") == " + result);
            return result;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            boolean result = isReader.ready();
            logger.log(Level.TRACE, PREFIX + "isReader.ready() == " + result);
            return result;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void reset() throws IOException {
        try {
            logger.log(Level.TRACE, PREFIX + "isReader.reset()");
            isReader.reset();
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            long result = isReader.skip(n);
            logger.log(Level.TRACE, PREFIX + "isReader.skip(" + n + ") == " + result);
            return result;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }

    public void changeEncoding(String charset) throws UnsupportedEncodingException {
        try {
            logger.log(Level.TRACE, PREFIX + "changeEncoding(\"" + charset + "\")");
            isReader = new InputStreamReader(iStream, charset);
        } catch (RuntimeException e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            throw e;
        }
    }
}
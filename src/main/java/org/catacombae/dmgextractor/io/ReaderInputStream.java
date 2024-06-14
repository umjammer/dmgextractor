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

package org.catacombae.dmgextractor.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import static java.lang.System.getLogger;


public class ReaderInputStream extends InputStream {

    private static final Logger logger = getLogger(ReaderInputStream.class.getName());

    private final Reader r;
    private final CharsetEncoder encoder;
    private final byte[] chardata;
    private int remainingChardata = 0;
    private final LousyByteArrayStream lbas;
    private final OutputStreamWriter osw;

    private static class LousyByteArrayStream extends OutputStream {

        private final byte[] buffer;
        private int bufpos = 0;

        public LousyByteArrayStream(int buflen) {
//            logger.log(Level.TRACE, "Creating a LousyByteArrayStream with length " + buflen);
            buffer = new byte[buflen];
        }

        @Override
        public void write(int b) {
            buffer[bufpos++] = (byte) b;
        }

        public int reset(byte[] chardata) {
            int length = bufpos;
            System.arraycopy(buffer, 0, chardata, 0, length);
            bufpos = 0;
            return length;
        }
    }

    public ReaderInputStream(Reader r, Charset c) {
        this.r = r;
        this.encoder = c.newEncoder();
//        logger.log(Level.TRACE, "Creating a ReaderInputStream. encoder.maxBytesPerChar(): " + encoder.maxBytesPerChar());
        this.chardata = new byte[(int) Math.ceil(encoder.maxBytesPerChar())];

        lbas = new LousyByteArrayStream(chardata.length);
        osw = new OutputStreamWriter(lbas, encoder);
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        int res = read(b, 0, 1);
        if (res == 1)
            return b[0] & 0xFF;
        else
            return -1;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Please note that it is skipped by 204 bytes. Vad händer? När vi går in i read(3) är remainingChardata = 0,
     * och off = 0, len = 204. b.length = 4096. Alltså kommer ingen av de 4 första if-satserna vara
     * giltiga...
     * I saw the situation last in mind that the data was not returned?
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
//        logger.log(Level.TRACE, "ReaderInputStream.read(b.length=" + b.length + ", " + off + ", " + len + ")");
        if (len < 0) throw new IllegalArgumentException();
        if (len == 0) return 0;

        int originalOffset = off;
        int endPos = off + len;

        if (remainingChardata > 0) {
//            logger.log(Level.TRACE, "Remaining chardata! length=" + remainingChardata);
            int bytesToCopy = remainingChardata > len ? len : remainingChardata;
//            logger.log(Level.TRACE, "bytesToCopy=" + bytesToCopy);
            System.arraycopy(chardata, 0, b, off, bytesToCopy);
            off += bytesToCopy;
            remainingChardata -= bytesToCopy;
        }
        if (off == endPos) {
//            logger.log(Level.TRACE, "(1)returning with " + (off-originalOffset) + " from ReaderInputStream.read");
            return off - originalOffset;
        }

//        int baba = 3;
        while (off < endPos) {
//            if (baba > 0) {
//                logger.log(Level.TRACE, "  looping... off==" + off + " endPos=" + endPos);
//                --baba;
//            } else
//                baba = Integer.MIN_VALUE;
            int cur = r.read();
            if (cur < 0)
                break;

            if (Character.isHighSurrogate((char) cur)) {
                int lowSurrogate = r.read(); // UTF-16 is a crap encoding for a programming language

                if (lowSurrogate < 0)
                    throw new IOException("Too lazy to handle this error...");
                else if (!Character.isSurrogatePair((char) cur, (char) lowSurrogate))
                    throw new IOException("Encountered a high surrogate without a matching low surrogate... oh crap.");

                cur = Character.toCodePoint((char) cur, (char) lowSurrogate);
            }
            char[] charArray = Character.toChars(cur);
            String charString = new String(charArray);

            // Now we need to write
//            logger.log(Level.TRACE, "Writing codepoint: 0x" + Util.toHexStringBE(cur));

            osw.write(charString);
            osw.flush();

//            logger.log(Level.TRACE, "Resetting...");
            int chardataLength = lbas.reset(chardata);
            int remainingLength = endPos - off;
            int bytesToCopy = (chardataLength > remainingLength) ? remainingLength : chardataLength;
            System.arraycopy(chardata, 0, b, off, bytesToCopy);
            off += bytesToCopy;

            if (chardataLength > remainingLength) {
                remainingChardata = chardataLength - remainingLength;
                System.arraycopy(chardata, bytesToCopy, chardata, 0, remainingChardata);
            }
//            if (baba >= 0) {
//                logger.log(Level.TRACE, "  chardataLength=" + chardataLength + " remainingLength=" + remainingLength);
//                logger.log(Level.TRACE, "  bytesToCopy=" + bytesToCopy + " off=" + off);
//            }
        }
        int bytesRead = off - originalOffset;
        if (off < endPos && bytesRead == 0) { // We have a break due to end of stream
//            logger.log(Level.TRACE, "(3)returning -1 due to end of stream");
            return -1;
        } else {
//            logger.log(Level.TRACE, "(2)returning with " + bytesRead + " from ReaderInputStream.read");
            return bytesRead;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        logger.log(Level.DEBUG, "ReaderInputStream.skip(" + n + ")");
        byte[] skipBuffer = new byte[4096];
        long bytesSkipped = 0;
        while (bytesSkipped < n) {
//            logger.log(Level.TRACE, "  Looping...");
            long remainingBytes = n - bytesSkipped;
            int bytesToSkip = (int) (skipBuffer.length < remainingBytes ? skipBuffer.length : remainingBytes);
//            logger.log(Level.TRACE, "  Skipping " + bytesToSkip + " this iteration.");
            int res = read(skipBuffer, 0, bytesToSkip);
            if (res > 0) {
//                logger.log(Level.TRACE, "  Actually skipped " + res + " bytes.");
//                logger.log(Level.TRACE, " This is the data skipped: " + Util.byteArrayToHexString(skipBuffer, 0, res));
                bytesSkipped += res;
            } else {
//                logger.log(Level.TRACE, "encountered EOF!");
                break; // Seems we can't skip all bytes
            }
        }
//        logger.log(Level.TRACE, " bytesSkipped=" + bytesSkipped + " n=" + n);
        return bytesSkipped; // super.skip(n);
    }
}

/*-
 * Copyright (C) 2011 Erik Larsson
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

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import static java.lang.System.getLogger;


/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
@PropsEntity(url = "file:local.properties")
public class TestCase {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "dmg")
    String dmg = "src/test/resources/test.dmg";

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);
        }
    }

    private static final Logger logger = getLogger(TestCase.class.getName());

    @Test
    @Disabled("file type is wrong")
    void test1() {
        SparseBundle sb = new SparseBundle(Path.of(dmg).toFile());
        System.out.println("image size: " + sb.getSize() + " bytes");
        System.out.println("band size: " + sb.getBandSize() + " bytes");
        System.out.println("band count: " + sb.getBandCount() + " bands");

        ReadableSparseBundleStream stream = new ReadableSparseBundleStream(sb);
        byte[] buf = new byte[91673];
        long bytesRead = 0;
        long startTime = System.currentTimeMillis();
        long lastTime = startTime;

        while (true) {
            int curBytesRead = stream.read(buf);
            if (curBytesRead == -1)
                break;
            else if (curBytesRead < 0)
                throw new RuntimeException("Wtf... curBytesRead=" + curBytesRead);

            bytesRead += curBytesRead;

            long curTime = System.currentTimeMillis();
            if (curTime - lastTime > 1000) {
                logger.log(Level.DEBUG, "Transferred " + bytesRead + " bytes in " +
                        (curTime - startTime) / 1000.0 + " seconds.");
                lastTime = curTime;
            }
        }

        logger.log(Level.DEBUG, "Transfer complete.");

        long curTime = System.currentTimeMillis();
        logger.log(Level.DEBUG, "Transferred " + bytesRead + " bytes in " +
                (curTime - startTime) / 1000.0 + " seconds.");
    }
}

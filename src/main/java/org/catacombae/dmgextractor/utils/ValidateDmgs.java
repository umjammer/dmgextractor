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

package org.catacombae.dmgextractor.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.LinkedList;

import static java.lang.System.getLogger;


/**
 * Wrapper to allow processing of a list of dmg files. (I was too lazy
 * to write a shell script for this..)
 */
public class ValidateDmgs {

    private static final Logger logger = getLogger(ValidateDmgs.class.getName());

    public static void main(String[] args) throws IOException {
        LinkedList<String> fileList = new LinkedList<>();
        for (String currentList : args) {
            try {
                BufferedReader listIn = new BufferedReader(new InputStreamReader(new FileInputStream(currentList)));
                String currentDmg = listIn.readLine();
                while (currentDmg != null) {
                    fileList.add(currentDmg);
                    currentDmg = listIn.readLine();
                }
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
        ValidateDmg.main(fileList.toArray(String[]::new));
    }
}

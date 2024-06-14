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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * User interface implementation using plain old System.in and System.out for a
 * text-based UI.
 *
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
class TextModeUI extends BasicUI implements UserInterface {

    private static final Logger logger = getLogger(TextModeUI.class.getName());

    /** A string containing 79 backspace characters. */
    public static final String BACKSPACE79 =
            "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b" +
                    "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b" +
                    "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b" +
                    "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
    private final PrintStream ps = System.out;
    private final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    public TextModeUI(boolean verbose) {
        super(verbose);
    }

    @Override
    public boolean warning(String... messageLines) {
        if (messageLines.length > 0) {
            ps.println("WARNING: " + messageLines[0]);
            for (int i = 1; i < messageLines.length; ++i)
                ps.println("         " + messageLines[i]);
        }
        return true;
    }

    @Override
    public void error(String... messageLines) {
        if (messageLines.length > 0) {
            ps.println("!------>ERROR: " + messageLines[0]);
            for (int i = 1; i < messageLines.length; ++i)
                ps.println("          " + messageLines[i]);
        }
    }

    @Override
    public void reportProgress(int progressPercentage) {
        if (progressPercentage != previousPercentage) {
            previousPercentage = progressPercentage;
            ps.println("--->Progress: " + progressPercentage + "%");
        }
    }

    @Override
    public void reportFinished(boolean simulation, int errorsReported, int warningsReported, long totalExtractedSize) {
        StringBuilder summary = new StringBuilder();
        if (errorsReported != 0)
            summary.append(errorsReported).append(" errors reported");
        else
            summary.append("No errors reported");

        if (warningsReported != 0)
            summary.append(" (").append(warningsReported).append(" warnings emitted)");

        summary.append(".");

        ps.println();
        ps.println(summary);
        if (verbose)
            ps.println("Size of extracted data: " + totalExtractedSize + " bytes");
    }

    @Override
    public boolean cancelSignaled() {
        return false;
    }

    @Override
    public void displayMessage(String... messageLines) {
//        ps.print(BACKSPACE79);
        for (String s : messageLines)
            ps.println(s);
        if (messageLines.length < 1)
            ps.println();
    }

    @Override
    public File getInputFileFromUser() {
////        String s = "";
//        while (true) {
//            printCurrentLine("Please specify the path to the dmg file to extract from: ");
//            File f = new File(stdin.readLine().trim());
//            while (!f.exists()) {
//                println("File does not exist!");
//                printCurrentLine("Please specify the path to the dmg file to extract from: ");
//                f = new File(stdin.readLine().trim());
//            }
//            return f;
//        }

        // Text mode operation is not interactive anymore.
        return null;
    }

    @Override
    public boolean getOutputConfirmationFromUser() {
//        String s = "";
//        while (true) {
//            printCurrentLine("Do you want to specify an output file (y/n)? ");
//            s = stdin.readLine().trim();
//            if (s.equalsIgnoreCase("y"))
//                return true;
//            else if (s.equalsIgnoreCase("n"))
//                return false;
//        }

        // Text mode operation is not interactive anymore.
        return false;
    }

    @Override
    public File getOutputFileFromUser(File inputFile) {
//        final String msg1 = "Please specify the path of the iso file to extract to: ";
//        final String msg2 = "The file already exists. Do you want to overwrite?";
//        while (true) {
//            printCurrentLine(msg1);
//            File f = new File(stdin.readLine().trim());
//            while (f.exists()) {
//                while (true) {
//                    printCurrentLine(msg2 + " (y/n)? ");
//                    String s = stdin.readLine().trim();
//                    if (s.equalsIgnoreCase("y"))
//                        return f;
//                    else if (s.equalsIgnoreCase("n"))
//                        break;
//                }
//                printCurrentLine(msg1);
//                f = new File(stdin.readLine().trim());
//            }
//            return f;
//        }

        // Text mode operation is not interactive anymore.
        return null;
    }

    @Override
    public char[] getPasswordFromUser() {
        displayMessage("The disk image you are trying to extract is encrypted.");
        try {
            char[] reply = prompt("Please enter password: ");
            if (reply != null)
                return reply;
            else
                return null;
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            return null;
        }
    }

    private char[] prompt(String s) throws IOException {
        char[] result = null;

        ps.print(s);

        if (Java6Util.isJava6OrHigher()) {
            result = Java6Util.readPassword();
        }

        if (result == null) {
            String line = stdin.readLine();
            if (line != null) {
                result = line.toCharArray();
            } else {
                result = null;
            }
        }

        return result;
    }

    @Override
    public void setProgressFilenames(String inputFilename, String outputFilename) {
        // We currently don't act on this.
    }
}

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


interface UserInterface {

    boolean cancelSignaled();

    void displayMessageVerbose(String... messageLines);

    File getInputFileFromUser();

    boolean getOutputConfirmationFromUser();

    File getOutputFileFromUser(File inputFile);

    char[] getPasswordFromUser();

    /**
     * If outputFilename is null, is would mean that a simulation is in progress.
     *
     * @param inputFilename
     * @param outputFilename
     */
    void setProgressFilenames(String inputFilename, String outputFilename);

    /**
     * Unconditionally displays a message to the user, to inform about certain
     * things that happen in a program.<br>
     * <b>This method may block execution, so use it sparsely.</b>
     *
     * @param messageLines the message, line by line.
     */
    void displayMessage(String... messageLines);

    /**
     * Issues a warning message to the user. Returns true if the process should
     * proceed despite the warning, and false if the process should be
     * aborted.<br>
     * <b>This method may block execution, so use it sparsely.</b>
     *
     * @param messageLines the message, line by line.
     * @return true if the process should proceed despite the warning, and
     * false if the process should be aborted.
     */
    boolean warning(String... messageLines);

    /**
     * Issues an error message to the user.<br>
     * <b>This method may block execution, so use it sparsely.</b>
     *
     * @param messageLines the message, line by line.
     */
    void error(String... messageLines);

    /**
     * This method should be called to bring up a summary after a finished
     * extraction/simulation process.
     *
     * @param simulation         set to true when the extraction was only simulated,
     *                           false for a real extraction.
     * @param errorsReported     the number of errors encountered during the
     *                           extraction.
     * @param warningsReported   the number of warnings encountered during the
     *                           extraction.
     * @param totalExtractedSize the outgoing data size, i.e. the data that was
     *                           written (or should have been written, in the case of a simulation).
     */
    void reportFinished(boolean simulation, int errorsReported, int warningsReported, long totalExtractedSize);

    /**
     * Sets the current progress value to a specified percentage. This method
     * should not be used except for when the progress meter is to be reset or
     * set forcibly to 100% when the process has completed.
     *
     * @param progressPercentage the percentage to set the progress to (range
     *                           0-100).
     */
    void reportProgress(int progressPercentage);

    /**
     * Used in conjunction with <code>addProgressRaw(...)</code>, and
     * denotes the total length of the data on which we monitor progress.
     *
     * @param len the number of bytes of data that is the maximum value for
     *            raw progress.
     */
    void setTotalProgressLength(long len);

    /**
     * Adds progress as raw bytes, instead of setting it as percentage. The
     * percentage will automatically be calculated from the value previously
     * set through <code>setTotalProgressLength()</code>.
     *
     * @param value the byte value to add to the current progress.
     */
    void addProgressRaw(long value);

    class NullUI extends BasicUI {

        public NullUI() {
            super(false);
        }

        @Override
        public void reportProgress(int progressPercentage) {
        }

        @Override
        public void displayMessage(String... messageLines) {
        }

        @Override
        public boolean warning(String... messageLines) {
            return true;
        }

        @Override
        public void error(String... messageLines) {
        }

        @Override
        public void reportFinished(boolean simulation, int errorsReported, int warningsReported, long totalExtractedSize) {
        }

        @Override
        public boolean cancelSignaled() {
            return false;
        }

        @Override
        public File getInputFileFromUser() {
            return null;
        }

        @Override
        public boolean getOutputConfirmationFromUser() {
            return false;
        }

        @Override
        public File getOutputFileFromUser(File inputFile) {
            return null;
        }

        @Override
        public char[] getPasswordFromUser() {
            return null;
        }

        @Override
        public void setProgressFilenames(String inputFilename, String outputFilename) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}

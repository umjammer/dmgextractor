package org.catacombae.dmg.udif;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;


class UDIFInputStreamTest {

    /** Test code. */
    public static void main(String[] args) throws IOException {
        if (args.length != 2)
            System.out.println("usage: java org.catacombae.udif.UDIFInputStream <infile> <outfile>");
        File inFile = new File(args[0]);
        File outFile = new File(args[1]);

        RandomAccessFile inRaf = null;
        FileOutputStream outFos = null;

        if (inFile.canRead())
            inRaf = new RandomAccessFile(inFile, "r");
        else {
            System.out.println("Can't read from input file!");
            System.exit(0);
        }

        if (!outFile.exists())
            outFos = new FileOutputStream(outFile);
        else {
            System.out.println("Output file already exists!");
            System.exit(0);
        }

        UDIFInputStream dis = new UDIFInputStream(inRaf, inFile.getPath());
        byte[] buffer = new byte[8192];
        long bytesExtracted = 0;
        int bytesRead = dis.read(buffer);
        while (bytesRead > 0) {
            bytesExtracted += bytesRead;
            outFos.write(buffer, 0, bytesRead);
            bytesRead = dis.read(buffer);
        }
        dis.close();
        inRaf.close();
        outFos.close();

        System.out.println("Extracted " + bytesExtracted + " bytes to \"" + outFile + "\".");
    }
}
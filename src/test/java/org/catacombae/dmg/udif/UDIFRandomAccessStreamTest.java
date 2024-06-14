package org.catacombae.dmg.udif;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.catacombae.io.ReadableFileStream;

import static org.junit.jupiter.api.Assertions.*;


class UDIFRandomAccessStreamTest {

    public static void main(String[] args) throws IOException {
        System.out.println("UDIFRandomAccessStream simple test program");
        System.out.println("(Simply extracts the contents of a DMG file to a designated output file)");
        if (args.length != 2)
            System.out.println("  ERROR: You must supply exactly two arguments: 1. the DMG, 2. the output file");
        else {
            byte[] buffer = new byte[4096];
            UDIFRandomAccessStream dras = new UDIFRandomAccessStream(new UDIFFile(new ReadableFileStream(
                    new RandomAccessFile(args[0], "r"), args[0])));
            FileOutputStream fos = new FileOutputStream(args[1]);

            long totalBytesRead = 0;

            int bytesRead = dras.read(buffer);
            while (bytesRead > 0) {
                totalBytesRead += bytesRead;
                fos.write(buffer, 0, bytesRead);
                bytesRead = dras.read(buffer);
            }
            System.out.println("Done! Extracted " + totalBytesRead + " bytes.");
            System.out.println("Length: " + dras.length() + " bytes");
        }
    }
}
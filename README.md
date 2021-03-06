[![jitpack](https://jitpack.io/v/umjammer/dmgextractor.svg)](https://jitpack.io/#umjammer/dmgextractor) [![Parent](https://img.shields.io/badge/Parent-vavi--apps--fuse-pink)](https://github.com/umjammer/vavi-apps-fuse)

# dmg extractor

DMGExtractor is a lightweight Java application for extracting the contents of a Mac OS X Disk Image file (extension .dmg) to raw binary format (such as an ISO file). It can handle most disk images of type CUDIFDiskImage, with the exception of UDCO images (using a compression method that is unknown to me). It also (as of version 0.70) handles AES-128 encrypted disk images.
This application has been tested and developed under version 1.5.0 (5.0) of the Sun JDK. Anything less is probably insufficient.
The program is suited for both console and graphical use. If want to use it in console mode, you should download the zip file distribution. Running the script hfsx.sh (unixes) or hfsx.bat (windows) will print out some usage information.

This program started as a Java port of vu1tur's dmg2iso program, but dmg2iso didn't work for me on any of the files I tried using it with, so I rewrote it in Java, and subsequently added a lot of new features. I also made a very basic GUI (dialog based).

I publish this program under the GPL license version 3, so feel free to modify it, make it work for you as long as you stay within the GPLv3. License text:
GNU General Public License version 3

If you have any comment on my work or would like to report a bug, just send me an e-mail. Also, if you publish any modifications to the program, I'd like to know about it so that I may port them back into my source tree.

----

[Project Page](http://www.catacombae.org/dmgextractor/)

/*-
 * Copyright (C) 2006-2011 Erik Larsson
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

package org.catacombae.plist;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.catacombae.dmgextractor.io.RandomAccessInputStream;
import org.catacombae.dmgextractor.io.SynchronizedRandomAccessStream;
import org.catacombae.io.ReadableByteArrayStream;
import org.catacombae.xml.DebugXMLContentHandler;
import org.catacombae.xml.NodeBuilder;
import org.catacombae.xml.NodeBuilderContentHandler;
import org.catacombae.xml.NullXMLContentHandler;
import org.catacombae.xml.XMLNode;
import org.catacombae.xml.apx.APXParser;
import org.catacombae.xml.apx.ParseException;
import org.xml.sax.SAXException;

import static java.lang.System.getLogger;


/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public class XmlPlist {

    private static final Logger logger = getLogger(XmlPlist.class.getName());

    private final XMLNode rootNode;

    public XmlPlist(byte[] data) {
        this(data, 0, data.length);
    }

    public XmlPlist(byte[] data, boolean useSAXParser) {
        this(data, 0, data.length, useSAXParser);
    }

    public XmlPlist(byte[] data, int offset, int length) {
        this(data, offset, length, false);
    }

    public XmlPlist(byte[] data, int offset, int length, boolean useSAXParser) {
//        plistData = new byte[length];
//        System.arraycopy(data, offset, plistData, 0, length);
        rootNode = parseXMLData(data, useSAXParser);
    }

    public PlistNode getRootNode() {
        return new XmlPlistNode(rootNode);
    }

    private XMLNode parseXMLData(byte[] plistData, boolean defaultToSAX) {
//        InputStream is = new ByteArrayInputStream(plistData);
        NodeBuilder handler = new NodeBuilder();

        if (defaultToSAX) {
            parseXMLDataSAX(plistData, handler);
        } else {
            // First try to parse with the internal homebrew parser, and if it
            // doesn't succeed, go for the SAX parser.
//            logger.log(Level.TRACE, "Trying to parse xml data...");
            try {
                parseXMLDataAPX(plistData, handler);
//                logger.log(Level.TRACE, "xml data parsed...");
            } catch (Exception e) {
                logger.log(Level.ERROR, e.getMessage(), e);
                logger.log(Level.DEBUG, "APX parser threw exception... falling back to SAX parser. Report this error!");
                handler = new NodeBuilder();
                parseXMLDataSAX(plistData, handler);
            }
        }

        XMLNode[] rootNodes = handler.getRoots();
        if (rootNodes.length != 1)
            throw new RuntimeException("Could not parse DMG-file!");
        else
            return rootNodes[0];
    }

    private void parseXMLDataAPX(byte[] buffer, NodeBuilder handler) {
        try {
            ReadableByteArrayStream ya = new ReadableByteArrayStream(buffer);
            SynchronizedRandomAccessStream bufferStream = new SynchronizedRandomAccessStream(ya); // new ReadableByteArrayStream(buffer));

            // First we parse the xml declaration using a US-ASCII charset just to extract the charset description
//            logger.log(Level.TRACE, "parsing encoding");
            InputStream is = new RandomAccessInputStream(bufferStream);
            APXParser encodingParser = APXParser.create(
                    new InputStreamReader(is, StandardCharsets.US_ASCII),
                    new NullXMLContentHandler(StandardCharsets.US_ASCII));
            String encodingName = encodingParser.xmlDecl();
//            logger.log(Level.TRACE, "encodingName=" + encodingName);
            if (encodingName == null)
                encodingName = "US-ASCII";

            Charset encoding = Charset.forName(encodingName);

            // Then we proceed to parse the entire document
            is = new RandomAccessInputStream(bufferStream);
            Reader usedReader = new BufferedReader(new InputStreamReader(is, encoding));
//            logger.log(Level.TRACE, "parsing document");
//            try { FileOutputStream dump = new FileOutputStream("dump.xml"); dump.write(buffer); dump.close(); }
//            catch(Exception e) { e.printStackTrace(); }

            if (false) { //
                APXParser documentParser = APXParser.create(usedReader, new DebugXMLContentHandler(encoding));
                documentParser.xmlDocument();
                System.exit(0);
            } else {
                var documentParser = APXParser.create(usedReader, new NodeBuilderContentHandler(handler, bufferStream, encoding));
                documentParser.xmlDocument();
            }

        } catch (ParseException pe) {
//            logger.log(Level.TRACE, "Could not read the partition list...");
            throw new IllegalArgumentException(pe);
        }
    }

    private void parseXMLDataSAX(byte[] buffer, NodeBuilder handler) {
        try {
            InputStream is = new ByteArrayInputStream(buffer);
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
//            logger.log(Level.TRACE, "validation: " + saxParser.getProperty("validation"));
//            logger.log(Level.TRACE, "external-general-entities: " + saxParser.getProperty("external-general-entities"));
//            logger.log(Level.TRACE, "external-parameter-entities: " + saxParser.getProperty("external-parameter-entities"));
//            logger.log(Level.TRACE, "is-standalone: " + saxParser.getProperty("is-standalone"));
//            logger.log(Level.TRACE, "lexical-handler: " + saxParser.getProperty("lexical-handler"));
//            logger.log(Level.TRACE, "parameter-entities: " + saxParser.getProperty("parameter-entities"));
//            logger.log(Level.TRACE, "namespaces: " + saxParser.getProperty("namespaces"));
//            logger.log(Level.TRACE, "namespace-prefixes: " + saxParser.getProperty("namespace-prefixes"));
//            logger.log(Level.TRACE, ": " + saxParser.getProperty(""));
//            logger.log(Level.TRACE, ": " + saxParser.getProperty(""));
//            logger.log(Level.TRACE, ": " + saxParser.getProperty(""));
//            logger.log(Level.TRACE, ": " + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));
//            logger.log(Level.TRACE, "" + saxParser.getProperty(""));

//            logger.log(Level.TRACE, "isValidating: " + saxParser.isValidating());
            saxParser.parse(is, handler);
        } catch (SAXException se) {
            logger.log(Level.ERROR, se.getMessage(), se);
//            logger.log(Level.TRACE, "Could not read the partition list... exiting.");
            throw new RuntimeException(se);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

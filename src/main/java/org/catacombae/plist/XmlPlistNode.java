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

import java.io.IOException;
import java.io.Reader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.LinkedList;

import org.catacombae.dmgextractor.io.ConcatenatedReader;
import org.catacombae.io.RuntimeIOException;
import org.catacombae.util.Util;
import org.catacombae.xml.XMLElement;
import org.catacombae.xml.XMLNode;
import org.catacombae.xml.XMLText;

import static java.lang.System.getLogger;


/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public class XmlPlistNode extends PlistNode {

    private static final Logger logger = getLogger(XmlPlistNode.class.getName());

    private final XMLNode xmlNode;

    public XmlPlistNode(XMLNode xmlNode) {
        this.xmlNode = xmlNode;
    }

    public XMLNode getXMLNode() {
        return xmlNode;
    }

    private String[] getKeys() throws RuntimeIOException {
        LinkedList<String> keyList = new LinkedList<>();

        for (XMLElement xe : xmlNode.getChildren()) {
            if (xe instanceof XMLNode xn) {
                if (xn.qName.equals("key")) {
                    for (XMLElement xeChild : xn.getChildren()) {
                        if (xeChild instanceof XMLText xtChild) {
                            String key;

                            try {
                                key = Util.readFully(xtChild.getText());
                            } catch (IOException e) {
                                throw new RuntimeIOException(e);
                            }

                            keyList.addLast(key);
                        }
                    }
                }
            }
        }

        return keyList.toArray(String[]::new);
    }

    @Override
    public PlistNode[] getChildren() {
        LinkedList<PlistNode> children = new LinkedList<>();

        if (xmlNode.qName.equals("dict")) {
            for (String key : getKeys()) {
                children.add(cdkey(key));
            }
        } else if (xmlNode.qName.equals("array")) {
            for (XMLElement xe : xmlNode.getChildren()) {
                if (xe instanceof XMLNode) {
                    children.add(new XmlPlistNode((XMLNode) xe));
                } else if (xe instanceof XMLText) {
                    String text = "";
                    try {
                        text = Util.readFully(((XMLText) xe).getText());
                    } catch (IOException ex) {
                        logger.log(Level.ERROR, ex.getMessage(), ex);
                    }
                    throw new RuntimeException("Unexpected text inside array plist element: \"" + text + "\"");
                } else {
                    logger.log(Level.DEBUG, xe.toString());
                    throw new RuntimeException("Unexpected element inside array: " + xe);
                }
            }
        } else {
            throw new RuntimeException("getChildren called for non-dict/array type \"" + xmlNode.qName + "\".");
        }

        return children.toArray(PlistNode[]::new);
    }

    /**
     * The concept of "changing directory" in a tree is perhaps not a
     * perfect way to describe things. But this method will look up the
     * first subnode of our node that is of the type <code>type</code>
     * and return it.
     * If you have more than one of the same type, tough luck. You only
     * get the first.
     */
    @Override
    public PlistNode cd(String type) {
        for (XMLElement xn : xmlNode.getChildren()) {
            if (xn instanceof XMLNode && ((XMLNode) xn).qName.equals(type))
                return new XmlPlistNode((XMLNode) xn);
        }
        return null;
    }

    /**
     * This is different from the <code>cd</code> method in that it
     * searches for a node of the type "key", and looks up the <code>
     * XMLText</code> within. It then compares the text with the String
     * <code>key</code>. If they match, it returns the node coming
     * after the key node. Else it continues to search. If no match is
     * found, <code>null</code> is returned.
     */
    @Override
    public PlistNode cdkey(String key) {
        return cdkeyXml(key);
    }

    private XmlPlistNode cdkeyXml(String key) {
        boolean keyFound = false;
        for (XMLElement xn : xmlNode.getChildren()) {
            if (xn instanceof XMLNode) {
                if (keyFound)
                    return new XmlPlistNode((XMLNode) xn);

                else if (((XMLNode) xn).qName.equals("key")) {
                    for (XMLElement xn2 : ((XMLNode) xn).getChildren()) {
                        try {
                            if (xn2 instanceof XMLText) {
                                String s = Util.readFully(((XMLText) xn2).getText());
//                                logger.log(Level.TRACE, "cdkey searching: \"" + s + "\"");
                                if (s.equals(key))
                                    keyFound = true;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Reader getKeyValue(String key) {
//        logger.log(Level.TRACE, "XMLNode.getKeyValue(\"" + key + "\")");
        XmlPlistNode keyNode = cdkeyXml(key);
        if (keyNode == null)
            return null;

        XMLElement[] nodeChildren = keyNode.getXMLNode().getChildren();
        if (nodeChildren.length != 1) {
//            logger.log(Level.TRACE, "  nodeChildren.length == " + nodeChildren.length);

            LinkedList<Reader> collectedReaders = new LinkedList<>();
            for (XMLElement xe : keyNode.getXMLNode().getChildren()) {
                if (xe instanceof XMLText) {
                    try {
                        Reader xt = ((XMLText) xe).getText();
                        collectedReaders.addLast(xt);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
//                    logger.log(Level.TRACE, "\"");
//                    for(int i = 0; i < xt.length(); ++i) System.out.print(xt.charAt(i));
//                    logger.log(Level.TRACE, "\"");
//                    logger.log(Level.TRACE, "free memory: " + Runtime.getRuntime().freeMemory() + " total memory: " + Runtime.getRuntime().totalMemory());
                }
            }
            ConcatenatedReader result;
            if (collectedReaders.isEmpty())
                result = null;
            else {
//                logger.log(Level.TRACE, "doing a toString... free memory: " + Runtime.getRuntime().freeMemory() + " total memory: " + Runtime.getRuntime().totalMemory());
//                result = returnString.toString();
//                logger.log(Level.TRACE, "done.free memory: " + Runtime.getRuntime().freeMemory() + " total memory: " + Runtime.getRuntime().totalMemory());
                result = new ConcatenatedReader(collectedReaders.toArray(Reader[]::new));
            }
            return result;
        } else if (nodeChildren[0] instanceof XMLText) {
//            logger.log(Level.TRACE, "Special case!");
            try {
                return ((XMLText) nodeChildren[0]).getText();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else
            return null;
    }
}

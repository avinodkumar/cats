/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;

/**
 * Simple class to cache the IR codes that are loaded from the keyList.default file.
 */
public final class IRCodeCache
{
	public static final String KEYSET_FILE_NAME = "keyList.default";
	public static final String KEYSET_FILE_LOCATION = "/root/cats/" + KEYSET_FILE_NAME;
    /**
     * default logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(IRCodeCache.class);
    /**
     * standard singleton pattern.
     */
    private static final IRCodeCache irCodeCache = new IRCodeCache();
    /**
     * map of maps of keycodes. the key of this map is the settop type, it's value is the map of key-to-ircode.
     */
    private static Map<String /* stbType */, Map<RemoteCommand, String>> allKeyCodes;

    /**
     * @return the only instance of the IRCodeCache in the system, since this is a singleton
     */
    public static IRCodeCache getInstance()
    {
        return irCodeCache;
    }
    
    /**
     * private constructor which loads the keyList.default when the JVM is started.
     */
    private IRCodeCache()
    {
		File keySetFile = new File(KEYSET_FILE_LOCATION);
		if(keySetFile.exists()) {
			logger.info("keySetFile [" + KEYSET_FILE_LOCATION + "] exists, load it");
			loadCodeMap(keySetFile);
		} else {
			logger.info("Attempting to load local file");
			loadCodeMap();
		}
    }

    public static String getIrCode(final String irKeySet, final RemoteCommand c) {
    	if(irKeySet == null || c == null) {
    		logger.warn("getIrCode: KeySet='" + irKeySet + "'");
			if(c == null) {
				logger.warn("getIrCode: RemoteCommand = null");
			} else {
				logger.warn("getIrCode: RemoteCommand = '" + c.name() + "'");
			}
			return null;
		}
		String irCode = getKeyCodes(irKeySet).get(c);
		if(irCode != null) {
			return irCode;
		}
		logger.warn("irCode not found for Keyset[" + irKeySet + "] RemoteCommand[" + c.name() + "]");
		return null;
    	
    }

    /**
     * @param stbType
     *            The type of settop box to get the keymap for. Currently supported types include
     *            <ul>
     *            <li>TV_PANASONIC</li>
     *            <li>DCT_SAMSUNG</li>
     *            <li>DCT_SA</li>
     *            <li>DCT_SA_XMP</li>
     *            <li>DCT_MOTOROLA</li>
     *            <li>DCT_PANASONIC</li>
     *            </ul>
     * @return the map of key-to-irCode for the specified type of settop
     */
    public static Map<RemoteCommand, String> getKeyCodes(final String stbType)
    {
        return allKeyCodes.get(stbType);
    }

	private void loadCodeMap(File file)
	{
		{
			InputStream fileIn = null;
			try {
				final JAXBContext ctx = JAXBContext.newInstance(KeyFile.class, KeyFile.KeyEntry.class);
				fileIn = new FileInputStream(file);
				if (fileIn == null) {
					logger.error("Unable to find the keyList.default file, which contains all the IR codes");
				}
				final KeyFile kf = (KeyFile) ctx.createUnmarshaller().unmarshal(fileIn);
				parseKeyFile(kf);
			} catch (FileNotFoundException ex) {
				logger.error("Unable to find key file for uploading.");
			} catch (JAXBException e) {
				logger.error("Unable to parse the keyList.default file", e);
			} finally {
				try {
					fileIn.close();
				} catch (IOException ex) {
					logger.error( "{}", ex );
				}
			}
		}
	}

    /**
     * helper method to load the keymap from the keyList.default file.
     */
    private void loadCodeMap()
    {
        try
        {
            final JAXBContext ctx = JAXBContext.newInstance(KeyFile.class, KeyFile.KeyEntry.class);

            final InputStream fileIn = Thread.currentThread().getContextClassLoader().getResourceAsStream(KEYSET_FILE_NAME);
            if (fileIn == null)
            {
                logger.error("Unable to find the keyList.default file, which contains all the IR codes");
            }

            final KeyFile kf = (KeyFile) ctx.createUnmarshaller().unmarshal(fileIn);
            parseKeyFile(kf);
        }
        catch (JAXBException e)
        {
            logger.error("Unable to parse the keyList.default file", e);
        }
    }

    /**
     * Helper method to turn the KeyFile class into the allKeyCodes map.
     * @param kf
     *            the keyfile representation to parse
     */
    private void parseKeyFile(final KeyFile kf)
    {
        this.allKeyCodes = new HashMap<String, Map<RemoteCommand, String>>();

        for (KeyFile.KeyEntry e : kf.getEntries())
        {
            Map<RemoteCommand, String> m = this.allKeyCodes.get(e.getStbType());
            if (m == null)
            {
                m = new HashMap<RemoteCommand, String>();
                this.allKeyCodes.put(e.getStbType(), m);
            }

            m.put(RemoteCommand.parse(e.getName()), e.getIrCode());
        }
    }

    /**
     * This class represents the contents of the keyList.default file. It is here to make parsing that xml file MUCH easier.
     */
    @XmlRootElement(name = "KEYS")
    protected static class KeyFile
    {

        /**
         * The list &lt;KEY&gt; tags.
         */
        private List<KeyEntry> entries;

        /**
         * @return The list &lt;KEY&gt; tags.
         */
        @XmlElement(name = "KEY")
        public List<KeyEntry> getEntries()
        {
            return entries;
        }

        /**
         * @param entries
         *            The list &lt;KEY&gt; tags
         */
        public void setEntries(final List<KeyEntry> entries)
        {
            this.entries = entries;
        }

        /**
         * A class to represent the &lt;KEY&gt; tag to make parsing this xml much easier.
         */
        protected static class KeyEntry
        {

            /**
             * The column attribute of the keyList.default file.
             */
            private int column;
            /**
             * The panel attribute of the keyList.default file.
             */
            private int panel;
            /**
             * The row attribute of the keyList.default file.
             */
            private int row;
            /**
             * The keyformat attribute of the keyList.default file. This is always PRONTO.
             */
            private String keyFormat;
            /**
             * The name of the key ("0", "OK", "ONDEMAND", etc.).
             */
            private String name;
            /**
             * The kind of STB.
             * @See IRCodeCache.getKeyCodes()
             */
            private String stbType;
            /**
             * The PRONTO encoded string that represents the IR code for the current key.
             */
            private String irCode;

            /**
             * @return The column attribute of the keyList.default file
             */
            @XmlAttribute(name = "COLUMN")
            public int getColumn()
            {
                return column;
            }

            /**
             * @param column
             *            The column attribute of the keyList.default file
             */
            public void setColumn(final int column)
            {
                this.column = column;
            }

            /**
             * @return The PRONTO encoded string that represents the IR code for the current key
             */
            @XmlAttribute(name = "VALUE")
            public String getIrCode()
            {
                return irCode;
            }

            /**
             * @param irCode
             *            The PRONTO encoded string that represents the IR code for the current key
             */
            public void setIrCode(final String irCode)
            {
                this.irCode = irCode;
            }

            /**
             * @return The name of the key ("0", "OK", "ONDEMAND", etc.)
             */
            @XmlAttribute(name = "KEYFORMAT")
            public String getKeyFormat()
            {
                return keyFormat;
            }

            /**
             * @param keyFormat
             *            The name of the key ("0", "OK", "ONDEMAND", etc.)
             */
            public void setKeyFormat(final String keyFormat)
            {
                this.keyFormat = keyFormat;
            }

            /**
             * @return The name of the key ("0", "OK", "ONDEMAND", etc.)
             */
            @XmlAttribute(name = "NAME")
            public String getName()
            {
                return name;
            }

            /**
             * @param name
             *            The name of the key ("0", "OK", "ONDEMAND", etc.)
             */
            public void setName(final String name)
            {
                this.name = name;
            }

            /**
             * @return The panel attribute of the keyList.default file
             */
            @XmlAttribute(name = "PANEL")
            public int getPanel()
            {
                return panel;
            }

            /**
             * @param panel
             *            The panel attribute of the keyList.default file
             */
            public void setPanel(final int panel)
            {
                this.panel = panel;
            }

            /**
             * @return The row attribute of the keyList.default file
             */
            @XmlAttribute(name = "ROW")
            public int getRow()
            {
                return row;
            }

            /**
             * @param row
             *            The row attribute of the keyList.default file
             */
            public void setRow(final int row)
            {
                this.row = row;
            }

            /**
             * @return The kind of STB
             * @See IRCodeCache.getKeyCodes()
             */
            @XmlAttribute(name = "TYPE")
            public String getStbType()
            {
                return stbType;
            }

            /**
             * @param stbType
             *            The kind of STB
             * @See IRCodeCache.getKeyCodes()
             */
            public void setStbType(final String stbType)
            {
                this.stbType = stbType;
            }
        }
    }
}

package com.rastadrian.jblinky.probes.cctray;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A SAX Parser for the Cruise Control Tray document.
 *
 * @author Adrian Pena
 */
class CCTrayParser {

    /**
     * De-serializes a Cruise Control Tray.
     * @param content the serialized CC Tray.
     * @return a de-serialized {@link CCTray} object.
     */
    static CCTray parseCCTray(String content) {
        if(StringUtils.isBlank(content)) {
            return null;
        }
        try {
            CCTrayHandler handler = new CCTrayHandler();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(content.getBytes("UTF-8")), handler);
            return handler.getCCTray();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class CCTrayHandler extends DefaultHandler {
        private final List<Project> projects;

        private CCTrayHandler() {
            projects = new ArrayList<Project>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(StringUtils.equals(qName, "Project")) {
                String name = attributes.getValue("name");
                String lastBuildStatus = attributes.getValue("lastBuildStatus");
                projects.add(new Project(name, lastBuildStatus));
            }
        }

        private CCTray getCCTray() {
            CCTray ccTray = new CCTray();
            ccTray.setProjects(projects);
            return ccTray;
        }
    }
}

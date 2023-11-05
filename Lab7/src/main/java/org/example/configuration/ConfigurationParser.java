package org.example.configuration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ConfigurationParser {
    public CustomConfiguration parseXmlFile(String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            Element configurationElement = document.getDocumentElement();
            CustomConfiguration configuration = new CustomConfiguration();
            NodeList childNodes = configurationElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeType() == Element.ELEMENT_NODE) {
                    Element element = (Element) childNodes.item(i);
                    String nodeName = element.getNodeName();
                    String nodeValue = element.getTextContent();
                    switch (nodeName) {
                        case "url" -> configuration.setUrl(nodeValue);
                        case "user" -> configuration.setUser(nodeValue);
                        case "password" -> configuration.setPassword(nodeValue);
                    }
                }
            }

            return configuration;
        } catch (Exception e) {
            throw new RuntimeException("Cannot load configuration file!", e);
        }
    }
}

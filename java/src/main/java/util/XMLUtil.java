package util;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;

public class XMLUtil {
	/**
	 * 
	 * @param root
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static void validateEntry(Element root, String name) {
		if (root == null)
			throw new RuntimeException("XML:entry[" + name + "]: No root element was found.");
		
		if (!root.getTagName().equals(name))
			throw new RuntimeException("XML:entry[" + name + "]: " + name + " was expected as the root node. Instead it was: " + root.getTagName());
		return;
	}
	
	/**
	 * 
	 * @param entry
	 * @param properties
	 * @throws Exception
	 */
	public static void getTagsMasked(Element entry, Map<String,String> properties) {
		//
		NamedNodeMap list = entry.getAttributes();
		for (int i = 0; i < list.getLength(); i++) {
			Node propertyNode = list.item(i);
			if (propertyNode.getNodeType() != Node.ATTRIBUTE_NODE)
				continue;
			Attr propertyElement = (Attr)propertyNode;
			String propertyKey = propertyElement.getName();
			String propertyValue = propertyElement.getValue();
			
			if (!(properties.containsKey(propertyKey)))
				throw new RuntimeException("XML:tagsMasked[" + propertyKey + "]: Not found.");
			properties.put(propertyKey, propertyValue);
		}

		// Check all properties were supplied
		for (String value : properties.values())
			if (value == null)
				throw new RuntimeException("XML:tagsMasked[" + value +"]: Expected and missing.");
	}
	
	/**
	 * Gets all attributes from an element and accumulates them into a map.
	 * @param entry			The element.
	 * @param properties	The map.
	 * @throws Exception
	 */
	public static void getAllTags(Element entry, Map<String,String> properties) throws Exception {
		NamedNodeMap list = entry.getAttributes();
		for (int i = 0; i < list.getLength(); i++)
			properties.put(list.item(i).getNodeName(), list.item(i).getNodeValue());
	}
	
	/**
	 * Gets all attributes from an element and accumulates them into a map. Deprecated.
	 * @param entry			The element.
	 * @param properties	The map.
	 * @throws Exception
	 */
	public static void getAllTags2(Element entry, Map<String,String> properties) throws Exception {
		// Get node list
		NodeList list = entry.getChildNodes();
		
		// For each node
		for (int i = 0; i < list.getLength(); i++) {
			Node propertyNode = list.item(i);
			if (propertyNode.getNodeType() != Node.ELEMENT_NODE)
				throw new Exception();
			Element propertyElement = (Element)propertyNode;
			String propertyKey = propertyElement.getTagName();
			String propertyValue = propertyElement.getAttribute("value");
			
			properties.put(propertyKey, propertyValue);
		}
	}
	
	/**
	 * 
	 * @param entry
	 * @param map
	 * @param reference
	 * @throws Exception
	 */
	public static void getNestedTags(Element entry, Map<String,Map<String,String>> map, Map<String,String> reference) {
		//
		NodeList list = entry.getChildNodes();
		
		//
		for (int i = 0; i < list.getLength(); i++) {
			Node propertyNode = list.item(i);
			if (propertyNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element propertyElement = (Element)propertyNode;
			String propertyKey = propertyElement.getTagName();
			
			Map<String,String> properties = new HashMap<>(reference);
			getTagsMasked(propertyElement, properties);
			
			map.put(propertyKey, properties);
		}
	}
}

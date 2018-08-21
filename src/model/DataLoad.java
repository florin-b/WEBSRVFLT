package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import beans.LatLng;
import enums.EnumZona;

public class DataLoad {

	

	public static List<LatLng> getZona(EnumZona zona) throws ParserConfigurationException, SAXException, IOException {

		List<LatLng> coords = new ArrayList<LatLng>();

		InputStream iStream = DataLoad.class.getClassLoader().getResourceAsStream(DataFiles.getDataFile(zona));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(iStream);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("trkpt");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				double lat = Double.parseDouble(eElement.getAttribute("lat"));
				double lon = Double.parseDouble(eElement.getAttribute("lon"));

				LatLng coord = new LatLng(lat, lon);

				coords.add(coord);

			}

		}

		return coords;

	}

}

package com.droegec.mwc.model.exporters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.WebTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLExporter extends Exporter {
	
	private final static DateFormat xml_sitemap_dateformat = new SimpleDateFormat("yyyy-MM-dd");

	public XMLExporter(WebTreeNode root) {
		super(root);
	}
	public XMLExporter(List<DRKLink> linkList, List<WebTreeNode> nodeList) {
		super(linkList, nodeList);
	}

	@Override
	public void exportNodeSitemap(BufferedWriter buffer) throws IOException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// No debería ocurrir
			e.printStackTrace();
		}
 
		// root element
		Document doc = docBuilder.newDocument();
		doc.setXmlVersion("1.0");
		Element urlset = doc.createElement("urlset");
		urlset.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
		urlset.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		urlset.setAttribute("xsi:schemaLocation", "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd");
		doc.appendChild(urlset);
 
 
		@SuppressWarnings("unchecked")
		Enumeration<WebTreeNode> en = root.depthFirstEnumeration();
		while (en.hasMoreElements()) {
			WebTreeNode n = en.nextElement();
			if (!n.isExternal() && n.getContentType() != null && n.getContentType().startsWith("text/html")) {
				// url
				Element url = doc.createElement("url");
				Element loc = doc.createElement("loc");
				loc.appendChild(doc.createTextNode(n.getURL().toString()));
				url.appendChild(loc);
				if (n.getLastModification() > 0) {
					Element lastmod = doc.createElement("lastmod");
					lastmod.appendChild(doc.createTextNode(xml_sitemap_dateformat.format(new Date(n.getLastModification()))));
					url.appendChild(lastmod);
				}
				
				urlset.appendChild(url);
			}
		}
		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		StreamResult result =  new StreamResult(buffer);
		try {
			
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
			
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void exportNodeLinks(BufferedWriter buffer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportLinksFirstLevel(BufferedWriter buffer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportNodeErrors(BufferedWriter buffer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportNodesFirstLevel(BufferedWriter buffer) throws IOException {
		// TODO Auto-generated method stub

	}

}

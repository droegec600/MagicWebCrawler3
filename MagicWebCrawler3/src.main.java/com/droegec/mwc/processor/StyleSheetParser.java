package com.droegec.mwc.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.WebTreeModel;
import com.droegec.mwc.model.WebTreeNode;

public class StyleSheetParser extends Parser {
	private static final long serialVersionUID = -9096942243295253518L;

	public StyleSheetParser(WebTreeNode nodo,  WebTreeModel model) {
		super(nodo, model);
	}
	
	/**
	 * Inicia el procesamiento de la URL.
	 * El parser obtiene los links de la URL con el que fue instanciado.
	 */
	public void parse() {

		findURLs();
	}

	@Override
	protected void findURLs() {

		String css = nodeContent(null);
		
		// url()
		Matcher regex = Pattern.compile("url\\(['\"]?([^'\"\\)]+)['\"]?\\)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(css);
		
		while(regex.find()) {
			String link = regex.group(1);
			
			if (link != null) {
				DRKLink newlink;
				try {
					
					if (link.startsWith("http://") || link.startsWith("https://"))
						newlink = new DRKLink("url", new URL(link), nodo, regex.start(), regex.end());
					else {
						String path = (nodo.getURL().getPath().length() > 0)?nodo.getURL().getPath().substring(0, nodo.getURL().getPath().lastIndexOf('/')+1):"/";
						if (link.startsWith("/"))
							newlink = new DRKLink("url", new URL(nodo.getURL().getProtocol() + "://" + nodo.getURL().getHost() + link), nodo, regex.start(), regex.end());
						else
							newlink = new DRKLink("url", new URL(nodo.getURL().getProtocol() + "://" + nodo.getURL().getHost() + compactPath(path + link)), nodo, regex.start(), regex.end());
					}
					
					nodo.addLink(newlink);
				}
				catch(MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		// @import 'custom.css';
		regex = Pattern.compile("@import ['\"]+([^'\"]+)['\"]+", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(css);
		
		while(regex.find()) {
			String link = regex.group(1);
			
			if (link != null) {
				DRKLink newlink;
				try {
					
					if (link.startsWith("http://") || link.startsWith("https://"))
						newlink = new DRKLink("import", new URL(link), nodo, regex.start(), regex.end());
					else {
						String path = (nodo.getURL().getPath().length() > 0)?nodo.getURL().getPath().substring(0, nodo.getURL().getPath().lastIndexOf('/')+1):"/";
						if (link.startsWith("/"))
							newlink = new DRKLink("import", new URL(nodo.getURL().getProtocol() + "://" + nodo.getURL().getHost() + link), nodo, regex.start(), regex.end());
						else
							newlink = new DRKLink("import", new URL(nodo.getURL().getProtocol() + "://" + nodo.getURL().getHost() + compactPath(path + link)), nodo, regex.start(), regex.end());
					}
					
					nodo.addLink(newlink);
				}
				catch(MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		

	}
	
	/* (non-Javadoc)
	 * @see ar.com.drk.drkspiderjava.processor.Parser#getSource(ProgressListener progress)
	 */
	@Override
	public String getSource(ProgressListener progress) {
		return nodeContent(progress);
	}
	
	@Override
	public String getParserMIMEType() {
		return "text/css.*";
	}

	@Override
	public void finishedGetContent(String content, boolean ok) {
	}	
}

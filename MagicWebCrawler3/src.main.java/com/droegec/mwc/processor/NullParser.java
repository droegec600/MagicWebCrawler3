package com.droegec.mwc.processor;

import com.droegec.mwc.model.WebTreeModel;
import com.droegec.mwc.model.WebTreeNode;

public class NullParser extends Parser {
	private static final long serialVersionUID = -7500992977392870297L;

	public NullParser(WebTreeNode nodo,  WebTreeModel model) {
		super(nodo, model);
	}
	
	/**
	 * Este parser solo lee el tipo de contenido de la URL.
	 */
	public void parse() {

		nodeContent(null); // Proceso la descarga del contenido
	}

	@Override
	public String getParserMIMEType() {
		return null;
	}
	
	@Override
	public void finishedGetContent(String content, boolean ok) {
	}
}

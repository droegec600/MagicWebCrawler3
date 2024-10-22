package com.droegec.mwc.model.exporters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.WebTreeNode;

public class PlainTextExporter extends Exporter {
	
	public PlainTextExporter(WebTreeNode root) {
		super(root);
	}
	public PlainTextExporter(List<DRKLink> linkList, List<WebTreeNode> nodeList) {
		super(linkList, nodeList);
	}

	@Override
	public void exportNodeSitemap(BufferedWriter buffer) throws IOException {
		@SuppressWarnings("unchecked")
		Enumeration<WebTreeNode> en = root.depthFirstEnumeration();
		while (en.hasMoreElements()) {
			WebTreeNode n = en.nextElement();
			if (!n.isExternal() && n.getContentType() != null && n.getContentType().startsWith("text/html"))
				buffer.write(n.getURL().toString()+"\n");
		}
	}
	
	@Override
	public void exportNodeLinks(BufferedWriter buffer) throws IOException {
		@SuppressWarnings("unchecked")
		Enumeration<WebTreeNode> en = root.depthFirstEnumeration();
		while (en.hasMoreElements()) {
			WebTreeNode n = en.nextElement();
			buffer.write(n.getURL().toString()+"\n");
		}
	}
	
	@Override
	public void exportNodeErrors(BufferedWriter buffer) throws IOException {
		for (DRKLink l : root.getBrokenLinks(true)) {
			buffer.write(l.getURL().toString()+"\n");
		}
	}
	
	@Override
	public void exportLinksFirstLevel(BufferedWriter buffer) throws IOException {
		for (DRKLink l : ((linkList == null)?root.getLinks():linkList)) {
			buffer.write(l.getURL().toString()+"\n");
		}
	}

	@Override
	public void exportNodesFirstLevel(BufferedWriter buffer) throws IOException {
		for (WebTreeNode n : nodeList) {
			buffer.write(n.getURL().toString()+"\n");
		}
	}
	
}

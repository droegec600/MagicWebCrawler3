package com.droegec.mwc.model.exporters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.WebTreeNode;

public class CSVExporter extends Exporter {
	
	public CSVExporter(WebTreeNode root) {
		super(root);
	}
	public CSVExporter(List<DRKLink> linkList, List<WebTreeNode> nodeList) {
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
		buffer.write("\"ID\",\"Parent ID\",\"Link\",\"Parent link\",\"Status\",\"Content type\",\"Last modified\",\"Title\",\"Description\",\"Keywords\",\"Size in bytes\",\"Is noindex\",\"SEO score %\"\n");
		@SuppressWarnings("unchecked")
		Enumeration<WebTreeNode> en = root.depthFirstEnumeration();
		while (en.hasMoreElements()) {
			WebTreeNode n = en.nextElement();
			StringBuilder str = new StringBuilder("\"");
			str.append(n.hashCode());
			str.append("\",\"");
			str.append(((n.getParent() != null)?n.getParent().hashCode():""));
			str.append("\",\"");
			str.append(n.getURL().toString());
			str.append("\",\"");
			str.append((n.getParent() != null)?((WebTreeNode)n.getParent()).getURL().toString():"");
			str.append("\",\"");
			str.append(n.getStatus());
			str.append("\",\"");
			str.append(n.getContentType());
			str.append("\",\"");
			str.append(n.getLastModificationString());
			str.append("\",\"");
			str.append(n.getTitle());
			str.append("\",\"");
			str.append(n.getMeta("Description"));
			str.append("\",\"");
			str.append(n.getMeta("Keywords"));
			str.append("\",\"");
			str.append(n.getSize());
			str.append("\",\"");
			str.append((n.isNoIndex())?"Yes":"No");
			str.append("\",\"");
			str.append((n.isHTML())?n.getSEOReview().getResult():"");
			str.append("\"\n");
			buffer.write(str.toString());
		}
	}
	
	@Override
	public void exportNodeErrors(BufferedWriter buffer) throws IOException {
		buffer.write("\"Error link\",\"Status\",\"Parent\",\"Tag\",\"Anchor\",\"nofollow\",\"External\"\n");
		for (DRKLink l : root.getBrokenLinks(true)) {
			StringBuilder str = new StringBuilder("\"");
			str.append(l.getURL().toString());
			str.append("\",\"");
			str.append(l.getNode().getStatus());
			str.append("\",\"");
			str.append((l.getNode().getParent() != null)?((WebTreeNode)l.getNode().getParent()).getURL().toString():"");
			str.append("\",\"");
			str.append(l.getTag());
			str.append("\",\"");
			str.append(l.getAnchor());
			str.append("\",\"");
			str.append((l.isNoFollow())?"Yes":"No");
			str.append("\",\"");
			str.append((l.isExternal())?"Yes":"No");
			str.append("\"\n");
			buffer.write(str.toString());
		}
	}
	
	@Override
	public void exportLinksFirstLevel(BufferedWriter buffer) throws IOException {
		buffer.write("\"Link\",\"Status\",\"Parent\",\"Tag\",\"Anchor\",\"nofollow\",\"External\",\"Depth\"\n");
		for (DRKLink l : ((linkList == null)?root.getLinks():linkList)) {
			StringBuilder str = new StringBuilder("\"");
			str.append(l.getURL().toString());
			str.append("\",\"");
			str.append(l.getNode().getStatus());
			str.append("\",\"");
			str.append((l.getNode().getParent() != null)?((WebTreeNode)l.getNode().getParent()).getURL().toString():"");
			str.append("\",\"");
			str.append(l.getTag());
			str.append("\",\"");
			str.append(l.getAnchor());
			str.append("\",\"");
			str.append((l.isNoFollow())?"Yes":"No");
			str.append("\",\"");
			str.append((l.isExternal())?"Yes":"No");
			str.append("\",\"");
			str.append(l.getDepth());
			str.append("\"\n");
			buffer.write(str.toString());
		}
	}

	@Override
	public void exportNodesFirstLevel(BufferedWriter buffer) throws IOException {
		buffer.write("\"Link\",\"Status\",\"Parent\",\"Content type\",\"Last modified\",\"Title\",\"Description\",\"Keywords\",\"Size in bytes\",\"Is noindex\",\"SEO score %\"\n");
		for (WebTreeNode n : nodeList) {
			StringBuilder str = new StringBuilder("\"");
			str.append(n.getURL().toString());
			str.append("\",\"");
			str.append(n.getStatus());
			str.append("\",\"");
			str.append((n.getParent() != null)?((WebTreeNode)n.getParent()).getURL().toString():"");
			str.append("\",\"");
			str.append(n.getContentType());
			str.append("\",\"");
			str.append(n.getLastModificationString());
			str.append("\",\"");
			str.append(n.getTitle());
			str.append("\",\"");
			str.append(n.getMeta("Description"));
			str.append("\",\"");
			str.append(n.getMeta("Keywords"));
			str.append("\",\"");
			str.append(n.getSize());
			str.append("\",\"");
			str.append((n.isNoIndex())?"Yes":"No");
			str.append("\",\"");
			str.append((n.isHTML())?n.getSEOReview().getResult():"");
			str.append("\"\n");
			buffer.write(str.toString());
		}
	}
	
}

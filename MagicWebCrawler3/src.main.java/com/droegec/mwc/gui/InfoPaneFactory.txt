package com.droegec.mwc.gui;

import javax.swing.JPanel;

import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.model.WebTreeNode;

public class InfoPaneFactory {

	static public JPanel getInfoPane(MainWindow wnd, WebTreeNode node, ProgressListener progress) {
		
		if (node.getContentType() != null && node.getContentType().startsWith("text/html"))
			return new HTMLInfoPane(wnd, node, progress);
		
		return new GenericNodeInfoPane(node);
	}
}

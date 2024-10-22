package com.droegec.mwc.gui.panes;

import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.model.WebTreeNode;

public class InfoPaneFactory {

	static public InfoPane getInfoPane(MainWindow wnd, WebTreeNode node, ProgressListener progress) {
		
		if (node.getContentType() != null && node.getContentType().startsWith("text/html"))
			return new HTMLInfoPane(wnd, node, progress);
		if (node.getContentType() != null && node.getContentType().startsWith("image/"))
			return new ImageInfoPane(wnd, node, progress);
		if (node.getContentType() != null && node.getContentType().startsWith("text/javascript"))
			return new JavascriptInfoPane(wnd, node, progress);
		if (node.getContentType() != null && node.getContentType().startsWith("text/css"))
			return new StyleSheetInfoPane(wnd, node, progress);
		
		return new GenericNodeInfoPane(wnd, node, progress);
	}
}

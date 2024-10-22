package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.gui.MainWindow;

public class SearchNodeGoogleLinks extends AbstractSpiderAction {
	private static final long serialVersionUID = -4424035248257321000L;

	public SearchNodeGoogleLinks(MainWindow wnd, String text) {
		super(wnd, text, new ImageIcon("/images/world_link.png"));
		
		putValue(SHORT_DESCRIPTION, "Search links to this URL in Google index");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode())
			DRKTools.showInBrowser("http://www.google.com/search?q=link:"+wnd.getSelectedNode().getURL().toString(), null);
	}

}

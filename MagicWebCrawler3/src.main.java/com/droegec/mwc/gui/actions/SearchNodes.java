package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.gui.SearchNodesDlg;
import com.droegec.mwc.gui.ShowNodes;

public class SearchNodes extends AbstractSpiderAction {
	private static final long serialVersionUID = -1198095486157419863L;

	public SearchNodes(MainWindow wnd, String text) {
		super(wnd, text);
	}

	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode()) {
			SearchNodesDlg dlg = new SearchNodesDlg(wnd);
			if (dlg.ok) {
				System.out.println ("SearchNodes");
				new ShowNodes(wnd, "Error links", wnd.getSelectedNode().getNodesMatching(dlg.title, dlg.title_regex, dlg.description, dlg.description_regex, 
						dlg.keywords, dlg.keywords_regex, dlg.author, dlg.author_regex, dlg.noindex));
			}
		}
	}

}

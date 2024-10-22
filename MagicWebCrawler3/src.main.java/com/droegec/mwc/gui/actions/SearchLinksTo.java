package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.gui.SearchLinksDlg;
import com.droegec.mwc.gui.ShowLinks;

public class SearchLinksTo extends AbstractSpiderAction {
	private static final long serialVersionUID = -1198095486157419863L;

	public SearchLinksTo(MainWindow wnd, String text) {
		super(wnd, text);
	}

	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode()) {
			SearchLinksDlg dlg = new SearchLinksDlg(wnd);
			if (dlg.ok) {
				new ShowLinks(wnd, "Link search...", wnd.getSelectedNode().getLinksTo(dlg.tag, dlg.url, dlg.url_regex, dlg.anchor, dlg.anchor_regex, dlg.error, dlg.nofollow, dlg.external));
			}
		}
	}

}

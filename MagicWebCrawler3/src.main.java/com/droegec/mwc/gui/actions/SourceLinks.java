package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.gui.ShowLinks;

public class SourceLinks extends AbstractSpiderAction {
	private static final long serialVersionUID = -1198095486157419863L;

	public SourceLinks(MainWindow wnd, String text) {
		super(wnd, text);
	}

	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode()) {
			new ShowLinks(wnd, "Source links...", wnd.getSelectedNode().getSourceLinksDeep());
		}
	}

}

package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.gui.MainWindow;

public class ShowNodeInBrowser extends AbstractSpiderAction {
	private static final long serialVersionUID = -6948588469727477764L;

	public ShowNodeInBrowser(MainWindow wnd, String text) {
		super(wnd, text, new ImageIcon("/images/link_go.png"));
		putValue(SHORT_DESCRIPTION, "Open this URL in your default browser");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode())
			DRKTools.showInBrowser(wnd.getSelectedNode().getURL().toString(), null);
	}

}

package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.gui.SearchWithGoogle;

public class GoogleSearch extends AbstractSpiderAction {
	private static final long serialVersionUID = -215590814971257589L;

	public GoogleSearch(MainWindow wnd, String text) {
		super(wnd, text, new ImageIcon("/images/find.png"));
		
		putValue(SHORT_DESCRIPTION, "Search within this domain using Google index");
	}

	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode())
			new SearchWithGoogle(wnd, wnd.getSelectedNode().getURL().toString());
	}

}

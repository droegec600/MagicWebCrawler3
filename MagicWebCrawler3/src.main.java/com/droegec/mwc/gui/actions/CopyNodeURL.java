package com.droegec.mwc.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import com.droegec.mwc.gui.MainWindow;

public class CopyNodeURL extends AbstractSpiderAction {

	private static final long serialVersionUID = 4667189107899389288L;

	public CopyNodeURL(MainWindow wnd, String text) {
		super(wnd, text);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if (wnd.isThereSelectedNode()) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(wnd.getSelectedNode().getURL().toString()), wnd);
		}
	}
}

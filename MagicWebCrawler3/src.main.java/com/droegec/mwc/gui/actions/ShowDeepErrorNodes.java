package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import com.droegec.mwc.gui.ShowNodes;
import com.droegec.mwc.gui.MainWindow;

public class ShowDeepErrorNodes extends AbstractSpiderAction {
	private static final long serialVersionUID = 2650626689328938107L;

	public ShowDeepErrorNodes(MainWindow wnd, String text) {
		super(wnd, text);
	}

	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode())
			new ShowNodes(wnd, "Error nodes (deep)", wnd.getSelectedNode().getErrorNodes(true));
	}
	
}

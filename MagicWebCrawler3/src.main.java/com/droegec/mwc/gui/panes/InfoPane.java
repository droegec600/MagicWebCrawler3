package com.droegec.mwc.gui.panes;

import javax.swing.JPanel;

import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.NodeTextLocation;

public abstract class InfoPane extends JPanel {
	private static final long serialVersionUID = 1531186708101697227L;

	abstract public void locateLink(DRKLink link);

	abstract public void locateContent(NodeTextLocation gotoContent);
}

package com.droegec.mwc.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.model.WebTreeNode;

public class SpiderTreeCellRederer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -2802851534547972945L;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		WebTreeNode node = (WebTreeNode)value;

		if (node.getStatus() >= 200 && node.getStatus() < 400)
			setIcon(DRKTools.OK_ICON);
		else if (node.getStatus() >= 400 && node.getStatus() < 600)
			setIcon(DRKTools.BAD_ICON);
		else if (node.getStatus() == -1)
			setIcon(DRKTools.FORBIDEN_ICON);
		else
			setIcon(DRKTools.UK_ICON);

		return this;
	}

}

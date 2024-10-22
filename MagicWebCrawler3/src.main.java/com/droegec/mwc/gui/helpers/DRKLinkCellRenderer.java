package com.droegec.mwc.gui.helpers;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.droegec.mwc.model.DRKLink;

public class DRKLinkCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = -4416015327928828499L;
	final private List<DRKLink> links;
	
	public DRKLinkCellRenderer(List<DRKLink> links) {
		this.links = links;
	}

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        JComponent l = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        Color fg_color;
        int status = links.get(table.getRowSorter().convertRowIndexToModel(row)).getNode().getStatus();
        if (status >= 300 && status < 400) {
        	fg_color = Color.ORANGE;
        }
        else if (status >= 400 && status < 500) {
        	fg_color = Color.RED;
        }
        else if (status >= 500) {
        	fg_color = Color.YELLOW;
        }
        else {
        	fg_color = (isSelected)?UIManager.getDefaults().getColor("TextField.selectionForeground"):UIManager.getDefaults().getColor("TextField.foreground");
        }
        l.setForeground(fg_color);

        return l;
    }
}

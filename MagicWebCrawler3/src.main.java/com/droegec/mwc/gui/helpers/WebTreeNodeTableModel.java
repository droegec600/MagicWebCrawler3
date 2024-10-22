package com.droegec.mwc.gui.helpers;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.droegec.mwc.model.WebTreeNode;

public class WebTreeNodeTableModel implements TableModel {
	
	final private List<WebTreeNode> nodes;
	
	public WebTreeNodeTableModel(List<WebTreeNode> nodes) {
		this.nodes = nodes;
	}

	@Override
	public int getRowCount() {
		return nodes.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "URL";
		case 1:
			return "ContentType";
		case 2:
			return "Status";
		case 3:
			return "Size";
		case 4:
			return "Is noindex";
		default:
			return "";
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return Integer.class;
		case 3:
			return Integer.class;
		case 4:
			return Boolean.class;
		default:
			return Object.class;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return nodes.get(rowIndex).getURL().toString();
		case 1:
			return nodes.get(rowIndex).getContentType();
		case 2:
			return nodes.get(rowIndex).getStatus();
		case 3:
			return nodes.get(rowIndex).getSize();
		case 4:
			return nodes.get(rowIndex).isNoIndex();
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}
}

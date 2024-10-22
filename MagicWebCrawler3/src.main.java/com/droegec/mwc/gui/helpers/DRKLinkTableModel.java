package com.droegec.mwc.gui.helpers;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.droegec.mwc.model.DRKLink;

public class DRKLinkTableModel implements TableModel {

	private final List<DRKLink> links;
	
	public DRKLinkTableModel(List<DRKLink> links) {
		this.links = links;
	}
	
	@Override
	public int getRowCount() {
		return links.size();
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Tag";
		case 1:
			return "URL";
		case 2:
			return "Anchor";
		case 3:
			return "HTTP Status";
		case 4:
			return "nofollow";
		case 5:
			return "External";
		case 6:
			return "Depth";
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
			return String.class;
		case 3:
			return Integer.class;
		case 4:
			return Boolean.class;
		case 5:
			return Boolean.class;
		case 6:
			return Integer.class;
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
			return links.get(rowIndex).getTag();
		case 1:
			return links.get(rowIndex).getURL().toString();
		case 2:
			return links.get(rowIndex).getAnchor();
		case 3:
			return (links.get(rowIndex).getNode() != null)?links.get(rowIndex).getNode().getStatus():0;
		case 4:
			return links.get(rowIndex).isNoFollow();
		case 5:
			return links.get(rowIndex).isExternal();
		case 6:
			return links.get(rowIndex).getDepth();
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

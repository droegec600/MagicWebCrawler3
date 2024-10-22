package com.droegec.mwc.gui.helpers;

import java.net.HttpCookie;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CookieTableModel implements TableModel {
	
	final private List<HttpCookie> cookies;

	public CookieTableModel(List<HttpCookie> cookies) {
		this.cookies = cookies;
	}
	
	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public int getRowCount() {
		return cookies.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Name";
		case 1:
			return "Value";
		case 2:
			return "Path";
		case 3:
			return "Domain";
		case 4:
			return "Has expired";
		case 5:
			return "Is secure";
		default:
			return "";
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return cookies.get(rowIndex).getName();
		case 1:
			return cookies.get(rowIndex).getValue();
		case 2:
			return cookies.get(rowIndex).getPath();
		case 3:
			return cookies.get(rowIndex).getDomain();
		case 4:
			return cookies.get(rowIndex).hasExpired();
		case 5:
			return cookies.get(rowIndex).getSecure();
		default:
			return null;
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
			return String.class;
		case 4:
			return Boolean.class;
		case 5:
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
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

}

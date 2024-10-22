package com.droegec.mwc.gui.helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.droegec.mwc.model.MetaTag;

public class MetaTagTableModel implements TableModel {

	private final Map<String, MetaTag> meta;
	private final List<String> keys;
	
	public MetaTagTableModel(Map<String, MetaTag> meta) {
		this.meta = meta;
		this.keys = new LinkedList<String>(meta.keySet());
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int c) {
		return (c==0)?"Name":"Content";
	}

	@Override
	public int getRowCount() {
		return meta.keySet().size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		if (c==0)
			return keys.get(r);
		else
			return meta.get(keys.get(r)).toString();
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
	}

	public MetaTag getModelItemAt(int r) {
		return meta.get(keys.get(r));
	}

}

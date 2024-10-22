package com.droegec.mwc.gui.helpers;

import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.TableColumnModel;

/**
 * Esta clase se encarga de mantener las preferencias
 * de las columnas de una tabla de edición genérica para
 * cada entidad y ventana en la que aparece.
 * @author Leandro
 */
public class TablePreferences extends WindowAdapter {

	private final JTable table;
	private final Preferences entity_p;
	private Preferences p;
	private Window window = null;

	/**
	 * Una vez construido, el decorador no tiene efecto hasta que encuentra
	 * la ventana en la que se colocó la tabla.
	 * @param table tabla a la cual se conectará este decorador
	 * @param reference una clase para referencia, usualmente la ventana
	 * @param subreference una cadena para poder tener distintas preferencias en una misma ventana
	 */
	public TablePreferences(JTable table, Class<?> reference, String subreference) {
		this.table = table;
		/*
		 * Este es el nodo de preferencias para la entidad que nos pasaron.
		 * Más tarde generaremos el nodo correspondiente a la cambinación de esta
		 * entidad y de la ventana en la que nos encontremos.
		 */
		if (subreference != null)
			this.entity_p = Preferences.userNodeForPackage(reference).node(reference.getName()+"."+subreference);
		else
			this.entity_p = Preferences.userNodeForPackage(reference).node(reference.getName());
		
		addWindowObserver();
	}
	
	/**
	 * Una vez construido, el decorador no tiene efecto hasta que encuentra
	 * la ventana en la que se colocó la tabla.
	 * @param table tabla a la cual se conectaró este decorador
	 * @param reference entidad de la que se trata
	 */
	public TablePreferences(JTable table, Class<?> reference) {
		this(table, reference, null);
	}

	protected void addWindowObserver() {
		/*
		 * INTERESANTE: Tenemos que esperar a que se establezca la paternidad de la
		 * tabla, antes de poder encontrar la ventana en la que se encuentra. Para esto
		 * observamos cuando entra un ancestro y nos fijamos si es una ventana en ese
		 * momento. Si es así, le instalamos un observador de ventana para guardar los
		 * datos al cerrar. 
		 */
		table.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent arg0) {
				if (window == null &&
					TablePreferences.this.table.getTopLevelAncestor() instanceof Window) {
					TablePreferences.this.window = ((Window)TablePreferences.this.table.getTopLevelAncestor());
					TablePreferences.this.p = entity_p.node(window.getClass().getName());
					initialize(); // Antes de conectarme a los eventos
					window.addWindowListener(TablePreferences.this);
					table.addHierarchyListener(new HierarchyListener() {
						@Override
						public void hierarchyChanged(HierarchyEvent e) {
							saveState();						}
					});
				}
			}
			public void ancestorMoved(AncestorEvent arg0) {
			}
			public void ancestorRemoved(AncestorEvent arg0) {
			}
		});
	}
	
	protected void initialize() {
		if (p.getBoolean("tcm.hasData", false)) {
			TableColumnModel cm = table.getColumnModel();
			/*
			 * Este código resulve el problema de ubicar cada columna en su lugar.
			 * Es peligroso ya que se detiene cuando termina con todas las columnas. 
			 */
			boolean keepGoing;
			int loops = 0;
			do {
				keepGoing = false;
				++loops;
				for (int i = 0; i < cm.getColumnCount(); i++) {
					int index = p.getInt("tcm.c_"+i+".mindex", i);
					if (index != cm.getColumn(i).getModelIndex()) {
						table.moveColumn(i, index);
						keepGoing = true;
						break;
					}				
				}
				if (loops > 1000)
					throw new RuntimeException("Más de 1000 iteraciones para ordenar las cabeceras.");
			}
			while(keepGoing);
			// Ancho de las columnas
			for (int i = 0; i < cm.getColumnCount(); i++) {
				cm.getColumn(i).setPreferredWidth(p.getInt("tcm.c_"+i+".width", 100));
			}
			// Order
			if (p.getBoolean("tcm.sort.isset", false)) {
				SortOrder orders[] = SortOrder.values();
				List<SortKey> sks = new LinkedList<RowSorter.SortKey>(); 
				sks.add(new SortKey(p.getInt("tcm.sort.column", 0), orders[p.getInt("tcm.sort.order", 0)]));
				table.getRowSorter().setSortKeys(sks);
			}
			// Ventana
			if (window != null) {
				window.setLocation(p.getInt("tcm.window.x", 30), p.getInt("tcm.window.y", 30));
				window.setSize(p.getInt("tcm.window.width", 650), p.getInt("tcm.window.height", 350));
			}
		}
		else {
			saveState();
		}
	}

	private void saveState() {
		p.putBoolean("tcm.hasData", true);
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			p.putInt("tcm.c_"+i+".width", table.getColumnModel().getColumn(i).getWidth());
		}
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			p.putInt("tcm.c_"+i+".mindex", table.getColumnModel().getColumn(i).getModelIndex());
		}
		List<? extends SortKey> s = table.getRowSorter().getSortKeys();
		if (!s.isEmpty()) {
			p.putBoolean("tcm.sort.isset", true);
			p.putInt("tcm.sort.column", s.get(0).getColumn());
			p.putInt("tcm.sort.order", s.get(0).getSortOrder().ordinal());
		}
		else {
			p.putBoolean("tcm.sort.isset", false);
		}
		if (window != null) {
			p.putInt("tcm.window.x", window.getX());
			p.putInt("tcm.window.y", window.getY());
			p.putInt("tcm.window.width", window.getWidth());
			p.putInt("tcm.window.height", window.getHeight());
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// Guardo la información
		saveState();
		super.windowClosing(e);
	}

}

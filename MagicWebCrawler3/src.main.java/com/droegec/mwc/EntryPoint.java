package com.droegec.mwc;

import javax.swing.UIManager;

import com.droegec.mwc.gui.MainWindow;

/**
 *  *
 * @author _Leo_
 */
public class EntryPoint {

	/**
	 *
	 *
	 *
	 * */
	public static void main(String[] args) {
		// Lanzamos la GUI y terminamos...
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	/**
	 * Muestra la ventana principal.
	 *
	 */
    private static void createAndShowGUI() {

    	// Look and feel default de la plataforma
        try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        // Lanzamos la ventana principal
        MainWindow wnd = new MainWindow();
        wnd.setVisible(true);
    }
 
}

package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.droegec.mwc.gui.MainWindow;

public class AbstractSpiderAction extends AbstractAction {
	private static final long serialVersionUID = 2538443210005825825L;
	final protected MainWindow wnd;
	
	public AbstractSpiderAction(MainWindow wnd, String text) {
		super(text);
		this.wnd = wnd;
	}
	
	public AbstractSpiderAction(MainWindow wnd, String text, Icon icon) {
		super(text, icon);
		this.wnd = wnd;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}

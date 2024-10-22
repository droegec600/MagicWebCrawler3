package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.droegec.mwc.gui.MainWindow;

public class SiteWideSeoReview extends AbstractSpiderAction {
	private static final long serialVersionUID = 9029528498084641862L;

	public SiteWideSeoReview(MainWindow wnd, String text) {
		super(wnd, text, new ImageIcon("/images/site_wide_seo.png"));
		putValue(SHORT_DESCRIPTION, "Show SEO analysis for this website");
	}

	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(wnd, "The overall score for this site is "+wnd.getDocument().getSEOResult().getResult()+"%", "Site wide SEO", JOptionPane.INFORMATION_MESSAGE);
	}

}

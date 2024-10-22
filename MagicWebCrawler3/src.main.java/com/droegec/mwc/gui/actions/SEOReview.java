package com.droegec.mwc.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.gui.SEOReviewDlg;
import com.droegec.mwc.gui.actions.AbstractSpiderAction;

public class SEOReview extends AbstractSpiderAction {
	private static final long serialVersionUID = 4457264615083190785L;

	public SEOReview(MainWindow wnd, String text) {
		// super(wnd, text, new ImageIcon(SEOReview.class.getResource("/images/seo.png")));
		super(wnd, text, null);
		putValue(SHORT_DESCRIPTION, "Show SEO analysis for this node");
	}

	public void actionPerformed(ActionEvent e) {
		if (wnd.isThereSelectedNode())
			new SEOReviewDlg(wnd, wnd.getSelectedNode());
	}

}

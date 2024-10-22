package com.droegec.mwc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.model.WebTreeNode;

public class ErrorLinks extends JDialog {
	private static final long serialVersionUID = 3588614949379078595L;
	private final JPanel contentPanel = new JPanel();
	private final List<WebTreeNode> errors;
	private ListModel<String> model = new AbstractListModel<String>() {
		private static final long serialVersionUID = -3610378275058951159L;

		@Override
		public String getElementAt(int i) {
			return errors.get(i).getURL().toString();
		}

		@Override
		public int getSize() {
			return errors.size();
		}
	};
	private final JList<String> list = new JList<String>(model);
	private final Action action_go = new SwingAction();
	private final ListSelectionListener listener = new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			action_go.setEnabled(list.getSelectedIndex() >= 0);
		}
	};
	
	/**
	 * Create the dialog.
	 * 

	 */
	public ErrorLinks(Frame owner, List<WebTreeNode> errors) {
		super(owner);
		this.errors = errors;
		setTitle("Error links");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.setLayout(new BorderLayout());
			scrollPane.setViewportView(list);
			contentPanel.add(scrollPane, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				JButton btnGo = new JButton("Show...");
				btnGo.setEnabled(false);
				btnGo.setAction(action_go);
				buttonPane.add(btnGo);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		list.addListSelectionListener(listener);
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        if (evt.getClickCount() == 2 && action_go.isEnabled()) {
		        	action_go.actionPerformed(new ActionEvent(evt, 0, ""));
		        }
		    }
		});		setLocation(DRKTools.getCenterLocation(this));
		setVisible(true);
	}

	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = 2094257015305105483L;
		public SwingAction() {
			setEnabled(false);
			putValue(NAME, "Go...");
			putValue(SHORT_DESCRIPTION, "Go to node");
		}
		public void actionPerformed(ActionEvent e) {
			((MainWindow)getOwner()).gotoNode(ErrorLinks.this.errors.get(list.getSelectedIndex()));
		}
	}
}

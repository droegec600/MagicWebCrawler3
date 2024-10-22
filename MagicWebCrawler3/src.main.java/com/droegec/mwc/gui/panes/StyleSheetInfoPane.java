package com.droegec.mwc.gui.panes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.gui.MainWindow;
import com.droegec.mwc.gui.helpers.DRKLinkCellRenderer;
import com.droegec.mwc.gui.helpers.DRKLinkTableModel;
import com.droegec.mwc.gui.helpers.TablePreferences;
import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.NodeTextLocation;
import com.droegec.mwc.model.WebTreeNode;

@SuppressWarnings("serial")
public class StyleSheetInfoPane extends InfoPane {

	final private WebTreeNode nodo;
	private JLabel lbl_size = null;
	private JLabel size = null;
	private JLabel lbl_content_type = null;
	private JLabel contenttype = null;
	private JLabel lblstatus = null;
	private JTextField status = null;
	private RTextScrollPane src_scroll = null;
	private RSyntaxTextArea source = null;
	private ProgressListener progress;
	private JTable links_table;
	
	/**
	 * This is the default constructor
	 */
	public StyleSheetInfoPane(MainWindow wnd, WebTreeNode nodo, ProgressListener progress) {
		super();
		this.nodo = nodo;
		this.progress = progress;
		initialize();
		
		contenttype.setText(nodo.getContentType());
		size.setText(String.valueOf(nodo.getSize())+" bytes");
		status.setText(nodo.getStatus()+" - "+nodo.getError());

		getSource();
	}
	
	protected void getSource() {
		StyleSheetInfoPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		new Thread() {
			@Override
			public void run() {
				final String src = nodo.getSource(progress);
				
				SwingUtilities.invokeLater(new Runnable() {
					
					public void run() {
						source.setText(src); source.setCaretPosition(0);
						StyleSheetInfoPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				});
			}
		}.start();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.gridy = 0;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.insets = new Insets(3, 3, 5, 3);
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints4.gridy = 0;
		lblstatus = new JLabel();
		lblstatus.setText("Status:");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(3, 3, 5, 3);
		gridBagConstraints2.gridy = 1;
		contenttype = new JLabel();
		contenttype.setText("");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints11.anchor = GridBagConstraints.EAST;
		gridBagConstraints11.gridy = 1;
		lbl_content_type = new JLabel();
		lbl_content_type.setText("Content type:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new Insets(3, 3, 5, 3);
		gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 2;
		size = new JLabel();
		size.setText("0 bytes");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.gridy = 2;
		lbl_size = new JLabel();
		lbl_size.setText("Size:");
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		this.setSize(539, 395);
		this.add(lbl_size, gridBagConstraints);
		this.add(size, gridBagConstraints1);
		this.add(lbl_content_type, gridBagConstraints11);
		this.add(contenttype, gridBagConstraints2);
		this.add(lblstatus, gridBagConstraints4);
		this.add(getStatus(), gridBagConstraints5);
		
		JScrollPane links_scroll = new JScrollPane();
		links_scroll.setBorder(new TitledBorder(null, "Links", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_links_scroll = new GridBagConstraints();
		gbc_links_scroll.weighty = 0.2;
		gbc_links_scroll.gridwidth = 3;
		gbc_links_scroll.insets = new Insets(0, 0, 5, 5);
		gbc_links_scroll.fill = GridBagConstraints.BOTH;
		gbc_links_scroll.gridx = 0;
		gbc_links_scroll.gridy = 3;
		add(links_scroll, gbc_links_scroll);
		
		links_table = new JTable();
		links_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		links_table.setAutoCreateRowSorter(true);
		links_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		links_table.setModel(new DRKLinkTableModel(nodo.getLinks()));
		links_table.setDefaultRenderer(Object.class, new DRKLinkCellRenderer(nodo.getLinks()));
		links_table.setDefaultRenderer(Integer.class, new DRKLinkCellRenderer(nodo.getLinks()));
		links_table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel selection = (ListSelectionModel)e.getSource();
				
				if (selection.getMinSelectionIndex() >= 0) {
					DRKLink link = StyleSheetInfoPane.this.nodo.getLinks().get(links_table.getRowSorter().convertRowIndexToModel(selection.getMinSelectionIndex()));
					highlightLink(link);
				}
			}
		});
		new TablePreferences(links_table, DRKLink.class, "link");
		
		
		
		links_scroll.setViewportView(links_table);
		
		source = new RSyntaxTextArea();
		source.setEditable(false);
		source.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
		source.setCodeFoldingEnabled(DRKTools.prefs.getBoolean("use_code_folding", false));
		source.setAntiAliasingEnabled(true);
		source.setUseSelectedTextColor(true);
		src_scroll = new RTextScrollPane();
		src_scroll.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CSS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		src_scroll.setViewportView(source);
		src_scroll.setFoldIndicatorEnabled(true);
		src_scroll.setLineNumbersEnabled(true);
		
		GridBagConstraints gbc_src_scroll = new GridBagConstraints();
		gbc_src_scroll.weighty = 0.8;
		gbc_src_scroll.weightx = 1.0;
		gbc_src_scroll.gridwidth = 2;
		gbc_src_scroll.fill = GridBagConstraints.BOTH;
		gbc_src_scroll.gridx = 0;
		gbc_src_scroll.gridy = 4;
		add(src_scroll, gbc_src_scroll);
		
	}

	@Override
	public void locateLink(DRKLink link) {
		int index = nodo.getLinks().indexOf(link);
		int ti = links_table.getRowSorter().convertRowIndexToView(index);
		links_table.getSelectionModel().setSelectionInterval(ti, ti);
		highlightLink(link);
	}
	

	public void highlightLink(final DRKLink link) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				source.requestFocusInWindow();
				src_scroll.scrollRectToVisible(source.getBounds());
				source.setSelectionStart(link.getStart());
				source.setSelectionEnd(link.getEnd());
				source.setSelectionColor( (link.getNode() != null && link.getNode().hasError())?Color.RED:Color.BLUE );
			}
		});
	}
	
	/**
	 * This method initializes status	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStatus() {
		if (status == null) {
			status = new JTextField();
			status.setEditable(false);
		}
		return status;
	}

	@Override
	public void locateContent(NodeTextLocation gotoContent) {
	}

}

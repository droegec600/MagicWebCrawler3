package com.droegec.mwc.gui.panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
import com.droegec.mwc.gui.helpers.MetaTagTableModel;
import com.droegec.mwc.gui.helpers.TablePreferences;
import com.droegec.mwc.model.DRKLink;
import com.droegec.mwc.model.MetaTag;
import com.droegec.mwc.model.NodeTextLocation;
import com.droegec.mwc.model.WebTreeNode;

@SuppressWarnings("serial")
public class HTMLInfoPane extends InfoPane {

	final private WebTreeNode nodo;
	final private ProgressListener progress;
	private JLabel lbl_size = null;
	private JLabel size = null;
	private JLabel lbl_content_type = null;
	private JLabel contenttype = null;
	private RTextScrollPane src_scroll = null;
	private RSyntaxTextArea source = null;
	private JScrollPane links_scroll = null;
	private JTable links = null;
	private JLabel lblStatus = null;
	private JTextField status = null;
	private JLabel lblpr = null;
	private JTextField keywords = null;
	private JLabel lbldesc = null;
	private JLabel lbltitle = null;
	private JTextField html_title = null;
	private JTextArea html_desc = null;
	private JLabel lblaut = null;
	private JTextField html_author = null;
	private JLabel lblicon = null;
	private JLabel icon = null;
	private JLabel lblil = null;
	private JLabel lblel = null;
	private JLabel lblnl = null;
	private JLabel internal_links = null;
	private JLabel external_links = null;
	private JLabel nofollow_links = null;
	private JScrollPane html_desc_scroll;
	private JSplitPane top_panel;
	private JPanel controls_panel;
	private JLabel lblRobots;
	private JTextField robots;
	private JScrollPane meta_scroll;
	private JTable meta_tags;
	private MetaTagTableModel meta_tag_model;
	private JLabel lblSourceLinks;
	private JLabel source_links;
	
	/**
	 * This is the default constructor
	 * @param wnd 
	 */
	public HTMLInfoPane(MainWindow wnd, WebTreeNode nodo, ProgressListener progress) {
		super();
		this.nodo = nodo;
		this.progress = progress;
		initialize();
		
		contenttype.setText(nodo.getContentType());
		size.setText(String.valueOf(nodo.getSize())+" bytes");
		status.setText(nodo.getStatus()+" - "+nodo.getError());
		robots.setText((nodo.getMeta("robots")!=null)?nodo.getMeta("robots").toString():"");
		if (nodo.isNoIndex())
			robots.setForeground(Color.RED);

		links.setModel(new DRKLinkTableModel(nodo.getLinks()));
		links.setDefaultRenderer(Object.class, new DRKLinkCellRenderer(nodo.getLinks()));
		links.setDefaultRenderer(Integer.class, new DRKLinkCellRenderer(nodo.getLinks()));

		meta_tag_model = new MetaTagTableModel(nodo.getMetaMap());
		meta_tags.setModel(meta_tag_model);
		new TablePreferences(meta_tags, HTMLInfoPane.class, "meta_tags");

		links.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel selection = (ListSelectionModel)e.getSource();
				
				if (selection.getMinSelectionIndex() >= 0) {
					DRKLink link = HTMLInfoPane.this.nodo.getLinks().get(links.getRowSorter().convertRowIndexToModel(selection.getMinSelectionIndex()));
					highlightLink(link);
				}
			}
		});
		
		meta_tags.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel selection = (ListSelectionModel)e.getSource();
				if (selection.getMinSelectionIndex() >= 0) {
					MetaTag tag = meta_tag_model.getModelItemAt(meta_tags.getRowSorter().convertRowIndexToModel(selection.getMinSelectionIndex()));
					highlightMetaTag(tag);
				}
			}
		});
		
		getSourceHTML();
		top_panel.setDividerLocation(DRKTools.prefs.getInt("html_info_pane_divider", 20));
		top_panel.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				DRKTools.prefs.putInt("html_info_pane_divider", top_panel.getDividerLocation());
			}
		});
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		controls_panel = new JPanel();
		GridBagLayout gbl_controls_panel = new GridBagLayout();
		controls_panel.setLayout(gbl_controls_panel);
		controls_panel.setSize(803, 482);
		GridBagConstraints gridBagConstraints62 = new GridBagConstraints();
		gridBagConstraints62.gridx = 1;
		gridBagConstraints62.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints62.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints62.gridy = 13;
		nofollow_links = new JLabel();
		nofollow_links.setText("0");
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 1;
		gridBagConstraints51.anchor = GridBagConstraints.WEST;
		gridBagConstraints51.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints51.gridy = 12;
		external_links = new JLabel();
		external_links.setText("0");
		GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
		gridBagConstraints42.gridx = 1;
		gridBagConstraints42.anchor = GridBagConstraints.WEST;
		gridBagConstraints42.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints42.gridy = 11;
		internal_links = new JLabel();
		internal_links.setText("0");
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.anchor = GridBagConstraints.EAST;
		gridBagConstraints15.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints15.gridy = 11;
		lblil = new JLabel();
		lblil.setText("Internal links:");
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 1;
		gridBagConstraints8.anchor = GridBagConstraints.WEST;
		gridBagConstraints8.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints8.gridy = 9;
		icon = new JLabel();
		icon.setText("");
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.anchor = GridBagConstraints.EAST;
		gridBagConstraints7.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints7.gridy = 9;
		lblicon = new JLabel();
		lblicon.setText("Icon:");
		GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
		gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints61.gridy = 7;
		gridBagConstraints61.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints61.gridx = 1;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.gridy = 6;
		gridBagConstraints6.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints6.gridx = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.insets = new Insets(3, 3, 5, 0);
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 0;
		gridBagConstraints22.anchor = GridBagConstraints.EAST;
		gridBagConstraints22.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints22.gridy = 1;
		lblStatus = new JLabel();
		lblStatus.setText("Status:");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.weightx = 0.4;
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints2.gridy = 2;
		contenttype = new JLabel();
		contenttype.setText("");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints11.anchor = GridBagConstraints.EAST;
		gridBagConstraints11.gridy = 2;
		lbl_content_type = new JLabel();
		lbl_content_type.setText("Content type:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints1.weightx = 0.3;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 3;
		size = new JLabel();
		size.setText("0 bytes");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.gridy = 3;
		lbl_size = new JLabel();
		lbl_size.setText("Size:");
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.insets = new Insets(0, 0, 5, 0);
		gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints13.gridy = 2;
		gridBagConstraints13.weightx = 0.6;
		gridBagConstraints13.weighty = 0.3;
		gridBagConstraints13.gridheight = 7;
		gridBagConstraints13.gridx = 2;
		controls_panel.add(getLinks_scroll(), gridBagConstraints13);
		controls_panel.add(lbl_size, gridBagConstraints);
		controls_panel.add(size, gridBagConstraints1);
		controls_panel.add(lbl_content_type, gridBagConstraints11);
		controls_panel.add(contenttype, gridBagConstraints2);
		GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
		gridBagConstraints23.gridx = 0;
		gridBagConstraints23.anchor = GridBagConstraints.EAST;
		gridBagConstraints23.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints23.gridy = 4;
		lbltitle = new JLabel();
		lbltitle.setText("Title:");
		controls_panel.add(lbltitle, gridBagConstraints23);
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints31.gridy = 4;
		gridBagConstraints31.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints31.gridx = 1;
		controls_panel.add(getHtml_title(), gridBagConstraints31);
		GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
		gridBagConstraints14.gridx = 0;
		gridBagConstraints14.anchor = GridBagConstraints.EAST;
		gridBagConstraints14.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints14.gridy = 5;
		lbldesc = new JLabel();
		lbldesc.setText("Description:");
		GridBagConstraints gbc_html_desc_scroll = new GridBagConstraints();
		gbc_html_desc_scroll.weighty = 0.6;
		gbc_html_desc_scroll.fill = GridBagConstraints.BOTH;
		gbc_html_desc_scroll.insets = new Insets(3, 3, 5, 5);
		gbc_html_desc_scroll.gridx = 1;
		gbc_html_desc_scroll.gridy = 5;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.gridy = 6;
		lblpr = new JLabel();
		lblpr.setText("Keywords:");
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.EAST;
		gridBagConstraints5.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints5.gridy = 7;
		lblaut = new JLabel();
		lblaut.setText("Author:");
		GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
		gridBagConstraints24.gridx = 0;
		gridBagConstraints24.anchor = GridBagConstraints.EAST;
		gridBagConstraints24.insets = new Insets(3, 3, 5, 5);
		gridBagConstraints24.gridy = 12;
		lblel = new JLabel();
		lblel.setText("External links:");
		GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
		gridBagConstraints32.gridx = 0;
		gridBagConstraints32.anchor = GridBagConstraints.NORTHEAST;
		gridBagConstraints32.insets = new Insets(3, 3, 0, 5);
		gridBagConstraints32.gridy = 13;
		lblnl = new JLabel();
		lblnl.setText("No follow links:");
		controls_panel.add(lblaut, gridBagConstraints5);
		GridBagConstraints gbc_lblRobots = new GridBagConstraints();
		gbc_lblRobots.anchor = GridBagConstraints.EAST;
		gbc_lblRobots.insets = new Insets(5, 5, 5, 5);
		gbc_lblRobots.gridx = 0;
		gbc_lblRobots.gridy = 8;
		controls_panel.add(getLblRobots(), gbc_lblRobots);
		GridBagConstraints gbc_robots = new GridBagConstraints();
		gbc_robots.insets = new Insets(5, 5, 5, 5);
		gbc_robots.fill = GridBagConstraints.HORIZONTAL;
		gbc_robots.gridx = 1;
		gbc_robots.gridy = 8;
		controls_panel.add(getRobots(), gbc_robots);
		GridBagConstraints gbc_meta_scroll = new GridBagConstraints();
		gbc_meta_scroll.gridheight = 5;
		gbc_meta_scroll.weighty = 0.4;
		gbc_meta_scroll.fill = GridBagConstraints.BOTH;
		gbc_meta_scroll.gridx = 2;
		gbc_meta_scroll.gridy = 9;
		controls_panel.add(getMeta_scroll(), gbc_meta_scroll);
		GridBagConstraints gbc_lblSourceLinks = new GridBagConstraints();
		gbc_lblSourceLinks.anchor = GridBagConstraints.EAST;
		gbc_lblSourceLinks.insets = new Insets(5, 5, 5, 5);
		gbc_lblSourceLinks.gridx = 0;
		gbc_lblSourceLinks.gridy = 10;
		controls_panel.add(getLblSourceLinks(), gbc_lblSourceLinks);
		GridBagConstraints gbc_source_links = new GridBagConstraints();
		gbc_source_links.anchor = GridBagConstraints.WEST;
		gbc_source_links.insets = new Insets(5, 5, 5, 5);
		gbc_source_links.gridx = 1;
		gbc_source_links.gridy = 10;
		controls_panel.add(getSource_links(), gbc_source_links);
		controls_panel.add(lblel, gridBagConstraints24);
		controls_panel.add(lbldesc, gridBagConstraints14);
		controls_panel.add(getHtml_desc_scroll(), gbc_html_desc_scroll);
		controls_panel.add(lblpr, gridBagConstraints4);
		controls_panel.add(lblnl, gridBagConstraints32);
		controls_panel.add(lblStatus, gridBagConstraints22);
		controls_panel.add(getStatus(), gridBagConstraints3);
		controls_panel.add(getKeywords(), gridBagConstraints6);
		controls_panel.add(getHtml_author(), gridBagConstraints61);
		controls_panel.add(lblicon, gridBagConstraints7);
		controls_panel.add(icon, gridBagConstraints8);
		controls_panel.add(lblil, gridBagConstraints15);
		controls_panel.add(internal_links, gridBagConstraints42);
		controls_panel.add(external_links, gridBagConstraints51);
		controls_panel.add(nofollow_links, gridBagConstraints62);

		// Split
		top_panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		top_panel.setTopComponent(controls_panel);
		top_panel.setBottomComponent(getSrc_scroll());
		
		this.setLayout(new BorderLayout());
		this.add(top_panel, BorderLayout.CENTER);
	}

	protected void getSourceHTML() {
		HTMLInfoPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		new Thread() {
			@Override
			public void run() {
				final String html = nodo.getSource(progress);
				String url = null;
				if (nodo.getRel("shortcut icon") != null) {
					if (!nodo.getRel("shortcut icon").startsWith("http://"))
						url = nodo.getURL().getProtocol()+"://"+nodo.getURL().getHost()+nodo.getURL().getPath() + nodo.getRel("shortcut icon");
					else
						url = nodo.getRel("shortcut icon");
				}
				URL icon_url = null;
				final ImageIcon loadedIcon;
				if (url != null) {
					try {
						icon_url = new URL(url);
					} catch (MalformedURLException e) {
					}
				}
				if (icon_url != null)
					loadedIcon = new ImageIcon(icon_url); // bloquea
				else
					loadedIcon = null;
				
				SwingUtilities.invokeLater(new Runnable() {
					
					public void run() {
						icon.setIcon(loadedIcon);
						source.setText(html); source.setCaretPosition(0);
						html_title.setText(nodo.getTitle()); html_title.setCaretPosition(0);
						html_desc.setText((nodo.getMeta("Description")!=null)?nodo.getMeta("Description").toString():""); html_desc.setCaretPosition(0);
						keywords.setText((nodo.getMeta("Keywords")!=null)?nodo.getMeta("Keywords").toString():""); keywords.setCaretPosition(0);
						html_author.setText((nodo.getMeta("Author")!=null)?nodo.getMeta("Author").toString():""); html_author.setCaretPosition(0);
						source_links.setText(String.valueOf(nodo.getSourceLinkCount()));
						internal_links.setText(String.valueOf(nodo.getInternalLinkCount()));
						external_links.setText(String.valueOf(nodo.getExternalLinkCount()));
						nofollow_links.setText(String.valueOf(nodo.getNofollowLinkCount()));
						HTMLInfoPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						if (links.getSelectionModel().getMinSelectionIndex() >=0) {
							int ti = links.getRowSorter().convertRowIndexToModel(links.getSelectionModel().getMinSelectionIndex());
							DRKLink selected_link = nodo.getLinks().get(ti);
							highlightLink(selected_link);
						}
					}
				});
			}
		}.start();
	}

	/**
	 * This method initializes src_scroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private RTextScrollPane getSrc_scroll() {
		if (src_scroll == null) {
			src_scroll = new RTextScrollPane();
			src_scroll.setMinimumSize(new Dimension(600, 300));
			src_scroll.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HTML", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			src_scroll.setViewportView(getSource());
			src_scroll.setFoldIndicatorEnabled(true);
			src_scroll.setLineNumbersEnabled(true);
		}
		return src_scroll;
	}

	/**
	 * This method initializes source	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private RSyntaxTextArea getSource() {
		if (source == null) {
			source = new RSyntaxTextArea();
			source.setRows(30);
			source.setEditable(false);
			source.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
			source.setCodeFoldingEnabled(DRKTools.prefs.getBoolean("use_code_folding", false));
			source.setAntiAliasingEnabled(true);
			source.setUseSelectedTextColor(true);
		}
		return source;
	}

	/**
	 * This method initializes links_scroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getLinks_scroll() {
		if (links_scroll == null) {
			links_scroll = new JScrollPane();
			links_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			links_scroll.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Links", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			links_scroll.setViewportView(getLinks());
		}
		return links_scroll;
	}

	/**
	 * This method initializes links	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JTable getLinks() {
		if (links == null) {
			links = new JTable();
			links.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			links.setAutoCreateRowSorter(true);
			links.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			new TablePreferences(links, DRKLink.class, "link");
		}
		return links;
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

	/**
	 * This method initializes keywords	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getKeywords() {
		if (keywords == null) {
			keywords = new JTextField();
			keywords.setEditable(false);
		}
		return keywords;
	}

	/**
	 * This method initializes html_title	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getHtml_title() {
		if (html_title == null) {
			html_title = new JTextField();
			html_title.setEditable(false);
		}
		return html_title;
	}

	/**
	 * This method initializes html_desc	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextArea getHtml_desc() {
		if (html_desc == null) {
			html_desc = new JTextArea();
			html_desc.setBackground(UIManager.getColor("TextField.disabledBackground"));
			html_desc.setFont(new Font("Tahoma", Font.PLAIN, 11));
			html_desc.setLineWrap(true);
			html_desc.setEditable(false);
			html_desc.setRows(3);
		}
		return html_desc;
	}

	/**
	 * This method initializes html_author	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getHtml_author() {
		if (html_author == null) {
			html_author = new JTextField();
			html_author.setEditable(false);
		}
		return html_author;
	}

	@Override
	public void locateLink(DRKLink link) {
		int index = nodo.getLinks().indexOf(link);
		int ti = links.getRowSorter().convertRowIndexToView(index);
		links.getSelectionModel().setSelectionInterval(ti, ti);
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

	public void highlightMetaTag(final MetaTag tag) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				source.requestFocusInWindow();
				src_scroll.scrollRectToVisible(source.getBounds());
				source.setSelectionStart(tag.getStart());
				source.setSelectionEnd(tag.getEnd());
				source.setSelectionColor(Color.BLUE);
			}
		});
	}


	@Override
	public void locateContent(final NodeTextLocation gotoContent) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				source.requestFocusInWindow();
				src_scroll.scrollRectToVisible(source.getBounds());
				source.setSelectionStart(gotoContent.start);
				source.setSelectionEnd(gotoContent.end);
				source.setSelectionColor(Color.BLUE);
			}
		});
	}


	private JScrollPane getHtml_desc_scroll() {
		if (html_desc_scroll == null) {
			html_desc_scroll = new JScrollPane();
			html_desc_scroll.setViewportView(getHtml_desc());
		}
		return html_desc_scroll;
	}
	private JLabel getLblRobots() {
		if (lblRobots == null) {
			lblRobots = new JLabel("Robots:");
		}
		return lblRobots;
	}
	private JTextField getRobots() {
		if (robots == null) {
			robots = new JTextField();
			robots.setEditable(false);
			robots.setColumns(10);
		}
		return robots;
	}
	private JScrollPane getMeta_scroll() {
		if (meta_scroll == null) {
			meta_scroll = new JScrollPane();
			meta_scroll.setMinimumSize(new Dimension(250, 100));
			meta_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			meta_scroll.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Meta tags", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			meta_scroll.setViewportView(getMetaTags());
		}
		return meta_scroll;
	}
	private JTable getMetaTags() {
		if (meta_tags == null) {
			meta_tags = new JTable();
			meta_tags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			meta_tags.setAutoCreateRowSorter(true);
			//meta_tags.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return meta_tags;
	}
	private JLabel getLblSourceLinks() {
		if (lblSourceLinks == null) {
			lblSourceLinks = new JLabel("Source links:");
		}
		return lblSourceLinks;
	}
	private JLabel getSource_links() {
		if (source_links == null) {
			source_links = new JLabel("0");
		}
		return source_links;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

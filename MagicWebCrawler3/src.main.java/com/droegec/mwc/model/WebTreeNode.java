package com.droegec.mwc.model;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import com.droegec.mwc.DRKTools;
import com.droegec.mwc.ProgressListener;
import com.droegec.mwc.WorkManager;
import com.droegec.mwc.processor.HTMLParser;
import com.droegec.mwc.processor.NullParser;
import com.droegec.mwc.processor.Parser;

public class WebTreeNode extends DefaultMutableTreeNode implements WebTreeNodeListener, Serializable {

    private static final long serialVersionUID = -9090587511248487982L;

    private transient Set<WebTreeNodeListener> listeners = new HashSet<WebTreeNodeListener>();
    private Parser parser = null;
    private int status = 0;
    private int size = 0;
    private int internalLinkCount = 0;
    private int externalLinkCount = 0;
    private int nofollowLinkCount = 0;
    private int errorLinkCount = 0;
    private String contentType = "";
    private List<DRKLink> links = new ArrayList<DRKLink>();
    private List<DRKLink> sourceLinks = new ArrayList<DRKLink>();
    private String error = "OK";
    private String title = "";
    private Map<String, MetaTag> meta = new HashMap<String, MetaTag>();
    private Map<String, String> rel = new HashMap<String, String>();
    private String content;
    private PageSEOReview seo_review = null;
    private long lastModification;
    private boolean hasBrokenLinks = false;
    // new Begin 12.01.2016
    // define domain for retrieval checked by HTMLParser
    private static String domain = null;
    // new End   12.01.2016
    public WebTreeNode(URL url) {
        setUserObject(url);
    }

    public WebTreeNode() {
        try {
            setUserObject(new URL("http://127.0.0.1"));
        } catch (MalformedURLException e) {
            // No debería ocurrir
        }
    }

    public void browse(final int this_level, final Map<URL, WebTreeNode> URLs, final WorkManager manager, final WebTreeModel model, final boolean full) {
        int count = 0;
        // new Beginn CHD 12.01.2019
        // set domain for retrieval
        if (this_level == 1) domain = this.getURL().getHost();
        // new END CHD 12.01.2019
        if (full || getStatus() == 0 || hasBrokenLinks) {
            // Robots.txt
            if ((model.getRobots() != null) && !model.getRobots().canAccess(this.getURL())) {
                parser = new NullParser(this, model);
                error = "Forbidden by robots.txt";
                status = -1;
                return; // URL prohibida
            }

            // Manual exclude
            if (!model.canAccess(this.getURL())) {
                parser = new NullParser(this, model);
                error = "Forbidden by user excludes";
                status = -1;
                return; // URL prohibida
            }

            // Si es un recheck elimino todos los links anteriores
            if (!full) {
                links.clear();
            }

            parser = Parser.getParserForNode(this, model);
            if (manager.keepWorking()) {
                parser.parse();
                model.nodeChanged(this);

            } else {
                return; // Stop
            }

            manager.setUnknownProgress(true, "+ " + getURL().toString());

            /* Por cada link agrego un hijo (si es necesario) y completo información extra */
            for (DRKLink link : links) {

                link.setDepth(this_level);

                WebTreeNode newChild;
                URL newURL = link.getURL();
                if (!URLs.keySet().contains(newURL)) {
                    // Si no pasamos por esa URL antes...
                    newChild = new WebTreeNode(newURL);
                    add(newChild);
                    link.setNode(newChild); // Indico al link, el nodo que generó
                    URLs.put(newURL, newChild);
                } else {
                    // La URL ya fue revisada...
                    link.setNode(URLs.get(newURL)); // Indico el nodo (ya existente)
                }
            }

        } // if full || error

        manager.addLinkCount(links.size());

        // Esto me permite conectar los Links con los nodos que ya existen
        if (this_level >= model.getMaxDepth() || isExternal()) {
            return;
        }

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                final WebTreeNode hijo = (WebTreeNode) hijo_obj;

                if (!manager.keepWorking()) {
                    break;
                }

                ++count;
                if (this_level == 1) {
                    manager.setOverallProgress(((float) count) / links.size());
                }

                manager.setProgress(((float) count) / links.size(), "+ " + getURL().toString() + " (" + count + "/" + getChildCount() + ")");
                model.addTask(new Runnable() {
                    @Override
                    public void run() {
                        hijo.browse(this_level + 1, URLs, manager, model, full);
                    }
                });
            }

        }
    }

    /**
     * Precalcula los contadores de links
     */
    public void countLinksDeep() {

        internalLinkCount = 0;
        externalLinkCount = 0;
        nofollowLinkCount = 0;
        errorLinkCount = 0;

        // Precalculo los contadores de enlace
        for (DRKLink link : links) {
            if (link.isExternal()) {
                ++externalLinkCount;
            } else {
                ++internalLinkCount;
            }
            if (link.isNoFollow()) {
                ++nofollowLinkCount;
            }
            WebTreeNode node = link.getNode();
            if ((node != null ) && node.hasError()) {
                ++errorLinkCount;
                hasBrokenLinks = true;
            }
        }

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                hijo.countLinksDeep();
            }
        }
    }

    public boolean isExternal() {
        return ((parent != null) && !((WebTreeNode) parent).getURL().getHost().equals(getURL().getHost()));
    }

    /**
     * @return el tamaño del contenido en bytes.
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size el tamaño del contenido en bytes.
     */
    public void setSize(int size) {
        this.size = size;
    }

    public void setLastModification(long lastModification) {
        this.lastModification = lastModification;

    }

    public long getLastModification() {
        return lastModification;
    }

    /**
     * Obtiene una lista de links con error
     *
     * @param deep recorrer en profundidad
     * @return lista de DRKLinks con error
     */
    public List<DRKLink> getBrokenLinks(boolean deep) {

        // Arranco con los propios
        List<DRKLink> linking = new LinkedList<DRKLink>();
        for (DRKLink l : links) {
            if (l.getNode().hasError()) {
                linking.add(l);
            }
        }

        if (deep && getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                linking.addAll(hijo.getBrokenLinks(deep));
            }
        }
        return linking;
    }

    /**
     * @return La URL de este nodo.
     */
    public URL getURL() {
        return (URL) getUserObject();
    }

    public boolean hasError() {
        return (status >= 400);
    }

    /**
     * Pone el estado de este nodo en i.
     *
     * @param i
     */
    public void setStatus(int i) {
        status = i;
    }

    /**
     * @return Devuelve el estado del nodo de acuerdo a los estado de error de
     * HTTP
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return Returns the contentType.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Agrega un link como hijo de este nodo.
     *
     * @param link texto de la URL

     */
    public void addLink(DRKLink link) {
        links.add(link);
    }

    public List<DRKLink> getLinks() {
        return links;
    }

    /**
     * @return all descendant node count
     */
    public int getTotalChildCount() {
        int count = 0;
        if (getChildCount() > 0) {
            count += getChildCount();
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                count += hijo.getTotalChildCount();
            }
        }
        return count;
    }

    /* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#toString()
     */
    @Override
    public String toString() {
        String text;
        if ((parent != null) && (getURL().getHost().equals(((WebTreeNode) parent).getURL().getHost()))) {
            text = (getURL().getFile().length() > 0) ? getURL().getFile() : getURL().getProtocol() + "://" + getURL().getHost();
        } else {
            text = getURL().toString();
        }

        // Si hay que mostrar SEO score en el árblo
        if (DRKTools.prefs.getBoolean("show_seo_in_tree", true) && seo_review != null) {
            return text + " (" + seo_review.getResult() + "%)";
        } else {
            return text;
        }
    }

    public void addWebTreeNodeListener(WebTreeNodeListener listener) {
        listeners.add(listener);
    }

    public void removeWebTreeNodeListener(WebTreeNodeListener listener) {
        listeners.remove(listener);
    }

    public void nodeAdded(WebTreeNode source, Object[] path, int[] childIndices, WebTreeNode[] children) {
        for (WebTreeNodeListener listener : listeners) {
            listener.nodeAdded(source, path, childIndices, children);
        }
    }

    protected void suscribeIfPossible(MutableTreeNode newChild) {
        // Si este hijo es de mi tipo, me suscribo a sus eventos
        if (newChild instanceof WebTreeNode) {
            ((WebTreeNode) newChild).addWebTreeNodeListener(this);
        }
    }

    protected void unsuscribeIfPossible(MutableTreeNode newChild) {
        // Si este hijo es de mi tipo, me desuscribo a sus eventos
        if (newChild instanceof WebTreeNode) {
            ((WebTreeNode) newChild).removeWebTreeNodeListener(this);
        }
    }

    /* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#add(javax.swing.tree.MutableTreeNode)
     */
    @Override
    public void add(MutableTreeNode newChild) {

        // Si este hijo es de mi tipo, me suscribo a sus eventos
        suscribeIfPossible(newChild);

        super.add(newChild); // llama a mi insert
    }

    /* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
     */
    @Override
    public void insert(MutableTreeNode newChild, int childIndex) {

        // Si este hijo es de mi tipo, me suscribo a sus eventos
        suscribeIfPossible(newChild);

        super.insert(newChild, childIndex);
    }

    /* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#remove(int)
     */
    @Override
    public void remove(int childIndex) {

        MutableTreeNode child = (MutableTreeNode) getChildAt(childIndex);
        unsuscribeIfPossible(child);

        super.remove(childIndex);
    }

    /* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#remove(javax.swing.tree.MutableTreeNode)
     */
    @Override
    public void remove(MutableTreeNode aChild) {

        unsuscribeIfPossible(aChild);

        super.remove(aChild);
    }

    /* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#removeAllChildren()
     */
    @Override
    public void removeAllChildren() {
        for (Object node : children) {
            unsuscribeIfPossible((MutableTreeNode) node);
        }
        super.removeAllChildren();
    }

    public String getSource(ProgressListener progress) {
        return parser.getSource(progress);
    }

    public void setErrorInformation(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMeta(String key, MetaTag value) {
        meta.put(key.toLowerCase(), value);
    }

    public MetaTag getMeta(String key) {
        return meta.get(key.toLowerCase());
    }

    public void setRel(String key, String value) {
        rel.put(key.toLowerCase(), value);
    }

    public String getRel(String key) {
        return rel.get(key.toLowerCase());
    }

    public Parser getParser() {
        return parser;
    }

    public List<DRKLink> getSourceLinksDeep() {
        return sourceLinks;
    }

    public int getSourceLinkCount() {
        return sourceLinks.size();
    }

    public int getInternalLinkCount() {
        return internalLinkCount;
    }

    public int getInternalLinkCountDeep() {
        int count = getInternalLinkCount();
        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                count += hijo.getInternalLinkCountDeep();
            }
        }
        return count;
    }

    public int getExternalLinkCount() {
        return externalLinkCount;
    }

    public int getExternalLinkCountDeep() {
        int count = getExternalLinkCount();
        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                count += hijo.getExternalLinkCountDeep();
            }
        }
        return count;
    }

    public int getNofollowLinkCount() {
        return nofollowLinkCount;
    }

    public int getNofollowLinkCountDeep() {
        int count = getNofollowLinkCount();
        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                count += hijo.getNofollowLinkCountDeep();
            }
        }
        return count;
    }

    public int getErrorLinkCount() {
        return errorLinkCount;
    }

    public int getErrorLinkCountDeep() {
        int count = getErrorLinkCount();
        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                count += hijo.getErrorLinkCountDeep();
            }
        }
        return count;
    }

    public Map<String, MetaTag> getMetaMap() {
        return meta;
    }

    public boolean isNoIndex() {
        return (meta.containsKey("robots") && (meta.get("robots").toString().toLowerCase().indexOf("noindex") >= 0 || meta.get("robots").toString().toLowerCase().indexOf("nofollow") >= 0));
    }

    public void search(String text, boolean use_regex, boolean case_insensitive, List<NodeTextLocation> nodes) {

        if (content != null) {
            if (use_regex) {

                int flags = Pattern.MULTILINE | ((case_insensitive) ? Pattern.CASE_INSENSITIVE : 0);
                Matcher regex = Pattern.compile(text, flags).matcher(content);

                while (regex.find()) {
                    nodes.add(new NodeTextLocation(this, regex.start(), regex.end()));
                }
            } else {
                int idx;
                if (case_insensitive) {
                    idx = content.indexOf(text);
                } else {
                    idx = content.toLowerCase().indexOf(text.toLowerCase());
                }

                while (idx > -1) {
                    nodes.add(new NodeTextLocation(this, idx, idx + text.length()));
                    if (case_insensitive) {
                        idx = content.indexOf(text, idx + text.length());
                    } else {
                        idx = content.toLowerCase().indexOf(text.toLowerCase(), idx + text.length());
                    }
                }
            }
        }

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                hijo.search(text, use_regex, case_insensitive, nodes);
            }
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTextContent() {
        if (content == null) {
            content = parser.getSource(null);
        }
        return HTMLParser.getTextFromHTML(content);
    }

    /**
     * Obtiene la lista de errores (en profundidad o s�lo el nodo)
     *
     * @return lista de nodos con error
     */
    public List<WebTreeNode> getErrorNodes(boolean deep) {
        List<WebTreeNode> errors = new LinkedList<WebTreeNode>();

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                if (hijo.hasError()) {
                    errors.add(hijo);
                } else if (deep) {
                    errors.addAll(hijo.getErrorNodes(deep));
                }
            }
        }
        return errors;
    }

    /**
     * Returns a list of unique links in depth
     *
     * @return link list
     */
    public List<DRKLink> getLinksDeep() {

        // Utilizo un set para no tener repetidos
        Set<DRKLink> allLinks = new HashSet<DRKLink>(links);

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                allLinks.addAll(hijo.getLinksDeep());
            }
        }

        return new LinkedList<DRKLink>(allLinks);
    }

    /**
     * Obtiene la lista de enlaces a una página o dominio según regex (en
     * profundidad o sólo el nodo)
     *
     * @param tag texto del tag
     * @param url texto de url
     * @param url_regex es regex
     * @param anchor texto de anchor
     * @param anchor_regex es regex
     * @param error Boolean links con error, sin error o todos (null)
     * @param external Boolean links externos, locales o todos (null)
     * @param nofollow Boolean links nofollow, sin nofollow o todos (null)
     * @return lista de nodos con URL coincidente
     */
    public List<DRKLink> getLinksTo(String tag, String url, boolean url_regex, String anchor, boolean anchor_regex, Boolean error, Boolean nofollow, Boolean external) {
        List<DRKLink> linking = new LinkedList<DRKLink>();

        for (DRKLink l : links) {
            boolean matches_tag = false;
            boolean matches_url = false;
            boolean matches_anchor = false;
            boolean matches_error = false;
            boolean matches_nofollow = false;
            boolean matches_external = false;

            if (!"".equals(tag) && l.getTag().equalsIgnoreCase(tag)) {
                matches_tag = true;
            }

            if (!"".equals(url)) {
                if ((url_regex && l.getURL().toString().matches(url))
                        || (!url_regex && l.getURL().toString().equalsIgnoreCase(url))) {
                    matches_url = true;
                }
            }

            if (!"".equals(anchor)) {
                if ((anchor_regex && l.getAnchor().matches(anchor))
                        || (!anchor_regex && l.getAnchor().equalsIgnoreCase(anchor))) {
                    matches_anchor = true;
                }
            }

            if (error != null) {
                matches_error = l.getNode().hasError() == error;
            }
            if (nofollow != null) {
                matches_nofollow = l.isExternal() == nofollow;
            }
            if (external != null) {
                matches_external = l.isExternal() == external;
            }

            if (("".equals(tag) || matches_tag)
                    && ("".equals(url) || matches_url)
                    && ("".equals(anchor) || matches_anchor)
                    && (error == null || matches_error)
                    && (nofollow == null || matches_nofollow)
                    && (external == null || matches_external)) {
                // Coincide
                linking.add(l);
            }
        }

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                linking.addAll(hijo.getLinksTo(tag, url, url_regex, anchor, anchor_regex, error, nofollow, external));
            }
        }
        return linking;
    }

    /**
     * Obtiene la lista de nodos según regex(en profundidad o sólo el nodo)
     *
     * @param title_regex
     * @param title
     * @param author_regex
     * @param author
     * @param keywords_regex
     * @param keywords
     * @param description_regex
     * @param description
     * @param noindex
     * @return lista de nodos con URL coincidente
     */
    public List<WebTreeNode> getNodesMatching(String title, boolean title_regex, String description, boolean description_regex, String keywords, boolean keywords_regex, String author, boolean author_regex, Boolean noindex) {
        List<WebTreeNode> matching = new LinkedList<WebTreeNode>();

        boolean matches_title = false;
        boolean matches_description = false;
        boolean matches_keywords = false;
        boolean matches_author = false;
        boolean matches_noindex = false;

        if (!"".equals(title)) {
            if ((title_regex && getTitle().matches(title))
                    || (!title_regex && getTitle().equalsIgnoreCase(title))) {
                matches_title = true;
            }
        }
        if (!"".equals(description)) {
            if ((description_regex && getTitle().matches(description))
                    || (!description_regex && getTitle().equalsIgnoreCase(description))) {
                matches_description = true;
            }
        }
        if (!"".equals(keywords)) {
            if ((keywords_regex && getTitle().matches(keywords))
                    || (!keywords_regex && getTitle().equalsIgnoreCase(keywords))) {
                matches_keywords = true;
            }
        }
        if (!"".equals(author)) {
            if ((author_regex && getTitle().matches(author))
                    || (!author_regex && getTitle().equalsIgnoreCase(author))) {
                matches_author = true;
            }
        }
        if (noindex != null) {
            matches_noindex = isNoIndex() == noindex;
        }

        if (("".equals(title) || matches_title)
                && ("".equals(description) || matches_description)
                && ("".equals(keywords) || matches_keywords)
                && ("".equals(author) || matches_author)
                && (noindex == null || matches_noindex)) {
            // Coincide
            matching.add(this);
        }

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                matching.addAll(hijo.getNodesMatching(title, title_regex, description, description_regex, keywords, keywords_regex, author, author_regex, noindex));
            }
        }
        return matching;
    }

    public void createSEOReview(String html) {
        seo_review = new PageSEOReview(this, html);
    }

    public PageSEOReview getSEOReview() {
        if (isHTML() && seo_review == null) {
            seo_review = new PageSEOReview(this);
        }
        return seo_review;
    }

    public boolean isHTML() {
        return (contentType != null && contentType.startsWith("text/html"));
    }

    /**
     * Get SEO score average for the whole tree
     */
    public SiteSEOReview getDeepSEOResult() {

        SiteSEOReview r = new SiteSEOReview(getSEOReview());

        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                if (hijo.getContentType() != null && hijo.getContentType().startsWith("text/html")) {
                    r.add(hijo.getDeepSEOResult());
                }
            }
        }
        return r;
    }

    // Serialize
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Recover transient properties
        listeners = new HashSet<WebTreeNodeListener>();
    }

    public String getLastModificationString() {
        return (getLastModification() > 0) ? DRKTools.dateFormatter.format(new Date(getLastModification())) : "";
    }

    /**
     * Elimina todos los nodos con error en profundidad
     */
    public Vector<WebTreeNode> purgeErrorNodes() {
        Set<DRKLink> linksToRemove = new HashSet<DRKLink>();
        Vector<WebTreeNode> toRemove = new Vector<WebTreeNode>();
        Vector<WebTreeNode> toRemoveDeep = new Vector<WebTreeNode>();
        if (getChildCount() > 0) {
            for (Object hijo_obj : children) {
                WebTreeNode hijo = (WebTreeNode) hijo_obj;
                if (hijo.hasError()) {
                    toRemove.add(hijo);
                } else {
                    toRemoveDeep.addAll(hijo.purgeErrorNodes());
                }
            }
            for (WebTreeNode aChild : toRemove) {
                remove(aChild);
            }

            // Saco los links que apuntan a los nodos eliminados
            for (DRKLink l : links) {
                if (toRemove.contains(l.getNode())) {
                    linksToRemove.add(l);
                }
            }
            links.removeAll(linksToRemove);

            // Paso los mío y los de mis descendientes
            toRemove.addAll(toRemoveDeep);
        }
        return toRemove;
    }

    public void addSourceLink(DRKLink link) {
        sourceLinks.add(link);
    }
   // new CHD Begin 12.01.2019
    public String getDomain () {
        return domain;
    }
}

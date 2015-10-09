package net.rss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultChannel implements Channel {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultChannel.class);
    private final String target;
    private final DocumentBuilder documentBuilder;
    private final XPathFactory xPathFactory = XPathFactory.newInstance();
    private final XPath xPath = xPathFactory.newXPath();
    private String title;
    private String link;
    private List<News> news;

    public DefaultChannel(String target) throws ParserConfigurationException {
        this.target = target;
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    public void update() throws IOException, SAXException, XPathException {
        final URL url = new URL(target);
        final URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        InputStream is = null;
        try{
            is = connection.getInputStream();
            final String mimeType = connection.getContentEncoding();
            LOG.debug("mime type: {}", mimeType);
            final Document doc = documentBuilder.parse(is);
            final DefaultNamespaceContext namespaceContext = new DefaultNamespaceContext();
            final NamedNodeMap map = doc.getDocumentElement().getAttributes();
            for (int i = 0; i < map.getLength(); i++){
                final Node attr = map.item(i);
                LOG.debug("top attribute: {} -> {}", attr.getNodeName(), attr.getTextContent());
                if (attr.getNodeName().startsWith("xmlns:")){
                    namespaceContext.addNamespaceURI(attr.getTextContent(), attr.getNodeName().substring(6));
                }
            }
            xPath.setNamespaceContext(namespaceContext);

            final NodeList nlChannel = (NodeList) xPath.compile("//channel").evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nlChannel.getLength(); i++){
                final Node nChannel = nlChannel.item(i);
                if (nChannel instanceof Element) processChannel((Element) nChannel, namespaceContext);
            }

        } finally{
            if (is != null) try{
                is.close();
            } catch(IOException x){
                LOG.error("cannot close input stream", x);
            }
        }
    }

    private void processChannel(final Element eChannel, final NamespaceContext namespaceContext) throws XPathExpressionException {
        this.title = xPath.compile("./title/text()").evaluate(eChannel);
        this.link = xPath.compile("./link/text()").evaluate(eChannel);
        this.news = new ArrayList<News>();
        final NodeList nlItem = (NodeList) xPath.compile("./item").evaluate(eChannel, XPathConstants.NODESET);
        for (int i = 0; i < nlItem.getLength(); i++){
            final Node nItem = nlItem.item(i);
            if (nItem instanceof Element) processItem((Element) nItem, namespaceContext);
        }

    }

    private void processItem(final Element eItem, final NamespaceContext namespaceContext) throws XPathExpressionException {
        final String title = xPath.compile("./title/text()").evaluate(eItem);
        final String description = xPath.compile("./description/text()").evaluate(eItem);
        final String link = xPath.compile("./link/text()").evaluate(eItem);
        // rest is optional
        final Map<String, Object> parameters = new HashMap<String, Object>();
/*
        final String pubDate = xPath.compile("./pubDate/text()").evaluate(eItem);
        final String guid = xPath.compile("./guid/text()").evaluate(eItem);
        String author = null;
        Node nAuthor = (Node) xPath.compile("./author").evaluate(eItem, XPathConstants.NODE);
        if (nAuthor == null){
            final String prefix = namespaceContext.getPrefix("http://purl.org/dc/elements/1.1/");
            if (prefix != null) nAuthor = (Node) xPath.compile(String.format("./%s:creator", prefix)).evaluate(eItem, XPathConstants.NODE);
        }
        if (nAuthor != null) author = nAuthor.getTextContent();
        final List<String> categories = new ArrayList<String>();
        final NodeList nlCategory = (NodeList) xPath.compile("./category").evaluate(eItem, XPathConstants.NODESET);
        for (int i = 0; i < nlCategory.getLength(); i++){
            final Node nCategory = nlCategory.item(i);
            categories.add(nCategory.getTextContent());
        }
*/
        try{
            news.add(new DefaultNews(link, title, description, parameters));
        } catch(NoSuchAlgorithmException x){
            LOG.error("unknown digest algortihm", x);
        }
    }

    public String getTitle() {
        if (title == null) try{
            update();
        } catch(Exception x){
            LOG.error("cannot update channel");
        }
        return title;
    }

    public String getLink() {
        if (link == null) try{
            update();
        } catch(Exception x){
            LOG.error("cannot update channel");
        }
        return link;
    }

    public List<News> getNews() {
        if (news == null) try{
            update();
        } catch(Exception x){
            LOG.error("cannot update channel");
        }
        return news;
    }
}

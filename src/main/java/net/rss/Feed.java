package net.rss;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;

public class Feed {
    private final String target;
    private static final String[] FEEDS = {
            "http://www.nytimes.com/services/xml/rss/nyt/Business.xml",
            "http://www.thestreet.com/feeds/rss/index.xml"
    };

    public Feed(final String target) {
        this.target = target;
    }

    public void processDocument(final Element doc) throws XPathException {
        final DefaultNamespaceContext namespaceContext = new DefaultNamespaceContext();
        final String rootTagName = doc.getTagName();
        System.out.println("rootTagName = " + rootTagName);
        final NamedNodeMap map = doc.getAttributes();
        for (int i = 0; i < map.getLength(); i++){
            final Node attr = map.item(i);
            System.out.printf("%s -> %s%n", attr.getNodeName(), attr.getTextContent());
            if (attr.getNodeName().startsWith("xmlns:")){
                namespaceContext.addNamespaceURI(attr.getTextContent(), attr.getNodeName().substring(6));
            }
        }
        // RSS 2.0
        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xPath = factory.newXPath();
        xPath.setNamespaceContext(namespaceContext);
        final XPathExpression expr = xPath.compile("//rss/channel/item");
        final NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++){
            final Node item = nodes.item(i);
            if (item instanceof Element) processItem((Element) item, namespaceContext);
        }
    }

    public void processItem(final Element item, final NamespaceContext namespaceContext) throws XPathException {
        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xPath = factory.newXPath();
        xPath.setNamespaceContext(namespaceContext);
        final String title = xPath.compile("./title/text()").evaluate(item);
        System.out.println("title = " + title);
        final String link = xPath.compile("./link/text()").evaluate(item);
        System.out.println("link = " + link);
        final String pubDate = xPath.compile("./pubDate/text()").evaluate(item);
        System.out.println("pubDate = " + pubDate);
        final String description = xPath.compile("./description/text()").evaluate(item);
        System.out.println("description = " + description);
        final String guid = xPath.compile("./guid/text()").evaluate(item);
        System.out.println("guid = " + guid);
        final String author = xPath.compile("./author/text()").evaluate(item);
        System.out.println("author = " + author);
        final NodeList nlCategory = (NodeList) xPath.compile("./category").evaluate(item, XPathConstants.NODESET);
        for (int i = 0; i < nlCategory.getLength(); i++){
            final Node nCategory = nlCategory.item(i);
            System.out.printf(" category = %s%n", nCategory.getTextContent());
        }



        System.out.printf("-------------------%n");

    }

    public static void main(String[] args) throws Exception {
        for (final String feed: FEEDS){
            final Channel channel = new DefaultChannel(feed);
            System.out.printf("title: %s, link:%s%n", channel.getTitle(), channel.getLink());
            for (final News news: channel.getNews()){
                System.out.printf(" %s (%s)%n", news.getTitle(), news.getLink());
            }
        }

    }
}

package net.rss;

import javax.xml.namespace.NamespaceContext;
import java.util.*;

public class DefaultNamespaceContext implements NamespaceContext {
    private Map<String, Set<String>> namespaces = new HashMap<String, Set<String>>();

    public void addNamespaceURI(final String namespaceURI, final String prefix){
        Set<String> prefixes = namespaces.get(namespaceURI);
        if (prefixes == null){
            prefixes = new TreeSet<String>();
            namespaces.put(namespaceURI, prefixes);
        }
        prefixes.add(prefix);
    }

    public String getNamespaceURI(String prefix) {
        for (final Map.Entry<String, Set<String>> entry: namespaces.entrySet()) if (entry.getValue().contains(prefix)) return entry.getKey();
        return null;
    }

    public String getPrefix(String namespaceURI) {
        if (namespaces.containsKey(namespaceURI)){
            final Iterator<String> it = namespaces.get(namespaceURI).iterator();
            if (it.hasNext()) return it.next();
        }
        return null;
    }

    public Iterator getPrefixes(String namespaceURI) {
        if (namespaces.containsKey(namespaceURI)) return namespaces.get(namespaceURI).iterator();
        return null;
    }
}

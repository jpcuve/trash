package net.rss;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

public class DefaultNews implements News {
    private final String id;
    private final String link;
    private final String title;
    private final String description;
    private final Map<String, Object> parameters;

    public DefaultNews(String id, String link, String title, String description, Map<String, Object> parameters) {
        this.id = id;
        this.link = link;
        this.title = title;
        this.description = description;
        this.parameters = parameters;
    }

    public DefaultNews(String link, String title, String description, Map<String, Object> parameters) throws NoSuchAlgorithmException {
        this.link = link;
        this.title = title;
        this.description = description;
        this.parameters = parameters;
        final MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update((link + title + description).getBytes());
        this.id = UUID.nameUUIDFromBytes(md.digest()).toString();
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}

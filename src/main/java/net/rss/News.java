package net.rss;

import java.util.Map;

public interface News {
    String getId();
    String getTitle();
    String getDescription();
    String getLink();
    Map<String, Object> getParameters();
}

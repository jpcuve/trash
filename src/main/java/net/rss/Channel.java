package net.rss;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Aug 2, 2010
 * Time: 11:47:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Channel {
    String getTitle();
    String getLink();
    List<News> getNews();
}

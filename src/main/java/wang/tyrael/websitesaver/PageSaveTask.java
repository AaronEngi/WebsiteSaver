package wang.tyrael.websitesaver;

import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wang.tyrael.FileUtil;
import wang.tyrael.http.HttpDefault;
import wang.tyrael.http.UrlParser;
import wang.tyrael.log.LogAdapter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

public class PageSaveTask implements Runnable{
    public static String save(String url, String dir){
        System.out.println("save:" + url);
        Response response = HttpDefault.get(url);
        UrlParser urlParser = new UrlParser(url);
        String path = urlParser.getPath();
        PrintStream ps = null;
        try {
            String body = response.body().string();
            String fullPath = dir + path;
            System.out.println(fullPath);
            FileUtil.write(fullPath, body);
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return null;
    }

    private final String indexUrl;
    private final String dir;

    private String thisDir;
    private UrlParser urlParser;

    private String body;
    private Document document;

    private Set<String> set = new HashSet<>();

    public PageSaveTask(String indexUrl, String dir) {
        this.indexUrl = indexUrl;
        this.dir = dir;
    }

    @Override
    public void run() {
        init();
        saveIndex();
    }

    private void init(){
        urlParser = new UrlParser(indexUrl);
        thisDir = dir + File.separator + urlParser.getHost();
    }

    private void saveIndex() {
        body = save(indexUrl, thisDir);
        document = Jsoup.parse(body);
        document.setBaseUri(indexUrl);
        saveMedia();
        saveImport();
        recordLink();
    }

    /**
     * 保存脚本等
     */
    private void saveMedia(){

        Elements media = document.select("[src]");

        for (Element src : media) {
            String url = src.attr("abs:src");
            save(url, thisDir);
        }
    }

    private void saveImport(){
        Elements imports = document.select("link[href]");
        for (Element link : imports) {
            String url = link.attr("abs:href");
            save(url, thisDir);
        }
    }

    private void recordLink(){
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String url = link.attr("abs:href");
            set.add(url);
        }
    }

    /**
     *
     * @return a 标签
     */
    public Set<String> getA(){
        return set;
    }

}

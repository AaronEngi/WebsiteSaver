package wang.tyrael.websitesaver;

import wang.tyrael.http.UrlParser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SiteSaveTask implements Runnable {
    private final String indexUrl;
    private final String dir;

    private Set<String> saved = new HashSet<>();
    private Queue<String> toDo = new LinkedList<>();

    public SiteSaveTask(String indexUrl, String dir) {
        this.indexUrl = indexUrl;
        this.dir = dir;
    }

    @Override
    public void run() {
        init();
        while (true){
            String pageUrl = toDo.poll();
            if(pageUrl == null){
                break;
            }
            PageSaveTask pageSaveTask = new PageSaveTask(pageUrl, dir);
            pageSaveTask.run();

            saved.add(pageUrl);
            Set<String> as = pageSaveTask.getA();
            for(String url : as){
                if(isToSave(url)){
                    toDo.offer(url);
                }
            }

        }
    }

    private void init(){
        toDo.add(indexUrl);
    }

    private boolean isToSave(String url){
        if(saved.contains(url)){
            return false;
        }
        if(saved.contains(url + "#") || saved.contains(url + "/#")){
            return false;
        }
        UrlParser urlParser = new UrlParser(indexUrl);
        UrlParser newUrlParser = new UrlParser(url);
        if(!urlParser.getHost().equalsIgnoreCase(newUrlParser.getHost())){
            return false;
        }
        return true;
    }
}

import org.junit.Test;
import wang.tyrael.websitesaver.PageSaveTask;

public class PageSaveTaskTest {
    @Test
    public void test(){
        PageSaveTask pageSaveTask =new PageSaveTask("http://www.gedc.net.cn/", ".");
        pageSaveTask.run();
    }
}

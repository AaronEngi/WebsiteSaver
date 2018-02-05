import org.junit.Test;
import wang.tyrael.websitesaver.SiteSaveTask;

public class SiteSaveTaskTest {
    @Test
    public void test(){
        new SiteSaveTask("http://www.gedc.net.cn/", ".").run();
    }
}

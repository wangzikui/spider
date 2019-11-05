package WebProcess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapQID2Tag implements IWorkable {

    private static Logger logger = LogManager.getLogger(MapQID2Tag.class);

    private WebDriver webDriver;
    private String driverLoc = "src/webDriver/forWin/chromedriver.exe";
    private String url = "https://www.zhihu.com/question/";
    private ArrayList<Pattern> patterns = new ArrayList<>();

    public MapQID2Tag() {
        //创建一个　ChromeDriver 接口
        System.setProperty("webdriver.chrome.driver",this.driverLoc);
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File(System.getProperty("webdriver.chrome.driver"))).usingAnyFreePort().build();
        try {
            service.start();
        } catch (IOException e) {
            logger.error("ChromeDriverService启动异常", e);
        }
        //创建一个　chrome 浏览器实例
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("lang=zh_CN.UTF-8");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        dc.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        webDriver = new RemoteWebDriver(service.getUrl(), dc);

        String pattern = "(?<=keywords\" content=\").*?(?=\")";
        patterns.add(Pattern.compile(pattern));
    }

    public MapQID2Tag(String driverLoc, String url) {
        this.url = url;
        this.driverLoc = driverLoc;
        new ExtendUserToken();
    }

    @Override
    public String work(String input) {
        logger.info(input);

        if (input == null) return null;
        String[] source = input.split("\t");
        if (source.length < 3) {
            return null;
        }
        logger.info("1");
        StringBuilder sb = new StringBuilder();
        String[] QID = source[2].split(",");
        if (QID.length == 0 || QID[0].equals("null")) return null;
        logger.info("2");
        int i = Integer.min(3, QID.length);
        while (i > 0) {
            String qUrl = url + QID[i - 1];
            logger.info("m1");
            webDriver.get(qUrl);
            logger.info("m2");
            Matcher m = patterns.get(0).matcher(webDriver.getPageSource());
            logger.info("m3");
            if (m.find()) {
                sb.append(m.group()).append(",");
            }
            logger.info("m4");
            --i;
        }
        logger.info("3");
        sb.insert(0, source[1] + "\t");
        sb.insert(0, source[0] + "\t");
        String result = sb.append("\n").toString();
        logger.info(result);
        return result;
    }

    @Override
    public void close() {
        webDriver.close();
    }
}

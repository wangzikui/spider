import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
执行于第一阶段爬虫
爬取urltoken下的    关注与被关注18行   answers第19行 asks第18行
 */
//TODO: 耦合直接进了IOProcess，url切入时机不好，有空改。
public class WebProcess {
    private static Logger logger = LogManager.getLogger(WebProcess.class);

    private WebDriver webDriver;
    private ArrayList<Pattern> patterns = new ArrayList<>();
    private String url = "https://www.zhihu.com/people/";

    public WebProcess(String driverLoc, String url) {
        this.url = url;
        System.setProperty("webdriver.chrome.driver",driverLoc);
        //创建一个　ChromeDriver 接口
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

        //String pattern = "(?<=urltoken\":\").*?(?=\")";   //匹配urltoken
        String pattern = "(?<=urlToken\":\").*?(?=\")";
        patterns.add(Pattern.compile(pattern));
    }

    public String work(String input) {
        String peopleUrl = url + input;    //e.g. https://www.zhihu.com/people/octavia-51-97/
        String followingUrl = peopleUrl + "/" + "following";  //e.g. https://www.zhihu.com/people/octavia-51-97/following //TODO:考虑是否把followers加上
        String answersUrl = peopleUrl + "/" + "answers";  //https://www.zhihu.com/people/octavia-51-97/answers    //TODO:考虑是否把asks加上

        webDriver.get(followingUrl);
        logger.info(webDriver.getPageSource());
        Matcher m = patterns.get(0).matcher(webDriver.getPageSource());
        logger.info(m.find());
        while (m.find()) {
            logger.info(m.group());
        }

        return input;
    }

}

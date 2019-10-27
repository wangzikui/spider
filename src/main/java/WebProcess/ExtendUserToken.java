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

/*
执行于第一阶段爬虫
爬取urltoken下的    关注与被关注18行   answers第19行 asks第18行
 */
//TODO: 耦合直接进了IOProcess，url切入时机不好，有空改。
public class ExtendUserToken implements IWorkable {
    private static Logger logger = LogManager.getLogger(ExtendUserToken.class);

    private WebDriver webDriver;
    private String driverLoc = "src/webDriver/forWin/chromedriver.exe";
    private String url = "https://www.zhihu.com/people/";
    private ArrayList<Pattern> patterns = new ArrayList<>();

    public ExtendUserToken() {
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

        //webDriver.manage().addCookie(new Cookie("_zap","c98e96d4-b280-4ef0-98e5-5e5a0e4b9e64"));
        //webDriver.manage().addCookie(new Cookie("d_c0","\"AHCh9CuCFBCPTkLIWiP8UE-gP_womjCUthA=|1569034207\""));
        //webDriver.manage().addCookie(new Cookie("tst","r"));
        //webDriver.manage().addCookie(new Cookie("q_c1","07f5719eee064783b0d6845fa6e6cbbe|1571836136000|1571836136000"));
        //webDriver.manage().addCookie(new Cookie("_xsrf","9ec689a8-fd2e-4777-b1ef-e4dbbe13c316"));
        //webDriver.manage().addCookie(new Cookie("Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49","1571835821,1571835916,1571967304,1571989245"));
        //webDriver.manage().addCookie(new Cookie("capsion_ticket","\"2|1:0|10:1571989251|14:capsion_ticket|44:ZGQxYjVkMWM0ZWJkNGE1MWIzZWM2MmIzOTQ4OTE1Y2Q=|496c025775666a4c5613c8082e02768ba4a3a3dbc8a5c3efb9c19dde4e66a7e4\""));
        //webDriver.manage().addCookie(new Cookie("z_c0","\"2|1:0|10:1571989259|4:z_c0|92:Mi4xZmpIa0FnQUFBQUFBY0tIMEs0SVVFQ1lBQUFCZ0FsVk5DX1dmWGdCTzhLM1hXaFFjRUtiY3dDT0xJblM2YXdLT1JB|3861332b6cb9746910edc57be380510e517b73f1645bf583702870e1cccea501\""));
        //webDriver.manage().addCookie(new Cookie("tgw_l7_route","a37704a413efa26cf3f23813004f1a3b"));
        //webDriver.manage().addCookie(new Cookie("Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49","571992923"));
        //webDriver.manage().addCookie(new Cookie("","571992923"));

        //String pattern = "(?<=urltoken\":\").*?(?=\")";   //匹配urltoken
        String pattern = "(?<=urlToken\":\").*?(?=\")";
        patterns.add(Pattern.compile(pattern));
    }



    public ExtendUserToken(String driverLoc, String url) {
        this.url = url;
        this.driverLoc = driverLoc;
        new ExtendUserToken();
    }

    public String work(String input) {
        if (input == null) return null;
        String peopleUrl = url + input;    //e.g. https://www.zhihu.com/people/octavia-51-97/
        String followingUrl = peopleUrl + "/" + "following";  //e.g. https://www.zhihu.com/people/octavia-51-97/following //TODO:考虑是否把followers加上
        String answersUrl = peopleUrl + "/" + "answers";  //https://www.zhihu.com/people/octavia-51-97/answers    //TODO:考虑是否把asks加上

        StringBuilder sb = new StringBuilder();

        webDriver.get(followingUrl);
        Matcher m = patterns.get(0).matcher(webDriver.getPageSource());
        while (m.find()) {
            String tmp = m.group();
            if (tmp.equals(input)) continue;
            sb.append(tmp);
            sb.append("\n");
        }

        return sb.toString();
    }

    public void close() {
        webDriver.close();
    }
}

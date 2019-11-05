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

/**
 * 输入urlTokens
 * 输出business   location    Question（逗号分隔）  ------\t分隔
 * */
public class GetBusinessLocQuestion implements IWorkable{

    private static Logger logger = LogManager.getLogger(GetBusinessLocQuestion.class);

    private WebDriver webDriver;
    private String driverLoc = "src/webDriver/forWin/chromedriver.exe";
    private String url = "https://www.zhihu.com/people/";
    private ArrayList<Pattern> patterns = new ArrayList<>();

    public GetBusinessLocQuestion() {
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

        String pattern1 = "(?<=business).*?\"name\":\".*?(?=\")";   //":{"id":"19619368","type":"topic","url":"http:\u002F\u002Fwww.zhihu.com\u002Ftopics\u002F19619368","name":"计算机软件
        patterns.add(Pattern.compile(pattern1));
        String pattern2 = "(?<=locations).*?\"name\":\".*?(?=\")"; //":[{"id":"19560551","type":"topic","url":"http:\u002F\u002Fwww.zhihu.com\u002Ftopics\u002F19560551","name":"深圳市
        patterns.add(Pattern.compile(pattern2));
        String pattern3 = "(?<=\"https://www.zhihu.com/question/)\\d*";   //274741086
        patterns.add(Pattern.compile(pattern3));
    }

    @Override
    public String work(String input) {
        if (input == null) return null;
        String peopleUrl = url + input;    //e.g. https://www.zhihu.com/people/octavia-51-97/
        String answersUrl = peopleUrl + "/" + "answers";  //https://www.zhihu.com/people/octavia-51-97/answers    //TODO:考虑是否把asks加上

        StringBuilder sb = new StringBuilder();
        webDriver.get(answersUrl);
        String source = webDriver.getPageSource();

        //TODO:执行
        Matcher m1 = patterns.get(0).matcher(source);
        if (m1.find()) {
            String tmp = m1.group();
            if (tmp.length() < 44) {    //没写行业
                tmp = "";
            } else {
                String[] str = tmp.split("\"");
                tmp = str[str.length - 1];
            }
            sb.append(tmp).append("\t");
        }

        Matcher m2 = patterns.get(1).matcher(source);
        if (m2.find()) {
            String tmp = m2.group();
            if (tmp.length() < 50) {    //没写居住地
                tmp = "";
            } else {
                String[] str = tmp.split("\"");
                tmp = str[str.length - 1];
            }
            sb.append(tmp).append("\t");
        }

        Matcher m3 = patterns.get(2).matcher(source);
        while (m3.find()) {
            sb.append(m3.group()).append(",");
        }

        String result = sb.append("\n").toString();
        logger.info(result);
        return result;
    }

    @Override
    public void close() {
        webDriver.close();
    }
}

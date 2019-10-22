import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Spider {
    private static ChromeDriverService service;
    private static WebDriver webDriver;

    /**
     * 创建一个浏览器实例
     * @return　webDriver
     */

    public WebDriver getChromeDriver() throws MalformedURLException {
        System.setProperty("webdriver.chrome.driver","src/webDriver/forWin/chromedriver.exe");
        //创建一个　ChromeDriver 接口
        service = new ChromeDriverService.Builder().usingDriverExecutable(new File(System.getProperty("webdriver.chrome.driver"))).usingAnyFreePort().build();
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ChromeDriverService启动异常");
        }
        //创建一个　chrome 浏览器实例
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("lang=zh_CN.UTF-8");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        dc.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return new RemoteWebDriver(service.getUrl(), dc);
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        Spider spider = new Spider();
        WebDriver webDriver = spider.getChromeDriver();
        int i = 10;
        while (i > 0) {
            webDriver.get("https://www.zhihu.com/question/33573424");
            System.out.println(i + "---------------------------------------------------------------------------------------------------");
            System.out.println(webDriver.getPageSource());
            Thread.sleep(3000);
            --i;
        }

        webDriver.close();
    }
}

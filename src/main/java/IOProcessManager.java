import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IOProcessManager {

    private static Logger logger = LogManager.getLogger(IOProcessManager.class);

    private String upStreamDirectory = "src/ioDir/urltokens_input";  //数据来向
    private String downStreamDirectory = "src/ioDir/urltokens_output";    //数据去向
    private String inputPrefix = "";  //匹配输入文件前缀
    private String inputSuffix = ".txt";    //匹配输入文件后缀
    private String outputPrefix = "";  //匹配输出文件前缀
    private String outputSuffix = ".txt";  //匹配输出文件后缀
    private int outputLimit = 100; //超出limit，将.tmp后缀去掉，并关闭输出流
    private int maxAsycNum = 5; //最大同时工作的线程
    private ExecutorService pool = null;    //管理IOProcess的线程池

    public void init() {    //通过配置文件初始化
        pool = Executors.newFixedThreadPool(maxAsycNum);
    }


    public void addProcess() {
        String lockedSourceFileName = preProcessForUpStream();
        if (lockedSourceFileName == null || lockedSourceFileName.isEmpty()) {
            logger.info(lockedSourceFileName + "非法");
            return;
        }
        String outputFileName = downStreamDirectory + "/" + outputPrefix + System.currentTimeMillis() + outputSuffix;
        IOProcess ioProcess = new IOProcess(lockedSourceFileName, outputFileName, outputLimit);

        pool.submit(ioProcess);

    }

    private String preProcessForUpStream() {
        FileFilter ff = pathname -> {
            if (pathname.getName().startsWith(inputPrefix) && pathname.getName().endsWith(inputSuffix)) return true;
            return false;
        };
        File[] inputFileList = new File(upStreamDirectory).listFiles(ff);
        if (inputFileList == null || inputFileList.length == 0) {   //上游没有名称合法文件
            logger.info(upStreamDirectory + "未发现合规文件");
            return "";
        }
        Random random = new Random();   //可能损耗性能，但看着舒服
        int r = random.nextInt(inputFileList.length);

        //为即将被读的文件加锁，方式是后缀.lock
        String sourceFileName = inputFileList[r].getName();
        String lockedSourceFileName = upStreamDirectory + "/" + sourceFileName + ".lock";
        inputFileList[r].renameTo(new File(lockedSourceFileName));
        return lockedSourceFileName;
    }
}

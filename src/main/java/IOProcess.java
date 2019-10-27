import WebProcess.IWorkable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class IOProcess implements Runnable{
    private static Logger logger = LogManager.getLogger(IOProcess.class);

    private String inputFile = "";
    private String outputFile = "";
    private int outputLimit = 0;
    private BufferedReader br;
    private BufferedWriter out;

    private IWorkable workable;

    public IOProcess (String inputFile, String outputFile, int outputLimit) {
        outputFile = outputFile + ".tmp";   //未写好的文件后缀tmp
        logger.info("新建任务：inputFile：" + inputFile + "|outputfile:" + outputFile + "|inputLine" + outputLimit);

        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.outputLimit = outputLimit;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(this.inputFile)));
            out = new BufferedWriter(new FileWriter(new File(this.outputFile), true));
        } catch (IOException e) {
            logger.error("inputFile：" + inputFile + "|outputfile:" + outputFile + "|inputLine" + outputLimit, e);
        }
    }

    @Override
    public void run() {
        logger.info("开始工作：inputFile：" + inputFile + "|outputfile:" + outputFile + "|inputLine" + outputLimit);
        while (outputLimit > 0) {
            logger.info("readline");
            String source = readLine();
            //TODO: 交由webProcess处理
            logger.info("work");
            String result = workable.work(source);
            logger.info("writeline");
            writeLine(result);
            //if (source == null || source.isEmpty()) break;    //感觉不够优雅
            --outputLimit;
        }
        close();
        logger.info("工作完成：inputFile：" + inputFile + "|outputfile:" + outputFile + "|inputLine" + outputLimit);
    }

    public void setWorkable(IWorkable workable) {
        this.workable = workable;
    }

    public String readLine() {
        String source = "";
        try {
            source = br.readLine();
        } catch (IOException e) {
            logger.error("inputFile：" + inputFile + "|outputfile:" + outputFile + "|inputLine" + outputLimit, e);
        }
        return source;
    }

    public void writeLine(String line) {
        if (line == null || line.isEmpty()) return;
        try {
            out.write(line);
            out.flush();
        } catch (IOException e) {
            logger.error("inputFile：" + inputFile + "|outputfile:" + outputFile + "|inputLine" + outputLimit, e);
        }
    }

    public void close() {
        workable.close();
        try {
            br.close();
            out.close();
            String lockedInputfile = inputFile + "ed";
            new File(inputFile).renameTo(new File(lockedInputfile));
            String oriOutputfile = outputFile.substring(0, outputFile.length() - 4);
            new File(outputFile).renameTo(new File(oriOutputfile));     //还原.tmp命名
        } catch (IOException e) {
            logger.error("inputFile：" + inputFile + "|outputfile:" + outputFile + "|inputLine" + outputLimit, e);
        }
    }
}

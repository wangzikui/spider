import WebProcess.ExtendUserToken;
import WebProcess.GetBusinessLocQuestion;
import WebProcess.IWorkable;
import WebProcess.MapQID2Tag;

//汇集一些场景
public class Senario {
    public static void extendUrlTokens() throws InterruptedException{
        IOProcessManager ioProcessManager = new IOProcessManager();
        ioProcessManager.init();
        int i = 50;
        while (i > 0) {
            IWorkable webProcess = new ExtendUserToken();
            ioProcessManager.addProcess(webProcess);
            Thread.sleep(1000*20);
            --i;
        }

        //WebProcess webProcess = new WebProcess();
        //webProcess.work("biao-jie-ying-ping");
    }

    public static void reduceDuplicate() throws InterruptedException {  //去重
        IOProcessManager ioProcessManager = new IOProcessManager( "src/ioDir/urltokens_input",  "src/ioDir/urltokens_output", "", "txt", 2000);
        ioProcessManager.init();

        /*WebProcess.IWorkable reduceDup = new ReduceDup();
        ioProcessManager.addProcess(reduceDup);*/

        int i = 124;
        while (i > 0) {
            IWorkable reduceDup = new ReduceDup();
            ioProcessManager.addProcess(reduceDup);
            --i;
            Thread.sleep(1000*5);
        }
    }

    public static void getLocBusinessQuestion() throws InterruptedException {
        /*IWorkable getBusinessLocQuestion = new GetBusinessLocQuestion();
        logger.info(getBusinessLocQuestion.work("hopkinschau"));*/
        IOProcessManager ioProcessManager = new IOProcessManager( "src/ioDir/urltokens_output",  "src/ioDir/business_loc_Question_output", "", "txt", 1000);
        ioProcessManager.init();

        int i = 124;
        while (i > 0) {
            IWorkable getBusinessLocQuestion = new GetBusinessLocQuestion();
            ioProcessManager.addProcess(getBusinessLocQuestion);
            //if(i % 5 == 0) Thread.sleep(1000*600);
            --i;
        }
    }

    public static void mapQID2Tag() throws InterruptedException {
        /*IWorkable mapQID2Tag = new MapQID2Tag();
        mapQID2Tag.work("互联网\t深圳市\t326511036,48510028,326511036,319371540,48510028,319371540,331904582,48510028,331904582,");*/
        IOProcessManager ioProcessManager = new IOProcessManager( "src/ioDir/business_loc_Question_output/8",  "src/ioDir/spider_final", "", "txt", 1000);
        ioProcessManager.init();

        int i = 10;
        while (i > 0) {
            IWorkable mapQID2Tag = new MapQID2Tag();
            ioProcessManager.addProcess(mapQID2Tag);
            if(i % 2 == 0)
                Thread.sleep(1000*600);
            --i;
        }
    }

    public static void mv() throws InterruptedException {
        IWorkable mv = new IWorkable() {
            @Override
            public String work(String input) {
                return input;
            }

            @Override
            public void close() {}
        };
        IOProcessManager ioProcessManager = new IOProcessManager( "/Users/skedush/IdeaProjects/first/src/testData",  "/Users/skedush/IdeaProjects/first/src/testData2", "", "txt", 1000);
        ioProcessManager.init();

        int i = 9;
        while (i > 0) {
            ioProcessManager.addProcess(mv);
            //if(i % 5 == 0)
            Thread.sleep(1000*20);
            --i;
        }
    }
}

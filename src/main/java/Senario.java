import WebProcess.*;

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

    public static void zhuhuJsonParse() {
        //String test = "{\"answer_count\": 7, \"articles_count\": 0, \"avatar_url\": \"https://pic4.zhimg.com/da8e974dc_is.jpg\", \"business\": {\"id\": \"\", \"type\": \"topic\", \"url\": \"\", \"name\": \"航运业\", \"avatar_url\": \"https://pic4.zhimg.com/e82bab09c_is.jpg\", \"excerpt\": \"\", \"introduction\": \"\"}, \"description\": \"\", \"educations\": [{\"school\": {\"id\": \"19588914\", \"type\": \"topic\", \"url\": \"https://www.zhihu.com/topics/19588914\", \"name\": \"上海海事大学\", \"avatar_url\": \"https://pic2.zhimg.com/a0634640e_is.jpg\", \"excerpt\": \"上海海事大学（Shanghai Maritime University）是一所以航运、物流、海洋为特色学科，具有工学、管理学、经济学、法学、文学、理学和艺术学等学科门类的多科性大学，入选国家卓越工程师教育培养计划和上海市首批深化创新创业教育改革示范高校。学校原为交通运输部直属高校，2000年划归上海市管理，2008年上海市人民政府与交通运输部签订协议，共建上海海事大学。学校前身为创建于1909年7月的晚清邮传部上海高等实业学堂船政科，…\", \"introduction\": \"上海海事大学（Shanghai Maritime University）是一所以航运、物流、海洋为特色学科，具有工学、管理学、经济学、法学、文学、理学和艺术学等学科门类的多科性大学，入选国家卓越工程师教育培养计划和上海市首批深化创新创业教育改革示范高校。学校原为交通运输部直属高校，2000年划归上海市管理，2008年上海市人民政府与交通运输部签订协议，共建上海海事大学。学校前身为创建于1909年7月的晚清邮传部上海高等实业学堂船政科，开创了中国高等航海教育的先河。历经邮传部高等商船学堂、吴淞商船学校、交通部吴淞商船专科学校、重庆商船专科学校、国立吴淞商船专科学校、上海航务学院等时期。1959年交通部组建上海海运学院，2004年经教育部批准更名为上海海事大学。学校设有2个博士后科研流动站（交通运输工程、电气工程），4个一级学科博士点，17个二级学科博士点，14个一级学科硕士学位授权点，59个二级学科硕士学位授权点，8个专业学位授权点和10个工程硕士学位授权领域，47个本科专业。拥有11个省部级重点研究基地。现有1个国家重点（培育）学科，1个上海市高峰学科，2个上海市高原学科，9个部市级重点学科，工程学科进入ESI全球前1%。5个国家级特色专业，1个国家级综合改革试点专业，6个教育部卓越工程师教育培养计划专业，17个上海市本科教育高地。\"}, \"major\": {\"id\": \"19580451\", \"type\": \"topic\", \"url\": \"https://www.zhihu.com/topics/19580451\", \"name\": \"供应链\", \"avatar_url\": \"https://pic1.zhimg.com/8e566cfb5_is.jpg\", \"excerpt\": \"供应链的概念是从扩大的生产(Extended Production)概念发展而来，对供应链的定义为“供应链是围绕核心企业，从配套零件开始到制成中间产品及最终产品、最后由销售网络把产品送到消费者手中的一个由供应商、制造商、分销商直到最终用户所连成的整体功能网链结构”。\", \"introduction\": \"供应链的概念是从扩大的生产(Extended Production)概念发展而来，对供应链的定义为“供应链是围绕核心企业，从配套零件开始到制成中间产品及最终产品、最后由销售网络把产品送到消费者手中的一个由供应商、制造商、分销商直到最终用户所连成的整体功能网链结构”。\"}, \"diploma\": 0}], \"employments\": [], \"favorited_count\": 51, \"follower_count\": 112, \"following_count\": 4, \"gender\": 1, \"headline\": \"传统物流转型跨境电子商务物流\", \"locations\": [{\"id\": \"19558830\", \"type\": \"topic\", \"url\": \"https://www.zhihu.com/topics/19558830\", \"name\": \"杭州\", \"avatar_url\": \"https://pic2.zhimg.com/389a772ec26fa8910f8cea8867adef59_is.jpg\", \"excerpt\": \"东南形胜，三吴都会，钱塘自古繁华。烟柳画桥，风帘翠幕，参差十万人家。云树绕堤沙。怒涛卷霜雪，天堑无涯。市列珠玑，户盈罗绮，竞豪奢。重湖叠巘清嘉。有三秋桂子，十里荷花。羌管弄晴，菱歌泛夜，嬉嬉钓叟莲娃。千骑拥高牙。乘醉听萧鼓，吟赏烟霞。异日图将好景，归去凤池夸。\", \"introduction\": \"<p>东南形胜，三吴都会，钱塘自古繁华。烟柳画桥，风帘翠幕，参差十万人家。云树绕堤沙。怒涛卷霜雪，天堑无涯。市列珠玑，户盈罗绮，竞豪奢。</p><p>重湖叠巘清嘉。有三秋桂子，十里荷花。羌管弄晴，菱歌泛夜，嬉嬉钓叟莲娃。千骑拥高牙。乘醉听萧鼓，吟赏烟霞。异日图将好景，归去凤池夸。</p>\"}], \"name\": \"林虓\", \"thanked_count\": 26, \"url_token\": \"lin-xiao-42-60\", \"user_type\": \"people\", \"voteup_count\": 25}";
        //IWorkable zhihuJsonParse = new ZhihuJsonParse();
        //System.out.println(zhihuJsonParse.work(test));
        IWorkable zhihuJsonParse = new ZhihuJsonParse();
        IOProcessManager ioProcessManager = new IOProcessManager( "src/ioDir/zhihuDownloadData/input",  "src/ioDir/zhihuDownloadData/output", "", "json", 100000);
        ioProcessManager.init();

        ioProcessManager.addProcess(zhihuJsonParse);
    }
}

import WebProcess.IWorkable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

public class ReduceDup implements IWorkable {
    private static Logger logger = LogManager.getLogger(ReduceDup.class);

    private HashSet<String> set = new HashSet<String>();

    @Override
    public String work(String input) {
        if (input == null || input.length() == 0 || input.equals("\n")) {
            String[] a = new String[]{};
            String result =  "\n" + String.join("\n",set.toArray(a));
            logger.info(result);
            return result;
        }
        set.add(input);
        return "#";
    }

    @Override
    public void close() {

    }
}

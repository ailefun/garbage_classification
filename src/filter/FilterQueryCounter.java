package filter;

import sun.util.resources.cldr.ig.CurrencyNames_ig;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.Executor.*;
/*
*
张开双手变成翅膀守护你，你要相信相信我们会像童话故事里，幸福和快乐是结局，
* *
*
* */
/**
 * @author le
 * date:    2020/6/1
 * describe：记录垃圾查询的次数
 */
@WebFilter(filterName = "FilterQueryCounter", urlPatterns = "/garbage/ServletQueryGarbage")
public class FilterQueryCounter implements Filter {


    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Properties queryLog;
    File file;
    String garbabgrName;

    @Override
    public void destroy() {
        System.out.println("FilterQueryCounter:" + "destroy");
        executorService.shutdown();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("FilterQueryCounter:" + "doFilter");
        HttpServletRequest request = (HttpServletRequest) req;
        String queryString = request.getQueryString();
        if (queryString != "") {
            String decode = URLDecoder.decode(queryString, "UTF-8");
            /*应用正则表达式*/
            garbabgrName = decode.substring(8);
            System.out.println(garbabgrName + "---garbabgrName");
        }
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                System.out.println(1);
                String property = queryLog.getProperty(garbabgrName);
                System.out.println(property + "---property");
                if (property == null) {
                    queryLog.setProperty(garbabgrName, "1");
                } else {
                    int count = 0;
                    try {
                        count = Integer.parseInt(property);
                    } catch (NumberFormatException e) {
                        //silent
                    }
                    count++;
                    queryLog.setProperty(garbabgrName, Integer.toString(count));
                }
                try {
                    queryLog.store(new FileWriter(file), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(5);
        chain.doFilter(req, resp);
        System.out.println(6);

    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("FilterQueryCounter:" + "init");
        String appPath = config.getServletContext().getRealPath("/");
        file = new File(appPath, "QueryCounter.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        queryLog = new Properties();
        try {
            FileReader fileReader = new FileReader(file);
            queryLog.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

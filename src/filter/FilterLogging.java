package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.Date;

/**
 * @author le
 * date:    2020/6/1
 * describe：记录网站的访问请况
 */
@WebFilter(filterName = "FilterLogging", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "logFileName",
                        value = "log.txt"),
                @WebInitParam(name = "prefix", value = "URL:    ")
        })

public class FilterLogging implements Filter {
    private PrintWriter logger;
    private String prefix;

    @Override
    public void destroy() {
        System.out.println("FilterLogging:  " + "destroy.");
        if (logger != null){
            logger.close();
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("FilterLogging:  " + "doFilter.");
        HttpServletRequest request = (HttpServletRequest) req;
        String log = new Date() + " " + prefix + request.getContextPath() + request.getServletPath() +
                " " + request.getRemoteAddr();
        String queryString = request.getQueryString();
        System.out.println(queryString + "---queryString");
        if (queryString != null){
            log.concat(" " + "queryString:" + URLDecoder.decode(queryString , "UTF-8"));
        }
        logger.println();
        logger.flush();
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("FilterLogging:  " + "init.");
        prefix = config.getInitParameter("prefix");
        String logFileName = config.getInitParameter("logFileName");
        String appPath = config.getServletContext().getRealPath("/");
        System.out.println("logFileName:    " + logFileName);
        try {
            File file = new File(appPath, logFileName);
            logger = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
    }

}

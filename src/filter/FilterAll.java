package filter;



import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author le
 * date:    2020/5/27
 * describe：
 */
@WebFilter(filterName = "FilterAll", urlPatterns = "/*")
public class FilterAll implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        String servletPath = httpServletRequest.getServletPath();  //获取客户端所请求的脚本文件的文件路径
        if (servletPath.endsWith(".js")){
            System.out.println("访问了js");
            resp.setCharacterEncoding("UTF-8");
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/javascript; charset=UTF-8");
        }else if (servletPath.endsWith(".css")){
            System.out.println("访问了css");
            System.out.println("访问了js");
            resp.setContentType("text/css; charset=UTF-8");
        }else{
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            req.setCharacterEncoding("UTF-8");
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}

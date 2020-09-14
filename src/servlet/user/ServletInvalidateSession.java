package servlet.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author le
 * date:    2020/5/30
 * describe：让session失效，用于用户退出。
 */
/*添加urlPatterns重启服务器才能使用 */
@WebServlet(name = "ServletInvalidateSession ", urlPatterns ="/user/ServletInvalidateSession")
public class ServletInvalidateSession extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了" + "ServletInvalidateSession");
        HttpSession session = request.getSession(false);
        session.invalidate();
//        response.getWriter().print("已使会话过期。");
        response.getWriter().print("true");

    }
}

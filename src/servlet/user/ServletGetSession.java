package servlet.user;


import javaBean.UserBean;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author le
 * date:    2020/5/27
 * describe：用户登录后，将session传入前端的sessionObject中，用于前端权限验证
 */
@WebServlet(name = "ServletGetSession",urlPatterns = "/user/ServletGetSession")
public class ServletGetSession extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了ServletGetSession");
        HttpSession session = request.getSession(false);
        String username = "";
        int isAdmin = -1;
        response.setContentType("text/html; charset = utf-8");
        if (session != null && session.getAttribute("username") != null) {
//            System.out.println(session + "---session");
            username = (String) session.getAttribute("username");
//            System.out.println(username +"---1");
            isAdmin = (int) session.getAttribute("isAdmin");
//            System.out.println(isAdmin + "---isAdmin");
            UserBean userBean = new UserBean();
            userBean.setUsername(username);
//            System.out.println(userBean.getUsername() +"---2");
            userBean.setIsAdmin(isAdmin);
            JSONArray jsonArray = JSONArray.fromObject(userBean);
            //为了当浏览器直接在地址栏访问这个路径的时候，输出的中文不乱码，实际上用处很小。

            response.getWriter().print(jsonArray);
            System.out.println(jsonArray + "---jsonArray");
        }else{
//            response.getWriter().print("false");
            //用户没登录，没session，不操作
//            response.getWriter().print("");
        }
//        response.getOutputStream().write(("username:" + username).getBytes("UTF-8"));
//        response.getOutputStream().write(("----isAdmin:" + isAdmin).getBytes("UTF-8"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

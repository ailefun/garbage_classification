package servlet.user;

import Dao.DS;
import oracle.jdbc.OracleTypes;
import tools.ToolsUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

/**
 * @author le
 * date:    2020/5/27
 * describe：	验证登录并将用户名和用户的类型放入session。
 */
@WebServlet(name = "ServletLogin",urlPatterns = "/user/ServletLogin")
public class ServletLogin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
//        将用户名的原文计算出来
        Base64.Decoder decoder = Base64.getDecoder();//重复的代码
        byte[] usernameByte = decoder.decode(username);
        String usernameOriginal = URLDecoder.decode((new String(usernameByte)),"utf-8");
        System.out.println(usernameOriginal + "---usernameOriginal");
        String passwordMD5 = ToolsUtils.MD5(ToolsUtils.MD5(password));
//        System.out.println(password + "---passwordMD5");
        //获取连接池
        Connection conn = null;
        try {
            conn = DS.dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(conn);
        CallableStatement cs = null;
        int isAdmin = -1;
        try {
            cs = conn.prepareCall("{call pak_user.query_user_isAdmin(?,?,?)}");
            cs.setString(1, usernameOriginal);
            cs.setString(2, passwordMD5);
            System.out.println(usernameOriginal + "---usernameOriginal");
            System.out.println(passwordMD5 + "---passwordMD5");
            cs.registerOutParameter(3, OracleTypes.NUMBER);
            cs.execute();
            isAdmin = cs.getInt(3);
            // 用户名和密码正确，将username和isAdmin放到session
            HttpSession session = request.getSession();
            session.setAttribute("username",usernameOriginal);
            session.setAttribute("isAdmin",isAdmin);
        } catch (SQLException e) {
//            e.printStackTrace();
            String sqlState = e.getSQLState();
            System.out.println(sqlState);
            if (sqlState.equals("02000")) {
                System.out.println("用户名或者密码错误");
//                response.sendRedirect("index.jsp");
            }
        } finally {
            try {
                cs.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (isAdmin == 0) {
            System.out.println("一般用户，登录成功");
            response.getWriter().print("true");
            System.out.println(true);
        } else if (isAdmin == 1) {
            System.out.println("管理员，登录成功");
            response.getWriter().print("true");
            System.out.println(true);
        } else {
            System.out.println("登录失败");
            response.getWriter().print("false");
            System.out.println(false);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

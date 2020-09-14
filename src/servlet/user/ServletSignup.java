package servlet.user;

import Dao.DS;
import tools.ToolsUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

/**
 * @author le
 * date:    2020/5/26
 * describe：接收前端传来的经过base64编码的username和password，
 *           将用户名的原文和哈希两次的password存入数据库。
 */
@WebServlet(name = "ServletSignup",urlPatterns = "/user/ServletSignup")
public class ServletSignup extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + "---username--signup");
//        将用户名的原文计算出来，在前端用base64编码的密码则直接哈希两次后放入数据库
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] usernameByte = decoder.decode(username);
        String usernameOriginal = URLDecoder.decode((new String(usernameByte)),"utf-8");
        System.out.println(usernameOriginal + "---usernameOriginal");
        String passwordMD5 = ToolsUtils.MD5(ToolsUtils.MD5(password));
        System.out.println(password + "---password");
        System.out.println(passwordMD5 + "---passwordMD5");
        //获取连接池
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
        System.out.println(conn);
        try {
            cs = conn.prepareCall("{call pak_user.insert_user(?,?)}");
            cs.setString(1, usernameOriginal);
            cs.setString(2, passwordMD5);
            System.out.println(usernameOriginal + "---usernameOriginal");
            System.out.println(passwordMD5 + "---passwordMD5");
            cs.execute();
            response.getWriter().print("true");
            System.out.println(true);
        } catch (SQLException e) {
            e.printStackTrace();
            String sqlState = e.getSQLState();
            System.out.println(sqlState);
            if (sqlState.equals("23000")) {
                /*这个用的不多，应该用ajax验证用户名是否重复*/
                System.out.println("用户名重复");
                response.getWriter().print("false");
                System.out.println(false);
            }
        } finally {
            try {
                conn.close();
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

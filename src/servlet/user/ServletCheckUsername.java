package servlet.user;

import Dao.DS;
import oracle.jdbc.OracleTypes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * @author le
 * date:    2020/5/25
 * describe：注册时检查用户名是否重复。
 */
@WebServlet(name = "ServletCheckUsername", urlPatterns = "/user/CheckUsername")
public class ServletCheckUsername extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //获取用户输入的数据
        String username = request.getParameter("username");
        System.out.println(username + "---username");
        //在数据库中查询
        Connection conn = null;
        CallableStatement cs = null;
        try {
            int isAdmin = -1;
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_user.query_user_isAdmin_forAdmin(?,?)}");
            cs.setString(1, username);
            cs.registerOutParameter(2, OracleTypes.NUMBER);
            cs.execute();
            isAdmin = cs.getInt(2);
            System.out.println(isAdmin + "---isAdmin");
            if (isAdmin == -1) {
                System.out.println("false");
                response.getWriter().print("false");
            } else {
                System.out.println("true");
                response.getWriter().print("true");
            }
        } catch (SQLException e) {
//            e.printStackTrace();
        } finally {
            try {
                cs.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

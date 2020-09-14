package servlet.admin;

import Dao.DS;
import oracle.jdbc.OracleTypes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author le
 * date:    2020/5/27
 * describe：
 */
/*当urlPatterns忘了加/的时候，主页竟然也无法访问，
估计是这导致了urlPatterns出了问题，正常的匹配功能也失效了*/
@WebServlet(name = "ServletUpdateUser", urlPatterns = "/admin/ServletUpdateUser")
public class ServletUpdateUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了" + "ServletUpdateUser");
        //获取前端参数
        String username = request.getParameter("username");
        int isAdmin = Integer.parseInt(request.getParameter("isAdmin"));
        String userRemarks = request.getParameter("userRemarks");
        //连接数据库
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_user.query_user_isAdmin_forAdmin(?,?)}");
            cs.setString(1, username);
            cs.registerOutParameter(2, OracleTypes.NUMBER);
            cs.execute();
            System.out.println("执行完了query_user_isAdmin_forAdmin。");
            cs = conn.prepareCall("{call pak_user.update_user(?,?,?)}");
            cs.setString(1, username);
            cs.setInt(2, isAdmin);
            cs.setString(3, userRemarks);
            cs.execute();
            System.out.println("执行完了update_user。");
            response.getWriter().print("true");
            System.out.println("true");
        } catch (SQLException e) {
            response.getWriter().print("false");
            System.out.println("false");
            e.printStackTrace();
        } finally {
            try {
                cs.close();
                conn.close();
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

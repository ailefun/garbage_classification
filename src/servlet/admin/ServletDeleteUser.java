package servlet.admin;

import Dao.DS;

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
@WebServlet(name = "ServletDeleteUser", urlPatterns = "/admin/ServletDeleteUser")
public class ServletDeleteUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取前端参数
        System.out.println("访问了" + "ServletDeleteUser。");
        String usernameRequest = request.getParameter("username");
        //连接数据库
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            /*过滤器已经验证过了username能不能操作*/
            cs = conn.prepareCall("{call pak_user.delete_user(?)}");
            cs.setString(1, usernameRequest);
            cs.execute();
            System.out.println("执行完了" + "delete_user。");
            response.getWriter().print("true");
            System.out.println("true");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("false");
            System.out.println("false");
        } finally {
            try {
                System.out.println(cs + "---cs");
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

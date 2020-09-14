package servlet.admin;

import Dao.DS;
import javaBean.UserBean;
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
 * describe：这个servlet暂时用不上
 */
@WebServlet(name = "ServletQueryUser", urlPatterns = "/admin/ServletQueryUser")
public class ServletQueryUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了ServletQueryUser");
        //获取前端参数
        String username = request.getParameter("username");
        UserBean userBean = new UserBean();
        //连接数据库
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_user.query_user_info(?,?,?,?,?)}");
            cs.setString(1, username);
            cs.registerOutParameter(1, OracleTypes.VARCHAR);
            cs.registerOutParameter(2, OracleTypes.NUMBER);
            cs.registerOutParameter(3, OracleTypes.DATE);
            cs.registerOutParameter(4, OracleTypes.DATE);
            cs.registerOutParameter(5, OracleTypes.VARCHAR);
            cs.execute();
            userBean.setUsername(cs.getString(1));
            userBean.setIsAdmin(cs.getInt(2));
            userBean.setRegistrationTime(cs.getDate(3));
            userBean.setLastLoginTime(cs.getDate(4));
            userBean.setUserRemarks(cs.getString(5));

//            将数据发送到前端
            response.getOutputStream().write((userBean.getUsername() +
                    userBean.getIsAdmin() +
                    userBean.getLastLoginTime() +
                    userBean.getRegistrationTime() +
                    userBean.getUserRemarks()
            ).getBytes("UTF-8"));
        } catch (SQLException e) {
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
        doGet(request, response);
    }
}

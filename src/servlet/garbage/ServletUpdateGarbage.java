package servlet.garbage;

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
 * date:    2020/5/28
 * describe：
 */
@WebServlet(name = "ServletUpdateGarbage", urlPatterns = "/garbage/ServletUpdateGarbage")
public class ServletUpdateGarbage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了" + "ServletUpdateGarbage");
        //从前端接收参数
        String garbage = request.getParameter("garbage");
        String type = request.getParameter("type");
        String describe = request.getParameter("describe");
        System.out.println(garbage + "-----garbage");
        //连接数据库
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_garbage.update_garbage(?,?,?)}");
            cs.setString(1, garbage);
            cs.setString(2, type);
            cs.setString(3, describe);
            cs.execute();
//            response.getOutputStream().write("更新完毕。".getBytes("UTF-8"));
            response.getWriter().print("true");
            System.out.println(true);
        } catch (SQLException e) {
            response.getWriter().print("false");
            System.out.println(false);
            e.printStackTrace();
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

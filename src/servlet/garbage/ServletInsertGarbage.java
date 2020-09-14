package servlet.garbage;

import Dao.DS;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author le
 * date:    2020/5/28
 * describe：
 */
@WebServlet(name = "ServletInsertGarbage", urlPatterns = "/garbage/ServletInsertGarbage")
public class ServletInsertGarbage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了" + "ServletQueryarbage");
        //从前端接收参数
        String garbage = request.getParameter("garbage");
        String type = request.getParameter("type");
        String describe = request.getParameter("describe");
        String garbageRemarks = request.getParameter("garbageRemarks");
        //从session中获取username
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        if (username == null) {
            response.getOutputStream().write(("请登录。".getBytes("UTF-8")));
            return;
        }
        //连接数据库
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            System.out.println(garbage + "-----garbage");
            cs = conn.prepareCall("{call pak_garbage.insert_garbage(?,?,?,?,?)}");
            cs.setString(1, garbage);
            cs.setString(2, type);
            cs.setString(3, describe);
            cs.setString(4, username);
            cs.setString(5, garbageRemarks);
            cs.execute();
            response.getWriter().print("true");
//            response.getOutputStream().write("插入执行完毕。".getBytes("UTF-8"));
        } catch (SQLException e) {
            response.getWriter().print("false");
            e.printStackTrace();
        }finally {
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

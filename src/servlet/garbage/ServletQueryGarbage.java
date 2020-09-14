package servlet.garbage;

import Dao.DS;
import javaBean.GarbageBean;
import net.sf.json.JSONArray;
import oracle.jdbc.OracleTypes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author le
 * date:    2020/5/28
 * describe：获取前端参数，传给存储过程。
 */
@WebServlet(name = "ServletQueryGarbage", urlPatterns = "/garbage/ServletQueryGarbage")
public class ServletQueryGarbage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了" + "ServletQueryarbage");
        //从前端接收参数
        String garbage = request.getParameter("garbage");
        System.out.println(garbage + "-----garbage");
        //连接数据库
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_garbage.query_garbage(?,?,?)}");
            cs.setString(1, garbage);
            cs.registerOutParameter(1, OracleTypes.VARCHAR);
            cs.registerOutParameter(2, OracleTypes.VARCHAR);
            cs.registerOutParameter(3, OracleTypes.VARCHAR);
            cs.execute();
            GarbageBean garbageBean = new GarbageBean();
            garbageBean.setGarbage(cs.getString(1));
            garbageBean.setType(cs.getString(2));
            garbageBean.setDescribe(cs.getString(3));
            JSONArray jsonArray = JSONArray.fromObject(garbageBean);
            //把json数据传递到前端，记着前端用ajax接收
            PrintWriter out = response.getWriter();
            out.print(jsonArray);
        } catch (SQLException e) {
            response.getWriter().print("false");
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

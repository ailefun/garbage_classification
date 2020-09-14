package servlet.garbage;

import Dao.DS;
import net.sf.json.JSONArray;

import oracle.jdbc.OracleTypes;
import tools.ToolsUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author le
 * date:    2020/5/28
 * describe：显示垃圾信息的表格用这个servlet
 */
@WebServlet(name = "ServletQueryGarbageAll", urlPatterns = "/garbage/ServletQueryGarbageAll")
public class ServletQueryGarbageAll extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("访问了" + "ServletQueryGarbageAll");
        //连接数据库
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        System.out.println(1);
        try {
            System.out.println(2);
            conn = DS.dataSource.getConnection();
            System.out.println(3);
            cs = conn.prepareCall("{call pak_garbage.query_garbage_all(?)}");
            System.out.println(4);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println(5);
            cs.execute();
            System.out.println(6);
            //把rs转换成json数组
            rs = (ResultSet) cs.getObject(1);
            //先转成List格式，再转成json格式，convertList这个方法是网上找的
            /*原理是将resultSet转换成metaData之后获取每一列数据，存入hashmap，再全放到list里*/
            JSONArray jsonData = JSONArray.fromObject(ToolsUtils.convertList(rs));
            System.out.println(jsonData.toString());
            PrintWriter out = response.getWriter();    //把json数据传递到前端，记着前端用ajax接收
            out.print(jsonData);
        } catch (SQLException e) {
            response.getWriter().print("false");
            e.printStackTrace();
        } finally {
            try {
                rs.close();
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

package filter;

import Dao.DS;
import oracle.jdbc.OracleTypes;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
@WebFilter(filterName = "FilterGarbageManage", servletNames =
        {"ServletDeleteGarbage", "ServletUpdateGarbage"})
public class FilterGarbageManage implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //在用户执行删除和更新垃圾信息之前，验证要操作的垃圾是否是由用户创建
        HttpSession session = ((HttpServletRequest) req).getSession();
        System.out.println(session + "===session");
        String username = (String) session.getAttribute("username");
        System.out.println(username + "---username");
        //获取将要操作的garbage
        String garbage = req.getParameter("garbage");
        //用存储过程查询
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_garbage.query_garbage_creater(?,?)}");
            cs.setString(1, garbage);
            cs.registerOutParameter(1, OracleTypes.VARCHAR);
            cs.setString(2, username);
            cs.execute();
            System.out.println(cs.getString(1) + "---cs.getString(1)-garbage");
        }catch (SQLException e){
            String sqlState = e.getSQLState();
            System.out.println(sqlState);
            if (sqlState.equals("02000")) {
                System.out.println("操作失败。");
                resp.getOutputStream().write("操作失败".getBytes("UTF-8"));
                return;
            }
        }finally {
            try {
                cs.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}

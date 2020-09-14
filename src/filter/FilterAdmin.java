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
 * date:    2020/5/27
 * describe：获取用户session中isAdmin的值，来为"ServletQueryUser",
 * "ServletDeleteUser", "ServletUpdateUser"过滤权限。
 * 根据session中username的值，需要被操作的用户是否是一般用户。
 */
@WebFilter(filterName = "FilterAdmin",
        servletNames = {"ServletQueryUser", "ServletDeleteUser", "ServletUpdateUser"})
public class FilterAdmin implements Filter {
    @Override
    public void destroy() {
    }

    /*这个过滤器负责验证所有管理员权限*/
    /*正常情况下，前端会根据用户权限显示界面，一会程序不会执行返回错误信息的代码*/
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("访问了ServletDeleteUser");
        /*用浏览器访问index.jsp的时候会自动创建一个session，不知道是为什么*/
        HttpSession session = ((HttpServletRequest) req).getSession();
        System.out.println(session + "===session");
//        String usernameSession = "";
//        int isAdmin = -1;
//        usernameSession = (String) session.getAttribute("username");
        Object isAdmin = session.getAttribute("isAdmin");
        System.out.println(isAdmin + "-------- Object isAdmin = session.getAttribute(\"isAdmin\");");
        /*isAdmin == null说明没登录，(int)isAdmin != 1，说明不是管理员，就应该return
         *    isAdmin不是null才能继续判断*/
        if (isAdmin == null || (int) isAdmin != 1) {
            System.out.println("Access denied......");
            resp.getOutputStream().write("Access denied......".getBytes("UTF-8"));
        } else {
            String username = req.getParameter("username");
            Connection conn = null;
            CallableStatement cs = null;
            try {
                //要求：管理员不能对其它管理员进行增删查改
                conn = DS.dataSource.getConnection();
                /*检查要操作的这个用户（username）是不是管理员，username不是当前访问servlet的用户*/
                cs = conn.prepareCall("{call pak_user.query_user_isAdmin_forAdmin(?,?)}");
                cs.setString(1, username);
                cs.registerOutParameter(2, OracleTypes.NUMBER);
                cs.execute();
                System.out.println("执行完了query_user_isAdmin_forAdmin。");
                int isAdmin2 = cs.getInt(2);
                if (isAdmin2 == 1) {
                    resp.getWriter().print("false");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                String sqlState = e.getSQLState();
                /*未找到任何数据时会返回这个sqlState = "02000"
                 * 说明用户没有提交需要操作的用户名，这在前端不允许*/
                System.out.println(sqlState);
                if (sqlState.equals("02000")) {
                    System.out.println("用户没有提交需要操作的用户名");
//                    ((HttpServletResponse)resp).sendRedirect("index.jsp");
                    resp.getOutputStream().write("用户没有提交需要操作的用户名。".getBytes("UTF-8"));
                }
            } finally {
                try {
                    cs.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            chain.doFilter(req, resp);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}

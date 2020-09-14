package Dao;

import net.sf.json.JSONArray;
import oracle.jdbc.OracleTypes;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import tools.ToolsUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author le
 * date:    2020/4/15
 * describe：获取dataSource
 */
public class DS {
    public static DataSource dataSource = null;

    static {
        System.out.println(11);
        //新建一个配置文件对象
        Properties properties = new Properties();
        //通过类加载器找到文件路径，读配置文件
        System.out.println(12);
        InputStream resourceAsStream = new DS().getClass()
                .getClassLoader().getResourceAsStream("dbcpconfig.properfies");
        //把文件以输入流的方式加载到配置对象中
        System.out.println(13);
        try {
            properties.load(resourceAsStream);
            System.out.println(14);
            //创建数据源对象
            dataSource = BasicDataSourceFactory.createDataSource(properties);
            System.out.println(15);
        } catch (Exception e) {
            System.out.println(16);
            e.printStackTrace();
        }
    }

    public static void main(String arg[]) throws SQLException {
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        System.out.println();
        try {
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_garbage.query_garbage_all(?)}");
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            //把rs转换成json数组
            rs = (ResultSet) cs.getObject(1);
            //先转成List格式，再转成json格式，convertList这个方法是网上找的
            /*原理是将resultSet转换成metaData之后获取每一列数据，存入hashmap，再全放到list里*/
            JSONArray jsonData = JSONArray.fromObject(ToolsUtils.convertList(rs));
            System.out.println(jsonData.toString());
        } catch (Exception e) {
        }
    }
}

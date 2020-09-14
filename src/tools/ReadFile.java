package tools;

import Dao.DS;
import javaBean.GarbageBean;
import oracle.sql.CharacterSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author le
 * date:    2020/5/28
 * describe：从网站上抓的数据，存入数据库
 */
public class ReadFile {
    public static String readFileContent(String fileName) throws SQLException {
//        CharacterSet dbCharset = CharacterSet.make(CharacterSet.UTF8_CHARSET);
        //用于进行转码
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        Connection conn = null;
            CallableStatement cs = null;
        try {
            conn = DS.dataSource.getConnection();
            cs = conn.prepareCall("{call pak_garbage.insert_garbage(?,?,?,?,?)}");

            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            String string;
            GarbageBean garbageBean = new GarbageBean();

            int n = 0;
            while ((tempStr = reader.readLine()) != null) {
//                sbf.append(tempStr);
                string = tempStr.replace("VM1122:2 ", "").
                        replace("$", "");
                if (n % 3 == 1) {
                    garbageBean.setType(string);
                    System.out.println(garbageBean.getGarbage() + "---garbage;" +
                            garbageBean.getType() + "---type");
                    //连接数据库
                    try {
                        cs.setString(1, garbageBean.getGarbage());
                        cs.setString(2, garbageBean.getType());
                        cs.setString(3, "");
                        cs.setString(4, "admin");
                        cs.setString(5, "");
                        System.out.println(cs.toString() + "---string");
                        cs.execute();
//                        response.getOutputStream().write("插入执行完毕。".getBytes("UTF-8")
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                    }

                } else if (n % 3 == 2) {
//                    System.out.print("  id");
                } else {
                    garbageBean.setGarbage(string);
                }
                n++;
//                System.out.println(string);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            cs.close();
            conn.close();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static void main(String arg[]) throws SQLException {
        ReadFile.readFileContent("D:\\Users\\le\\Desktop\\garbage_classification\\src\\tools\\garbageInfo.txt");

    }
}

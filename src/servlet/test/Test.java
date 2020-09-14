package servlet.test;

import tools.ToolsUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author le
 * date:    2020/5/26
 * describe：测试用
 */
public class Test {

    public static void main(String arg[]) throws UnsupportedEncodingException {
        System.out.println(ToolsUtils.MD5("admin"));
    }
}


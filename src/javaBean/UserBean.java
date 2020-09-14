package javaBean;

import java.sql.SQLType;
import java.util.Date;

/**
 * @author le
 * date:    2020/5/26
 * describe：
 */
public class UserBean {
    //有些属性在servlet里面可能永远用不到，为了和数据库的列名对应，也在这里写
    private String username;
    private String password;
    private int isAdmin;
    private Date registrationTime;
    private Date lastLoginTime;
    private int userId;
    private String userRemarks;

    public UserBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserRemarks() {
        return userRemarks;
    }

    public void setUserRemarks(String userRemarks) {
        this.userRemarks = userRemarks;
    }

    public static void main(String arg[]) {

    }
}

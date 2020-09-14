package javaBean;

import java.util.Date;

/**
 * @author le
 * date:    2020/5/26
 * describe：
 */
public class GarbageBean {
    private String garbage;
    private String type;
    private String describe;
    private String username;
    private Date createTime;
    private Date lastQueryTime;
    private int garbageId;
    private String garbageRemarks;

    public GarbageBean() {
    }

    public String getGarbage() {
        return garbage;
    }

    public void setGarbage(String garbage) {
        this.garbage = garbage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastQueryTime() {
        return lastQueryTime;
    }

    public void setLastQueryTime(Date lastQueryTime) {
        this.lastQueryTime = lastQueryTime;
    }

    public int getGarbageId() {
        return garbageId;
    }

    public void setGarbageId(int garbageId) {
        this.garbageId = garbageId;
    }

    public String getGarbageRemarks() {
        return garbageRemarks;
    }

    public void setGarbageRemarks(String garbageRemarks) {
        this.garbageRemarks = garbageRemarks;
    }

    public static void main(String arg[]) {

    }
}

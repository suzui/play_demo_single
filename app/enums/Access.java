package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;

@EnumClass(name = "权限", visible = false)
public enum Access implements BaseEnum {
    ADMIN(100, "超级管理员"), AUTH(101, "权限管理"), STATISTICS(102, "统计管理"), LOG(103, "日志管理"), SYSTEM(104, "系统设置"), PERSON(105, "用户管理");
    
    private int code;
    private String intro;
    
    private Access(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static Access covert(int code) {
        for (Access access : Access.values()) {
            if (access.code == code) {
                return access;
            }
        }
        return null;
    }
    
    public int code() {
        return this.code;
    }
    
    public String intro() {
        return this.intro;
    }
    
}
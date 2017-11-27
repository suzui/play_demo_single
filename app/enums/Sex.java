package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;

@EnumClass(name = "性别")
public enum Sex implements BaseEnum {
    NOPOINT(100, "无"), FEMALE(101, "男"), MALE(102, "女");
    private int code;
    private String intro;
    
    private Sex(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static Sex convert(int code) {
        for (Sex sex : Sex.values()) {
            if (sex.code == code) {
                return sex;
            }
        }
        return NOPOINT;
    }
    
    public int code() {
        return this.code;
    }
    
    public String intro() {
        return this.intro;
    }
}
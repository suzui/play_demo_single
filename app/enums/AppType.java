package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;
import org.apache.commons.lang.StringUtils;

@EnumClass(name = "APP类型")
public enum AppType implements BaseEnum {
    USER(101, "用户端"), ADMIN(102, "管理端");
    private int code;
    private String intro;
    
    private AppType(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static AppType convert(int code) {
        for (AppType type : AppType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
    
    public static AppType convert(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return convert(Integer.parseInt(code));
    }
    
    public int code() {
        return this.code;
    }
    
    public String intro() {
        return this.intro;
    }
    
}

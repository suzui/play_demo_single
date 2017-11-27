package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;
import org.apache.commons.lang.StringUtils;

@EnumClass(name = "CLIENT类型")
public enum ClientType implements BaseEnum {
    WEB(100, "web"), IOS(101, "iOS端"), ANDROID(102, "安卓端");
    private int code;
    private String intro;
    
    private ClientType(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static ClientType convert(int code) {
        for (ClientType clientType : ClientType.values()) {
            if (clientType.code == code) {
                return clientType;
            }
        }
        return ClientType.WEB;
    }
    
    public static ClientType convert(String code) {
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

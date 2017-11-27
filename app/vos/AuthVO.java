package vos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import annotations.DataField;
import models.auth.Auth;
import org.apache.commons.lang.BooleanUtils;

public class AuthVO extends OneData {
    
    @JsonInclude(Include.NON_NULL)
    @DataField(name = "权限id", enable = false)
    public transient Long id;
    @DataField(name = "权限id", enable = false)
    public Long authId;
    @DataField(name = "权限名称")
    public String name;
    @DataField(name = "包含权限代码")
    public String codes;
    @DataField(name = "包含权限内容")
    public String access;
    @DataField(name = "是否分配", enable = false)
    public Integer flag;
    
    public AuthVO() {
    
    }
    
    public AuthVO(Auth auth) {
        this.id = auth.id;
        this.authId = auth.id;
        this.name = auth.name;
        this.codes = auth.codes;
        this.access = auth.access();
    }
    
    public AuthVO(Auth auth, boolean flag) {
        this(auth);
        this.flag = BooleanUtils.toIntegerObject(flag);
    }
    
}

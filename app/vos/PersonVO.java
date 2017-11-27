package vos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import annotations.DataField;
import models.person.AccessToken;
import models.person.Person;

public class PersonVO extends OneData {
    
    @DataField(name = "用户id", enable = false)
    public Long personId;
    @DataField(name = "用户名")
    public String username;
    @DataField(name = "手机号")
    public String phone;
    @DataField(name = "邮箱")
    public String email;
    @DataField(name = "编号")
    public String number;
    @DataField(name = "姓名")
    public String name;
    @DataField(name = "头像")
    public String avatar;
    @DataField(name = "简介")
    public String intro;
    @DataField(name = "性别")
    public Integer sex;
    @DataField(name = "token")
    public String accesstoken;
    @JsonInclude(Include.NON_NULL)
    @DataField(name = "密码", enable = false)
    public String password;
    @DataField(name = "首次登录时间", enable = false)
    public Long firstLoginTime;
    @DataField(name = "最后登录时间", enable = false)
    public Long lastLoginTime;
    @DataField(name = "登录次数", enable = false)
    public Integer loginAmount;
    
    
    public PersonVO() {
    }
    
    public PersonVO(Person person) {
        this.id = person.id;
        this.personId = person.id;
        this.username = person.username;
        this.phone = person.phone;
        this.email = person.email;
        this.number = person.number;
        this.name = person.name;
        this.avatar = person.avatar;
        this.intro = person.intro;
        this.sex = person.sex.code();
        this.firstLoginTime = person.firstLoginTime;
        this.lastLoginTime = person.lastLoginTime;
        this.loginAmount = person.loginAmount;
    }
    
    public PersonVO(AccessToken accessToken) {
        this(accessToken.person);
        this.accesstoken = accessToken.accesstoken;
    }
    
}

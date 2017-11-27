package models.person;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.RandomStringUtils;

import enums.ClientType;
import models.BaseModel;
import utils.CodeUtils;

@Entity
public class AccessToken extends BaseModel {
    
    public String accesstoken;
    @ManyToOne
    public Person person;
    public String appVersion;
    public String appType;
    public String osVersion;
    @Enumerated(EnumType.STRING)
    public ClientType clientType;
    public String deviceToken;
    public String pushToken;
    public Boolean notify = true;
    
    public static AccessToken add(Person person) {
        AccessToken at = findByPerson(person);
        if (at != null) {
            return at;
        }
        at = new AccessToken();
        at.person = person;
        at.accesstoken = CodeUtils.md5(person.id + RandomStringUtils.random(6) + System.currentTimeMillis());
        return at.save();
    }
    
    public void update(String appVersion, String appType, String osVersion, String clientType, String deviceToken) {
        this.appVersion = appVersion;
        this.appType = appType;
        this.osVersion = osVersion;
        this.clientType = ClientType.convert(clientType);
        this.deviceToken = deviceToken;
        this.person.firstLoginTime = this.person.firstLoginTime == null ? System.currentTimeMillis() : this.person.firstLoginTime;
        this.person.lastLoginTime = System.currentTimeMillis();
        this.person.loginAmount = this.person.loginAmount == null ? 0 : this.person.loginAmount;
        this.person.loginAmount++;
        this.person.save();
        this.save();
    }
    
    public void pushToken(String pushToken) {
        this.pushToken = pushToken;
        this.save();
    }
    
    public void del() {
        this.logicDelete();
    }
    
    public static AccessToken findByAccesstoken(String accesstoken) {
        return AccessToken.find(defaultSql("accesstoken=?"), accesstoken).first();
    }
    
    public static AccessToken findByPerson(Person person) {
        return AccessToken.find(defaultSql("person=? "), person).first();
    }
    
    public static AccessToken findByPersonAndClientType(Person person, ClientType clientType) {
        return AccessToken.find(defaultSql("person=? and clientType=?"), person, clientType).first();
    }
    
    public static Person findPersonByAccesstoken(String accesstoken) {
        return AccessToken
                .find("select at.person from AccessToken at where at.deleted=false and at.accesstoken=?", accesstoken)
                .first();
    }
    
}

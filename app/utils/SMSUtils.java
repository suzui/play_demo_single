package utils;

import enums.CaptchaType;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;

public class SMSUtils {
    
    public static final String APPHOST = "http://v.juhe.cn/sms/send";
    public static final String APPKEY = "f75b91e41da77c0cb85426b3367f5061";
    
    public static void send(CaptchaType type, String captcha, String phone) {
        HttpResponse response = WS.url(APPHOST).setParameter("tpl_id", type.sms())
                .setParameter("mobile", phone).setParameter("tpl_value", "#code#=" + captcha)
                .setParameter("key", APPKEY).get();
        Logger.info("[sms response] %s", response.getString());
    }
    
}

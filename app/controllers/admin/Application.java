package controllers.admin;

import annotations.ActionMethod;
import annotations.ParamField;
import enums.AppType;
import enums.ClientType;
import models.area.Area;
import org.apache.commons.lang.StringUtils;
import play.cache.Cache;
import play.db.jpa.Transactional;
import vos.*;

public class Application extends ApiController {
    @Transactional(readOnly = true)
    public static void index() {
        renderHtml("admin api...");
    }
    
    @ActionMethod(name = "版本号详情", clazz = VersionVO.class)
    public static void version(@ParamField(name = "客户端类型") Integer clientType) {
        renderJSON(Result.succeed(new VersionVO(AppType.ADMIN, ClientType.convert(clientType))));
    }
    
    @ActionMethod(name = "配置参数", clazz = ConfigVO.class)
    public static void configData() {
        ConfigVO configData = new ConfigVO();
        renderJSON(Result.succeed(configData));
    }
    
    @ActionMethod(name = "增量数据", clazz = ConfigVO.class)
    public static void incrementData() {
        IncrementData incrementData = new IncrementData();
        renderJSON(Result.succeed(incrementData));
    }
    
    @ActionMethod(name = "地区数据", clazz = AreaVO.class)
    public static void areaData(@ParamField(name = "地址code", required = false) String code) {
        AreaVO areaVO = null;
        if (StringUtils.isBlank(code)) {
            areaVO = AreaVO.tree();
        } else {
            Area area = Area.findByCode(code);
            areaVO = new AreaVO(area);
            areaVO.children(Area.fetchByParent(area));
        }
        renderJSON(Result.succeed(areaVO));
    }
    
    @ActionMethod(name = "七牛token", clazz = QiniuVO.class)
    public static void qiniuUptoken() {
        String upToken = (String) Cache.get("qiniuUptoken");
        renderJSON(Result.succeed(new QiniuVO(upToken)));
    }
    
    public static void qiniuUptokenSimple() {
        String upToken = (String) Cache.get("qiniuUptoken");
        renderJSON(new QiniuVO(upToken));
    }
    
}

package models.area;

import models.BaseModel;
import utils.AmapUtils;
import utils.AmapUtils.AmapResult;
import utils.AmapUtils.AreaResult;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Area extends BaseModel {
    
    public String code;
    public String name;
    public String center;
    public String level;// country|province|city
    @ManyToOne
    public Area parent;
    
    public static Area add(String code, String name, String center, String level, Area parent) {
        Area area = new Area();
        area.code = code;
        area.name = name;
        area.center = center;
        area.level = level;
        area.parent = parent;
        return area.save();
    }
    
    public static void init() {
        if (!fetchAll().isEmpty()) {
            return;
        }
        AmapResult amapResult = AmapUtils.getAll();
        for (AreaResult areaResult : amapResult.districts) {
            Area area = add(areaResult.adcode, areaResult.name, areaResult.center, areaResult.level, null);
            init(areaResult, area);
        }
    }
    
    public static void init(AreaResult areaResult, Area parent) {
        for (AreaResult careaResult : areaResult.districts) {
            Area area = add(careaResult.adcode, careaResult.name, careaResult.center, careaResult.level, parent);
            init(careaResult, area);
        }
    }
    
    public void del() {
        this.logicDelete();
    }
    
    public static Area findByID(Long id) {
        return Area.find(defaultSql("id=?"), id).first();
    }
    
    public static Area findByCode(String code) {
        return Area.find(defaultSql("code=?"), code).first();
    }
    
    public static Area findRoot() {
        return Area.find(defaultSql("parent is null")).first();
    }
    
    public static List<Area> fetchByLevel(String level) {
        return Area.find(defaultSql("level = ?"), level).fetch();
    }
    
    public static List<Area> fetchByParent(Area parent) {
        return Area.find(defaultSql("parent=?"), parent).fetch();
    }
    
    public static List<Area> fetchAll() {
        return Area.find(defaultSql()).fetch();
    }
    
}

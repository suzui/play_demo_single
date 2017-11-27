package vos;

import annotations.DataField;
import models.area.Area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AreaVO extends OneData {
    
    @DataField(name = "地区code", enable = false)
    public String code;
    @DataField(name = "名称", enable = false)
    public String name;
    @DataField(name = "上级节点code", enable = false)
    public String parentCode;
    @DataField(name = "下级节点", enable = false)
    public List<AreaVO> children;
    
    public AreaVO() {
    }
    
    public AreaVO(Area area) {
        this.code = area.code;
        this.name = area.name;
        this.parentCode = area.parent == null ? null : area.parent.code;
    }
    
    public void children(List<Area> areas) {
        this.children = areas.stream().map(area -> new AreaVO(area)).collect(Collectors.toList());
    }
    
    public static List<AreaVO> list(List<Area> areas) {
        return areas.stream().map(area -> new AreaVO(area)).collect(Collectors.toList());
    }
    
    public static AreaVO tree() {
        Area root = Area.findRoot();
        Map<String, AreaVO> map = new HashMap<>();
        for (Area area : Area.fetchAll()) {
            AreaVO areaVO = new AreaVO(area);
            map.put(area.code, areaVO);
        }
        for (AreaVO areaVO : map.values()) {
            if (areaVO.code.equals(root.code)) {
                continue;
            }
            if (map.get(areaVO.parentCode).children == null) {
                List<AreaVO> list = new ArrayList<>();
                map.get(areaVO.parentCode).children = list;
            }
            map.get(areaVO.parentCode).children.add(areaVO);
        }
        return map.get(root.code);
    }
    
}

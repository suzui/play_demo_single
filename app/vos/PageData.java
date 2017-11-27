package vos;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import annotations.DataField;
import play.Logger;

public class PageData extends Data {

	@DataField(name = "分页页码")
	public Integer page;
	@DataField(name = "每页数量")
	public Integer size;
	@DataField(name = "总页数")
	public Integer totalPage;
	@DataField(name = "总条数")
	public Integer totalSize;

	@DataField(name = "数组")
	public List<? extends OneData> array;

	public PageData() {

	}

	public PageData(List<? extends OneData> array) {
		this.page = 1;
		this.size = array.size();
		this.totalPage = this.page;
		this.totalSize = this.size;
		this.array = array;
	}

	public PageData(int page, int size, int totalSize, List<? extends OneData> array) {
		this.page = page;
		this.size = size;
		this.totalPage = (totalSize - 1) / size + 1;
		this.totalSize = totalSize;
		this.array = array;
	}

	public Map<Object, Object> doc(Class<? extends OneData> onedata) {
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		try {
			for (Field f : this.getClass().getFields()) {
				DataField df = f.getAnnotation(DataField.class);
				if (df == null) {
					continue;
				}
				Type type = f.getType();
				if (List.class.isAssignableFrom((Class<?>) type)) {
					List<Map<Object, Object>> list = new ArrayList<>();
					if (onedata != null) {
						list.add(onedata.newInstance().doc());
					}
					map.put(f.getName(), list);
				} else {
					map.put(f.getName(), df.name() + "|" + f.getType().getSimpleName());
				}
			}
		} catch (Exception e) {
			Logger.info("[datadoc]:%s", e.getMessage());
		}
		return map;
	}

}

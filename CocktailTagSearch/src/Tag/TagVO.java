package Tag;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;

public class TagVO {
	private int id = 0;
	private String name = null;
	private String desc = null;
	private String category = null;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public TagVO() {
		
	}
	public TagVO(int tag_id, String tag_name, String desc, String category) {
		this.id = tag_id;
		this.name = tag_name;
		this.desc = desc;
		this.category = category;
	}
	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		
		hashMap.put("id", getId());
		hashMap.put("name", getName());
		hashMap.put("desc", getDesc());
		hashMap.put("category", getCategory());
		return hashMap;
	}
	@Override
	public String toString() {
		return "TagVO [id=" + id + ", name=" + name + ", desc=" + desc + ", category=" + category + "]";
	}
	
}

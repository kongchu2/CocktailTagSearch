package Cocktail;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Tag.TagVO;

public class CocktailVO {
	private int id = 0;
	private String name = null;
	private String image = null;
	private String desc = null;
	private String history = null;
	private String taste = null;
	private String base = null;
	private String build = null;
	private String glass = null;
	private ArrayList<TagVO> tagList;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getTaste() {
		return taste;
	}
	public void setTaste(String taste) {
		this.taste = taste;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getBuild() {
		return build;
	}
	public void setBuild(String build) {
		this.build = build;
	}
	public String getGlass() {
		return glass;
	}
	public void setGlass(String glass) {
		this.glass = glass;
	}
	public ArrayList<TagVO> getTagList() {
		return tagList;
	}
	public void setTagList(ArrayList<TagVO> tagList) {
		this.tagList = tagList;
	}
	public HashMap<String, Object> toHashMap() {
		HashMap<String ,Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", getId());
		hashMap.put("name", getName());
		hashMap.put("image", getImage());
		hashMap.put("desc", getDesc());
		hashMap.put("history", getHistory());
		hashMap.put("taste", getTaste());
		hashMap.put("base", getBase());
		hashMap.put("build", getBuild());
		hashMap.put("glass", getGlass());
		JSONArray tempTagArray = new JSONArray();
		for(TagVO tag : getTagList()) {
			HashMap<String, Object> tempHashMap = tag.toHashMap();
			JSONObject tagJson = new JSONObject(tempHashMap);
			tempTagArray.add(tagJson);
		}
		hashMap.put("tags", tempTagArray);
		return hashMap;
	}
	public HashMap<String, Object> toHashMapNeedToSearch() {
		HashMap<String ,Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", getId());
		hashMap.put("name", getName());
		hashMap.put("image", getImage());
		hashMap.put("desc", getDesc());
		JSONArray tempTagArray = new JSONArray();
		for(TagVO tag : getTagList()) {
			HashMap<String, Object> tempHashMap = tag.toHashMap();
			JSONObject tagJson = new JSONObject(tempHashMap);
			tempTagArray.add(tagJson);
		}
		hashMap.put("tags", tempTagArray);
		return hashMap;
	}
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("name", getName());
		json.put("image", getImage());
		json.put("desc", getDesc());
		json.put("history", getHistory());
		json.put("taste", getTaste());
		json.put("base", getBase());
		json.put("build", getBuild());
		json.put("glass", getGlass());
		JSONArray tagArray = new JSONArray();
		for(TagVO tag : tagList) {
			tagArray.add(tag.toJSON());
		}
		json.put("tags", tagArray);
		return json;
	}
	@Override
	public String toString() {
		return "CocktailVO [id=" + id + ", name=" + name + ", image=" + image + ", desc=" + desc + ", history="
				+ history + ", taste=" + taste + ", base=" + base + ", build=" + build + ", glass=" + glass
				+ ", tagList=" + tagList + "]";
	}
	
	
}

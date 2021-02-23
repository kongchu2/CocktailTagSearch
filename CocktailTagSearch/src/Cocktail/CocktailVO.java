package Cocktail;
import java.util.ArrayList;

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
	@Override
	public String toString() {
		return "CocktailVO [id=" + id + ", name=" + name + ", image=" + image + ", desc=" + desc + ", history="
				+ history + ", taste=" + taste + ", base=" + base + ", build=" + build + ", glass=" + glass
				+ ", tagList=" + tagList + "]";
	}
	
	
}

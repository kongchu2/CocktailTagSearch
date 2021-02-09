package Tag;


public class TagVO {
	private int id;
	private String name;
	private String desc;
	private String category;
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
	public TagVO(int tag_id, String tag_name, String desc, String category) {
		this.id = tag_id;
		this.name = tag_name;
		this.desc = desc;
		this.category = category;
	}
	@Override
	public String toString() {
		return "TagVO [id=" + id + ", name=" + name + ", desc=" + desc + ", category=" + category + "]";
	}
	
}

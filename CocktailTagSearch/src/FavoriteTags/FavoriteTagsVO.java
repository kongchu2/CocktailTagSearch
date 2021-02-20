package FavoriteTags;

public class FavoriteTagsVO {
	private int member_id;
	private int tag_id;
	public int getMember_id() {
		return member_id;
	}
	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}
	public int getTag_id() {
		return tag_id;
	}
	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}
	
	@Override
	public String toString() {
		return "FavoriteTagsVO [member_id=" + member_id + ", tag_id=" + tag_id + "]";
	}
}

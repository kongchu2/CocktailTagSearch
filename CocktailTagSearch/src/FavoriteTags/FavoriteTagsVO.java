package FavoriteTags;

public class FavoriteTagsVO {
	private int memberId;
	private int tagId;
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	
	@Override
	public String toString() {
		return "FavoriteTagsVO [memberId=" + memberId + ", tagId=" + tagId + "]";
	}
}

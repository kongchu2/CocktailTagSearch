package FavoriteCocktail;

public class FavoriteCocktailVO {
	private int memberId;
	private int cocktailId;
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getCocktailId() {
		return cocktailId;
	}
	public void setCocktailId(int cocktailId) {
		this.cocktailId = cocktailId;
	}
	
	@Override
	public String toString() {
		return "FavoriteCocktailVO [memberId=" + memberId + ", cocktailId=" + cocktailId + "]";
	}
}

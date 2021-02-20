package FavoriteCocktail;

public class FavoriteCocktailVO {
	private int member_id;
	private int cocktail_id;
	public int getMember_id() {
		return member_id;
	}
	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}
	public int getCocktail_id() {
		return cocktail_id;
	}
	public void setCocktail_id(int cocktail_id) {
		this.cocktail_id = cocktail_id;
	}
	
	@Override
	public String toString() {
		return "FavoriteCocktailVO [member_id=" + member_id + ", cocktail_id=" + cocktail_id + "]";
	}
}

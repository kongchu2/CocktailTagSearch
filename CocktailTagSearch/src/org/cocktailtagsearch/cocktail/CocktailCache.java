package org.cocktailtagsearch.cocktail;

import java.util.ArrayList;

import org.cocktailtagsearch.tag.TagVO;

public class CocktailCache {
	private final static int SCROLLING_LOAD_COUNT = 12;
	public static ArrayList<CocktailVO> CocktailList;
	public static ArrayList<CocktailVO> getCocktailList(int cocktailLength) {
		ArrayList<CocktailVO> temp = new ArrayList<CocktailVO>();
		for(int i=cocktailLength;i<cocktailLength+SCROLLING_LOAD_COUNT&&CocktailList.size()>i;i++)
			temp.add(CocktailList.get(i));
		return temp;
	}
	public static ArrayList<CocktailVO> getSearchedCocktailList(String searchStr,int cocktailLength) {
		ArrayList<CocktailVO> list = new ArrayList<CocktailVO>();
		for(int i=0;i<CocktailList.size();i++) {
			CocktailVO vo = CocktailList.get(i);
			if(vo.getName().contains(searchStr)) {
				list.add(vo);
			}
		}
		ArrayList<CocktailVO> temp = new ArrayList<CocktailVO>();
		for(int i=cocktailLength;i<cocktailLength+SCROLLING_LOAD_COUNT&&list.size()>i;i++)
			temp.add(list.get(i));
		return temp;
	}
 	public static ArrayList<CocktailVO> getCocktailListByTagList(ArrayList<Integer> tagList, int cocktailLength) {
		ArrayList<CocktailVO> list = new ArrayList<CocktailVO>();
		for(int i=0;i<CocktailList.size();i++) {
			CocktailVO vo = CocktailList.get(i);
			boolean flag = true;
			for(Integer tagId : tagList) {
				for(TagVO tag : vo.getTagList()) {
					if(tag.getId() == tagId) {
						flag = true;
						break;
					} else {
						flag = false;
					}
				}
			}
			if(flag) list.add(vo);
		}
		ArrayList<CocktailVO> temp = new ArrayList<CocktailVO>();
		for(int i=cocktailLength;i<cocktailLength+SCROLLING_LOAD_COUNT&&list.size()>i;i++)
			temp.add(list.get(i));
		return temp;
	}
	public static ArrayList<CocktailVO> getSearchedCocktailListByTagList(String searchStr,ArrayList<Integer>  tagIdList,  int cocktailLength) {
		ArrayList<CocktailVO> list = new ArrayList<CocktailVO>();
		for(int i=0;i<CocktailList.size();i++) {
			CocktailVO vo = CocktailList.get(i);
			if(vo.getName().contains(searchStr)) {
				boolean flag = true;
				for(Integer tagId : tagIdList) {
					for(TagVO tag : vo.getTagList()) {
						if(tag.getId() == tagId) {
							flag = true;
							break;
						} else {
							flag = false;
						}
					}
				}
				if(flag) list.add(vo);
			}
		}
		ArrayList<CocktailVO> temp = new ArrayList<CocktailVO>();
		for(int i=cocktailLength;i<cocktailLength+SCROLLING_LOAD_COUNT&&list.size()>i;i++)
			temp.add(list.get(i));
		return temp;
	}
}

package org.cocktailtagsearch.tag;

import java.util.ArrayList;

public class TagCache {
	private final static int MAX_SHOW_TAG_COUNT = 6;
	private static ArrayList<TagVO> TagList;
	public static void init() {
		TagQuerier dao = new TagQuerier();
		TagCache.TagList = dao.getTagList();
	}
	static {
		if(TagList == null) {
			init();
		}
	}
	public static ArrayList<TagVO> getTagList() {
		ArrayList<TagVO> temp = new ArrayList<TagVO>();
		for(int i=0;i<MAX_SHOW_TAG_COUNT&&TagList.size()>i;i++)
			temp.add(TagList.get(i));
		return temp;
	}                                                         
	public static ArrayList<TagVO> getSearchedTagList(String searchStr) {
		ArrayList<TagVO> list = new ArrayList<TagVO>();
		for(int i=0;i<TagList.size();i++) {
			TagVO vo = TagList.get(i);
			if(vo.getName().contains(searchStr)) {
				list.add(vo);
			}
		}
		ArrayList<TagVO> temp = new ArrayList<TagVO>();
		for(int i=0;i<MAX_SHOW_TAG_COUNT&&list.size()>i;i++)
			temp.add(list.get(i));
		return temp;
	}
 	public static ArrayList<TagVO> getTagListWithoutTagIdList(ArrayList<Integer> tagList) {
		ArrayList<TagVO> list = new ArrayList<TagVO>();
		for(int i=0;i<TagList.size();i++) {
			TagVO vo = TagList.get(i);
			Integer voId = vo.getId();
			boolean flag = true;
			for(Integer tagId : tagList) {
				if(voId == tagId) {
					flag = false;
					break;
				}
			}
			if(flag) list.add(vo);
		}
		ArrayList<TagVO> temp = new ArrayList<TagVO>();
		for(int i=0;i<MAX_SHOW_TAG_COUNT&&list.size()>i;i++)
			temp.add(list.get(i));
		return temp;
	}
	public static ArrayList<TagVO> getSearchedTagListWithoutTagIdList(String searchStr,ArrayList<Integer>  tagIdList) {
		ArrayList<TagVO> list = new ArrayList<TagVO>();
		for(int i=0;i<TagList.size();i++) {
			TagVO vo = TagList.get(i);
			if(vo.getName().contains(searchStr)) {
				boolean flag = true;
				Integer voId = vo.getId();
				for(Integer tagId : tagIdList) {
					if(voId == tagId) {
						flag = false;
						break;
					}
				}
				if(flag) list.add(vo);
			}
		}
		ArrayList<TagVO> temp = new ArrayList<TagVO>();
		for(int i=0;i<MAX_SHOW_TAG_COUNT&&list.size()>i;i++)
			temp.add(list.get(i));
		return temp;
	}
}


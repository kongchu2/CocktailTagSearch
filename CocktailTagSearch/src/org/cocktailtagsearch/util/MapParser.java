 package org.cocktailtagsearch.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.cocktailtagsearch.cocktail.CocktailVO;
import org.cocktailtagsearch.member.MemberVO;
import org.cocktailtagsearch.tag.TagVO;

public class MapParser {
	private static final String IMAGE_PATH = "image/cocktail/";

	public static CocktailVO convertHashMaptoCocktailVO(HashMap<String, Object> map) {
		CocktailVO vo = new CocktailVO();
		vo.setId(Caster.bigDecimalObjToInt(map.get("COCKTAIL_ID")));
		vo.setName((String)map.get("NAME"));
		vo.setImage(IMAGE_PATH + (String)map.get("IMAGE"));
		vo.setDesc((String)map.get("DESC"));
		vo.setHistory((String)map.get("HISTORY_DESC"));
		vo.setTaste((String)map.get("TASTE_DESC"));
		vo.setBase((String)map.get("BASE_ALCOHOL"));
		vo.setBuild((String)map.get("BUILD_METHOD"));
		vo.setGlass((String)map.get("COCKTAIL_GLASS"));
		return vo;
	}

	public static ArrayList<CocktailVO> convertHashMapListtoCocktailList(ArrayList<HashMap<String, Object>> list) {
		ArrayList<CocktailVO> resultList = new ArrayList<CocktailVO>();
		for (HashMap<String, Object> map : list) {
			CocktailVO vo = convertHashMaptoCocktailVO(map);
			resultList.add(vo);
		}
		return resultList;
	}

	public static TagVO convertHashMaptoTagVO(HashMap<String, Object> map) {
		TagVO vo = new TagVO();
		vo.setId(Caster.bigDecimalObjToInt(map.get("TAG_ID")));
		vo.setName((String)map.get("TAG_NAME"));
		vo.setDesc((String)map.get("DESC"));
		vo.setCategory( (String)map.get("CATEGORY"));
		return vo;
	}

	public static ArrayList<TagVO> convertHashMapListtoTagList(ArrayList<HashMap<String, Object>> list) {
		ArrayList<TagVO> resultList = new ArrayList<TagVO>();
		for (HashMap<String, Object> map : list) {
			TagVO vo = convertHashMaptoTagVO(map);
			resultList.add(vo);
		}
		return resultList;
	}

	public static MemberVO toMemberVO(HashMap<String, Object> map) {
		MemberVO member = new MemberVO();
		member.setMember_id(Caster.bigDecimalObjToInt(map.get("MEMBER_ID")));
		member.setLogin_id( (String)map.get("LOGIN_ID"));
		member.setName( (String)map.get("NAME"));
		member.setPassword( (String)map.get("PASSWORD"));
		member.setPermission(((String) map.get("PERMISSIONS")).charAt(0));
		return member;
	}

	public static ArrayList<MemberVO> toMemberList(ArrayList<HashMap<String, Object>> list) {
		ArrayList<MemberVO> resultList = new ArrayList<MemberVO>();
		for (HashMap<String, Object> map : list) {
			MemberVO vo = toMemberVO(map);
			resultList.add(vo);
		}
		return resultList;
	}
}

package Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import FavoriteCocktail.FavoriteCocktailVO;
import FavoriteTags.FavoriteTagsVO;
import basic.DAO;
import basic.JDBCConnection;
import basic.MapParser;

public class TagDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	DAO dao = new DAO();
	MapParser parser = new MapParser();

	public TagVO getTag(int id) {

		TagVO tag = null;

		try {
			conn = JDBCConnection.getConnection();

			int limit = 5;
			String sql = "SELECT * FROM (SELECT * FROM TAG WHERE TAG_ID=" + id + ") WHERE  ROWNUM <= " + limit;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				tag = new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
						rs.getString("CATEGORY"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}

		return tag;
	}

	public ArrayList<TagVO> getTagList() {

		ArrayList<TagVO> tagList = new ArrayList<TagVO>();

		try {
			conn = JDBCConnection.getConnection();

			stmt = conn.createStatement();
			int limit = 5;
			String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE ROWNUM <= " + limit
					+ " GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				tagList.add(new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
						rs.getString("CATEGORY")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}

		return tagList;

	}

	public ArrayList<TagVO> getTagListByTagIdList(ArrayList<Integer> tagIdList) {

		ArrayList<TagVO> tagList = new ArrayList<TagVO>();
		if (tagIdList.size() > 0) {
			try {
				conn = JDBCConnection.getConnection();

				stmt = conn.createStatement();
				int limit = 5;
				String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_ID IN(!) GROUP BY  TAG_ID, TAG_NAME, \"DESC\", CATEGORY ORDER BY TAG_ID";

				String subQueryWhere = "";
				for (int tagId : tagIdList) {
					subQueryWhere += tagId + ", ";
				}
				subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
				sql = sql.replace("!", subQueryWhere);

				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					tagList.add(new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
							rs.getString("CATEGORY")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCConnection.close(rs, stmt, conn);
			}
		}

		return tagList;

	}

	public ArrayList<TagVO> getTagListByTagIdListWithoutTagIdList(ArrayList<Integer> tagIdList,
			ArrayList<Integer> withoutTagIdList) {

		ArrayList<TagVO> tagList = new ArrayList<TagVO>();

		try {
			conn = JDBCConnection.getConnection();

			stmt = conn.createStatement();
			int limit = 5;
			String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_ID IN(1!) AND TAG_ID NOT IN(2!) GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";

			String subQueryWhere1 = "";
			for (int tagId : tagIdList) {
				subQueryWhere1 += tagId + ", ";
			}
			subQueryWhere1 = subQueryWhere1.substring(0, subQueryWhere1.length() - 2);
			sql = sql.replace("1!", subQueryWhere1);

			String subQueryWhere2 = "";
			for (int tagId : withoutTagIdList) {
				subQueryWhere2 += tagId + ", ";
			}
			subQueryWhere2 = subQueryWhere2.substring(0, subQueryWhere2.length() - 2);
			sql = sql.replace("2!", subQueryWhere2);

			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				tagList.add(new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
						rs.getString("CATEGORY")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, stmt, conn);
		}

		return tagList;

	}

	public TagVO getSearchedTagJustOne(String searchWord) {

		TagVO tag = null;

		try {
			conn = JDBCConnection.getConnection();

			String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_NAME LIKE'%" + searchWord
					+ "%' GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				tag = new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
						rs.getString("CATEGORY"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return tag;
	}

	public ArrayList<TagVO> getSearchedTagList(String searchWord) {

		ArrayList<TagVO> tagList = new ArrayList<TagVO>();

		try {
			conn = JDBCConnection.getConnection();

			// limit
			int limit = 5;
			String sql = "SELECT ROWNUM, TAG.* FROM (SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_NAME LIKE'%"
					+ searchWord + "%' GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY) TAG WHERE ROWNUM <= " + limit;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tagList.add(new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
						rs.getString("CATEGORY")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return tagList;
	}

	public ArrayList<TagVO> getSearchedTagListWithoutTagIdList(String searchWord, ArrayList<Integer> tagIdList) {

		ArrayList<TagVO> tagList = new ArrayList<TagVO>();

		try {
			conn = JDBCConnection.getConnection();

			// limit
			int limit = 5;
			String sql = "SELECT ROWNUM, TAG.* FROM (SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_NAME LIKE'%"
					+ searchWord
					+ "%' AND TAG_ID NOT IN(!) GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY) TAG WHERE ROWNUM <= "
					+ limit;

			String subQueryWhere = "";
			for (int tagId : tagIdList) {
				subQueryWhere += tagId + ", ";
			}
			subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
			sql = sql.replace("!", subQueryWhere);

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tagList.add(new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
						rs.getString("CATEGORY")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return tagList;
	}

	public ArrayList<TagVO> getTagListWithoutTagIdList(ArrayList<Integer> tagIdList) {

		ArrayList<TagVO> tagList = new ArrayList<TagVO>();

		try {
			conn = JDBCConnection.getConnection();

			// limit
			int limit = 5;
			String sql = "SELECT ROWNUM, TAG.* FROM (SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY FROM TAG WHERE TAG_ID NOT IN(!) GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY) TAG WHERE ROWNUM <= "
					+ limit;

			String subQueryWhere = "";
			for (int tagId : tagIdList) {
				subQueryWhere += tagId + ", ";
			}
			subQueryWhere = subQueryWhere.substring(0, subQueryWhere.length() - 2);
			sql = sql.replace("!", subQueryWhere);

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tagList.add(new TagVO(rs.getInt("TAG_ID"), rs.getString("TAG_NAME"), rs.getString("DESC"),
						rs.getString("CATEGORY")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return tagList;
	}

	public ArrayList<TagVO> getTagListByCocktailId(int cocktailId) {
		String sql = "SELECT TAG_ID, TAG_NAME, \"DESC\", CATEGORY " + "FROM TAG " + "WHERE TAG_ID IN ("
				+ "SELECT TAG_ID " + "FROM COCKTAIL_TAG " + "WHERE COCKTAIL_ID=?) "
				+ "GROUP BY TAG_ID, TAG_NAME, \"DESC\", CATEGORY";
		ArrayList<HashMap<String, Object>> list = dao.executeSQL(sql, cocktailId);
		ArrayList<TagVO> tagList = MapParser.convertHashMapListtoTagList(list);
		return tagList;
	}

	public int InsertTag(TagVO tag) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();

			ArrayList<TagVO> tl = getTagList();

			String sql = "INSERT INTO TAG" + "(\"TAG_ID\", \"TAG_NAME\", \"DESC\", \"CATEGORY\") "
					+ "VALUES ((select max(tag_id) from tag), ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, tag.getName());
			pstmt.setString(2, tag.getDesc());
			pstmt.setString(3, tag.getCategory());

			success = pstmt.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return success;
	}

	public int UpdateCocktail(TagVO tag, int tag_id) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();

			TagVO before_tag = getTag(tag_id);

			String sql = "UPDATE TAG SET " + "TAG_NAME=?, DESC=?, CATEGORY=? WHERE TAG_ID = " + tag_id;

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, tag.getName() != null ? tag.getName() : before_tag.getName());
			pstmt.setString(2, tag.getDesc() != null ? tag.getDesc() : before_tag.getDesc());
			pstmt.setString(3, tag.getCategory() != null ? tag.getCategory() : before_tag.getCategory());

			success = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return success;
	}

	public int DeleteCocktail(int tag_id) {
		int success = 0;
		try {
			conn = JDBCConnection.getConnection();

			String sql = "DELETE FROM COCKTAIL WHERE = " + tag_id;

			pstmt = conn.prepareStatement(sql);

			success = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCConnection.close(rs, pstmt, conn);
		}
		return success;
	}
}

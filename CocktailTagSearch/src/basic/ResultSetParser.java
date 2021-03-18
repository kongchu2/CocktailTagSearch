package basic;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultSetParser {
	public static ArrayList<HashMap<String, Object>> convertResultSetToArrayList(ResultSet rs){
		ArrayList<HashMap<String, Object>> tempList = null;
		try {
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			tempList = new ArrayList<HashMap<String, Object>>();
			while (rs.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>(columns);
				for (int i = 1; i <= columns; ++i) {
					row.put(md.getColumnName(i), rs.getObject(i));
				}
				tempList.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempList;
	}
}
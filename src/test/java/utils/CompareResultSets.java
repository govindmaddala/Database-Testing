package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;


public class CompareResultSets {
	
	
	public static boolean compareResultSets(ResultSet resultSet1,ResultSet resultSet2) throws SQLException {
		while(resultSet1.next()) {
			resultSet2.next();
			int count = resultSet1.getMetaData().getColumnCount();
			for(int i=1;i<=count;i++) {
				if(!StringUtils.equals(resultSet1.getString(i), resultSet2.getString(i))) {
					return false;
				}
			}
		}
		return true;
	}
}

package org.prasad.ratingsnormalization.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.prasad.ratingsnormalization.db.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalizationEvaluator {

	private static Logger LOGGER = LoggerFactory.getLogger(NormalizationEvaluator.class);

	public void evaluateNormalization() {
		DBConnectionManager connManager = DBConnectionManager.getInstance();
		Connection conn = connManager.getConnection();
		int count = 0;
		ResultSet results = getBenifittedBusinesses(conn);
		try {
			while(results.next()) {
				count++;
				LOGGER.info("Yelp Rating: "+results.getDouble("stars") +", NormalizedRating: "+results.getDouble("normalized_stars"));
			}	
			LOGGER.info("Total No of Businesses benifitted: "+count);
		}catch (SQLException e) {
			LOGGER.error("Error while reading business results from DB!", e);
		}
		
		
		connManager.closeConnection(conn);
	}
	
	private ResultSet getBenifittedBusinesses(Connection conn) {
		ResultSet rs = null;
		String query = "select * from business where stars != normalized_stars";
		rs = DBConnectionManager.getInstance().extractResults(conn, query);
		return rs;
	}
}

package org.prasad.ratingsnormalization.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.prasad.ratingsnormalization.db.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is class where user reviews are analyzed to identify stingy reviewers.
 * @author DurgaPrasad
 *
 */
public class ReviewsAnalyzer {

	public static Logger LOGGER = LoggerFactory.getLogger(ReviewsAnalyzer.class);

	/*
	 * 1. Create the intermediate reviews table with users having greater than 10 reviews.
	 * 2. For each user, extract all her reviews and check if all of them are below the business's average rating
	 * 3. Invoke RatingsNormalization handler for the qualifying users.
	 */
	public void analyzeReviews() {
		RatingsNormalizationHandler normHandler = new RatingsNormalizationHandler();
		DBConnectionManager connManager = DBConnectionManager.getInstance();
		Connection conn = connManager.getConnection();

		createIntermediateReviewsTable(conn);
		LOGGER.info("Intermediatereviews table created!");
		ResultSet rs = getUserIdsIntermediateTable(conn);
		try {
			while (rs.next()) {
				String userId = rs.getString("user_id");
				List<IntermediateReview> userReviews = this.getUserReviewsIfStingy(conn, userId);
				if (!(userReviews == null)) {
					normHandler.handleNormalization(conn, userReviews);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Error while reading reviews results from DB!", e);
		}
		connManager.closeConnection(conn);
	}

	private ResultSet getUserIdsIntermediateTable(Connection conn) {
		ResultSet rs = null;
		String query = "select distinct(user_id) from intermediatereviews";
		rs = DBConnectionManager.getInstance().extractResults(conn, query);
		return rs;
	}
	
	/*
	 * Create the intermediate table where reviews are joined with business on business_id for reviewers with at least 10 reviews
	 */
	private void createIntermediateReviewsTable(Connection conn) {
		String query = "drop table if exists intermediatereviews";
		DBConnectionManager.getInstance().executeUpdate(conn, query);
		
		query = "create table intermediatereviews as select reviews.user_id, reviews.business_id, reviews.stars as userRating, business.stars as businessRating,"
				+ "business.review_count from reviews join business on reviews.business_id = business.business_id "
				+ "where reviews.user_id in (select user_id from reviews group by user_id having count(*) > 10)";
		DBConnectionManager.getInstance().executeUpdate(conn, query);
	}

	/**
	 * Get all the reviews for the given user, iterate through each one to check if it is below the corresponding business's average rating.
	 * If all the reviews are below the average, consider this user to be stingy and return the reviews list. Return NULL otherwise.
	 * @param conn
	 * @param userId
	 * @return
	 */
	private List<IntermediateReview> getUserReviewsIfStingy(Connection conn, String userId) {
		List<IntermediateReview> filteredReviews = new ArrayList<IntermediateReview>();
		String query = "select * from intermediatereviews where user_id = '" + userId + "'";
		ResultSet results = DBConnectionManager.getInstance().extractResults(conn, query);
		IntermediateReview review = null;

		try {
			while (results.next()) {
				double userRating = results.getDouble("userRating");
				String businessId = results.getString("business_id");
				double businessRating = results.getDouble("businessRating");
				if (userRating >= businessRating) {
					return null;
				}
				else {
					review = new IntermediateReview();
					review.setUserRating(userRating);
					review.setBusinessId(businessId);
					review.setUserId(userId);
					review.setBusinessRating(businessRating);
					review.setReviewsCount(results.getInt("review_count"));
					filteredReviews.add(review);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Error while reading reviews results from DB!", e);
		}
		return filteredReviews.isEmpty() ? null : filteredReviews;
	}
}

package org.prasad.ratingsnormalization.core;

import java.sql.Connection;
import java.util.List;

import org.prasad.ratingsnormalization.db.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a way to fix the ratings of businesses whose average rating is affected by ratings from stingy users 
 * @author 
 *
 */
public class RatingsNormalizationHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(NormalizationEvaluator.class);

	/**
	 * Considers a business's average rating to be impacted by ratings from stingy users only if it has less than 20 ratings. 
	 * The assumption is that the effect will be averaged out as the number of reviews increase. This method fixes the ratings 
	 * of the affected businesses by the normalization factor. 
	 * @param conn
	 * @param userReviews
	 */
	public void handleNormalization(Connection conn, List<IntermediateReview> userReviews) {
		int reviewsCount = 0;
		double total = 0;
		double normalizedRating = 0.0;
		for (IntermediateReview review : userReviews) {
			reviewsCount = review.getReviewsCount();
			if(reviewsCount > 20) {
				continue;
			}
			total = reviewsCount * review.getBusinessRating();
			total += calculateNormalizationFactor(review.getBusinessRating() - review.getUserRating());
			normalizedRating = total / reviewsCount;
			updateBusinessTuple(conn, review.getBusinessId(), normalizedRating);
		}

	}

	/*
	 * Update the normalized_rating of the given business in the DB.
	 */
	public void updateBusinessTuple(Connection conn, String businessId, double normalizedRating) {
		String query = "update business set normalized_stars = " + normalizedRating + " where business_id = '"
				+ businessId + "'";
		DBConnectionManager.getInstance().executeUpdate(conn, query);
	}

	/*
	 * Calculates the normalization factor. Instead of taking the absolute difference between the given rating and the average
	 * rating as the normalization factor, the following logic makes sure that effect of larger differences is gracefully reduced.
	 * 
	 * Please note that this strategy to calculate the normalization factor is just a raw attempt with no research backing. In a 
	 * production system, ideally we need to build a correction model which has more levers for all the contributing factors.  
	 * @param scoreDifference
	 * @return
	 */
	private double calculateNormalizationFactor(double scoreDifference) {
		double normFactor = 0.0;
		if (scoreDifference < 1) {
			normFactor = scoreDifference;
		} else if (scoreDifference < 2) {
			normFactor = 0.5 + (scoreDifference - 1);
		} else if (scoreDifference < 3) {
			normFactor = 0.5 + 0.25 + (scoreDifference - 2);
		} else if (scoreDifference < 4) {
			normFactor = 0.5 + 0.25 + 0.125 + (scoreDifference - 3);
		} else {
			normFactor = 0.5 + 0.25 + 0.125 + 0.0625 + (scoreDifference - 4);
		}
		return normFactor;
	}
}

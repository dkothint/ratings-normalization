package org.prasad.ratingsnormalization.core;

public class IntermediateReview {

	private String userId;
	private String businessId;
	private double userRating;
	private double businessRating;
	private int reviewsCount;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public double getUserRating() {
		return userRating;
	}
	public void setUserRating(double userRating) {
		this.userRating = userRating;
	}
	public double getBusinessRating() {
		return businessRating;
	}
	public void setBusinessRating(double businessRating) {
		this.businessRating = businessRating;
	}
	public int getReviewsCount() {
		return reviewsCount;
	}
	public void setReviewsCount(int reviewsCount) {
		this.reviewsCount = reviewsCount;
	}
	
}

package org.prasad.ratingsnormalization.db;

import com.google.gson.annotations.SerializedName;

public class BusinessTuple {
	
	private int ID;
	
	@SerializedName("business_id")
	private String businessId;
	
	@SerializedName("stars")
	private double rating;
	
	@SerializedName("review_count")
	private int reviewsCount;
	
	@SerializedName("normalized_stars")
	private double normalizedRating;
	
	@SerializedName("name")
	private String businessName;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getReviewsCount() {
		return reviewsCount;
	}

	public void setReviewsCount(int reviewsCount) {
		this.reviewsCount = reviewsCount;
	}

	public double getNormalizedRating() {
		return normalizedRating;
	}

	public void setNormalizedRating(double normalizedRating) {
		this.normalizedRating = normalizedRating;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
}

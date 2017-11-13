package org.prasad.ratingsnormalization.db;

import com.google.gson.annotations.SerializedName;

public class ReviewsTuple {
	
	private int ID;
	
	@SerializedName("user_id")
	private String userId;
	
	@SerializedName("business_id")
	private String businessId;

	@SerializedName("stars")
	private double rating;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
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
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
}

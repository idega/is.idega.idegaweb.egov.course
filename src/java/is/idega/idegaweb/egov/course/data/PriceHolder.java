/*
 * $Id$ Created on Apr 10, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.data;

import com.idega.user.data.User;

public class PriceHolder implements Comparable {

	private User user = null;
	private float price = 0;
	private float discount = 0;
	private float cost = 0;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public int compareTo(Object arg0) {
		PriceHolder obj = (PriceHolder) arg0;
		int compare = (int) -(obj.getPrice() - getPrice());
		if (compare == 0) {
			compare = ((Integer) getUser().getPrimaryKey()).intValue() - ((Integer) obj.getUser().getPrimaryKey()).intValue();
		}

		return compare;
	}

	public boolean equals(Object obj) {
		PriceHolder holder = (PriceHolder) obj;
		return holder.getUser().equals(getUser()) && holder.getPrice() == getPrice();
	}

	public String toString() {
		return getUser().getName() + " - " + getPrice();
	}
}
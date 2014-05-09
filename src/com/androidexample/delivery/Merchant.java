package com.androidexample.delivery;

import java.text.DecimalFormat;

public class Merchant implements Comparable<Merchant> {

	String name;
	int id;
	int rating;
	boolean status;
	String cuisine;
	double distance;

	public void setName(String n) {name = n;}
	public String getName() {return name;}
	
	public void setID(int i) {id = i;}
	public int getID() {return id;}
	
	public void setStatus(boolean s) {status = s;}
	public boolean getStatus() {return status;}


	public void setCuisine(String n) {cuisine = n;}
	public String getCuisine() {return cuisine;}

	public void setDistance(double d) {distance = d;}
	public String getDistance() {
		DecimalFormat df1 = new DecimalFormat("#.#");
		return Double.valueOf(df1.format(distance)) + "mi away";
	}
	
	public void setRating(int r) {rating = r;}
	public int getRating() {return rating;}
	
	public Merchant(String n, int i, int r, boolean s, String c, double d) {
		name = n;
		id = i;
		rating = r;
		status = s;
	    cuisine = c;
	    distance = d;
	}
	
	public int compareTo(Merchant o) {
		return Double.compare(this.distance, o.distance);
	}
}

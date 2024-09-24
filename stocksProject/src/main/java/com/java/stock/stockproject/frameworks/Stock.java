package com.java.stock.stockproject.frameworks;

public class Stock {

	private int quantity;
	private String name;
	private double pricePerShare;
	private double totalPrice;
	
	public Stock(String n, int x, double pps)
	{
		quantity = x;
		name = n;
		pricePerShare = pps;
		totalPrice = quantity * pricePerShare;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getQuantity()
	{
		return quantity;
	}
	
	public void setQuantity(int x)
	{
		quantity = x;
	}
	
	public double gerPricePerShare()
	{
		return pricePerShare;
	}
	
	public double getTotalPrice()
	{
		return totalPrice;
	}
}

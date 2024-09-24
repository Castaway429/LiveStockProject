package com.java.stock.stockproject.frameworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;


public abstract class Account 
{
	private double balance;
	private File myHistory;
	DecimalFormat fmt = new DecimalFormat("#.##");
	
	public Account()
	{
		balance = 0.00;
	}
	
	public Account(double x, File y) //Overloader for custom 
	{
		balance = x;
		fmt.format(balance);
		myHistory = y;
	}

	public void deposit(double x) throws IOException
	{
		balance += x;
		fmt.format(balance);
	}
	
	public void withdraw(double x) throws IOException
	{
		balance -= x;
		fmt.format(balance);
	}
	
	//Getters and printers
	public double getBalance()
	{
		return balance;
	}
	
	public void setBalance(double x)
	{
		balance = x;
		fmt.format(balance);
	}
	
	public File getHistory()
	{
		return myHistory;
	}
	
	public void printHistory() throws FileNotFoundException
	{
		Scanner scn = new Scanner(myHistory);
		while (scn.hasNextLine())
		{
			String line = scn.nextLine();
			System.out.println(line);
		}
		
		scn.close();
	}
	
}

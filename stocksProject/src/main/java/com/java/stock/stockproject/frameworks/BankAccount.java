package com.java.stock.stockproject.frameworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.io.IOException;
import java.util.Scanner;

public class BankAccount extends Account
{	
	
	public BankAccount() throws IOException
	{
		super(10000.0, new File("/Users/Ibrahim/eclipse-workspace/stocksProject/src/main/java/com/java/stock/stockproject/frameworks/bank_transaction_history.txt"));

		File history = new File(super.getHistory().toString());
		Scanner scn = new Scanner(history);
		
		String fileContent = "";
		
		while (scn.hasNextLine())
		{
			fileContent = fileContent.concat(scn.nextLine()+"\n");
		}
				
		if (fileContent.indexOf("Creation") == -1)
		{
			writeToHistory("Creation", 10000.0);
			scn.close();
		} else
		{
			//The account has already been created
			int currBalance = fileContent.lastIndexOf("\t");
			super.setBalance(Double.parseDouble(fileContent.substring(currBalance + 1)));
		}
		

	}
	
	public BankAccount(double x) throws IOException
	{
		super(x, new File("/Users/Ibrahim/eclipse-workspace/stocksProject/src/main/java/com/java/stock/stockproject/frameworks/bank_transaction_history.txt"));
		
		//Checking for an existing balance!
		File history = new File(super.getHistory().toString());
		Scanner scn = new Scanner(history);
		
		String fileContent = "";
		
		while (scn.hasNextLine())
		{
			fileContent = fileContent.concat(scn.nextLine()+"\n");
		}
				
		if (fileContent.indexOf("Creation") == -1)
		{
			writeToHistory("Creation", x);
			scn.close();
		} else
		{
			//The account has already been created
			int currBalance = fileContent.lastIndexOf("\t");
			super.setBalance(Double.parseDouble(fileContent.substring(currBalance + 1)));
		}
		
	}
	
	public void deposit(double x) throws IOException
	{
		
		super.deposit(x);
		
		//NGL don't know what this does but I need this to work
		//it honestly just catches any errors that happens so the code still runs
		try {
			writeToHistory("Deposit", x);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void withdraw(double x) throws IOException
	{
		super.withdraw(x);
		
		//NGL don't know what this does but I need this to work
		//it honestly just catches any errors that happens so the code still runs
		try {
			writeToHistory("Withdraw", x);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeToHistory(String event, double amount) throws IOException
	{
		
		//Fix formatting when you have time!
		
		Scanner scn = new Scanner(super.getHistory());
	    StringBuilder currHistory = new StringBuilder(); //Building a new string
	    
		while (scn.hasNextLine())
		{
			String line = scn.nextLine();
			currHistory.append(line);
		}
		
		String log = event + "\t\t" + amount + "\t\t" + java.time.LocalDate.now() + "\t\t" + super.getBalance()+"\n";
		
		//System.out.println(log);
		//System.out.println(currHistory);

		FileWriter writer = new FileWriter(super.getHistory().toString(), true); //Put it to append mode?
		writer.write(log);
		
		writer.close();
		scn.close();
	}

}

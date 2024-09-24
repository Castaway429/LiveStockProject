package com.java.stock.stockproject.frameworks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StockAccount extends Account
{

	ArrayList<Stock> stocksList;
	File myPortfolio = new File("/Users/Ibrahim/eclipse-workspace/stocksProject/src/main/java/com/java/stock/stockproject/frameworks/stock_portfolio_save");
	
	//Save the balance and be able to get the balancec
	
	public StockAccount() throws FileNotFoundException
	{
		//IF ADDING THE STOCK BREAKS ADD BACK THE "-" IN THE SEPERATOR LINE
		super(0.0, new File("/Users/Ibrahim/eclipse-workspace/stocksProject/src/main/java/com/java/stock/stockproject/frameworks/stock_transaction_history.txt"));
		
		//Import cash balance from file
		Scanner scn = new Scanner(myPortfolio);
		String fileContent = "";

		while (scn.hasNextLine())
		{
			fileContent = fileContent.concat(scn.nextLine()+"\n");
		}
		
		//Get and set the balance
		double savedBalance = Double.parseDouble(fileContent.substring(fileContent.indexOf("$")+1, fileContent.indexOf("\n")));
		super.setBalance(savedBalance);

		//Initilize the array list
		stocksList = new ArrayList<Stock>();
		
		//Loop through the portfolio to find what stocks are already owned and add them to the list
		int start = fileContent.lastIndexOf("-");
		fileContent = fileContent.substring(start+2);
		
		// Split the content by "\t\t" and add elements to an ArrayList
		//System.out.println(fileContent);
		
		int count = 1;
		
		String nameFill = "";
		int quantFill = 0;
		double ppsFill = 0; 
		
		while(fileContent.indexOf("\t\t") != -1)
		{
			String x = fileContent.substring(0, fileContent.indexOf("\t\t"));
			
			//System.out.println(x);
			
			//Trim file content
			fileContent = fileContent.substring(fileContent.indexOf("\t\t")+2);	
			
			switch(count)
			{
			case 1:
				nameFill = x;
				break;
			case 2:
				quantFill = Integer.parseInt(x);
				break;
			case 3:
				ppsFill = Double.parseDouble(x);
				Stock createdStock = new Stock(nameFill, quantFill, ppsFill);
				stocksList.add(createdStock);
				System.out.println("Added stock to the arraylist");
				count = 0;
				
				break;
			}
			
			count++;
		}
		
        
		scn.close();
	}

	public void displayPortfolio() throws FileNotFoundException
	{
		Scanner scn = new Scanner(myPortfolio);
		while (scn.hasNextLine())
		{
			String line = scn.nextLine();
			System.out.println(line);
		}
		
		scn.close();
	}
	
	//Everything stocks
	
	
	public void buyShare(String compSymb, int numOfShares, double price) throws IOException
	{
		//Add to portfolio
		double total = numOfShares * price;
		if (total> super.getBalance())
		{
			System.out.println("~ Insufficent funds to purchase ~");
			return;
		}
		
		//Takes out the money from the account
		super.withdraw(total);
		
		
		//If stock already exists in the arraylist then add it
		boolean found = false;

		for (Stock x : stocksList)
		{
			System.out.println(x.getName());
			if (x.getName().equals(compSymb))
			{
				System.out.println("Updated quanity");
				//Increase quantity of x
				x.setQuantity(numOfShares + x.getQuantity());
				//update the quantity of x in the portfolio
				found = true;
				break;
			}
		}
		
		//If stock is not found then add it to the arraylist
		if (!found)
			System.out.println("Adding new stock!");
			//Create a new stock
			Stock stock = new Stock(compSymb, numOfShares, price);
			stocksList.add(stock);
		
		//Write to history
		try {
			writeToHistory("Buy", compSymb, numOfShares, price);
			updatePortfolio();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void sellShare(String compSymb, int numOfShares, double price) throws IOException
	{
		//Sell share logic here!
		//If stock already exists in the arraylist then add 
		Stock foundStock = null;
		
		for (Stock x : stocksList)
		{
			//Check if stock exits
			if (x.getName().equals(compSymb))
			{
				//Get the stock reference
				foundStock = x;
			}
		}
		
		double total = numOfShares * price;
				
		//Takes out the money from the account
		super.deposit(total);
		foundStock.setQuantity(foundStock.getQuantity() - numOfShares);

		int i = 0;
		if (foundStock.getQuantity() == 0)
		{
			for(Stock x : stocksList)
			{
				if (x.getName() == foundStock.getName())
				{
					stocksList.remove(i);
					break;
				}
				i++;
			}
		}
		
		//Write it to the history
		try {
			writeToHistory("Sell", compSymb, numOfShares, price);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getPriceOfStock(String symb) throws MalformedURLException
	{ 

		Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
		String date = yesterday.toString().substring(0,10);
		Double vw = 0.0;
				
		URL u = new URL("https://api.polygon.io/v2/aggs/ticker/"+symb+"/range/1/day/"+date+"/"+date+"?apiKey=Jpd65ssdpYysDNJsTErYSjNlLecC7P57");
		
		try {
			
			HttpURLConnection hr = (HttpURLConnection) u.openConnection();
			
			if (hr.getResponseCode() == 200)
			{

				InputStream im = hr.getInputStream();
				StringBuffer sb = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader(im));
				
				String line = br.readLine();
				while(line != null)
				{
					sb.append(line);
					line=br.readLine();
				}
				
				 // Parsing JSON response
                JSONParser parser = new JSONParser();
               JSONObject here = (JSONObject) parser.parse(sb.toString());

                // Output the parsed JSON object
                //System.out.println(here.toJSONString());
               
                JSONArray resultsArray = (JSONArray) here.get("results");
                if (resultsArray != null && !resultsArray.isEmpty()) {
                    JSONObject firstResult = (JSONObject) resultsArray.get(0);
                    vw = (Double) firstResult.get("vw");
                    //System.out.println("VW: " + vw);
                } else {
                    System.out.println("This stock does not exist! Try again!");
                }
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return vw;
	}
	
	//End of everything stocks	
	
	public void deposit(double x) throws IOException
	{
		super.deposit(x);
		updatePortfolio();
	}
	
	public void withdraw(double x) throws IOException
	{
		super.withdraw(x);
		updatePortfolio();
	}

	private void writeToHistory(String event, String compSymb, int numOfShares, double price) throws IOException
	{
		//Event		Company Symbol		Number		PPS		Total Value		Time	

		Scanner scn = new Scanner(super.getHistory());
	    StringBuilder currHistory = new StringBuilder(); //Building a new string
	    
		while (scn.hasNextLine())
		{
			String line = scn.nextLine();
			//System.out.println(line);
			currHistory.append(line);
		}
		
		String log = event + "\t\t" + compSymb + "\t\t" + numOfShares + "\t\t" + price + "\t\t" + (numOfShares * price) + "\t\t" + java.time.LocalTime.now();

		FileWriter writer = new FileWriter(super.getHistory().toString(), true); //Put it to append mode?
		writer.write("\n"+ log);

		
		writer.close();
		scn.close();
	}



	private void updatePortfolio() throws IOException
	{
		FileWriter writer = new FileWriter(myPortfolio, false); //Put it to append mode?
		FileWriter writeStock = new FileWriter(myPortfolio, true); //Append stocks to the end
		Scanner scn = new Scanner(myPortfolio);
		StringBuilder currHistory = new StringBuilder(); //Building a new string

		while (scn.hasNextLine())
		{
			String line = scn.nextLine();
			//Fix the cash balance
			currHistory.append(line);
		}
		
		//Cut currHistory to just include the stocks
		
		//Add stocks to the portfolio and array
		
		writer.write("Cash Balance = $" + super.getBalance() +"\n Company Symbol\t\tNumber\t\tPrice per share\t\tTotal Value" + "\n-");
		
		if (stocksList.size() > 0)
		{
			for (Stock x : stocksList)
			{
				writer.write("\n" + x.getName() + "\t\t" + x.getQuantity() + "\t\t" + x.gerPricePerShare() + "\t\t" + x.getTotalPrice()+"\n");
			}
		}
		
		
		scn.close();
		writer.close();
	}

	public ArrayList<Stock> getList()
	{
		return stocksList;
	}

}

package com.java.stock.stockproject;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.java.stock.stockproject.frameworks.BankAccount;
import com.java.stock.stockproject.frameworks.Stock;
import com.java.stock.stockproject.frameworks.StockAccount;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The "Main program" if you can call it that to run everything
 *
 */

public class Runner 
{
    public static void main( String[] args ) throws IOException
    {
        BankAccount bank = new BankAccount();
        StockAccount stock = new StockAccount();
                
        //This is essentially the type of relationship they will have
        //I definetly need some way for the to communicate between each other!!!
        /*print("\n\t! CAUTION !\n"
        		+ "In order to properly save data please exit via this page!\n");*/
        
        print("Welcome to the Account Management System.");
        mainMenu(bank, stock);
    }  
    
    //Main menu
    public static void mainMenu(BankAccount bank, StockAccount stock) throws IOException
    {        
    	Scanner scn = new Scanner(System.in);
    	 int selectedOption;
          
          do
          {
          	print("\nPlease select an account to access:\n"
          	      + "\t1. Stock Portfolio Account\n\t2. Bank Account\n\t3. Exit");
          	System.out.print("Option: ");
          	 selectedOption = scn.nextInt();
          } while(selectedOption <= 0);
       
          //Anything greater than 3 is an exit
          switch(selectedOption)
          {
          case 1:
        	  stockMenu(bank, stock);
        	  break;
          case 2:
        	  bankMenu(bank, stock);
        	  break;
          }
          
          if (selectedOption >= 3)
        	  print("\nSaving your account value!...");
          //Save things here
        	  print("\nThank you so much for using our software!");
      
         scn.close();
    }
    
    //Stock and sub stock menu
    public static void stockMenu(BankAccount bank, StockAccount stock) throws IOException
    {
   	 	int selectedOption;
   	 	Scanner scn = new Scanner(System.in);

    	print("\nStock Account Menu\nPlease select an option: ");
    	
    	 do
         {
    		 print("\t1. Display the price of a stock symbol\n\t2. Display current portfolio"
    	    	+ "\n\t3. Buy Shares\n\t4. Sell Shares\n\t5. View transaction history\n\t6. "
    	    	+ "Return to previous menu");
    		 System.out.print("Option: ");
    		 
         	 selectedOption = scn.nextInt();
         } while(selectedOption <= 0);
    	 
    
         switch(selectedOption)
         {
         case 1:
        	 print("\n== What stock symbol would you like to view? ==\n");
        	 System.out.print("Symbol: ");
        	 String stockNametoView = scn.next(); 
        	 double viewPrice = stock.getPriceOfStock(stockNametoView);
        	 if (viewPrice != 0.0)
            	 print("~ The current price of " +stockNametoView+ " is $" +viewPrice+" per stock! ~");

        	 stockMenu(bank, stock);
        	 break;
       	  	
         case 2: 
        	 print("\n\t~ Current Portfolio ~\n");
        	 stock.displayPortfolio();
        	 stockMenu(bank, stock); //Call back the function to redo the loop
        	 break;
       	  
         case 3: 
        	 print("\n== What stock symbol would you like to buy? ==\n");
        	 System.out.print("Symbol: ");
        	 String stockNametoBuy = scn.next();
        	 
        	 //Call to API to get prices
        	 double sellPrice = stock.getPriceOfStock(stockNametoBuy);
        	 if (sellPrice == 0.0)
        	 {
        		 stockMenu(bank, stock);
        		 break;
        	 } else
        	 {
        		 print("\n== How much would you like to buy of " +stockNametoBuy+ " ==\n");
            	 System.out.print("Quanitity: ");
            	 int quantityToBuy = scn.nextInt();
            	 
            	 
            	 stock.buyShare(stockNametoBuy, quantityToBuy, sellPrice);
            	 stockMenu(bank, stock);
            	 break;
        	 }
        	 
         case 4: //Sell
        	 boolean found = false;
        	 
        	 print("\n== What stock symbol would you like to sell? ==\n");
        	 System.out.print("Symbol: ");
        	 String stockNametoSell = scn.next();
        	 
        	 //Call to API to get prices
        	 print("\n== What how much would you like to sell==\n");
        	 System.out.print("AMt: ");
        	 int stockAmttoSell = scn.nextInt();
        	 
        	 ArrayList<Stock> list = stock.getList();      
        	 
        	 double selfSellPrice = 0.0;
        	 for (Stock x : list)
        	 {
        		 System.out.println(x.getName());
        		 if (x.getName().equals(stockNametoSell))
        		 {
        			 if (x.getQuantity() < stockAmttoSell)
        			 {
        				 System.out.println("~ Too much selling! ~");
        			 } else
            			 found = true;

        		 }
        	 }
        	 
        	 if (!found)
        	 {
     			System.out.println("~ You do not own any shares of this stock? ~");
     			stockMenu(bank, stock);
        		 break;
        	 }
        	 {
        		 stock.sellShare(stockNametoSell, stockAmttoSell, selfSellPrice);
        	 }
        	 
        	 //sold stock!!!
        	 stockMenu(bank, stock);
         case 5:   
        	 print("\n\t~ Current History ~\n");
        	 stock.printHistory();
        	 stockMenu(bank, stock);
        	 break;
         }
         
         if (selectedOption >= 6)
       	  mainMenu(bank, stock);
    	 
         scn.close();
    }
    
    //bank and sub bank menu
    public static void bankMenu(BankAccount bank, StockAccount stock) throws IOException
    {
    	int selectedOption;
   	 	Scanner scn = new Scanner(System.in);
		 print("\nBank Account Menu\nPlease select an option:");

    	 do
         {
    	    	print("\t1. View Balance\n\t2. Deposit money (into Bank Account via Stock Account)"
    	    			+ "\n\t3. Withdraw money(From Bank Account into Stock Account) \n\t4. View history\n\t5. "
    	    			+ "Return to previous menu");
    	    	System.out.print("Option: ");
    		 
         	 selectedOption = scn.nextInt();
         } while(selectedOption <= 0);
    	 
    
         switch(selectedOption)
         {
         case 1: //View Balance
        	 print("\n== Your current balance is $" + bank.getBalance() + ". ==");
        	 bankMenu(bank, stock);
        	 break;
        	 
         case 2: //Deposits
        	 if (stock.getBalance() == 0.0) //Ends the function if there is no money to be taken
        	 {
        		 print("\n!! Error !!\nBalance is too low to be withdrawn from!");
        		 bankMenu(bank, stock);
        	 }
        		 
        	 print("\n\t!! CAUTION !!\n"
          	 		+ "All deposits come from your Stock Accounts Balance and move to your Bank Accounts Cash Balance.");
          	 print("\nPress X to cancel transaction. Press any other key to continue:");
          	 String transactionChoice = scn.next();
          	 
          	 if ((transactionChoice.equals("X") || transactionChoice.equals("x")))
          	 {
          		 print("\n\t~ Cancelled transaction ~\n");
          		 bankMenu(bank, stock);
          	 } else
          	 {
          		 double withdrawalAmount = 0;
          		 boolean sufficentToProceed = false;
          		 
          		 do
          		 { 
          			 
          			print("\n== Enter how much you would like to withdraw ==\n");
               		withdrawalAmount = scn.nextDouble();
               		
               		if (withdrawalAmount > stock.getBalance())  //Too much being withdrawn
               		{
               			print("\n! Amount is too high to withdrawal !\nPress any key to withdrawal max amount. "
               					+ "Press X to cancel: ");
               			String s = scn.next();
               			
               			if (s.equals("X") || s.equals("x"))
               			{
                     		 print("\n\t~ Cancelled transaction ~\n");
               			} else
               			{
               				//User has decided to withdrawal the amount of money
               				//that is avaliable in the account
               				withdrawalAmount = stock.getBalance();
               				sufficentToProceed = true;
               			}
               			
               		}
               			
               		else if (withdrawalAmount <= 0) //No money to withdrawal in the account
               			print("\n! Invalid amount to withdrawl !\n");
               		
               		else //This is just a blanket statement if everything goes right
               			sufficentToProceed = true;
               		
          		 }	while(sufficentToProceed == false);
          		 
          		 stock.withdraw(withdrawalAmount);
          		 bank.deposit(withdrawalAmount);
          		 
          		 print("You have successfully deposited " + withdrawalAmount + " from your Stocks Account");
          		 bankMenu(bank, stock);
          		 break;
          	 }
        	 
         case 3: //Withdrawls
        	 if (bank.getBalance() <= 0.0)
        	 {
        		 print("\n!! Error !!\nBalance is too low to be withdrawn from!");
        		 bankMenu(bank, stock);
        		 break;
        	 }
        	 
        	 print("\n\t!! CAUTION !!\n"
          	 		+ "All withdrawals come from your Stock Account Balance and moves to your Bank Accoune Balance.");
          	 print("\nPress X to cancel transaction. Press any other key to continue:");
          	 String transactionChoice1 = scn.next();
          	 
          	 if (transactionChoice1.equals("X") || transactionChoice1.equals("x"))
          	 {
          		 print("\n\t~ Cancelled transaction ~\n");
          		 bankMenu(bank, stock);
          		 break;
          	 } else
          	 {
          		 double withdrawalAmount;
          		 boolean sufficentToProceed = false;
		 
          		 do
          		 {
          			 print("\n== Enter how much you would like to withdraw ==\n");
               		withdrawalAmount = scn.nextDouble();
               		
               		if (withdrawalAmount > bank.getBalance())
               		{
               			print("\n! Amount is too high to withdrawal !\nPress any key to withdrawal max amount. "
               					+ "Press X to cancel: ");
               			String s = scn.next();
               			
               			if (s.equals("X") || s.equals("x"))
               			{
                     		 print("\n\t~ Cancelled transaction ~\n");
               			} else
               			{
               				//User has decided to withdrawal the amount of money
               				//that is avaliable in the account
               				withdrawalAmount = bank.getBalance();
               				sufficentToProceed = true;
               			}
               		}
               		
               		else if (withdrawalAmount <= 0) //No money to withdrawal in the account
               			print("\n! Invalid amount to withdrawl !\n");
               		else //This is just a blanket statement if everything goes right
               			sufficentToProceed = true;
               		
          		 } while(sufficentToProceed == false);
          		 
          		 bank.withdraw(withdrawalAmount);
          		 stock.deposit(withdrawalAmount);
          		 
          		 print("You have successfully withdrawn " + withdrawalAmount + " from your Bank Account");
          		 bankMenu(bank, stock);
          		 break;
          	 }
        	 
         case 4:
        	 print("\n\t~ Current History ~\n");
        	 bank.printHistory();
        	 bankMenu(bank, stock);
        	 break;
         }
         
         if (selectedOption >= 5)
         {
        	 mainMenu(bank, stock);
         }
    	 
         scn.close();
    }
    
    //print method to make life not suck!
    public static void print(Object x)
    {
    	System.out.println(x);
    }
 
}

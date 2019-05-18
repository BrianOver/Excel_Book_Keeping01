import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Data 
{
	private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	private String whatForCat = "";
	
	ArrayList<ArrayList<String>> Getdata()
	{
		return data;
	}
	
	void Setdata(ArrayList<ArrayList<String>> d)
	{
		data = d;
	}
	
	void Addtodata(ArrayList<String> s)
	{
		data.add(s);
	}
	
	void SetwhatForCat(String s)
	{
		whatForCat = s;
	}
	
	String GetwhatForCat()
	{
		return whatForCat;
	}
	
	void Gen_Data() throws IOException //Start of data generation
	{
		int rand = (int) (Math.random()*15); //Picking a random amount of transactions to make
		if(rand < 3)
		{
			rand += 3;
		}
		
		for(int i = 0; i < rand; i++)
		{
			ArrayList<String> line = new ArrayList<String>(); //ArrayList that will contain each line of data
			int charge_type = (int) (Math.random()*9)+1; //Deciding the which type of charge it will be
			
			//int charge_type = (int) (Math.random()*8)+1;
			//int charge_type = 7;
			
			/*
			 * 4/10 chance for Out
			 * 3/10 chance for C
			 * 2/10 chance for T
			 * 1/10 chance for In		
			*/
			
			if(charge_type == 1 || charge_type == 2 || charge_type == 3 || charge_type == 9) //Out
			{
				line = SetDescripOut(line);
				line = SetDate(line);
				line.add("Out");
				line = SetTypeO(line);
				line = SetAmountO(line);
				line = SetReason(line);
				line = SetAccountOut(line);
				line.add(" ");
			}
			
			else if(charge_type == 4 || charge_type == 5 || charge_type == 6) //C
			{
				line = SetDescripOut(line);
				line = SetDate(line);
				line.add("C");
				line.add("Credit");
				line = SetAmountO(line);
				line = SetReason(line);
				line.add(" ");
				line.add(" ");
			}
		
			else if(charge_type == 7) //T
			{
				line = SetDescripT(line);
				line = SetDate(line);
				line.add("T");
				line.add("Transfer");
				line = SetAmountO(line);
				line = SetReason(line);
				if(line.get(0).equals("Transfer To Checking"))
				{
					line.add("Savings");
					line.add("Checking");
				}
				else if(line.get(0).equals("Transfer To Savings"))
				{
					line.add("Checking");
					line.add("Savings");
				}
				else
				{
					line.add(" ");
				}
			}
		
			else if(charge_type == 8) //In
			{
				line = SetDescripIn(line);
				line = SetDate(line);
				line.add("In");
				line = SetTypeIn(line);
				line = SetAmountO(line);
				line = SetReasonIn(line);
				line.add(" ");
				line = SetAccountIn(line);
			}
			
			//Exception for Credit Card - Payment
			if(line.contains("Credit Card - Payment"))
			{
				line.set(4, "Out");
				line.set(5, "Transfer");
				if(line.get(7).equals(" "))
				{
					line.set(7, "Savings");
				}	
				line.set(8, " ");
			}
			
			Addtodata(line);
		}
	}
	
	private ArrayList<String> SetDescripOut(ArrayList<String> line) throws IOException //Random description for ones that are Out/C
	{
		String description = "";
		
		BufferedReader reader = new BufferedReader(new FileReader("What_For.txt")); //Get num of lines in What_For.txt
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		
		int random = (int) (Math.random()*lines); //Random chance for What for
		FileReader fileReader = new FileReader("What_For.txt"); //Read from file
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		for(int i = 0; i < random; i++) //Read up to the random num
		{
			bufferedReader.readLine(); //Cycle through file
		}
		String whatFor = bufferedReader.readLine(); //Add the random selected name to description
		//String whatFor = "Gas";
		SetwhatForCat(whatFor);
		bufferedReader.close();	
	
		fileReader = new FileReader("Place_Names.txt"); //Read from file Place_Names.txt
		bufferedReader = new BufferedReader(fileReader);
		int whatLines = 0;		
		while(true) //Getting size of what_for category
		{
			String temp = bufferedReader.readLine();
			//System.out.println(whatFor);
			//System.out.println(temp);
			if(temp.equals(whatFor))
			{
				String tempRead = "";
				while(!tempRead.equals(";"))
				{
					tempRead = bufferedReader.readLine();
					whatLines++;
				}
				break;
			}
		}
		bufferedReader.close();
		
		fileReader = new FileReader("Place_Names.txt"); //Read from file Place_Names.txt
		bufferedReader = new BufferedReader(fileReader);
		random = (int) (Math.random()*whatLines); //Random amount in size of the what_for category
		while(true) //Moves the file reader up to the category
		{
			String tempRead = bufferedReader.readLine();
			if(tempRead.equals(whatFor))
			{
				break;
			}
		}
		for(int i = 0; i < random-1; i++) //Go to random
		{
			bufferedReader.readLine(); //Pull random var
		}
		String placeName = bufferedReader.readLine();
		bufferedReader.close();
		description = placeName + " - " + whatFor; //Set description
		
		line.add(description); //Add to line
		
		return line;
	}
	
	private ArrayList<String> SetDate(ArrayList<String> line) //Set date to local
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //Grabbing local date
		LocalDateTime now = LocalDateTime.now();
		String fulldate = dtf.format(now); //String with full date
		
		String day = fulldate.substring(8,10); //Adding day to line
		line.add(day);
		
		String month = fulldate.substring(5,7); //Adding month to line
		line.add(month);
		
		String year = fulldate.substring(0,4); //Adding year to line
		line.add(year);
		
		return line;
	}
	
	private ArrayList<String> SetTypeO(ArrayList<String> line) //Set type Out
	{
		//String[] array = {"Transfer","Debit","Cash","Check"};
		if(line.contains("Credit Card - Payment"))
		{
			line.add("Transfer");
			return line;
		}
		int rand = (int) (Math.random()*11);
		switch(rand)
		{
			case 0 : line.add("Debit");
				break;
			case 1 : line.add("Debit");
				break;
			case 2 : line.add("Debit");
				break;
			case 3 : line.add("Debit");
				break;
			case 4 : line.add("Debit");
				break;
			case 5 : line.add("Transfer");
				break;
			case 6 : line.add("Transfer");
				break;
			case 7 : line.add("Check");
				break;
			case 8 : line.add("Cash");
				break;
			case 9 : line.add("Cash");
				break;
			case 10 : line.add("Debit");
				break;
		}
		return line;
	}
	
	private ArrayList<String> SetAmountO(ArrayList<String> line) //Set amount
	{
		if(line.get(0).equals("Transfer To Checking") || line.get(0).equals("Transfer To Savings")) //Transfer amounts
		{
			int rand = (int) (Math.random()*300)+85;
			String rands = Integer.toString(rand);
			line.add(rands);
			return line;
		}
		
		if(line.get(4).equals("In")) //In amounts
		{
			if(line.contains("Work Pay"))
			{
				int rand = (int) (Math.random()*10000)+3000;
				String rands = Integer.toString(rand);
				line.add(rands);
				return line;
			}
			else
			{
				int rand = (int) (Math.random()*500)+50;
				String rands = Integer.toString(rand);
				line.add(rands);
				return line;
			}
		}
		
		if(line.get(0).equals("WOW - Video Game"))
		{
			line.add("16");
			return line;
		}
		
		//Out/C amounts
		int randTop = 0; //Random number to decide amount segment
		switch(GetwhatForCat()) //Correcting the amounts to be logical related to charge
		{
			case "Gas" : randTop = 1; //Low amount
				break;
			case "Groceries" : randTop = 2; //Medium amount
				break;
			case "Video Game" : randTop = (int) (Math.random()*3); //1/3 for low, 2/3 for medium
				break;
			case "Bill" : randTop = 2; //Medium amount
				break;
			case "Girl" : randTop = (int) (Math.random()*2); //1/2 for low, 1/2 for medium
				break;
			case "Food" : randTop = 1; //Low amount
				break;
			case "Car" : randTop = (int) (Math.random()*6)+4; //2/3 for low, 1/3 for high
				break;	
			case "Payment" : randTop = (int) (Math.random()*400)+100; String rands = Integer.toString(randTop); line.add(rands);
				break;
		}
		//Exceptions to the base random amount rules
		if(line.contains("Netflex - Bill")) 
		{
			randTop = 0;
			line.add("10");
			return (line);
		}
		else if(line.contains("HBO - Bill"))
		{
			randTop = 0;
			line.add("10");
			return (line);
		}
		else if(line.contains("Apartment - Bill"))
		{
			randTop = 4;
		}
		else if(line.contains("Steam - Video Game"))
		{
			int rand = (int) (Math.random()*70)+20;
			String rands = Integer.toString(rand);
			line.add(rands);
			return (line);
		}
		else if(line.contains("Car Wash - Car"))
		{
			int rand = (int) (Math.random()*15);
			String rands = Integer.toString(rand);
			line.add(rands);
			return (line);
		}
			
		if(randTop == 1 || randTop == 5 || randTop == 6) //low amount
		{
			int rand = (int) (Math.random()*25);
			if(rand == 0)
			{
				rand = (int) (Math.random()*25);
			}
			String randS = Integer.toString(rand);
			line.add(randS);
		}
			
		else if(randTop == 2 || randTop == 3) //Medium amount
		{
			int rand = (int) (Math.random() * 150) + 25;
			String randS = Integer.toString(rand);
			line.add(randS);
		}
			
		else if(randTop == 4) //High amount
		{
			int rand = (int) (Math.random()*2000) + 150;
			String randS = Integer.toString(rand);
			line.add(randS);
		}
		
		return line;
	}

	private ArrayList<String> SetAccountOut(ArrayList<String> line) //Set account Out
	{
		//String[] array = {"Checking","Savings"};
		
		if(line.get(5).equals("Debit")) //Logic checker
		{
			line.add("Checking");
			return line;
		}
		
		int rand = (int) (Math.random()*6);
		switch(rand)
		{
			case 0 : line.add("Checking");
				break;
			case 1 : line.add("Checking");
				break;
			case 2 : line.add("Checking");
				break;
			case 3 : line.add("Checking");
				break;
			case 4 : line.add("Checking");
				break;
			case 5 : line.add("Checking");
				break;
			case 6 : line.add("Savings");
				break;
			case 7 : line.add("Savings");
				break;
		}
		return line;
	}
	
	private ArrayList<String> SetReason(ArrayList<String> line) //Set reason
	{
		String fun[] = {"Video Game"}; //Creating reason categories
		String bills[] = {"Bill","Car"};
		String food[] = {"Food"};
		String groceries[] = {"Groceries"};
		String gas[] = {"Gas"};
		
		//Exceptions
		if(line.get(0).equals("Credit Card - Payment"))
		{
			line.add("Credit Card Payment");
			return line;
		}
		
		if(line.get(0).equals("Transfer To Checking"))
		{
			String reason[] = {"Spending Money"};
			int rand = (int) (Math.random()*reason.length);
			line.add(reason[rand]);
		    return line;
		}
		else if (line.get(0).equals("Transfer To Savings"))
		{
			String reason[] = {"Savings Money"};
			int rand = (int) (Math.random()*reason.length);
			line.add(reason[rand]);
		    return line;
		}
			
		if(Arrays.asList(fun).contains(GetwhatForCat()))
		{
			line.add("Fun");
		}
		else if(Arrays.asList(bills).contains(GetwhatForCat()))
		{
			line.add("Bills");
		}
		else if(Arrays.asList(food).contains(GetwhatForCat()))
		{
			line.add("Food");
		}
		else if(Arrays.asList(groceries).contains(GetwhatForCat()))
		{
			line.add("Groceries");
		}
		else if(GetwhatForCat() == "Girl")
		{
			line.add("Girl");
		}
		else if(Arrays.asList(gas).contains(GetwhatForCat()))
		{
			line.add("Gas");
		}
		
		return line;
	}
	
	private ArrayList<String> SetDescripT(ArrayList<String> line) throws IOException //Random description for ones that are T
	{
		//String array[] = {"Checking, "Savings"};
		int rand = (int) (Math.random()*4)+1;
		switch(rand)
		{
			case 1 : line.add("Transfer To Checking");
			break;
			case 2 : line.add("Transfer To Checking");
			break;
			case 3 : line.add("Transfer To Checking");
			break;
			case 4 : line.add("Transfer To Savings");
			break;
		}
		return line;
	}
	
	private ArrayList<String> SetDescripIn(ArrayList<String> line) //Set description In
	{
		String array[] = {"Work Pay","Money from Family"};
		int rand = (int) (Math.random()*array.length);
		line.add(array[rand]);
		SetwhatForCat(array[rand]);
		return line;
	}
	
	private ArrayList<String> SetTypeIn(ArrayList<String> line) //Set type In
	{
		String array[] = {"Transfer","Deposit","Cash","Check"};
		int rand = (int) (Math.random()*array.length);
		if(line.contains("Work Pay"))
		{
			line.add("Deposit");
		}
		else
		{
			line.add(array[rand]);
		}
		return line;
	}
	
	private ArrayList<String> SetReasonIn(ArrayList<String> line) //Set reason In
	{
		if(line.contains("Work Pay"))
		{
			line.add("Work Pay");
		}
		else
		{
			line.add("Gift");
		}
		return line;
	}
	
	private ArrayList<String> SetAccountIn(ArrayList<String> line) //Set account In
	{
		int rand = (int) (Math.random()*4)+1;
		switch(rand)
		{
			case 1 : line.add("Savings");
				break;
			case 2 : line.add("Savings");
				break;
			case 3 : line.add("Savings");
				break;
			case 4 : line.add("Checking");
				break;
		}
		return line;
	}
	
 	void Printdata()
	{
		ArrayList<ArrayList<String>> temp = Getdata();
		for (ArrayList<String> s : temp)
		{
			for (String q : s)
			{
				System.out.print(q + " | ");
			}
			System.out.println(" ");
		}
	}
	
	void PrintdataToFile() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("Day_Out.txt")); //Write to File for single day
		ArrayList<ArrayList<String>> temp = Getdata();
		for (ArrayList<String> s : temp)
		{
			for (String q : s)
			{
				writer.write(q + ";");
			}
			writer.newLine();
		}
		writer.close();
	
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //Grabbing local date
		LocalDateTime now = LocalDateTime.now();
		String fulldate = dtf.format(now); //String with full date
		writer = new BufferedWriter(new FileWriter("Total_Out.txt",true)); //Write to File for total data
		//writer.newLine();
		temp = Getdata();
		
		writer.newLine();
		writer.append(fulldate);
		writer.newLine();
		
		for (ArrayList<String> s : temp)
		{
			for (String q : s)
			{
				writer.append(q + " | ");
			}
			writer.newLine();
		}
		writer.close();
	}
}

import java.io.IOException;

public class Runner {

	public static void main(String[] args) throws IOException 
	{
		
		Data data = new Data();
		data.Gen_Data();
		data.Printdata();
		data.PrintdataToFile();
	}

}

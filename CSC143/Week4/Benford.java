import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Benford {
	private ArrayList<Integer> benfordList;
	
	public Benford(){
		benfordList = new ArrayList<Integer>();
	}
	
	public Benford(File fileName) throws FileNotFoundException, IOException{
		readCounts(fileName);
	}
	
	public void readCounts(File fileName)throws FileNotFoundException, IOException{
		Scanner fileReader = new Scanner(fileName);
		benfordList = new ArrayList<Integer>();
		
		while(fileReader.hasNextLine()){
			benfordList.add(getFirstNumber(fileReader.nextLine()));
		}
		fileReader.close();
	}
	
	public double[] benfordPercents(){
		double[] percentages = {0,0,0,0,0,0,0,0,0};
		for(int number : benfordList){
			percentages[number - 1]++;
		}
		
		for(int i = 0; i < percentages.length; i++){
			percentages[i] = percentages[i] / benfordList.size();
		}
		
		return percentages;
		
	}
	
	private int getFirstNumber(String textString){
		int firstNumber = 0;
		for(int i = 0; i < textString.length(); i++){
			if((int)textString.charAt(i) >= 49 && (int)textString.charAt(i) <= 57){
				firstNumber = (int)textString.charAt(i) - 48;
				break;
			}
		}
		
		return firstNumber;
	}
}

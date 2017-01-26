import java.util.ArrayList;


public class WeeklyProblemSeven {

	public static void main(String[] args) {
		ArrayList<Integer> badList = new ArrayList<Integer>();

	}
	
	public static ArrayList<Integer> cleanCorruptData(ArrayList<Integer> corruptList){
		
		for(int i = corruptList.size() - 1; i >= 0; i--){
			if(corruptList.size() % 2 != 0){
				corruptList.remove(corruptList.size() - 1);
			}
			
			if(i - 1 >= 0){
				if(corruptList.get(i - 1) % 2 != 0 || corruptList.get(i) % 2 > 0){
					corruptList.remove(i);
					corruptList.remove(i - 1);
				}
				else if(corruptList.get(i - 1) < corruptList.get(i)){
					corruptList.remove(i);
					corruptList.remove(i - 1);
				}
			}
		}
		return corruptList;
	}

}

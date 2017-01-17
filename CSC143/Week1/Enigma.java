/*
 * @author Walter Holley III CSC 143
 * Assignment #1 - Encryption
 * @version 1.0
 */
public class Enigma {
	//required instance members go here 
	public static final String outerRotor = "#BDFHJLNPRTVXZACEGIKMOQSUWY"; 
	public static String innerRotor = "#GNUAHOVBIPWCJQXDKRYELSZFMT";
	public static String middleRotor = "#EJOTYCHMRWAFKPUZDINSXBGLQV";
	private int _innerRotorOffset = 0;
	private int _middleRotorOffset = 0;
	
	
	public Enigma(){ 
		//default constructor - constructs enigma machine as shown in spec 
	} 
	
	//non-default constructor - constructs machine with user specified inner and middle rotors
	public Enigma(String s1, String s2){ 
		
		if(isRotorValid(s1) && isRotorValid(s2)){
			this.innerRotor = s1;
			this.middleRotor = s2;
		}
		
	}
	
	/*
	 *verify that rotStr is exactly 27 chars long 
	 *verify that all chars from english alphabet occur only once 
	 *verify that rotor starts with a # char
	 * @param rotStr rotor character string
	 */
	private boolean isRotorValid (String rotStr){
		boolean isValid = false;
		//TODO Revise validation with checksum algorithm
		int  rotorCheckSum = 2050;
		if(rotStr.length() == 27){
			
			if(rotStr.toCharArray()[0] == '#'){
				int checkSum = 0;
			
				for(int i = 0; i < rotStr.length(); i++){
					checkSum += (int)rotStr.toCharArray()[i];
				}
				
				if(rotorCheckSum == checkSum){
					isValid = true;
				}
			}
		}
		return isValid;
	} 
	
	public String encrypt (String message){
		
		String encryptedString = "";
		for(int i = 0; i < message.length(); i++){
			
			//call to encodeChar
			encryptedString += encodeChar(message.charAt(i));
			
			//call to rotateClockwise
			rotateClockwise();
		}
		
		return encryptedString;
	}
	
	public String decrypt (String message){ 
		String decryptedString = "";
		for(int i = 0; i < message.length(); i++){
			
			//call to rotateAntiClockwise
			decryptedString += decodeChar(message.charAt(i));
			
			//call to decodeChar
			rotateAntiClockwise();
		}
	
		return decryptedString;
	} 
	
	private char encodeChar(char c){
		//account for spaces
		if(c == ' '){
			c = '#';
		}
		
		int innerPosition = getRotorPosition(innerRotor.indexOf(c), this._innerRotorOffset);
		char outerChar = Enigma.outerRotor.toCharArray()[innerPosition];
		int middlePosition = getRotorPosition(this.middleRotor.indexOf(outerChar), this._middleRotorOffset);
		outerChar = Enigma.outerRotor.toCharArray()[middlePosition];
		
		if(outerChar == '#'){
			outerChar = ' ';
		}
		return outerChar;
	}

	private char decodeChar(char c){
		//account for spaces
		if(c == ' '){
			c = '#';
		}
			
		int outerPosition = Enigma.outerRotor.indexOf(c);
		char middleChar = this.middleRotor.toCharArray()[getRotorPosition(outerPosition, this._middleRotorOffset)];
		outerPosition = Enigma.outerRotor.indexOf(middleChar);
		char innerChar = this.innerRotor.toCharArray()[getRotorPosition(outerPosition, this._innerRotorOffset)];
		
		if(innerChar == '#'){
			innerChar = ' ';
		}
		
		return innerChar;
	}
	
	private int getRotorPosition(int base, int offSet){
		int position = base + offSet;
		
		if(position >= 0){
			if(position > 26){
				position = position % 27;
			}
		}
		else{
			position = 27 + position;
		}
		
		return position;
	
	}
	private void rotateClockwise(){ 
		if(this._innerRotorOffset == 26){
			this._innerRotorOffset = 0;
			if(this._middleRotorOffset == 26){
				this._middleRotorOffset = 0;
			}
			else{
				this._middleRotorOffset++;
			}
		}
		else{
			this._innerRotorOffset++;
		}
	} 
	
	private void rotateAntiClockwise(){ 
		if(this._innerRotorOffset == -26){
			this._innerRotorOffset = 0;
			if(this._middleRotorOffset == -26){
				this._middleRotorOffset = 0;
			}
			else{
				this._middleRotorOffset--;
			}
		}
		else{
			this._innerRotorOffset--;
		}
	}
	public void reset (){
		//resets to align all # chars on all rotors (returns rotors to initial configuration)
		this._innerRotorOffset = 0;
		this._middleRotorOffset = 0;
	}

}

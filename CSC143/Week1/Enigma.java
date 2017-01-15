
public class Enigma {
	public static final String outerRotor = "#BDFHJLNPRTVXZACEGIKMOQSUWY"; 
	//required instance members go here 
	private String _innerRotor = "#GNUAHOVBIPWCJQXDKRYELSZFMT";
	private String _middleRotor = "#EJOTYCHMRWAFKPUZDINSXBGLQV";
	private int _innerRotorOffset = 0;
	private int _middleRotorOffset = 0;
	
	
	public Enigma(){ 
		//default constructor - constructs enigma machine as shown in spec 
	} 
	
	//non-default constructor - constructs machine with user specified inner and middle rotors
	public Enigma(String innerRotor, String middleRotor){ 
		
		if(isRotorValid(innerRotor) && isRotorValid(middleRotor)){
			this._innerRotor = innerRotor;
			this._middleRotor = middleRotor;
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
		

		if(rotStr.length() == 27){
			
			if(rotStr.toCharArray()[0] == '#'){
				boolean matchFound = false;
				
				checkLoop:
				for(int i = 0; i < rotStr.length(); i++){
					for(int c = i + 1; c < rotStr.length(); c++){
						if(rotStr.toCharArray()[i] == rotStr.toCharArray()[c]){
							matchFound = true;
							break checkLoop;
						}
					}
				}
				
				if(!matchFound){
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
		
		reset();
		
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
		
		reset();
		
		return decryptedString;
	} 
	
	private char encodeChar(char c){
		//account for spaces
		if(c == ' '){
			c = '#';
		}
		
		int innerPosition = getRotorPosition(_innerRotor.indexOf(c), this._innerRotorOffset);
		char outerChar = Enigma.outerRotor.toCharArray()[innerPosition];
		int middlePosition = getRotorPosition(this._middleRotor.indexOf(outerChar), this._middleRotorOffset);
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
		char middleChar = this._middleRotor.toCharArray()[getRotorPosition(outerPosition, this._middleRotorOffset)];
		outerPosition = Enigma.outerRotor.indexOf(middleChar);
		char innerChar = this._innerRotor.toCharArray()[getRotorPosition(outerPosition, this._innerRotorOffset)];
		
		if(innerChar == '#'){
			innerChar = ' ';
		}
		
		return innerChar;
	}
	
	private int getRotorPosition(int base, int offSet){
		int sum = base + offSet;
		
		if(sum >= 0){
			if(sum > 26){
				sum = sum % 27;
			}
		}
		else{
			sum = 27 + sum;
		}
		
		return sum;
	
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

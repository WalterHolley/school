/**
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
		
	} 
	
	
	/**
	 * non-default constructor - constructs machine with user specified inner and middle rotors
	 * @param s1 the inner rotor
	 * @param s2 the middle rotor
	 */
	public Enigma(String s1, String s2){ 
		
		if(isRotorValid(s1) && isRotorValid(s2)){
			this.innerRotor = s1;
			this.middleRotor = s2;
		}
		
	}
	
	/**
	 *verify that rotStr is exactly 27 chars long, 
	 *that all chars from english alphabet occur only once, 
	 *and rotor starts with a '#' char
	 * @param rotStr rotor character string
	 * @return true if rotor is valid, otherwise false.
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
	
	/**
	 * Encrypts a string
	 * @param message the message to encrypt
	 * @return string representing the encrypted message
	 */
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
	
	/**
	 * Decrypts a string back to it's original state.
	 * You will have to ensure correct rotor settings for correct output
	 * @param message the message to decrypt
	 * @return decrypted result
	 */
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
	
	/**
	 * Encodes a character.  Masking it's original value
	 * @param c the character to encode
	 * @return the encoded representation of the character.
	 */
	private char encodeChar(char c){
		//account for spaces
		if(c == ' '){
			c = '#';
		}
		
		int innerPosition = getRotorPosition(innerRotor.indexOf(c), this._innerRotorOffset);
		char outerChar = this.outerRotor.toCharArray()[innerPosition];
		int middlePosition = getRotorPosition(this.middleRotor.indexOf(outerChar), this._middleRotorOffset);
		outerChar = this.outerRotor.toCharArray()[middlePosition];
		
		if(outerChar == '#'){
			outerChar = ' ';
		}
		return outerChar;
	}
	
	/**
	 * Decodes a character to it's original form.
	 * Correct rotor settings are required for correct output
	 * @param c the character to decode
	 * @return decoded character
	 */
	private char decodeChar(char c){
		//account for spaces
		if(c == ' '){
			c = '#';
		}
			
		int outerPosition = this.outerRotor.indexOf(c);
		char middleChar = this.middleRotor.toCharArray()[getRotorPosition(outerPosition, this._middleRotorOffset)];
		outerPosition = this.outerRotor.indexOf(middleChar);
		char innerChar = this.innerRotor.toCharArray()[getRotorPosition(outerPosition, this._innerRotorOffset)];
		
		if(innerChar == '#'){
			innerChar = ' ';
		}
		
		return innerChar;
	}
	
	/**
	 * determines the rotor's actual position
	 * @param base the base position of the unmoved rotor
	 * @param offSet the offset position of the base rotor
	 * @return number of the actual rotor position
	 */
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
	/**
	 * Iterates the rotor in a clockwise fashion.
	 */
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
	
	/**
	 * Iterates the rotor in an anti-clockwise fashion
	 */
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
	/**
	 * Returns the rotors to their original positions.
	 */
	public void reset (){
		//resets to align all # chars on all rotors (returns rotors to initial configuration)
		this._innerRotorOffset = 0;
		this._middleRotorOffset = 0;
	}

}

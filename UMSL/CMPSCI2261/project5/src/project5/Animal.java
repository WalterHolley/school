package project5;

public class Animal extends Creature implements Reproduction, Comparable<Animal>{
	private String _transportMethod;
	private String _skinType;
	
	public Animal(String genus, String species, String transportMethod, String skinType, int genome) {
		this._chromosomeCount = genome;
		this._genusName = genus;
		this._speciesName = species;
		this._transportMethod = transportMethod;
		this._skinType = skinType;
	}
	
	@Override
	public String eatFood() {
		return "ingestion";
	}

	@Override
	public String modeOfReproduction() {
		return "Sexual Reproduction";
	}
	
	/**
	 * 
	 * @return the movement method used by the animal
	 */
	public String getMethodOfTransportation() {
		return _transportMethod;
	}
	
	/**
	 * 
	 * @return The skin type of the animal
	 */
	public String getSkinType() {
		return _skinType;
	}
	
	/**
	 * sorts in ascending order of
	 * transportation method
	 */
	@Override
	public int compareTo(Animal o) {
		return this._transportMethod.toLowerCase().compareTo(o.getMethodOfTransportation().toLowerCase());
	}
	
	/**
	 * override of toString method
	 */
	@Override
	public String toString() {
		return String.format("Animal Genus: %s \t Species: %s \t Transport Method: %s", this._genusName, this._speciesName, this._transportMethod);
	}
}

package project5;

public class Fungi  extends Creature implements Reproduction, Comparable<Fungi>{
	private double _capSize;
	private double _stemHeight;
	
	public Fungi(String genus, String species, double stemHeight, double capSize, int genome) {
		this._capSize = capSize;
		this._chromosomeCount = genome;
		this._genusName = genus;
		this._speciesName = species;
		this._stemHeight = stemHeight;
	}
	
	@Override
	public String eatFood() {
		return "external digestion with hyphae";
	}
	

	@Override
	public String modeOfReproduction() {
		return "Spores";
	}
	
	/**
	 * 
	 * @return the size of the fungus cap
	 */
	public double getCapSize() {
		return _capSize;
	}
	
	/**
	 * 
	 * @return the height of the stem
	 */
	public double getStemHeight() {
		return _stemHeight;
	}
	
	/**
	 * Overrides default comparison.
	 * compares based on stem height
	 */
	@Override
	public int compareTo(Fungi o) {
		int result = 0;
		
		if(o.getStemHeight() > this._stemHeight)
			result = -1;
		else if(o.getStemHeight() < this._stemHeight)
			result = 1;
		return result;
	}
	
	/**
	 * override of toString method
	 */
	@Override
	public String toString() {
		return String.format("Fungi Genus: %s \t Species: %s \t Stem Height: %.2f", this._genusName, this._speciesName, 
				this._stemHeight);
	}
}

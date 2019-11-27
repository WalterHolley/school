package project5;

public abstract class Creature {
	protected String _genusName;
	protected String _speciesName;
	protected int _chromosomeCount;
	
	/**
	 * 
	 * @return the creatures primary source of food
	 */
	public String eatFood() {
		return "I eat all the food!";
	}
	
	/**
	 * 
	 * @return The genus of the creature
	 */
	public String getGenus() {
		return _genusName;
	}
	
	/**
	 * 
	 * @return the name of the species
	 */
	public String getSpeciesName() {
		return _speciesName;
	}
	
	/**
	 * 
	 * @return number of g
	 */
	public int getChromosomeCount() {
		return _chromosomeCount;
	}
}

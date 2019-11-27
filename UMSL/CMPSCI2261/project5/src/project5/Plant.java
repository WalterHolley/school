package project5;

public class Plant extends Creature implements Reproduction, Comparable<Plant>{
	private int _leafCount;
	private String _color;
	
	public Plant(String genus, String species, String color, int genome, int leafCount) {
		this._genusName = "Plant";
		this._speciesName = species;
		this._chromosomeCount = genome;
		this._leafCount = leafCount;
		this._color = color;
	}
	@Override
	public String eatFood() {
		return "sunlight (aka photosynthesis)";
	}

	/**
	 * Returns the plant's method of reproduction
	 */
	@Override
	public String modeOfReproduction() {
		return "Seeds";
	}
	
	/**
	 * 
	 * @return number of leaves on the plant
	 */
	public int getLeafCount() {
		return _leafCount;
	}
	
	/**
	 * 
	 * @return color of the plant
	 */
	public String getColor() {
		return _color;
	}
	
	/**
	 * Sorts based on the color of the plant in ascending order
	 */
	@Override
	public int compareTo(Plant o) {
		return this._color.compareTo(o.getColor());
	}
	
	/**
	 * override of toString method
	 */
	@Override
	public String toString() {
		return String.format("Plant Genus: %s \t Species: %s \t Color: %s", this._genusName, this._speciesName, 
				this._color);
	}
}

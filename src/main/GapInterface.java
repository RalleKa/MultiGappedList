package main;

/**
 * 
 * @author RalfK
 *
 */
interface GapInterface {
	/**
	 * moves the cursor to the position of the gap.
	 * If the gap does not exists, moves the cursor 
	 * to the position where it would be inserted to (next).
	 * Example for the existing gaps 1, 3, 5:
	 * request -> index
	 * 0 -> 0
	 * 1 -> 0
	 * 2 -> 0
	 * 3 -> 1
	 * 4 -> 1
	 * 5 -> 2
	 * 6 -> 2
	 * 7 -> 2
	 * @param gap
	 */
	public void toGapPosition(int gap);

	/**
	 * 
	 * @return the current value (element)
	 */
	public int getElement();

	/**
	 * adds a gap to the correct position, therefore moves the cursor
	 * @param gap the gap to be added
	 */
	public void add(int gap);

	/**
	 * removes a gap, if it exists
	 * @param gap the gap to be removed
	 */
	public void remove(int gap);

	/**
	 * removes the current element
	 * (if it is not the root element)
	 */
	public void remove();

	/**
	 * 
	 * @return the current Index of the cursor
	 */
	public int getIndex();
	
	/**
	 * 
	 * @return does the iterator contains a further gap
	 */
	public boolean hasNext();
	
	/**
	 * moves the cursor backwards
	 * @return the previous gap
	 */
	public int next();
	
	/**
	 * 
	 * @return does the iterator contains a previous gap
	 */
	public boolean hasPrevious();
	
	/**
	 * moves the cursor forward
	 * @return the next gap
	 */
	public int previous();
	
	/**
	 * moves the cursor back to the root
	 */
	public void toRoot();
	
	/**
	 * 
	 * @return the total amount of gaps
	 */
	public int size();
	
	/**
	 * removes all the elements and sets the cursor position back to root
	 */
	public void clear();
	
	/**
	 * tests if a gap exists
	 * @param gap the gap to be tested
	 * @return if the gap is contained
	 */
	public boolean contains(int gap);
	
	/**
	 * previews the next gap without moving the cursor
	 * @return the next gap
	 */
	public int previewNext();
	
	/**
	 * previews the previous gap without moving the cursor
	 * @return the previous gap
	 */
	public int previewPrevious();
}

package main;

/**
 * This array should store the elements of a list with gaps
 * @author RalfK
 *
 */
interface ArrayInterface<E> extends Resortable{
	
	/**
	 * gets an Element for the given Position
	 * @param listIndex The index in the List (not the array)
	 * @param gaps The amount of gaps before the element
	 * @return the Element
	 */
	E getElement(int listIndex, int gaps);
	
	/**
	 * gets an Element for the given Position
	 * @param arrayIndex The index in this Array (not the List)
	 * @return the Element
	 */
	E getElement(int arrayIndex);
	
	/**
	 * adds an Element to the given Position
	 * @param listIndex The index in the List (not the array)
	 * @param gaps The amount of gaps before the element
	 * @param element the Element
	 */
	void addElement(int listIndex, int gaps, E element);
	
	/**
	 * sets an Element to the given position
	 * @param arrayIndex The index in this Array (not the List)
	 * @param element the Element
	 */
	E setElement(int arrayIndex, E element);
	
	/**
	 * sets an Element to the given position
	 * @param listIndex The index in the List (not the array)
	 * @param gaps The amount of gaps before the element
	 * @param element the Element
	 */
	E setElement(int listIndex, int gaps, E element);
	
	/**
	 * removes an element from the given Position
	 * @param arrayIndex The index in this Array (not the List)
	 */
	E removeElement(int arrayIndex);
	
	/**
	 * removes an element from the given Position
	 * @param listIndex The index in the List (not the array)
	 * @param gaps The amount of gaps before the element
	 */
	E removeElement(int listIndex, int gaps);
	
	/**
	 * Removes an Element at its first appearance
	 * @param element the Element
	 */
	E removeElement(E element);
	
	/**
	 * Removes an Element at its last appearance
	 * @param element the Element
	 */
	void removeLastElement(E element);
	
	/**
	 * Removes an Element at all of its appearances
	 * @param element the Element
	 */
	void removeAllElement(E element);
	
	/**
	 * removes all Elements in the given Interval
	 * @param startArrayIndex The start of the interval (will be removed)
	 * @param endArrayIndex The end of the interval (will not be removed)
	 */
	void removeElements(int startArrayIndex, int endArrayIndex);
	
	/**
	 * removes all Elements in the given Interval
	 * @param startListIndex The start of the interval (will be removed)
	 * @param gapsBeforeStart The gaps in the array before the start
	 * @param endListIndex The end of the interval (will not be removed)
	 * @param gapsInIntervall The gaps in the array in the interval
	 */
	void removeElements(int startListIndex, int gapsBeforeStart, int endListIndex, int gapsInIntervall);
	
	/**
	 * Trims the array to its actual size to save storage
	 */
	void trim();
	
	/**
	 * when the array gets extended, what is the multiplier?
	 * oldSize * multiplier = new size
	 * 
	 * The default value for an arrayList is 2. It is not 
	 * recommended to make the multiplier much higher, as it
	 * will consume more memory. However, for a fast growing 
	 * list increasing the multiplier may save some time.
	 * @param m the multiplier
	 */
	void setMultiplier(byte m);
	
	/**
	 * returns the last Element in the Array +1 (NOT THE LENGTH OF THE ARRAY)
	 * @return the lastElementIndex
	 */
	int length();
	
	/**
	 * clears the used space of the Array
	 */
	void clear();
	
	/**
	 * must copy the array. If the array still contains gaps, skip them
	 */
	void copy(Object[] a);
}

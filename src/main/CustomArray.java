package main;

final class CustomArray<E> implements ArrayInterface<E>, Resortable{
	public static final byte DEFAULT_MULTIPLIER = 2;
	
	private GapInterface gaps;
	private Object[] array;
	private int multiplier;
	
	/**
	 * The arrayIndex, where the last Element is stored +1
	 */
	private int lastElementsIndex;
	
	public CustomArray(GapInterface gaps, int size) {
		this(gaps, size, DEFAULT_MULTIPLIER);
	}
	
	public CustomArray(GapInterface gaps, int size, byte multiplier) {
		this.gaps = gaps;
		this.array = new Object[size];
		this.lastElementsIndex = 0;
		setMultiplier(multiplier);
	}

	@Override
	public E getElement(int listIndex, int gaps) {
		return getElement(listIndex + gaps);
	}

	// must not be checked, as only E will be stored
	@SuppressWarnings("unchecked")
	@Override
	public E getElement(int arrayIndex) {
		return (E) array[arrayIndex];
	}
	
	private boolean increaseLengthAndExpandIfRequired(int arrayIndex) {
		if (arrayIndex >= array.length) {
			extend();
			lastElementsIndex++;
			
			return true;
		}
		
		lastElementsIndex++;
		return false;
	}
	
	/**
	 * 
	 * @param arrayIndex 
	 * @return if gaps where resolved
	 */
	private int moveGapTo(int listIndex, int gaps) {
		int arrayIndex = listIndex + gaps;
		
		// if the arrayIndex is a gap, we do not need to move elements
		// as the arrayIndex is the listIndex with all its gaps (including the gap where we would insert the element)
		// we use arrayIndex - 1 instead. This, however, only works with arrayIndex != 0, as 0 - 1 = -1, which is the same
		// as the root element
		if (arrayIndex != 0 & this.gaps.contains(arrayIndex - 1)) {
			this.gaps.remove();
			return arrayIndex - 1;
		}
		
		// if the insertion is to the last position of the Array, we dont need to move anything
		if (arrayIndex >= lastElementsIndex) {
			return increaseLengthAndExpandIfRequired(lastElementsIndex) ? listIndex : arrayIndex;
		}
		
		// if there are no gaps, we need to move to the last Position in the array (and eventually expand)
		if (this.gaps.size() == 0) {
			boolean increased = increaseLengthAndExpandIfRequired(lastElementsIndex + 1);
			
			arrayIndex = increased ? listIndex : arrayIndex;
			
			System.arraycopy(array, arrayIndex, array, arrayIndex + 1, lastElementsIndex - arrayIndex);
			
			return arrayIndex;
		}
		
		if (this.gaps.getElement() == -1 || (this.gaps.hasNext() && this.gaps.previewNext() - arrayIndex <= arrayIndex - this.gaps.getElement())) {
			// if there are gaps, but there is no previous gap, we move the elements to the next gap
			// if the next gap is closer than the previous, we move the items forward
			
			System.arraycopy(array, arrayIndex, array, arrayIndex + 1, this.gaps.previewNext() - arrayIndex);
			this.gaps.next();
		}else {
			// if there is a previous gap and it is closer than the next gap, we move the items backwards
			// as we resolved a gap in front of the arrayIndex, it decreases
			arrayIndex--;
			System.arraycopy(array, this.gaps.getElement() + 1, array, this.gaps.getElement(), arrayIndex - this.gaps.getElement());
		}
		
		this.gaps.remove();
		
		return arrayIndex;
	}

	@Override
	public void addElement(int listIndex, int gaps, E element) {
		// moves a gap to the given position
		// when we extend the array, we remove all gaps. 
		// Therefore the arrayIndex will be same as the listIndex
		int arrayIndex = moveGapTo(listIndex, gaps);
		
		// add the element
		array[arrayIndex] = element;
	}

	@Override
	public E setElement(int arrayIndex, E element) {
		// if the arrayIndex of the new element is beyond the lastElementsIndex, 
		// it is not supported to add an element to this position
		if (arrayIndex > lastElementsIndex) {
			throw new IndexOutOfBoundsException();
		}
		
		// tests, if the arrayLength must be extended
		if (isOutside(arrayIndex)) {
			extend();
		}
		
		// actually setting the element
		@SuppressWarnings("unchecked")
		E old = (E) array[arrayIndex];
		array[arrayIndex] = element;
		
		// if the Element extends the array by 1, increase the lastElementIndex
		lastElementsIndex = lastElementsIndex == arrayIndex ? lastElementsIndex + 1 : lastElementsIndex;
		
		return old;
	}

	@Override
	public E setElement(int listIndex, int gaps, E element) {
		return setElement(listIndex + gaps, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E removeElement(int arrayIndex) {
		gaps.add(arrayIndex);
		
		// if this gap was the last element, we can make the list smaller
		while (lastElementsIndex > 0 && gaps.contains(lastElementsIndex - 1)) {
			gaps.remove();
			lastElementsIndex--;
		}
		
		return (E) array[arrayIndex];
	}

	@Override
	public E removeElement(int listIndex, int gaps) {
		return removeElement(listIndex + gaps);
	}

	@Override
	public E removeElement(E element) {
		for (int i = 0; i < lastElementsIndex; i++) {
			if (element == array[i]) {
				return removeElement(i);
			}
		}
		
		return null;
	}

	@Override
	public void removeLastElement(E element) {
		for (int i = lastElementsIndex - 1; i >= 0; i--) {
			if (element == array[i]) {
				removeElement(i);
				
				return;
			}
		}
	}

	@Override
	public void removeAllElement(E element) {
		for (int i = 0; i < lastElementsIndex; i++) {
			if (element == array[i]) {
				removeElement(i);
			}
		}
	}

	@Override
	public void removeElements(int startArrayIndex, int endArrayIndex) {
		for (int i = startArrayIndex; i < endArrayIndex; i++) {
			removeElement(i);
		}
	}

	@Override
	public void removeElements(int startListIndex, int gapsBeforeStart, int endListIndex, int gapsInIntervall) {
		removeElements(startListIndex + gapsBeforeStart, endListIndex + gapsBeforeStart + gapsInIntervall);
	}

	@Override
	public void resort() {
		// sorting is same as copying all elements without gaps to the same array
		copyWithoutGaps(array, array);
	}

	@Override
	public void setMultiplier(byte m) {
		if (m < 2) {
			throw new IllegalArgumentException("The multiplier must be greater than 1, actual: " + m);
		}
		
		this.multiplier = m;
	}
	
	private boolean isOutside(int arrayIndex) {
		return arrayIndex >= array.length;
	}
	
	private void extend() {
		// creating a new array with increased size. However, the size can only be Integer.Max_Value at maximum
		Object[] ne = new Object[Math.min(Integer.MAX_VALUE, array.length * multiplier)];
		
		// copy the old array to the new. We use this process to remove gaps
		copyWithoutGaps(array, ne);
		
		// replace the old array by the new
		array = ne;
	}
	
	@Override
	public void trim() {
		// creating a new array with the required size
		Object[] ne = new Object[lastElementsIndex - gaps.size()];
				
		// copy the old array to the new. We use this process to remove gaps
		copyWithoutGaps(array, ne);
				
		// replace the old array by the new
		array = ne;
	}
	
	private void copyWithoutGaps(Object[] old, Object[] ne) {
		// moving the cursor to the first Position
		gaps.toRoot();
		
		// we dont need to move anything, if there are no gaps and the new array is the same as the old one
		if (old == ne && !gaps.hasNext()) {
			return;
		}
		
		// if the old object is the same as the first, we dont need to move the Elements before the first gap
		int start, gapsSoFar;
		if (old == ne) {
			start = gaps.next() + 1;
			gapsSoFar = 1;
		}else {
			start = 0;
			gapsSoFar = 0;
		}
		
		
		// we need to move Elements as long as there are further gaps
		while (gaps.hasNext()) {
			int nextGap = gaps.next();
			System.arraycopy(old, start, ne, start - gapsSoFar, nextGap - start);
			gapsSoFar++;
			start = nextGap + 1;
		}
		
		// There might be elements after the last gap, which need to be moved as well		
		System.arraycopy(old, start, ne, start - gapsSoFar, lastElementsIndex - start);
		
		lastElementsIndex -= gaps.size();
		gaps.clear();
	}

	@Override
	public int length() {
		return lastElementsIndex;
	}
	
	public void clear() {
		lastElementsIndex = 0;
	}

	@Override
	public void copy(Object[] a) {
		if (a.length < lastElementsIndex - gaps.size()) {
			throw new IllegalArgumentException("The array must have the size of the list");
		}
		
		
		copyWithoutGaps(array, a);
	}
}

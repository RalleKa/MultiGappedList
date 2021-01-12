package main;

import main.CustomLinkedList.CustomLinkedIterator;

/**
 * 
 * @author RalfK
 *
 */
final class Gaps implements GapInterface {
	/**
	 * There is no maximum limit of gaps stored in this list, before a resort is triggered. This is not recommended, unless the list will be sorted manually.
	 * If the list will never be sorted, it will get slower and lose performance.
	 */
	public static final int NO_GAP_LIMIT = 0;
	
	/**
	 * This value stores the maximum number of gaps supported.
	 */
	private int supportedGaps;
	
	/**
	 * The Linked List will save all the gaps. We use a LinkedList, because the list needs to support fast insertions. An index-based get will not be required, but fast iterating.
	 */
	private CustomLinkedList gaps;
	
	/**
	 * The list (or Resortable) to be resorted when the gap limit get exceeded.
	 */
	private Resortable onGapLimitExceeded;
	
	/**
	 * The Iterator of the CustomLinkedList
	 */
	private CustomLinkedIterator iterator;

	
	/**
	 * Creates an Object of Gaps to store the indexes of gaps
	 * @param supportedGaps The Limit of gaps supported as integer or NO_GAP_LIMIT
	 * @param onGapLimitExceeded 
	 */
	public Gaps(int supportedGaps, Resortable onGapLimitExceeded) {
		this.supportedGaps = supportedGaps;
		this.onGapLimitExceeded = onGapLimitExceeded;
		this.gaps = new CustomLinkedList();
		this.iterator = gaps.iterator();
	}
	
	/**
	 * changes the amount of supported gaps. Triggers a resort if required by the new rule
	 * @param supportedGaps the gaps to be supported or NO_GAP_LIMIT. Only accepts positive values.
	 */
	public void setSupportedGaps(int supportedGaps) {
		if (supportedGaps < 0) {
			throw new IllegalArgumentException("The Gap Limit can only be positive or NO_GAP_LIMIT");
		}
		
		this.supportedGaps = supportedGaps;
		
		testResort();
	}
	
	/**
	 * 
	 * @return the amount of supported gaps
	 */
	public int getSupportedGaps() {
		return supportedGaps;
	}
	
	/**
	 * tests if the gap limit is exceeded and triggers a resort if required
	 */
	private void testResort() {
		if (supportedGaps != NO_GAP_LIMIT && gaps.size() > supportedGaps) {
			onGapLimitExceeded.resort();
		}
	}
	
	/**
	 * returns the amount of gaps from the beginning to the given gap
	 * @param listIndex
	 * @return the amount of gaps
	 */
	public int getGapCount(int listIndex) {
		return getArrayIndex(listIndex) - listIndex;
	}
	
	/**
	 * calculates the index in the array out of the listIndex. (arrayIndex = listIndex + gapsTilArrayIndex)
	 * @param listIndex The ListIndex (The index requested by a user)
	 * @return The ArrayIndex (The index the element is at in the array)
	 */
	public int getArrayIndex(int listIndex) {
		int arrayIndex = listIndex;
		
		for (int gap: gaps) {
			if (gap <= arrayIndex) {
				arrayIndex++;
			}else {
				break;
			}
		}
		
		return arrayIndex;
	}

	public void toGapPosition(int gap) {
		if (gaps.size() == 0) {
			return;
		}

		if (gap > iterator.value()) {
			while (iterator.hasNext() && iterator.previewNext() <= gap) {
				iterator.next();
			}
		}else if (gap < iterator.value()) {
			while (iterator.hasPrevious() && iterator.previewPrevious() > gap) {
				iterator.previous();
			}
			
			iterator.previous();
		}
	}
	
	public int getElement() {
		return iterator.value();
	}
	
	public void add(int gap) {
		// moves the cursor to the position where the element would be added
		toGapPosition(gap);
		
		// if the element is already contained, do nothing
		if (gap == iterator.value()) {
			return;
		}
		
		// add the gap
		iterator.add(gap);
		
		// if the limit is exceeded, trigger a resort
		testResort();
	}
	
	public void remove(int gap) {
		toGapPosition(gap);
		if (getElement() == gap) {
			iterator.remove();
		}
	}
	
	public void remove() {
		// testing if it is the root element will be done in the iterator
		iterator.remove();
	}
	
	public int getIndex() {
		return iterator.nextIndex() - 1;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public int next() {
		return iterator.next();
	}

	@Override
	public void toRoot() {
		iterator.toRoot();
	}

	@Override
	public int size() {
		return iterator.size();
	}

	@Override
	public void clear() {
		iterator.clear();
	}

	@Override
	public boolean contains(int gap) {
		toGapPosition(gap);
		return iterator.value() == gap;
	}

	@Override
	public boolean hasPrevious() {
		return iterator.hasPrevious();
	}

	@Override
	public int previous() {
		return iterator.previous();
	}

	@Override
	public int previewNext() {
		return iterator.previewNext();
	}

	@Override
	public int previewPrevious() {
		return iterator.previewPrevious();
	}
}

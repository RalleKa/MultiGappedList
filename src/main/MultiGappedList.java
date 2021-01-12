package main;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class MultiGappedList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable, Resortable{
	private static final long serialVersionUID = -7683808877465290651L;
	public static final int DEFAULT_GAPS_SUPPORTED = 10;
	public static final int NO_GAP_LIMIT = Gaps.NO_GAP_LIMIT;
	public static final int DEFAULT_LENGTH = 100;
	
	private ArrayInterface<E> array;
	private int size;
	
	private Gaps gaps;
	
	public MultiGappedList() {
		this(DEFAULT_LENGTH);
	}
	
	public MultiGappedList(int length) {
		this(length, DEFAULT_GAPS_SUPPORTED);
	}
	
	public MultiGappedList(int length, int gapsSupported) {
		this(length, gapsSupported, CustomArray.DEFAULT_MULTIPLIER);
	}
	
	public MultiGappedList(int length, int gapsSupported, byte lengthMultiplier) {
		this.gaps = new Gaps(gapsSupported, this);
		
		this.array = new CustomArray<E>(gaps, length, lengthMultiplier);
		this.size = 0;
	}
	
	public class GappedIterator implements Iterator<E> {
		private int listIndex;
		
		public GappedIterator(int listIndex) {
			this.listIndex = listIndex - 1;
		}

		@Override
		public boolean hasNext() {
			return listIndex + 1 < size();
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			
			return array.getElement(gaps.getArrayIndex(++listIndex));
		}
		
		@Override
		public void remove() {
			array.removeElement(gaps.getArrayIndex(listIndex));
			size--;
			listIndex--;
		}
	}
	
	class GappedListIterator extends GappedIterator implements ListIterator<E> {
		public GappedListIterator(int index) {
			super(index);
		}
		
		@Override
		public boolean hasPrevious() {
			return super.listIndex >= 0;
		}

		@Override
		public E previous() {
			return array.getElement(gaps.getArrayIndex(super.listIndex--));
		}

		@Override
		public int nextIndex() {
			return super.listIndex + 1;
		}

		@Override
		public int previousIndex() {
			return super.listIndex;
		}

		@Override
		public void set(E e) {
			array.setElement(gaps.getArrayIndex(super.listIndex), e);
		}

		@Override
		public void add(E e) {
			array.addElement(super.listIndex, gaps.getGapCount(super.listIndex), e);
		}
		
		@Override
		public void remove() {
			super.remove();
			super.listIndex--;
		}
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Iterator<E> iterator() {
		return new GappedIterator(0);
	}

	@Override
	public void clear() {
		array.clear();
		gaps.clear();
		size = 0;
	}

	@Override
	public E get(int index) {
		return array.getElement(gaps.getArrayIndex(index));
	}

	@Override
	public E set(int index, E element) {
		return array.setElement(gaps.getArrayIndex(index), element);
	}

	@Override
	public void add(int index, E element) {
		array.addElement(index, gaps.getGapCount(index), element);
		size++;
	}

	@Override
	public E remove(int index) {
		size--;
		return array.removeElement(gaps.getArrayIndex(index));
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new GappedListIterator(index);
	}

	@Override
	public void resort() {
		array.resort();
	}

	@Override
	public Object[] toArray() {
		return toArray(new Object[size()]);
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// we can use the moment to resort the array
		resort();
		// copying the array using the internal copy method
		array.copy(a);
		// giving back the filled array
		return a;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// it is not necessary to trigger multiple resorts for this method
		// so we set the limit for gaps to no gap limit
		// in order to undo the change, we need to save the old value
		int supportedGaps = gaps.getSupportedGaps();
		
		try {
			gaps.setSupportedGaps(NO_GAP_LIMIT);
			
			// in order to delete every given item, we use the super method
	        boolean modified = super.removeAll(c);

	        // undo the changes to the amount of supported gaps
	        // may trigger a single resort
			gaps.setSupportedGaps(supportedGaps);
			
			// was this list modified
			return modified;
		}catch (Exception e) {
			// in case any exception occurs, we need to undo the changes to the amount of gaps as the Exception may get catched
			gaps.setSupportedGaps(supportedGaps);
			
			// the exception may get handled by the user of this list
			throw e;
		}		
	}
	
	/**
	 * Returns the gap the iterator is currently at. This gap might appear random.
	 * Use this information to add the next Item to this Position, if the index of the new Item does not matter
	 * and you want to save time.
	 * @return A Gap
	 */
	public int getGap() {
		// The gap is in the array. To get the ListIndex, we need to substrate 
		// the amount of gaps from the arrayIndex of the gap
		return gaps.getElement() - gaps.getIndex();
	}
}

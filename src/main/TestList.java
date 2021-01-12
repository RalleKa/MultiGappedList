package main;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestList {
	public static final int OPERATIONS_TO_DO = Integer.MAX_VALUE;
	public static final boolean OUTPUT = false;
	public static final int PERCENTAGE_ACCURACY = 10;
	
	public static final int ADD_LAST = 0;
	public static final int ADD_INDEX = 1;
	
	public static final int SET_INDEX = 2;
	
	public static final int REMOVE_INDEX = 3;
	public static final int REMOVE_OBJECT = 4;
	
	public static final int CLEAR = 5;
	public static final int IS_EMPTY = 6;
	
	public static final int TO_ARRAY = 7;
	public static final int TO_ARRAY_GIVEN = 8;

	public static final int CONTAINS = 9;
	
	public static final int CONTAINS_ALL = 10;
	public static final int ADD_ALL = 11;
	public static final int ADD_ALL_INDEX = 12;
	public static final int REMOVE_ALL = 13;
	public static final int RETAIN_ALL = 14;
	
	public static final int SUBLIST = 15;
	public static final int INDEX_OF = 16;
	public static final int LAST_INDEX_OF = 17;
	
	public static final int AMOUNT_OF_TESTS = 18;
	

	/**
	 * We test the new List by applying random operations on it. If it works correctly, it must do the same as a List that works properly (like the ArrayList)
	 */
	@Test
	void test() {
		int pointZeroOne = OPERATIONS_TO_DO / PERCENTAGE_ACCURACY / 100;
		int decimals = ((int) Math.log10(OPERATIONS_TO_DO));
		int lastSend = 0;
		
		// low resort level
		MultiGappedList<Long> e = new MultiGappedList<>(10, 3, (byte) 2);
		ArrayList<Long> a = new ArrayList<>(10);
		
		System.out.println(" elapsed      percent   remaining     Operations");
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < OPERATIONS_TO_DO; i++) {
			if (i / pointZeroOne > lastSend) {
				lastSend++;
				double time = (System.currentTimeMillis() - start) / 1000.;
				double percentage = lastSend * 1. / PERCENTAGE_ACCURACY;
				double timeNeeded = (int) (time / percentage * 100);
				int remaining = (int) (timeNeeded - time);
				
				System.out.println(String.format("%4d sec.     %5.1f%%     %4d sec.     %" + decimals + "d", (int) time, percentage, remaining, i));
			}
			int task = (int) (Math.random() * AMOUNT_OF_TESTS);

			switch(task) {
			case ADD_LAST:
				addLast(e, a);
				break;
			case ADD_INDEX:
				addIndex(e, a);
				break;
			case SET_INDEX:
				setIndex(e, a);
				break;
			case REMOVE_INDEX:
				removeIndex(e, a);
				break;
			case REMOVE_OBJECT:
				removeObject(e, a);
				break;
			case CLEAR:
				clear(e, a);
				break;
			case IS_EMPTY:
				isEmpty(e, a);
				break;
			case TO_ARRAY:
				toArray(e, a);
				break;
			case TO_ARRAY_GIVEN:
				toArrayGiven(e, a);
				break;
			case CONTAINS:
				contains(e, a);
				break;
			case CONTAINS_ALL:
				containsAll(e, a);
				break;
			case ADD_ALL:
				addAll(e, a);
				break;
			case ADD_ALL_INDEX:
				addAllIndex(e, a);
				break;
			case REMOVE_ALL:
				removeAll(e, a);
				break;
			case RETAIN_ALL:
				retainAll(e, a);
				break;
			case SUBLIST:
				subList(e, a);
				break;
			case INDEX_OF:
				indexOf(e, a);
				break;
			case LAST_INDEX_OF:
				lastIndexOf(e, a);
				break;
			}
		}
	}


	private void lastIndexOf(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 2) {
			int randomIndex = (int) (Math.random() * a.size());
			long randomValue = a.get(randomIndex);
			
			syso("lastIndexOf(" + randomValue + ") ");
			
			assertEquals(randomIndex, e.lastIndexOf(randomValue));
			assertEquals(a.lastIndexOf(randomValue), e.lastIndexOf(randomValue));
		}
	}


	private void indexOf(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 2) {
			int randomIndex = (int) (Math.random() * a.size());
			long randomValue = a.get(randomIndex);
			
			syso("indexOf(" + randomValue + ") ");
			
			assertEquals(a.indexOf(randomValue), e.indexOf(randomValue));
			assertEquals(randomIndex, e.indexOf(randomValue));
		}
	}


	private void subList(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 5) {
			int randomIndex1 = (int) (Math.random() * a.size());
			int randomIndex2 = (int) (Math.random() * a.size());

			syso("subList(" + Math.min(randomIndex1, randomIndex2) + "," + Math.max(randomIndex1, randomIndex2) + ")");
			
			List<Long> b =  a.subList(Math.min(randomIndex1, randomIndex2), Math.max(randomIndex1, randomIndex2));
			List<Long> f =  e.subList(Math.min(randomIndex1, randomIndex2), Math.max(randomIndex1, randomIndex2));
			
			assertEquals(b.size(), f.size());
			
			for(int i = 0; i < b.size(); i++) {
				assertEquals(b.get(i), f.get(i));
			}
		}
	}


	private void retainAll(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 2) {
			ArrayList<Long> randomElement1 = new ArrayList<>();
			randomElement1.add(a.get((int) (Math.random() * a.size())));
			randomElement1.add((long) (Math.random() * Long.MAX_VALUE));
			
			MultiGappedList<Long> randomElement2 = new MultiGappedList<>();
			randomElement2.add(randomElement1.get(0));
			randomElement2.add(randomElement1.get(1));
			
			syso("retainAll({" + randomElement1.get(0) + "," + randomElement1.get(1) + "}) ");
			
			a.retainAll(randomElement1);
			e.retainAll(randomElement2);
			
			eq(randomElement2, randomElement1);
		}
	}


	private void removeAll(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 2) {
			ArrayList<Long> randomElement = new ArrayList<>();
			randomElement.add(a.get((int) (Math.random() * a.size())));
			randomElement.add(a.get((int) (Math.random() * a.size())));
			
			syso("removeAll({" + randomElement.get(0) + "," + randomElement.get(1) + "}) ");
			
			a.removeAll(randomElement);
			e.removeAll(randomElement);
			
			eq(e, a);
		}
	}


	private void addAllIndex(MultiGappedList<Long> e, ArrayList<Long> a) {
		ArrayList<Long> randomValue = new ArrayList<>();
		randomValue.add((long) (Math.random() * Long.MAX_VALUE));
		randomValue.add((long) (Math.random() * Long.MAX_VALUE));
		int randomIndex = (int) (Math.random() * a.size());
		
		syso("addAll(" + randomIndex + ",{" + randomValue.get(0) + "," + randomValue.get(1) + "}) ");
		
		a.addAll(randomIndex, randomValue);
		e.addAll(randomIndex, randomValue);
		
		eq(e, a);
	}


	private void addAll(MultiGappedList<Long> e, ArrayList<Long> a) {
		ArrayList<Long> randomValue = new ArrayList<>();
		randomValue.add((long) (Math.random() * Long.MAX_VALUE));
		randomValue.add((long) (Math.random() * Long.MAX_VALUE));
		
		syso("addAll({" + randomValue.get(0) + "," + randomValue.get(1) + "}) ");
		
		a.addAll(randomValue);
		e.addAll(randomValue);
		
		eq(e, a);
	}


	private void containsAll(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 0) {
			ArrayList<Long> randomValue = new ArrayList<>();
			randomValue.add((long) (Math.random() * Long.MAX_VALUE));
			randomValue.add((long) (Math.random() * Long.MAX_VALUE));
			syso("containsAll({" + randomValue.get(0) + "," + randomValue.get(1) + "}) ");
			
			assertEquals(a.containsAll(randomValue), e.containsAll(randomValue));
			
			
			ArrayList<Long> randomElement = new ArrayList<>();
			randomElement.add(a.get((int) (Math.random() * a.size())));
			randomElement.add(a.get((int) (Math.random() * a.size())));
			syso("containsAll({" + randomElement.get(0) + "," + randomElement.get(1) + "}) ");
			
			assertEquals(a.containsAll(randomElement), e.containsAll(randomElement));
		}
	}


	private void contains(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 0) {
			long randomValue = (long) (Math.random() * Long.MAX_VALUE);
			syso("contains(" + randomValue + ") ");
			
			assertEquals(a.contains(randomValue), e.contains(randomValue));
			
			int randomIndex = (int) (Math.random() * a.size());
			long randomElement = a.get(randomIndex);
			syso("contains(" + randomElement + ") ");
			
			assertEquals(a.contains(randomElement), e.contains(randomElement));
		}
	}


	private void toArrayGiven(MultiGappedList<Long> e, ArrayList<Long> a) {
		syso("toArray(a)");
		Long[] array1 = new Long[a.size()];
		Long[] array2 = new Long[e.size()];
		
		a.toArray(array1);
		e.toArray(array2);
		
		assertEquals(array1.length, array2.length);
		
		for (int i = 0; i < array1.length; i++) {
			assertEquals(array1[i], array2[i]);
		}
	}


	private void toArray(MultiGappedList<Long> e, ArrayList<Long> a) {
		syso("toArray()");
		
		Object[] array1 = a.toArray();
		Object[] array2 = e.toArray();
		
		assertEquals(array1.length, array2.length);
		
		for (int i = 0; i < array1.length; i++) {
			assertEquals(array1[i], array2[i]);
		}
	}


	private void isEmpty(MultiGappedList<Long> e, ArrayList<Long> a) {
		syso("isEmpty()");
		
		assertEquals(a.isEmpty(), e.isEmpty());
	}


	private void clear(MultiGappedList<Long> e, ArrayList<Long> a) {
		// do not clear the list to often
		if (Math.random() < .02) {
			syso("clear()");
			a.clear();
			e.clear();
			
			eq(e, a);
		}
	}


	private void removeObject(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 0) {
			int randomIndex = (int) (Math.random() * a.size());
			long randomElement = a.get(randomIndex);
			
			syso("remove(" + randomElement + ") ");
			a.remove(randomElement);
			e.remove(randomElement);
			
			eq(e, a);
		}
	}


	private void removeIndex(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 0) {
			int randomIndex = (int) (Math.random() * a.size());
			
			syso("remove(" + randomIndex + ")");
			a.remove(randomIndex);
			e.remove(randomIndex);
			
			eq(e, a);
		}
	}


	private void setIndex(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (a.size() > 0) {
			long randomValue = (long) (Math.random() * Long.MAX_VALUE);
			int randomIndex = (int) (Math.random() * a.size());
			
			syso("set(" + randomIndex + "," + randomValue + ")");
			a.set(randomIndex, randomValue);
			e.set(randomIndex, randomValue);
			
			eq(e, a);
		}
	}


	private void addIndex(MultiGappedList<Long> e, ArrayList<Long> a) {
		long randomValue = (long) (Math.random() * Long.MAX_VALUE);
		int randomIndex = (int) (Math.random() * a.size());
		
		syso("add(" + randomIndex + "," + randomValue + ")");
		a.add(randomIndex, randomValue);
		e.add(randomIndex, randomValue);
		
		eq(e, a);
	}


	private void addLast(MultiGappedList<Long> e, ArrayList<Long> a) {
		long randomValue = (long) (Math.random() * Long.MAX_VALUE);
		
		syso("add(" + randomValue + ")");
		a.add(randomValue);
		e.add(randomValue);
		
		eq(e, a);
	}
	
	private void eq(MultiGappedList<Long> e, ArrayList<Long> a) {
		if (OUTPUT) {
			for (int i = 0; i < a.size(); i++) {
				System.out.println(i + "\t" + a.get(i) + "\t" + e.get(i));
			}
			System.out.println();
		}
		
		assertEquals(a.size(), e.size());
		
		for (int i = 0; i < a.size(); i++) {
			assertEquals(a.get(i), e.get(i));
		}
	}
	
	private void syso(String text) {
		if (OUTPUT) {
			syso(text);
		}
	}

}

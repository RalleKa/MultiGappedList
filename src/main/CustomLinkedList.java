package main;

import java.util.ListIterator;
import java.util.NoSuchElementException;

final class CustomLinkedList implements Iterable<Integer>{
	/**
	 * The root element of this linked list
	 */
	private Node root;
	
	/**
	 * The current size of the list
	 */
	private int size;
	
	/**
	 * creates an instance of the CustomLinkedList with size 0 and a root Node
	 */
	public CustomLinkedList() {
		root = new Node(-1);
		root.prev = root;
		size = 0;
	}
	
	/**
	 * A Node of the CustomLinkedList. May be a root Node.
	 * @author RalfK
	 *
	 */
	private class Node {
		/**
		 * The value of the Node (Element at this Position)
		 */
		private int value;
		
		/**
		 * The previous node. Used to iterate backwards
		 */
		private Node prev;
		
		/**
		 * The next node. Used to iterate forwards
		 */
		private Node next;
		
		/**
		 * creates a Node with the Value. The Node not yet has the correct prev and next Nodes and the prev and next nodes do not contain this node.
		 * Use addNext(int value) instead!
		 * @param value
		 */
		private Node(int value) {
			this.value = value;
		}
		
		/**
		 * This method adds a Node to the Next Position (behind the current Node) and updates the next and prev nodes of the previous, this and the next Node.
		 * @param value The Value to be contained by the new Node (The Element to be stored)
		 */
		private void addNext(int value) {
			Node node = new Node(value);
			
			if (next != null) {
				next.prev = node;
				node.next = next;
			}
			
			node.prev = this;
			this.next = node;
			
			next = node;
			size++;
		}
		
		/**
		 * Removes the current Node and updates the next and prev Nodes
		 */
		private void removeThis() {
			if (next != null) {
				next.prev = prev;
			}
			
			if (prev != null) {
				prev.next = next;
			}
			
			size--;
		}
	}
	
	/**
	 * This CustomLinkedIterator is a ListIterator used to iterate over the CustomLinkedList, as it does not contain a index-based get.
	 * @author RalfK
	 *
	 */
	class CustomLinkedIterator implements ListIterator<Integer> {
		/**
		 * The cursor of the Iterator
		 */
		private Node node;
		
		/**
		 * The index at which the cursor currently is
		 */
		private int index;
		
		/**
		 * Creates an Instance of CustomLinkedIterator. The starting Point is always the root Node of the List and does not contain a valid value itself.
		 * Use iterator.next() to move to the first valid argument
		 */
		private CustomLinkedIterator() {
			this.node = root;
			this.index = -1;
		}
		
		/**
		 * 
		 * @return The value (Element) at the current cursor position
		 */
		public int value() {
			return node.value;
		}
		
		/**
		 * 
		 * @return The index at which the cursor currently is
		 */
		public int index() {
			return this.index;
		}

		@Override
		public boolean hasNext() {
			return node.next != null;
		}
		
		/**
		 * private method to check if the given Node is a valid Node to iterate to.
		 * Throws NoSuchElementException if not.
		 * @param node The node at which the iterator would iterate to
		 */
		private void checkNode(Node node) {
			if (node == null || node == this.node) {
				throw new NoSuchElementException();
			}
			
			this.node = node;
		}

		@Override
		public Integer next() {
			checkNode(this.node.next);
			index++;
			
			return node.value;
		}

		@Override
		public boolean hasPrevious() {
			return node.prev != node;
		}

		@Override
		public Integer previous() {
			checkNode(this.node.prev);
			index--;
			
			return node.value;
		}

		@Override
		public int nextIndex() {
			return index + 1;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void remove() {
			if (node != root) {
				node.removeThis();
				node = node.prev;
			}
		}

		@Override
		public void set(Integer e) {
			node.value = e;			
		}

		@Override
		public void add(Integer e) {
			node.addNext(e);
		}
		
		public int previewNext() {
			if (this.node.next == null) {
				throw new NoSuchElementException();
			}
			
			return this.node.next.value;
		}
		
		public int previewPrevious() {
			if (this.node.prev == this.node) {
				throw new NoSuchElementException();
			}
			
			return this.node.prev.value;
		}
		
		public void toRoot() {
			node = root;
		}
		
		public void clear() {
			toRoot();
			node.next = null;
			size = 0;
		}
		
		/**
		 * 
		 * @return The current size of the CustomLinkedList
		 */
		public int size() {
			return size;
		}
	}
	
	/**
	 * 
	 * @return The current size of the CustomLinkedList
	 */
	public int size() {
		return size;
	}

	@Override
	public CustomLinkedIterator iterator() {
		return new CustomLinkedIterator();
	}
}

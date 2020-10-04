
import java.io.BufferedReader;
import java.io.FileReader;


public class Stack <E>{
	
	// create a class for each node in the list(stack)
	private static class Node <E>{
		private E element; // ref to element stored at Node;
		private Node<E> next; // ref to next Node in the list
		public Node(E e, Node<E> n) {
			element = e;
			next = n;
		};
		public E getElement() {
			return element;
		}
		public Node<E> getNext(){
			return next;
		}
		public void setNext(Node<E> n) {
			next = n;
		};
	};
	
	// instance variables
	private Node <E> head = null;
	private Node <E> tail = null;
	private int size = 0;
	
	//constructor for stack class
	public Stack() {};
	
	//access
	public int size() {
		return size;
	};
	public boolean isEmpty() {
		return size == 0;
	};
	public E first () {
		if(isEmpty()) {
			return null;
		}
		return head.getElement();
	};
	public E last() {
		if (isEmpty()) {
			return null;
		}
		return tail.getElement();
	};
	//modifier
	public void addFirst(E e) {
		head = new Node<>(e,head);
		if(size==0) {
			tail = head;
		}
		size++;
	};
	public void addLast(E e) {
		Node <E> newest  = new Node<>(e,null);
		if(isEmpty()) {
			head = newest;
		}
		else {
			tail.setNext(newest);
		}
		tail = newest;
		size++;
	};
	public String removeFirst() {
		if (isEmpty()) {
			return null;
		};
		E answer = head.getElement();
		head = head.getNext();
		size--;
		if(size==0) {
			tail=null;
		};
		return answer.toString();
	};

	//push the stack
	public void push (E e) {
		addFirst(e);
	};
	//pop the stack
	public String pop () throws Exception {
		// throw exception if empty
		  if(head == null){
              throw new Exception("empty stack");
          };
          
		return removeFirst();
		
	};
	//print the stack
	public String print() throws Exception {
	    String listString = "";
	    Node <E> top = head;
	    // throw exception if empty
	    if(top == null){
            throw new Exception("empty stack");
        };

	    while (top != null) {
	        listString = listString + top.getElement().toString() + " ";
	        top = top.getNext();
	    }
	    return listString;
	};
	
	//Data reader method. This is also where the POP/PRINT Screens are printed. 
	public void readFile(String filename, Stack test) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = reader.readLine();

		    while (line != null) {
		    	
		    	if (line.equals("POP")){
		    		test.pop();
		    	}
		    	else if(line.equals("PRINT")) {
		    		System.out.println(test.print());
		    		System.out.println();
		    	}
		    	else{
		    		test.push(line);
		    	}
//		        sb.append(line);
//		        sb.append(System.lineSeparator());
		        line = reader.readLine();
		    }
		 
		} finally {
		    reader.close();
		}
	};
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
	   Stack test = new Stack<>();
	   test.readFile("/Users/johnpurcell/ds20s-a1.txt", test);
	};
};	
 





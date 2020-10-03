public class Assignment1 {

    static class LinkedListStack<T> {
        // an inner class to define a single linked list node
        private class Node<T> {
            T value;
            Node<T> next;
        }
        // keep track of the head of the stack, head.next will point to the next element in the stack
        private Node<T> head;

        // constructor for the stack class
        public LinkedListStack(){
            this.head = null;
        }

        // pop implementation
        public T pop() {
            if(head == null){
                throw new CustomException("empty stack");
            }

            T value = head.value;
            head = head.next;

            return value;
        }

        // push implementation
        public void push(T value){
            Node<T> oldHead = this.head;
            this.head = new Node<>();
            this.head.value = value;
            this.head.next = oldHead;
        }
    }
    public static void main(String[] args){

        long startTime = System.currentTimeMillis();

        LinkedListStack<String> lls = new LinkedListStack<>();

        lls.push("YO");
        lls.push("WADDUP");

        System.out.println("pop1=" + lls.pop());
        System.out.println("pop2=" + lls.pop());
        //System.out.println("pop3=" + lls.pop());

        LinkedListStack<Integer> iLls = new LinkedListStack<>();
        iLls.push(15);
        iLls.push(20);
        iLls.push(25);

        System.out.println("pop4=" + iLls.pop());
        System.out.println("pop5=" + iLls.pop());
        System.out.println("pop6=" + iLls.pop());

        long endTime = System.currentTimeMillis();

        System.out.println(endTime-startTime);
    }
    
    static class CustomException extends RuntimeException {
        private static final long serialVersionUID = 1;
    
        //
        public CustomException(){
    
        }
    
        public CustomException(String message){
            super(message);
        }
    }
}

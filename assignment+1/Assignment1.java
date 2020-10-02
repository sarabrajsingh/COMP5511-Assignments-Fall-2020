public class Assignment1 {

    static class LinkedListStack {
        // an inner class to define a single linked list node
        private class Node {
            String value;
            Node next;
        }
        // keep track of the head of the stack, head.next will point to the next element in the stack
        private Node head;

        // constructor for the stack class
        public LinkedListStack(){
            this.head = null;
        }

        // pop implementation
        public String pop() {
            if(head == null){
                throw new CustomException("empty stack");
            }

            String value = head.value;
            head = head.next;
            return value;
        }

        // push implementation
        public void push(String value){
            Node oldHead = this.head;
            this.head = new Node();
            this.head.value = value;
            this.head.next = oldHead;
        }
    }
    public static void main(String[] args){
        LinkedListStack lls = new LinkedListStack();
        lls.push("YO");
        lls.push("WADDUP");

        System.out.println("pop1=" + lls.pop());
        System.out.println("pop2=" + lls.pop());
        System.out.println("pop3=" + lls.pop());

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
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * CSC 143 - Weekly Problem 14
 * @author Walter Holley III
 *
 */
public class weeklyProblem14 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stack<Integer> stack1 = new Stack<Integer>();
		stack1.add(42);
		stack1.add(33);
		stack1.add(-17);
		Queue<Integer> queue1 = new LinkedList<Integer>();
		queue1.add(42);
		queue1.add(33);
		queue1.add(-17);
		queue1.add(77);
		queue1.add(84);
		int queuePosition = 4;
		
		System.out.println("QUESTION 1 RESULT");
		question1();
		System.out.println("QUESTION 2 RESULT");
		question2();
		System.out.println("QUESTION 3 RESULT");
		question3();
		System.out.println("QuUESTION 4 RESULT");
		System.out.printf("Stack sent: %s\n", stack1.toString());
		System.out.printf("Stack returned: %s\n\n", mirImage(stack1).toString());
		System.out.println("QUESTION 5 RESULT");
		System.out.printf("Queue Position: %s\nQueue sent: %s\n", queuePosition, queue1.toString());
		System.out.printf("Queue returned: %s\n\n", moveElementToHead(queuePosition, queue1).toString());
		

	}
	
	/**
	 * Solution for Question 1
	 * From the instructions:
	 * <p>
	 * What is the final output?
	 * Add {1, 3, 5, 7} to a stack #1 
	 * Remove three items from stack, place in queue 
	 * Remove two items from queue, place in stack #2 
	 * Remove one item from stack #2, place in queue 
	 * Remove one item from stack #1, place in stack #2
	 */
	private static void question1(){
		Stack<Integer> stack1 = new Stack<Integer>();
		Stack<Integer> stack2 = new Stack<Integer>();
		Queue<Integer> queue1 = new LinkedList<Integer>();
		stack1.push(1);
		stack1.push(3);
		stack1.push(5);
		stack1.push(7);
		queue1.add(stack1.pop());
		queue1.add(stack1.pop());
		queue1.add(stack1.pop());
		stack2.push(queue1.remove());
		stack2.push(queue1.remove());
		queue1.add(stack2.pop());
		stack2.push(stack1.pop());
		
		System.out.printf("Stack 1 contents: %s\nQueue 1 Contents: %s\nStack 2 Contents: %s\n\n", stack1, queue1, stack2);
	}
	
	/**
	 * Solution for Question 2
	 * From the instructions:
	 * <p>
	 * A letter means push and an asterisk means pop in the following sequence. 
	 * Give the sequence of values returned by the pop operations when this sequence of operations
	 * is performed on an initially empty LIFO stack.
	 * E A S * Y * Q U E * * * S T * * * I O * N * * *
	 */
	private static void question2(){
		Stack<String> stack1 = new Stack<String>();
		stack1.push("E");
		stack1.push("A");
		stack1.push("S");
		System.out.print(stack1.pop());
		stack1.push("Y");
		System.out.print(stack1.pop());
		stack1.push("Q");
		stack1.push("U");
		stack1.push("E");
		System.out.print(stack1.pop());
		System.out.print(stack1.pop());
		System.out.print(stack1.pop());
		stack1.push("S");
		stack1.push("T");
		System.out.print(stack1.pop());
		System.out.print(stack1.pop());
		System.out.print(stack1.pop());
		stack1.push("I");
		stack1.push("O");
		System.out.print(stack1.pop());
		stack1.push("N");
		System.out.print(stack1.pop());
		System.out.print(stack1.pop());
		System.out.print(stack1.pop());
		System.out.print("\n\n");
		
		
	}
	
	/**
	 * Solution for Question 3
	 * From the instructions:
	 * <p>
	 * A letter means add and an asterisk means remove in the following sequence. 
	 * Give the sequence of values returned by the remove operation when this sequence of operations 
	 * is performed on an initially empty FIFO queue. 
	 * E A S * Y * Q U E * * * S T * * * I O * N * * *
	 */
	private static void question3(){
		Queue<String> queue1 = new LinkedList<String>();
		queue1.add("E");
		queue1.add("A");
		queue1.add("S");
		System.out.print(queue1.remove());
		queue1.add("Y");
		System.out.print(queue1.remove());
		queue1.add("Q");
		queue1.add("U");
		queue1.add("E");
		System.out.print(queue1.remove());
		System.out.print(queue1.remove());
		System.out.print(queue1.remove());
		queue1.add("S");
		queue1.add("T");
		System.out.print(queue1.remove());
		System.out.print(queue1.remove());
		System.out.print(queue1.remove());
		queue1.add("I");
		queue1.add("O");
		System.out.print(queue1.remove());
		queue1.add("N");
		System.out.print(queue1.remove());
		System.out.print(queue1.remove());
		System.out.print(queue1.remove());
		System.out.print("\n\n");
	}
	
	/**
	 * Solution for Questions 4
	 * From the instructions:
	 * <p>
	 * Write a method mirImage that accepts a stack of 
	 * integers as a parameter and appends the stack’s contents to itself 
	 * in reverse order.  <p>When a stack, front-->[42, 33, -17]<--back 
	 * is passed to this method, the stack becomes, front-->[42, 33, -17, -17, 33, 42]<--back.
	 * @param intStack A stack object containing integers
	 * @return the mirrored stack object
	 */
	private static Stack<Integer> mirImage(Stack<Integer> intStack){
		Stack<Integer> frontBack = (Stack<Integer>)intStack.clone();
		
		while(intStack.size() > 0){
			frontBack.push(intStack.pop());
		}
		return frontBack;	
	}
	
	/**
	 * Solution for Question 5
	 * From the instructions:
	 * <p>
	 * Write a method that moves the nth element (counting from the front, which is element 1)
	 * of the queue to the front, leaving the order of all other elements unchanged.
	 * @param elementPosition the position in the queue of the element you want moved to the front
	 * @param queueObject the queue containing the element you wish to move
	 * @return Queue object with the specified element in the front
	 */
	private static Queue<Integer> moveElementToHead(int elementPosition, Queue<Integer> queueObject){
		Integer newHead = null;
		
		if(elementPosition <= queueObject.size() && elementPosition > 0){
			Queue<Integer> tempQueue = new LinkedList<Integer>();
			int position = 1;
			while(queueObject.size() > 0){
				if(position == elementPosition){
					newHead = queueObject.poll();
					position++;
					continue;
				}
				tempQueue.add(queueObject.poll());
				position++;
			}
			
			queueObject.add(newHead);
			while(tempQueue.size() > 0){
				queueObject.add(tempQueue.poll());
			}			
		}
		return queueObject;
	}

}

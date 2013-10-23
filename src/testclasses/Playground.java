package testclasses;


import java.util.Stack;

import main.Vertex;

public class Playground {
	
	public static void main(String[] args) {
		Stack<Vertex> stacks = new Stack<Vertex>();
		
		stacks.push(new Vertex(1, 1));
		stacks.push(new Vertex(1, 5));
		
		
		Vertex top = stacks.pop();
		System.out.println(top);
		
		top = stacks.pop();
		System.out.println(top);
		
	}
}

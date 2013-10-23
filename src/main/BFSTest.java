package main;

public class BFSTest {
	private static BFS traverser;
	private static ShortGrid grid;
	
	public static void main(String[] args) {
		grid = new ShortGrid(20, 20);
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(i == 0 || i == 19 || j == 0 || j == 19) {
					grid.setStatus(i, j, 2);
				}
				else if((i*j) % 10 == 3) {
					grid.setStatus(i, j, 0);
				}
				else {
					grid.setStatus(i, j, 1);
				}
			}
		}
		
		for(int i = 0; i < 20; i++) {
			System.out.println();
			for(int j = 0; j < 20; j++) {
				System.out.print("|" + grid.getStatus(i, j) + "|");
			}
		}
		System.out.println();
		
		traverser = new BFS(grid, 20, 20);
		traverser.search(10, 10);
	}
}

package main;
import lejos.nxt.comm.RConsole;

public class Grid {
	/*
	 * Grid.java
	 * 
	 * This class contains the memory mappings for the Vroomba's grid area.
	 * Each tile info is stored in 2 bits of memory.  If we have a 100x100
	 * grid [0][0] (bottom-left) to [n][n] (top-right), we use about 2000 B.  
	 * These are stored in a two-dimensional array of bytes.
	 * 
	 * It's also important to note that the vroomba presumably starts at 
	 * [n/2][n/2] and we consider the plane to be the xz-plane where x is 
	 * left-right dimension (column) and z is up-down dimension (row). 
	 * 
	 * (int)Math.floor(x / 4) = Byte position in row that contains tile x
	 * x - ((int)Math.floor(x / 4)) * 4 = Tile's position in that byte
	 * 2 * (x - ((int)Math.floor(x / 4)) * 4) = Bit position of tile in byte
	 * 
	 * Zach - 03/24/2013
	 */
	
	public static class Tile {
		public static int TRAVERSABLE = 0;
		public static int VISITED = 1;
		public static int INTRAVERSABLE = 2;
	}
	
	private short[][] grid;
	private int nColumns;
	private int nRows;
	
	private int minX, maxX, minZ, maxZ;
	
	public Grid(int nRows, int nColumns) {
		grid = new short[nRows][nColumns];
		
		for(short[] row : grid) {
			
			for(short b : row)
				b = 0;
		}
	}
	
	public int getPosition(int x, int z) {
		short res = grid[x][z];
		
		return res;
	}
	
	public void setPosition(int x, int z, int value) {
		grid[x][z] = (short)value;
	}
	
	// for testing
	public void printGrid() {
		for(int i = 0; i < nRows; i++) {
			//System.out.print("\n|");
			System.out.println("\n|");
			for(int j = 0; j < nColumns; j++) {
				int tile = getPosition(i, j);
				if(tile == 0)
					System.out.print("00|");
				else if(tile == 1)
					System.out.print("01|");
				else if(tile == 2)
					System.out.print("10|");
				else
					System.out.print("11|");
			}
		}
	}
	
	public void updateMinX(int val) {
		minX = val;
	}
	
	public void updateMinZ(int val) {
		minZ = val;
	}
	
	public void updateMaxX(int val) {
		maxX = val;
	}
	
	public void updateMaxZ(int val) {
		maxZ = val;
	}
	
	public int getMinX() {
		return minX;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public int getMinZ() {
		return minZ;
	}
	
	public int getMaxZ() {
		return maxZ;
	}
	
	// TODO: make shift function

}
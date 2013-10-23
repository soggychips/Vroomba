package main;
import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

public class ShortGrid {
	
	public static class Tile {
		public static final short TRAVERSABLE = 0;
		public static final short VISITED = 1;
		public static final short INTRAVERSABLE = 2;
	}
	
	private short[][] grid;
	private short nColumns;
	private short nRows;
	
	private short minX, maxX, minZ, maxZ;
	
	public ShortGrid(int k, int l) {
		grid = new short[k][l];
		
		for(short i=0;i<k;i++){
			for(short j=0;j<l;j++){
				grid[i][j]= Tile.TRAVERSABLE;
			}
		}
	}
	
	/**
	 * 
	 * @param x Coordinate x
	 * @param z Coordinate z
	 * @return
	 */
	public short getStatus(int x, int z) {
		short b = grid[x][z];
		return b;
	}
	
	public void setStatus(int x, int z, int value) {		
		grid[x][z] = (short)value;		
	}
	
	// for testing
	public void printGrid() {
		for(short i = 0; i < nRows; i++) {
			//System.out.print("\n|");
			//RConsole.println("\n|");
			for(short j = 0; j < nColumns; j++) {
				short tile = getStatus(i, j);
				//RConsole.print(tile+"|");
			}
		}
	}
	
	public void updateMinX(int val) {
		minX = (short)val;
	}
	
	public void updateMinZ(int val) {
		minZ = (short)val;
	}
	
	public void updateMaxX(int val) {
		maxX = (short)val;
	}
	
	public void updateMaxZ(int val) {
		maxZ = (short)val;
	}
	
	public short getMinX() {
		return minX;
	}
	
	public short getMaxX() {
		return maxX;
	}
	
	public short getMinZ() {
		return minZ;
	}
	
	public short getMaxZ() {
		return maxZ;
	}
	
	// TODO: make shift function
	
	public boolean needsAttention(int x, int z, int n, int facing) {
		System.out.println("x: " +  x + " z: " + z + " f: " + facing);
		if(x - 1 < 0) x = 1;
		if(x + 1 > n) x = n - 1;
		if(z - 1 < 0) z = 1;
		if(z + 1 > n) z = n - 1;
		
		switch(facing) {
			case RoombaHelper.FACING_EAST:
				if(getStatus(x , z-1) == 0 || getStatus(x - 1, z) == 0 || getStatus(x, z + 1) == 0) {					
					return true;
				}
				break;
			case RoombaHelper.FACING_NORTH:
				if(getStatus(x - 1, z) == 0 || getStatus(x, z - 1) == 0 || getStatus(x + 1, z) == 0) {
					return true;
				}
				break;
			case RoombaHelper.FACING_SOUTH:
				if(getStatus(x-1, z) == 0 || getStatus(x, z + 1) == 0 || getStatus(x + 1, z) == 0) {
					return true;
				}
				break;
			case RoombaHelper.FACING_WEST:
				if(getStatus(x, z+1) == 0 || getStatus(x + 1, z) == 0 || getStatus(x, z - 1) == 0) {
					return true;
				}
			default:
				System.out.println("false");
				return false;
		}		
		
		return false;
	}

}


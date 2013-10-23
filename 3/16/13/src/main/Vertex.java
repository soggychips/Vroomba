package main;
public class Vertex {
		private int x;
		private int z;
		private boolean marked = false;
		
		public Vertex(int x, int z) {
			this.x = x;
			this.z = z;	
			marked = false;
		}
		
		public int posX() {
			return x;
		}
		
		public int posZ() {
			return z;
		}
		
		public void moveSouth() {
			z--;
		}
		
		public void moveNorth() {
			z++;
		}
		
		public void moveWest() {
			x--;
		}
		
		public void moveEast() {
			x++;
		}
		
		public boolean marked() {
			return marked;
		}
		
		public void markAsVisited() {
			marked = true;
		}

		public int compareTo(Vertex v) {
			return 0;
		}
		
		@Override
		public String toString() {
		return posX() + ", " + posZ();
		}
	}

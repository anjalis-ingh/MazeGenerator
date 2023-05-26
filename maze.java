import java.util.*;

public class maze {
    // global variables  
    private static final int east = 0, south = 1, west = 2, north = 3;
    public static int row, column;
    public static int arr [], height [], adjacent [];
    public static Stack<Integer> cell = new Stack<Integer>();
   
    // cell is a position or element in the maze board
    public static class Maze {  
        public int x, y;
        
        public Maze(int x, int y) {
            this.x = x;
	       this.y = y;
        }

        public void copy(Maze cell) {
            this.x = cell.x;
            this.y = cell.y;
        }
    }
    
    // walls link two neighboring cells together in any direction  
    public static class Wall { 
	   Maze cell;
	   int direction;    
	   boolean used;     
	   boolean deleted;  
	   boolean visited;  
	
	   public Wall(Maze cell, int direction) {
           this.cell = cell;
	       this.direction = direction;
	       this.used = false;
	       this.deleted = false;
       }
    }

    // maze board - row x column array whose values are the cells                                                                                                            
    public static Maze board [][];
    // walls is the edge where it store info of the cell and its direction
    // ex. wall[cell index][direction]
    public static Wall walls [][];
    // number of cells in the maze board
    public static int cellNum;  
    
    public static int rowFinder() {
    	int value = 0;
    	for (int i = 0; i < row * column; i++) 
    		if (arr[i] == -1) 
    		    value++;		 
    	return value;
    }

    // function to check if a cell has an unused edge or not 
    public static boolean allWallsUsed (int value) {
    	for (int i = 0; i < 4; i++) 
    	    if (!walls[value][i].used) 
    	        return false;
    	return true;
    }
    
    // function to union two sets together 
    public static void union(int cell, int cell2) {
    	int c1 = height[cell];
    	int c2 = height[cell2];
    	
    	if (c1 < c2) 
    		arr[cell] = cell2;
    	if (c1 > c2) 
    		arr[cell2] = cell;
    	else { 
    		height[cell2]++; 
    		arr[cell2] = cell;
    	}
    }
   
    // finds and returns the name/num of set containing cell
    public static int find(int cell) {
    	int index = cell;
    	while (arr[index] != -1){
    		index = arr[index];
    	}
    	
    	if (cell != index) {
    		int index2 = arr[cell];
        	while(index2 != index) {
        		arr[cell] = index;
        		cell = index2;
        		index2 = arr[index2];
    		}
    	}
    	return index;
    }

    // function that generates a randomized maze 
    // remove walls until a unique path from start to end is found 
    public static void mazeGenerator() {
    	// used for generating a random number for cell and direction  
    	Random randomGenerator = new Random();
    	int directions [] = new int[] {west, north, east, south};
    	int num, num2;
    	
    	while (rowFinder() > 1) {
    	    // get a random cell that has an unused edge
    		int i = randomGenerator.nextInt(cellNum);
    		while (allWallsUsed(i)) 
    		    i = randomGenerator.nextInt(cellNum);
    		
    		// find the direction of random unused edge 
    		int j = randomGenerator.nextInt(4);
    		while (walls[i][j].used) 
    		    j = randomGenerator.nextInt(4);
    		    
	        // return the name/num of the set they belong in 
	        num = find(i);
    		num2 = find(i + adjacent[j]);
    		
    		// if not in same set, union two sets, delete edge 
    		if (num!=num2) {
    		    union(num,num2);
    		    walls[i][j].deleted = true;
    		    walls[i + adjacent[j]][directions[j]].deleted = true;
    		}
    		// mark edge as used, will be used to form the maze 
    		else {
    		    walls[i][j].used = true;
    		}
    	}
    }	
    
    // function to find the unique path in maze (stack) 
    public static Stack<Integer> findPath() {
    	// variables 
    	int lastCell = row * column - 1;
    	int currCell;
    	Wall next;
    	boolean deadEnd;
    	
    	cell.push(0);
    	currCell = cell.peek(); 
    	while (currCell != lastCell) {
    	    currCell = cell.peek();
    	    deadEnd = true;
    	    
    	    for (int i = 0; i < 4; i++) {
        		next = walls[currCell][i];
        		if (next.deleted && !next.visited) {
        		    cell.push(currCell + adjacent[i]);
        		    walls[currCell][i].visited = true;
        		    deadEnd = false;
        		    break;
        		}
    	    } 
    	    
    	    if (cell.peek() == lastCell) 
    	        break;
    	    if (deadEnd) 
    	        cell.pop(); 
        }
        
        // after finding path, print the direction from one cell to the next
        // north, south, east, west
        for (int i = 1; i < cell.size(); i++) {
            int num = cell.get(i - 1), num2 = cell.get(i);
            
            if (num2 - num == adjacent[0]) System.out.print("E");
            else if (num2 - num == adjacent[1]) System.out.print("S");
            else if (num2 - num == adjacent[2]) System.out.print("W");
            else System.out.print("N");
        }
        
    	return cell;
    }
    
    // outputs the n x m matrix board in text format 
    public static void displayBoard() {
    	String line, pathStep;
	    // loop over each row 
        for (int i = 0; i < row; ++i) {
            // print at the start of every row 
            System.out.print("    -");
            // loop over every column, print remaining row lines
            for (int j = 0; j < column; ++j) {
        		line = (walls[i * column + j][north].deleted) ? "   -" : "----";
        		System.out.print(line);
    	    }
    	    
            System.out.println();
            
            // prints the west vertical boundary edges         
            if (i == 0) 
                System.out.print("Start");
            else 
                System.out.print("    |");
                
            for (int j = 0; j < column; ++j) {
		        pathStep = cell.contains(i * column + j) ? "X" : " ";
		        
		        // if last cell, print End, otherwise print the east vertical edges 
                if (i == row - 1 && j == column - 1)
		            System.out.print("  " + pathStep + " End");
                else {
		            line = (walls[i * column + j][east].deleted) ? "  " + pathStep + " " : "  " + pathStep + "|";
		            System.out.print(line);
		        }
            }
            System.out.println();
        }
        
        System.out.print("    -");
        
        // print last row  
        for (int j = 0; j < column; ++j) 
            System.out.print("----");
        System.out.println();
    }
    
    // driver code 
    public static void main(String[] args) {
	    Scanner scan = new Scanner(System.in); 
	    char letter; 

        // get user input for both row and column size of maze 
        // n x m matrix, row row = n 
        System.out.println("Random Maze Generator and Path Solver");
        System.out.print("Enter how many rows: ");
        row = scan.nextInt();
        
        // column row = m 
        System.out.print("Enter how many columns: ");
        column = scan.nextInt();
        System.out.println();
        
        // set the array sizes 
		arr = new int[row * column];
		height = new int[row * column];
		
		// used for navigating points adjacent of the current points
		// order navigation - east, south, west, north 
		adjacent = new int[] {1, column, -1, -column};
		
		for (int i = 0; i < row * column; i++) 
		    arr[i] = -1;
	    
	    // create a temp edge for all the boundary edges
	    Wall temp = new Wall(new Maze(0, 0), 0);
	    temp.used = true;
	    	     
	    // create a maze board with cells and walls array 
	    // 4 directions - north, south, east, west 
	    board = new Maze[row][column];
	    // number of cells in the maze board
	    cellNum = row * column;  
	    walls = new Wall[cellNum][4];         
	     
	    // assign all cells to a coordinate point and index value  
	    for (int i = 0; i < row; ++i) 
		  for (int j = 0; j < column; ++j) {
		    Maze cell = new Maze(i, j);
		    int cellIndex = i * column + j;   
		     
		    board[i][j] = cell;
		     
		    walls[cellIndex][east] = (j < column-1) ? new Wall(cell, east) : temp;
		    walls[cellIndex][south] = (i < row-1) ? new Wall(cell, south) : temp;        
		    walls[cellIndex][west] = (j > 0) ? walls[cellIndex - 1][east] : temp;         
		    walls[cellIndex][north] = (i > 0) ? walls[cellIndex - column][south] : temp;
		}
		
		// generates a randomized n x m matrix with a unique path 
		// display the maze board output to user
		System.out.println(row + " x " + column + " Matrix: ");
	    mazeGenerator();
	    displayBoard();
	    System.out.println();
	    
	    // find and mark the steps of the unique maze path 
	    // display the final board in the end 
	    System.out.print("Once ready, type the letter y to see the path solution: ");
	    letter = scan.next().charAt(0);
	    System.out.println();
	    
	    if (letter == 'y') {
	        System.out.print("Path Solution: ");
	        cell = findPath();
	        System.out.println();
	        displayBoard();
	        // output path directions (ex. SEN...)
	        
	    }
	    scan.close();
    }
}

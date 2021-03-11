//Chase Christiansen
/*DESCRIPTION: This single class program takes a text file for an input via an argument and reads each line into an array. The array size
is determined by the amount of lines in the file, as to allocate the exact amount of space needed. If the argument file has less than 2 values
in it, the user will get a message and the program will close. Upon using an argument file that has more than 1 line, the values are read into
an array that serves as the dimensions of matrices. The dimensions are used in the matrixChainOrder method to determine the minimum time cost
of the arrays of those dimensions to be multiplied. After that, the other method matrixChainOrderS is called with the same array as before to
calculate the optimal order of operations to get the minimum amount of computations when multiplying the arrays. The only difference in the
matrixChainOrderS method is that it returns the 2D array "s" instead of the integer value m[1][n-1] that represents the time cost. The countLines
method is only used for the precise space allocation for the array.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FastMatrixMulti {				
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(args[0]));
		String file = args[0];
		int arraySize = countLines(file);	
		int[] matrix = new int[arraySize];								//array "matrix" isn't a literal matrix. It represents the user input for the dimensions.
		int count = 0;
		while(scanner.hasNextInt() && count <= matrix.length) {			
			matrix[count++] = scanner.nextInt();
		}
		if(matrix.length < 2) {						//prevents the user from crashing the program with an input file that has less than 2 lines.
			System.out.println("Invalid argument. Please input an argument with at least 2 values.");
			System.exit(0);
		}
		int cost = matrixChainOrder(matrix);
		int[][] s = matrixChainOrderS(matrix);				//2D array for the printOptimalParens method.
		printOptimalParens(s, 1, matrix.length - 1);		//method call for the order of operations part of the output. the method does its own printing.
		System.out.println("\nThe minimum time cost is " + cost);		//printing the time cost.
	}

	public static int countLines(String file) {		//this method is for counting the amount of values in the file to allocate an array dynamically.
		Path path = Paths.get(file);				//not entirely necessary, but prevents from allocating excess memory for an array.
	    long lines = 0;
	    try {
	      lines = Files.lines(path).count();
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	    return (int)lines;							//returns the amount of line (values in this case as there is 1 value per line)  
	}												//in the file to use as the array size.

	public static int matrixChainOrder(int[] matrix) {
		int n = matrix.length;
		int[][] m = new int[n][n];					//allocating 2D array for the time cost value to be returned from.
		int[][] s = new int[n][n];					//allocating 2D array for the parameter for printing the order of operations.
		int i, j, k, q, h;
		
		for(i = 1; i < n; i++) {					//setting matrix indices to 0.
			m[i][i] = 0;
		}
		for(h = 2; h < n; h++) {					//for length of subchain starting at 2 until n.			
			for(i = 1; i < n - h + 1; i++) {		//for starting position of subchain until array size - subchain length + 1.
				j = i + h - 1;						//subchain ending position.
				if(j == n) {
					continue;
				}	
				m[i][j] = (int)Double.POSITIVE_INFINITY;
				for(k = i; k <= j - 1; k++) {									//trying different positions for outmost pair.
					q = m[i][k] + m[k + 1][j] + matrix[i - 1] * matrix[k] * matrix[j];
					if(q < m[i][j]) {
						m[i][j] = q;				//stores optimal time cost
						s[i][j] = k;				//position for outmost paranthesis for printing out optimal order of operations.
					}
				}
			}	
		}
		return m[1][n - 1];	
	}
	
	public static int[][] matrixChainOrderS(int[] matrix) {		//this method is literally like the other matrixChainOrder except it returns the 2D array S
		int n = matrix.length;								//which is needed for the printOptimalParens method. not good practice to duplicate the code like
		int[][] m = new int[n][n];					//this but it works entirely correctly and overall the program is still compact. so, unless there
		int[][] s = new int[n][n];					//is a way to return to values that I am unaware of, this is the best I could come up with. least it works.
		int i, j, k, q, h;
		
		for(i = 1; i < n; i++) {				
			m[i][i] = 0;
		}
		for(h = 2; h < n; h++) {					
			for(i = 1; i < n - h + 1; i++) {
				j = i + h - 1;				
				if(j == n) {
					continue;
				}				
				m[i][j] = (int)Double.POSITIVE_INFINITY;
				for(k = i; k <= j - 1; k++) {
					q = m[i][k] + m[k + 1][j] + matrix[i - 1] * matrix[k] * matrix[j];
					if(q < m[i][j]) {
						m[i][j] = q;
						s[i][j] = k;
					}
				}
			}	
		}
		return s;			//returns the 2D array s to be used for the printOptimalParens method.
	}
	
	public static void printOptimalParens(int[][] s, int i, int j) {
		if(i == j) {
			System.out.print("A" + i);							//when i equals j, print out the matrix A"i" for the order printing.
		}
		else {
			System.out.print("(");								//printing left parenthesis, followed by calling the method again with a new
			printOptimalParens(s, i, s[i][j]);					//value of j as s[i][j].
			printOptimalParens(s, s[i][j] + 1, j);				//calling method again with the value of i as s[i][j].
			System.out.print(")");								//printing right parenthesis.
		}
	}
}

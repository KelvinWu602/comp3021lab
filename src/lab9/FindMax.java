package lab9;

/**
 * 
 * COMP 3021
 * 
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29] 
another thread the max among the cells [30,59] 
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 * 
 * @author valerio
 *
 */

class Task implements Runnable{
    public int output;
    public int start;
    public int end;
    public int[] array;


    public Task(int[] array, int start, int end){
        this.start = start;
        this.end = end;
        this.array = array;
    }

    @Override
    public void run() {
        output = FindMax.findMax(start, end);
    }
}

public class FindMax {
	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) {
        Task tt1 = new Task(array, 0,30);
        Task tt2 = new Task(array, 30,60);
        Task tt3 = new Task(array, 60,90);

        Thread t1 = new Thread(tt1);
        Thread t2 = new Thread(tt2);
        Thread t3 = new Thread(tt3);
        t1.start();
        t2.start();
        t3.start();

        try{
        t1.join();
        t2.join();
        t3.join();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }

        int max = tt1.output;
        if(tt2.output>max){
            max = tt2.output;
        }
        if(tt3.output>max){
            max = tt3.output;
        }
        System.out.println(max);
	}

	public void printMax() {
		// this is a single threaded version
		int max = findMax(0, array.length - 1);
		System.out.println("the max value is " + max);
	}

	/**
	 * returns the max value in the array within a give range [begin,range]
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int findMax(int begin, int end) {
		// you should NOT change this function
		int max = array[begin];
		for (int i = begin; i < end; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
}
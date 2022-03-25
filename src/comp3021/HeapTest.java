package comp3021;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// import static org.junit.Assert.*;
// import org.junit.Before;
// import org.junit.Test;


public class HeapTest {

    private Heap<Integer> integerHeap;

    public void setup() {
        integerHeap = new Heap<>();
    }

    public void peek() {
        integerHeap.addAll(Arrays.asList(30, 10, 20));
        if(Integer.valueOf(10)!=integerHeap.peek()){
            System.err.println("Bad peek!");
        }
    }

    public void poll() {
        List<Integer> values = Arrays.asList(2, 53, 213, 5, 1, 5, 4, 210, 14, 26, 44, 35, 31, 33, 19, 52, 27);
        integerHeap.addAll(values);

        Collections.sort(values);
        for (int x : values) {
            int h = integerHeap.poll();
            if(Integer.valueOf(x)!=h){
                System.err.println("Poll: "+x + " : "+h);
            }
        }
    }

    public void size() {
    	List<Integer> values = Arrays.asList(10, 14, 26, 44, 35, 31, 33, 19, 52, 27);
        integerHeap.addAll(values);
        if(values.size()!=integerHeap.size()){
            System.err.println("size");
        }
    }

    

    public static void main(String [] args){
        HeapTest test1 = new HeapTest();
        test1.setup();
        test1.poll();
    }
}
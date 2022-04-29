package comp3021;

public class Task implements Runnable { 
	Object mymon, nextmon; 
	String myletter_;
	static String google_ = "";

	Task(Object m1, Object m2, String l)
	{ 
		mymon = m1; 
		nextmon = m2; 
		myletter_=l;
	}
	
	public void run() {
		while(true){
			synchronized(mymon){
				try {
					mymon.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			synchronized(nextmon){
				google_ = google_+myletter_;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(google_);
				nextmon.notify();
			}
		}
	}
	
	public static void main(String[]args){
		Object monitor1 = new Object(); 
		Object monitor2 = new Object(); 
		Object monitor3 = new Object();
		new Thread(new Task( monitor1, monitor2,"a")).start();
		new Thread(new Task( monitor2, monitor3,"b")).start();
		new Thread(new Task( monitor3, monitor1,"c")).start();
		synchronized(monitor1){
			//Give it a kick
			monitor1.notify();
		}
	}
}	
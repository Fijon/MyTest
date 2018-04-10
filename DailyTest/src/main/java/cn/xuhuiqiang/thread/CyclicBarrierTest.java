package cn.xuhuiqiang.thread;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CyclicBarrierTest {

	public static void main(String[] args) {
		CyclicBarrierTest test = new CyclicBarrierTest();
		CyclicBarrier cb = new CyclicBarrier(2, new ThreadEnd());
		Runnable one = test.new ThreadOne(cb);
		Runnable two = test.new ThreadTwo(cb);
		//new Thread(one).start();
		//new Thread(two).start();
		System.out.println("===========Main again============");

		try {
			Thread.sleep(1000 * 3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExecutorService es = Executors.newCachedThreadPool();
		one = test.new ThreadOne(cb);
		two = test.new ThreadTwo(cb);
		//es.execute(one);
		//es.execute(two);
		System.out.println("================Main Count Down =============");
		CountDownLatch countDownLatch = new CountDownLatch(2);
		CallableOne callOne = test.new CallableOne();
		CallableTwo callTwo = test.new CallableTwo();
				
		Callable<String> intputOne = test.new ThreadInput(countDownLatch, callOne);
		Callable<String> intputTwo = test.new ThreadInput(countDownLatch, callTwo);
		
		Future<String> oneResult = es.submit(intputOne);
		Future<String> twoResult = es.submit(intputTwo);
		System.out.println("wait for to son thread end ");
		try {
			countDownLatch.await();
			System.out.println(oneResult.get());
			System.out.println(twoResult.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class ThreadOne implements Runnable {

		private CyclicBarrier cb;

		public ThreadOne(CyclicBarrier cb) {
			super();
			this.cb = cb;
		}

		public void run() {
			System.out.println("-------------ThreadOne----------");
			try {
				Thread.sleep(2000 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("-------------ThreadOne end to Barrier----------");
			try {
				this.cb.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("-----------thread One end ---------------");
		}

	}

	class ThreadTwo implements Runnable {
		private CyclicBarrier cb;

		public ThreadTwo(CyclicBarrier cb) {
			super();
			this.cb = cb;
		}

		public void run() {
			System.out.println("-------------ThreadTwo----------");
			try {
				Thread.sleep(500 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("-------------ThreadTwo end to Barrier----------");
			try {
				this.cb.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("-----------ThreadTwo end ---------------");
		}
	}

	class CallableOne implements Callable<String> {

		public String call() throws Exception {
			System.out.println("*** callbale one ******");
			Thread.sleep(1000 * 5);
			Random rand = new Random();
			int ranInt = rand.nextInt(3);
			System.out.println( Thread.currentThread().getName() + " ======= " + ranInt);
			
			return ranInt %2 != 0 ? null:Thread.currentThread().getName();
		}
	}

	class CallableTwo implements Callable<String> {

		public String call() throws Exception {
			System.out.println("#### callbale two #####");
			Thread.sleep(1000 * 10);
			Random rand = new Random();
			int ranInt = rand.nextInt(3);
			System.out.println( Thread.currentThread().getName() + " ======= " + ranInt);
			return ranInt % 2 != 0 ? null:Thread.currentThread().getName();
		}
	}

	class ThreadInput implements Callable<String> {
		private CountDownLatch countDown;
		private Callable<String> task;
		public ThreadInput(CountDownLatch cb, Callable<String> task) {
			super();
			this.countDown = cb;
			this.task = task;
		}

		public String call() throws Exception {
		     String tmp = null;
		     do {
		    	 FutureTask<String> future = new FutureTask<String>(this.task);
		    	 new Thread(future).start();
		    	 try {
		    		 tmp = future.get();
		    	 }catch(Exception e) {
		    		 tmp = null;
		    	 }
		    	 if(null == tmp) {
		    		 System.out.println("$$$ do it again");
		    	 }
		     }while(tmp == null);
		     countDown.countDown();
			return tmp;
		}
	}

}

class ThreadEnd implements Runnable {

	public void run() {
		System.out.println("=========Thread end=========");

	}

}

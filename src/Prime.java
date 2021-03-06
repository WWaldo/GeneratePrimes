import java.math.BigInteger;

public class Prime extends Thread{

	public int perThread = 2500;
	public int startingPosition;
	public int threadNumber;
	public static int threads;
	public static int[] totals;

	public Prime(int threadNumber, int startingPosition, int threads)
	{
		this.threadNumber = threadNumber;
		this.startingPosition = startingPosition;
	}
	BigInteger denominatorBig = BigInteger.ONE;
	public boolean isPrime(BigInteger n)
	{
		denominatorBig = BigInteger.ONE;
		for(BigInteger j = n.subtract(BigInteger.valueOf(2)); j.compareTo(n.divide(BigInteger.valueOf(3))) > 0; j = j.subtract(BigInteger.valueOf(2)))
		{
			denominatorBig = denominatorBig.multiply(j).mod(n);
			if(denominatorBig.equals(BigInteger.ZERO))
				return false;
		}
		if((n.mod(BigInteger.valueOf(3)).equals(BigInteger.ZERO) && !n.equals(BigInteger.valueOf(3))) || (n.mod(BigInteger.valueOf(5)).equals(BigInteger.ZERO) && !n.equals(BigInteger.valueOf(5))))
			return false;
		return true;
	}
	Long denominator = 1L;
	public boolean isPrime(Long n)
	{
		denominator = 1L;
		//Making n/3 larger reduces accuracy of test
		//I believe the closer to 0 it gets the more accurate it is
		for(Long j = n-2; j > n/3; j-=2)
		{
			denominator = (denominator * j) % n;
			if(denominator == 0)
				return false;
		}
		if((n%3 == 0 && n != 3) || (n%5 == 0 && n != 5))
			return false;
		return true;
	}

	public void run()
	{
		int count = 1;
		int state = 0;
//		long total = 100000;
//		for(Long n = (long) startingPosition; n < total; n+=8)
//		{
//			//BigInteger n = BigInteger.valueOf(9223372036854775803L);
//			if(state == 0 && n >= total/4)
//			{
//				System.out.println("Thread " + threadNumber + " is 25% done");
//				state++;
//			}
//			else if(state == 1 && n >= total/2)
//			{
//				System.out.println("Thread " + threadNumber + " is 50% done");
//				state++;
//			}
//			else if(state == 2 && n >= (total/4) * 3)
//			{
//				System.out.println("Thread " + threadNumber + " is 75% done");
//				state++;
//			}
//			if(isPrime(n))
//			{
//				//System.out.println("Prime: " + n + " Prime #: " + count + " Thread #: " + threadNumber);
//				count++;
//			}
//		}
//		totals[threadNumber] = count - 1;

				for(BigInteger n = BigInteger.valueOf(startingPosition); n.compareTo(BigInteger.valueOf(10000L)) < 0; n = n.add(BigInteger.valueOf(threads * 2)))
				{
					if(isPrime(n))
					{
						//System.out.print("\rPrime: " + n + " Prime #: " + count + " Thread #: " + threadNumber);
						count++;
					}
				}
				totals[threadNumber] = count - 1;

	}
	public static void displayTotals()
	{
		int sum = 0;
		for(int i = 0; i < totals.length; i++)
		{
			sum += totals[i];
		}
		System.out.println("Total primes calculated: " + sum);
	}
	public static void main(String[] args) {
		System.out.println("Start");
		//2147483647
		//int threads;
		if( args.length > 0) {
			threads = Integer.parseInt(args[0]);
		} else	{
		 	threads = Runtime.getRuntime().availableProcessors();
		}		
		totals = new int[threads];
		System.out.println(threads);
		Prime [] thread = new Prime[threads];

		for(int i = 0; i < threads; i++)
		{
			thread[i] = new Prime(i, (2*i) + 3, threads);
		}
		//Prime thread0 = new Prime(0,3);
		//Prime thread1 = new Prime(1,5);
		//Prime thread2 = new Prime(2,7);
		//Prime thread3 = new Prime(3,9);

		Long start = System.nanoTime();

		for(int i = 0; i < threads; i++)
		{
			thread[i].start();
		}
		//thread0.start();
		//thread1.start();
		//thread2.start();
		//thread3.start();

		try {
			for(int i = 0; i < threads; i++)
			{
				thread[i].join();
			}
			//thread0.join();
			//thread1.join();
			//thread2.join();
			//thread3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\nRuntime: "+(System.nanoTime() - start)/1000000L+" milliseconds");
		displayTotals();
	}
}

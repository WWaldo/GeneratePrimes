import java.math.BigInteger;

public class Prime extends Thread{

	public int perThread = 2500;
	public int startingPosition;
	public int threadNumber;
	public static int[] totals = new int[4];

	public Prime(int threadNumber, int startingPosition)
	{
		this.threadNumber = threadNumber;
		this.startingPosition = startingPosition;
	}

	public boolean isPrime(BigInteger n)
	{
		BigInteger numerator = BigInteger.ONE;
		BigInteger denominator = BigInteger.ONE;

		for(BigInteger m = BigInteger.ONE; m.compareTo(n.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))) <= 0; m = m.add(BigInteger.ONE))
		{
			numerator = BigInteger.ONE;
			denominator = BigInteger.ONE;
			for(BigInteger j = n.subtract(m); j.compareTo(BigInteger.ONE) > 0; j = j.subtract(BigInteger.ONE))
			{
				denominator = denominator.multiply(j).mod(n);
				if(denominator.equals(BigInteger.ZERO))
					return false;
			}

			//System.out.println(denominator);
			for(BigInteger j = BigInteger.ZERO; n.subtract(m).compareTo(j) > 0; j = j.add(BigInteger.ONE))
			{
				numerator = numerator.multiply(n.subtract(j).mod(n));
				if(numerator.equals(BigInteger.ZERO))
					return true;
			}
			if(!numerator.divide(denominator).equals(BigInteger.ZERO))
				//			if(!numerator.mod(denominator).equals(BigInteger.ZERO))
				return false;
		}
		return false;
	}
	Long numerator = 1L;
	Long denominator = 1L;
	public boolean isPrime(Long n)
	{
		numerator = 1L;
		denominator = 1L;
		//Making n/3 larger reduces accuracy of test
		for(Long j = n-2; j > n/3; j-=2)
		{
			denominator = (denominator * j) % n;
			if(denominator == 0)
				return false;
		}
		if((n%3 == 0 && n != 3) || (n%5 == 0 && n != 5))
		{
			return false;
		}
		return true;
	}

	public void run()
	{
		int count = 1;
		int state = 0;
		long total = 10000;
		for(Long n = (long) startingPosition; n < total; n+=8)
		{
			//BigInteger n = BigInteger.valueOf(9223372036854775803L);
			if(state == 0 && n >= total/4)
			{
				System.out.println("Thread " + threadNumber + " is 25% done");
				state++;
			}
			else if(state == 1 && n >= total/2)
			{
				System.out.println("Thread " + threadNumber + " is 50% done");
				state++;
			}
			else if(state == 2 && n >= (total/4) * 3)
			{
				System.out.println("Thread " + threadNumber + " is 75% done");
				state++;
			}
			if(isPrime(n))
			{
				System.out.println("Prime: " + n + " Prime #: " + count + " Thread #: " + threadNumber);
				count++;
			}
		}
		totals[threadNumber] = count - 1;

		//		for(BigInteger n = BigInteger.valueOf(startingPosition); n.compareTo(BigInteger.valueOf(10000)) < 0; n = n.add(BigInteger.valueOf(8)))
		//		{
		//			if(isPrime(n))
		//			{
		//				System.out.println("Prime: " + n + " Prime #: " + count + " Thread #: " + (iterateBy-1)/2);
		//				count++;
		//			}
		//		}
		//		totals[threadNumber] = count - 1;

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
		Prime thread0 = new Prime(0,3);
		Prime thread1 = new Prime(1,5);
		Prime thread2 = new Prime(2,7);
		Prime thread3 = new Prime(3,9);

		Long start = System.nanoTime();
		thread0.start();
		thread1.start();
		thread2.start();
		thread3.start();

		try {
			thread0.join();
			thread1.join();
			thread2.join();
			thread3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println((System.nanoTime() - start)/1000000L);
		displayTotals();
	}
}

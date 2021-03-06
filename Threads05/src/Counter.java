/**
 * Взаимная блокировка процессов
 */
public class Counter {
	int counter = 0;

	public synchronized void change(Counter other) {
		counter++;
		// Здесь из сихронизированного метода одного объекта
		// вызывается синхронизированный метод другого объекта.
		if (other != null) other.change(null);
	}

	public static void main(String[] args) {
		final Counter c1 = new Counter();
		final Counter c2 = new Counter();
		Thread t1 = new Thread(() -> {
						for (int i = 0; i < 1000; ++i) {
							c1.change(c2);
						}
						System.out.format(
								"Thread %s done, counters = (%d, %d)\n",
								Thread.currentThread().getName(), c1.counter, c2.counter);
				});
		Thread t2 = new Thread(() -> {
						for (int i = 0; i < 1000; ++i) {
							c2.change(c1);
						}
						System.out.format(
								"Thread %s done, counters = (%d, %d)\n",
								Thread.currentThread().getName(), c1.counter, c2.counter);
				});
		t1.start();
		t2.start();
	}
}

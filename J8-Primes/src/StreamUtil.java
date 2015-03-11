import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public final class StreamUtil {
	/**
	 * Реализация "застегивания" потоков с помощью промежуточной структуры данных
	 * Stream.Builder.
	 * 
	 * @param func	Функция "застежки"
	 * @param s1	Первый поток
	 * @param s2	Второй поток
	 * @return		"Застегнутый" поток
	 */
	public static <T1,T2,R> Stream<R> zip(BiFunction<T1, T2, R> func, Stream<T1> s1, Stream<T2> s2) {
		Stream.Builder<R> builder = Stream.builder();
		Iterator<T1> it1 = s1.iterator();
		Iterator<T2> it2 = s2.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			builder.accept(func.apply(it1.next(), it2.next()));
		}
		return builder.build();
	}
	
	/**
	 * Реализация "застегивания" потоков с помощью простых итераторов
	 * Stream.Builder.
	 * 
	 * @param func	Функция "застежки"
	 * @param s1	Первый поток
	 * @param s2	Второй поток
	 * @return		"Застегнутый" поток
	 */
	public static <T1,T2,R> Stream<R> zipSplit(BiFunction<T1, T2, R> func, Stream<T1> s1, Stream<T2> s2) {
		Iterator<T1> it1 = s1.iterator();
		Iterator<T2> it2 = s2.iterator();
		Iterator<R> itRes = new Iterator<R>() {

			@Override
			public boolean hasNext() {
				return it1.hasNext() && it2.hasNext();
			}

			@Override
			public R next() {
				return func.apply(it1.next(), it2.next());
			}
		};
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(itRes, 0), false);
	}
	
	public static void main(String[] args) {
		Stream<Integer> s1 = Arrays.asList(0, 1, 1, 2, 3, 5, 8, 13, 21).stream();
		Stream<Integer> s2 = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21).stream();
		Stream<Integer> stream1 = zip(Integer::sum, s1, s2);
		stream1.forEach(x -> System.out.print(" " + x));
		System.out.println();
		
		s1 = Arrays.asList(0, 1, 1, 2, 3, 5, 8, 13, 21).stream();
		s2 = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21).stream();
		Stream<Integer> stream2 = zipSplit(Integer::sum, s1, s2);
		stream2.forEach(x -> System.out.print(" " + x));
		System.out.println();
		// Одним потоком не обойтись: после исполнения skip(1) поток уже не прочитать с начала!
//		s1 = Arrays.asList(0, 1, 1, 2, 3, 5, 8, 13, 21).stream();
//		zip(Integer::sum, s1, s1.skip(1)).forEach(x -> System.out.print(" " + x));
	}
}
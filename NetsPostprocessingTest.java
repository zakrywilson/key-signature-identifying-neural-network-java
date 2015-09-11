package musicTheory;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class NetsPostprocessingTest {

	@Test
	protected void testInterpretResults() {
		Random random = new Random();
		double[] values = new double[MusicTheoryNets.NUM_NODES];
		Arrays.fill(values, -1);
		int i = MusicTheoryNets.NUM_INPUT_NODES + MusicTheoryNets.NUM_HIDDEN_NODES;
		
		while (i < values.length) {
			values[i] = random.nextDouble();
			System.out.println(i + ": " + values[i]);
			i++;
		}
		
		double result = NetsPostprocessing.interpretResults(values);
		System.out.println("Result: " + result);
	}
	
	public static void main(String[] args) {
		NetsPostprocessingTest np = new NetsPostprocessingTest();
		
		// testInterpretResults
		np.testInterpretResults();
	}

}

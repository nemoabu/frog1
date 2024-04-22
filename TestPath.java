import javax.swing.JOptionPane;

public class TestPath {

	private static String[] expOutputs = new String[] {
		"12 1 2 3 4 3 7 3 2 11 16 22 24 ate 2 flies",
		"12 17 23 24 ate 0 flies",
		"0 5 1 5 11 16 17 18 24 ate 3 flies",
		"12 11 6 7 13 17 16 22 21 20 ate 12 flies",
		"48 19 21 23 25 27 29 28 14 13 12 26 42 43 57 73 87 118 147 176 207 205 203 201 ate 0 flies",
		"No solution",
		"77 75 94 92 71 50 41 43 24 14 13 ate 3 flies",
	};

	private static void runTest (int num) {
		boolean debugging = false;
		
			FrogPath prog = new FrogPath("pond" +num + ".txt");
			String res = prog.findPath();
			System.out.println(res);
			if (res.equals(expOutputs[num-1])) {
				System.out.println("TestPath - Test " + num + " Passed");
				if (debugging) JOptionPane.showMessageDialog(null,"TestPath - Test " + num + " Passed");
	
			} else {
				System.out.println("TestPath - Test " + num + " Failed");
				if (debugging) JOptionPane.showMessageDialog(null,"TestPath - Test " + num + " Failed");
			}
		

	}
	
	public static void main(String[] args) {
		
		Hexagon.TIME_DELAY = 000;

		// Run all tests sequentially.
		for (int i = 1; i <= 9; i++) {
			runTest(i);
		}	
		
	}

}

package eit.linecode;

/**
 * This class is used to calculate Dc balance.
 * 
 * @author Muhammad Obaid Ullah
 */
public class DcCalculator {
	/**
	 * This method takes 6T code , counts the DC balance by calculating the
	 * difference of positive and negative symbols.
	 * 
	 * @param six_ternary is the 6T code.
	 * @return The calculated DC Balance will be returned
	 */
	public int dcBalance(String six_ternary) {
		char char_array_6T[] = six_ternary.toCharArray();
		int plus = 0, minus = 0, dcBalance;
		for (int i = 0; i < char_array_6T.length; i++) { // counts the +'s and -'s in 6T by increasing plus and minus
															// variables
			if (char_array_6T[i] == '+') {
				plus++;
			}
			if (char_array_6T[i] == '-') {
				minus++;
			}
		}
		dcBalance = plus - minus; // Dc balance calculated and returned
		return dcBalance;
	}
}

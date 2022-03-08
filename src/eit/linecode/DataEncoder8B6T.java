package eit.linecode;

/**
 * This class implements the encoder functionality of the 8B/6T encoder.
 * <p>
 * Transforms the bytes into 6T data streams by using 8B6T conversion table
 * 
 * @author Muhammad Obaid Ullah
 */

public class DataEncoder8B6T {
	private static final int SINGLE_CODELENGTH = 6;
	private static final int BITS = 8;
	private static final int STREAMS = 3;
	private static final int LIMIT = 16;
	private static final int ARRAY_SIZE = 10000;

	/**
	 * This method converts byte array into 6T streams according to 8B6T Encoding
	 * method
	 * <p>
	 * streams of 6T code will be returned in three arrays
	 * 
	 * @param byte_array input given to method encode
	 * @return returns data stream of encoded 8B6T
	 */

	public String[] encode(byte[] byte_array) {
		Table8B6T table = new Table8B6T();
		String hex[] = new String[ARRAY_SIZE]; // Hexadecimal array
		int i;
		for (i = 0; i < byte_array.length; i++) {
			hex[i] = Integer.toHexString(byte_array[i]); /*
															 * bytes are converted into Hexadecimal String
															 */
			if (byte_array[i] < LIMIT && byte_array[i] >= 0) /*
																 * Hexadecimal byte between 0 and F will be converted to
																 * 00....0F to get the value from table 8B6T
																 */
			{
				hex[i] = "0" + hex[i];
			}
			if (byte_array[i] < 0) /*
									 * negative Decimal will be changed to Hexadecimal signed 2's complement
									 */
			{
				hex[i] = hex[i].substring(SINGLE_CODELENGTH, BITS);
			}
		}
		String code_6T[] = new String[ARRAY_SIZE]; // Array to store 6T code
		for (int f = 0; f < i; f++) {
			code_6T[f] = table.convert(hex[f]); /*
												 * 8B hexadecimal will be converted into 6T by using convert() method
												 */
		}
		String streams[] = new String[STREAMS];
		streams = makeDatastreams(code_6T, i);
		return streams;
	}

	/**
	 * This method invert() inverts the given 6T code algebraically.
	 * <p>
	 * + will be changed to - and - will be changed to + .
	 * 
	 * @param six_ternary code that is to be inverted.
	 * @return returns the inverted 6T code
	 */

	public static String invert(String six_ternary) {
		char char_array_6T[] = six_ternary.toCharArray(); // string is converted to array of 6T
		for (int i = 0; i < char_array_6T.length; i++) { /*
															 * 6T code letters will be checked , + will be changed to -,
															 * - will be changed to + , 0 remains same
															 */
			if (char_array_6T[i] == '+') {
				char_array_6T[i] = '-';

			} else if (char_array_6T[i] == '-') {
				char_array_6T[i] = '+';
			}
		}
		String ans = String.copyValueOf(char_array_6T, 0, SINGLE_CODELENGTH);
		// 6T is returned as answer in form of a string

		return ans;
	}

	/**
	 * This method will attach the Start of Stream Delimiter , check the DC Balance
	 * of the stream.
	 * <p>
	 * And finally it will attach the End of Stream Delimiter according to DC
	 * balance.
	 * 
	 * @param signal array of 6T codes
	 * @param i      length of array
	 * @return returns the data stream
	 */

	public static String[] makeDatastreams(String signal[],
			int i) { /*
						 * Signal is array of 6T code and i is its length
						 */
		String datastream[] = new String[STREAMS];
		datastream[0] = "+-+-+-+-+-+-+--+"; // P4 SOSA SOSB
		datastream[1] = "+-+-+-+-+-+-+-+--+"; // SOSA SOSA SOSB
		datastream[2] = "+-+-+-+-+-+-+-+-+--+"; // P3 SOSA SOSA SOSB
		DcCalculator calculate = new DcCalculator();
		int index1 = 0, index2 = 1, index3 = 2;
		int dcDataStream1 = 0, prevDc1 = 0, dcDataStream2 = 0, prevDc2 = 0, dcDataStream3 = 0, prevDc3 = 0;
		int cum1 = 0, cum2 = 0, cum3 = 0, dc1Sum = 0, dc2Sum = 0, dc3Sum = 0;
		for (int j = 0; j < i; j++) {

			if (index1 == j) /*
								 * index1 will be used to get the values 0,3,6,9.. of array signal[] that
								 * contains 6T signals
								 */
			{
				dcDataStream1 = calculate.dcBalance(signal[j]); /*
												 * dc balance of each individual code will be stored in dcdatastream,
												 * prevDc1 stores Dc balance of previous 6T code
												 */
				if (dcDataStream1 == 1 && prevDc1 == 1) /*
														 * if code's DC balance is 1 and previous code's DC balance is
														 * also 1 , code will be inverted by using invert() method
														 */
				{
					signal[j] = invert(signal[j]);
					dcDataStream1 = 0; // dc balance will be set to zero after inversion
					dc1Sum = -1; // Cumulative sum will be given an input of -1 after inversion
				} else if (dcDataStream1 == 0
						&& prevDc1 == 1)/*
										 * if current DC Balance is 0 and previous DC balance was 1 ,code stays same ,
										 * but they will add up and make dc cumulative balance as 1
										 */
				{
					dcDataStream1 = 1;
					dc1Sum = 0; // dc1Sum remains 0 as dc balance of current code is 0
				} else if (dcDataStream1 == 0 && prevDc1 == 0) {
					dc1Sum = 0; // dc1Sum remains 0 as dc balance of current code is 0
				} else // dcDatastream1 is 1 and previous dc balance was 0
				{
					dc1Sum = 1; // dc1Sum is equal to 1 as dc balance of current code is 1
				}
				cum1 = cum1 + dc1Sum; /*
										 * dc balance of each code will sum up to make cummulative dc balance of code
										 */
				prevDc1 = dcDataStream1; // current Code will become previous dc balance in next run
				datastream[0] = datastream[0] + signal[j]; /*
															 * each code will be added to data stream individually
															 */
				index1 = index1 + STREAMS; // index1 will be changed to next value as 3,6,9....
			}
			if (index2 == j) /*
								 * index2 will be used to get the values 1,4,7,10.. of array signal[] that
								 * contains 6T signals
								 */
			/*
			 * Same Process as Done with the above datastream1,(index1) will be repeated
			 * with other datastreams , in index2 and index3
			 */
			{
				dcDataStream2 = calculate.dcBalance(signal[j]);

				if (dcDataStream2 == 1 && prevDc2 == 1) {
					signal[j] = invert(signal[j]);
					dcDataStream2 = 0;
					dc2Sum = -1;
				} else if (dcDataStream2 == 0 && prevDc2 == 1) {
					dcDataStream2 = 1;
					dc2Sum = 0;
				} else if (dcDataStream2 == 0 && prevDc2 == 0) {
					dc2Sum = 0;
				} else {
					dc2Sum = 1;
				}
				cum2 = cum2 + dc2Sum;
				prevDc2 = dcDataStream2;
				datastream[1] = datastream[1] + signal[j];
				index2 = index2 + STREAMS;
			}
			if (index3 == j) /*
								 * index3 will be used to get the values 2,5,8,11.. of array signal[] that
								 * contains 6T signals
								 */
			{
				dcDataStream3 = calculate.dcBalance(signal[j]);
				/*
				 * Same Process as Done with the above datastream1,(index1) will be repeated
				 * with other datastreams , in index2 and index3
				 */
				if (dcDataStream3 == 1 && prevDc3 == 1) {
					signal[j] = invert(signal[j]);
					dcDataStream3 = 0;
					dc3Sum = -1;
				} else if (dcDataStream3 == 0 && prevDc3 == 1) {
					dcDataStream3 = 1;
					dc3Sum = 0;
				} else if (dcDataStream3 == 0 && prevDc3 == 0) {
					dc3Sum = 0;
				} else {
					dc3Sum = 1;
				}

				cum3 = cum3 + dc3Sum;
				prevDc3 = dcDataStream3;
				datastream[2] = datastream[2] + signal[j];
				index3 = index3 + STREAMS;
			}

		}
		final String e1 = "++++++", e2 = "++++--", e3 = "++--==", e4 = "------", e5 = "--===="; // endings of
																								// datastreams

		String eop1 = e1, eop4 = e4, eop3 = e3, eop2 = e2, eop5 = e5;
		if (cum1 == 0) /*
						 * if the cummulative sum of Dc Balance's of stream one is equal to zero eop4
						 * and eop1 will be inverted
						 */
		{
			eop4 = invert(eop4);
			eop1 = invert(eop1);

		}
		if (cum2 == 0) /*
						 * if the cummulative sum of Dc Balance's of stream two is equal to zero eop2
						 * and eop5 will be inverted
						 */
		{
			eop2 = invert(eop2);
			eop5 = invert(eop5);
		}
		if (cum3 == 0) /*
						 * if the cummulative sum of Dc Balance's of stream three is equal to zero eop3
						 * will be inverted
						 */
		{
			eop3 = invert(eop3);

		}
		// End of Stream Delimiter added into the stream
		datastream[0] = datastream[0] + eop1 + eop4;
		datastream[1] = datastream[1] + eop2 + eop5;
		datastream[2] = datastream[2] + eop3;

		return datastream; // returns array of three Datastreams
	}

}
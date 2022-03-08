package eit.linecode;

 
import eit.linecode.exception.DecodeException;
import eit.linecode.exception.EndOfPacketException;
import eit.linecode.exception.StartOfStreamException;

/**
 * This class decodes the transmission structure into hexadecimal bytes
 * according to the 8B/6T decoder Table.
 * <p>
 * The 6T codes of the data streams will be decoded into 8 bit hexadecimal
 * numbers.
 * <P>
 * And streams of bytes will be merged to make Single stream.
 * 
 * @author Muhammad Obaid Ullah
 *
 */
public class DataDecoder8B6T {
	private static final int START_OF_STREAM1 = 16;
	private static final int END_OF_STREAM = 12;
	private static final int START_OF_STREAM2 = 18;
	private static final int STREAM_ONE_LENGTH = 27;
	private static final int STREAM_TWO_LENGTH = 29;
	private static final int START_OF_STREAM3 = 20;
	private static final int END_OF_STREAM3 = 6;
	private static final int STREAM_THREE_LENGTH = 25;
	private static final int STREAM3 = 3;
	private static final int SIXT_CODE_LENGTH = 6;
	private static final int STREAM1 = 1;
	private static final int STREAM2 = 2;
	private static final int NO_OF_STREAMS = 3;
	private static final int HEXADECIMAL = 16;

	/**
	 * This method takes 6T data streams and decodes it into bytes according to 8B6T
	 * encoding. Begin , middle and end of stream will be checked
	 * <p>
	 * It will return the decoded streams in bytes.
	 * 
	 * @param input_stream is the streams that are taken as input from user in main
	 *               function
	 * @return decoded bytes of the entered stream(6T) according to 8B6T table will
	 *         be returned.
	 */
	public byte[] decode(String[] input_stream) {
		int dcBalance1 = 0, dcBalance2 = 0, dcBalance3 = 0;
		byte[] stream1, stream2, stream3, decodedBytes = null;

		String startOfStream1 = input_stream[0].substring(0, START_OF_STREAM1);
		int stream1Len = input_stream[0].length();
		String middleOfStream1 = input_stream[0].substring(START_OF_STREAM1, stream1Len - END_OF_STREAM);
		int code1Length = middleOfStream1.length();
		String endOfstream1 = input_stream[0].substring(stream1Len - END_OF_STREAM, stream1Len);
		// Start of stream one is being checked here
		if (stream1Len >= START_OF_STREAM1) {
			checkStartOfStream(startOfStream1, 1);
		} else {
			throw new StartOfStreamException("Start of Stream 1 is incorrect!!!");
		}

		// Check middle and the end of stream one
		if (stream1Len > STREAM_ONE_LENGTH) {
			checkEndOfStreamfirst(endOfstream1, 1);
			dcBalance1 = checkDcBalance(middleOfStream1, 1);
			checkEndOfStreamSecond(endOfstream1, dcBalance1, 1);
		} else {
			throw new EndOfPacketException("End of Stream 1 is incorrect!!!");
		}

		String startOfStream2 = input_stream[1].substring(0, START_OF_STREAM2);
		int stream2Len = input_stream[1].length();
		String middleOfStream2 = input_stream[1].substring(START_OF_STREAM2, stream2Len - END_OF_STREAM);
		int code2Length = middleOfStream2.length();
		String endOfstream2 = input_stream[1].substring(stream2Len - END_OF_STREAM, stream2Len);

		// Check start of stream two
		if (stream2Len >= START_OF_STREAM2) {
			checkStartOfStream(startOfStream2, 2);
		} else {
			throw new StartOfStreamException("Start of Stream 2 is incorrect!!!");
		}

		// Checking middle and end of Second Stream
		if (stream2Len > STREAM_TWO_LENGTH) {
			checkEndOfStreamfirst(endOfstream2, 2);
			dcBalance2 = checkDcBalance(middleOfStream2, 2);
			checkEndOfStreamSecond(endOfstream2, dcBalance2, 2);
		} else {
			throw new EndOfPacketException("End of Stream 2 is incorrect!!!");
		}

		String startOfStream3 = input_stream[2].substring(0, START_OF_STREAM3);
		int stream3Len = input_stream[2].length();
		String middleOfStream3 = input_stream[2].substring(START_OF_STREAM3, stream3Len - END_OF_STREAM3);
		int code3Length = middleOfStream3.length();
		String endOfstream3 = input_stream[2].substring(stream3Len - END_OF_STREAM3, stream3Len);

		// Checking start of third Stream
		if (stream3Len >= START_OF_STREAM3) {
			checkStartOfStream(startOfStream3, STREAM3);
		} else {
			throw new StartOfStreamException("Start of Stream 3 is incorrect!!!");
		}

		// Check middle and end of stream three

		if (stream3Len > STREAM_THREE_LENGTH) {
			checkEndOfStreamfirst(endOfstream3, STREAM3);
			dcBalance3 = checkDcBalance(middleOfStream3, STREAM3);
			checkEndOfStreamSecond(endOfstream3, dcBalance3, STREAM3);
		} else {
			throw new EndOfPacketException("End of Stream 3 is incorrect!!!");
		}

		if (code1Length >= code2Length && code1Length >= code3Length && code2Length >= code3Length) {
			if ((code1Length - code2Length > SIXT_CODE_LENGTH) || (code1Length - code3Length > SIXT_CODE_LENGTH)
					|| (code2Length - code3Length > SIXT_CODE_LENGTH)) {
				throw new DecodeException("Wrong data size!!!");
			}

			stream1 = stream6TtoByte(middleOfStream1, STREAM1);
			stream2 = stream6TtoByte(middleOfStream2, STREAM2);
			stream3 = stream6TtoByte(middleOfStream3, STREAM3);
			int lengthOf3Streams = stream1.length + stream2.length + stream3.length;
			decodedBytes = mergeDatastreams(stream1, stream2, stream3, lengthOf3Streams);
			return decodedBytes;
		} else {
			throw new DecodeException("Wrong data size!!!");
		}

	}

	/**
	 * In this method the three data streams of already decoded bytes will be merged
	 * and returned as single decoded byte array.
	 * <p>
	 * 
	 * @param stream1 first data stream already decoded of bytes
	 * @param stream2 second data stream already decoded of bytes
	 * @param stream3 third data stream already decoded of bytes
	 * @param length  total length of all three streams
	 * @return fullDataStream ,which is single data stream after merging the three
	 *         data streams.
	 */
	public byte[] mergeDatastreams(byte[] stream1, byte[] stream2, byte[] stream3, int length) {
		int index = 0, index1 = 0;
		byte[] fullDataStream = new byte[length];

		for (int i = 0; i < length; i = i + NO_OF_STREAMS) {
			fullDataStream[index] = stream1[index1];
			index++;
			if (index1 < stream2.length) {
				fullDataStream[index] = stream2[index1];
				index++;
			}
			if (index1 < stream3.length) {
				fullDataStream[index] = stream3[index1];
				index++;
			}
			index1++;
		}

		return fullDataStream;
	}

	/**
	 * This method compares the start of stream with valid stream starts of Delimiters
	 *   <p>
	 * An exception will be thrown if the mismatch happens.
	 * 
	 * @param streamEnd    the ending part of transmission.
	 * @param streamNumber Number of stream that is to be checked.
	 */
	public void checkEndOfStreamfirst(String streamEnd, int streamNumber) {
		final String endOfStream1_i = "------++++++";
		final String endOfStream1 = "++++++------";
		final String endOfStream2_i = "----++++====";
		final String endOfStream2 = "++++----====";
		final String endOfStream3_i = "--++==";
		final String endOfStream3 = "++--==";

		if (streamNumber == STREAM1) {
			if (!endOfStream1_i.equals(streamEnd) && !endOfStream1.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 1 is incorrect!!!");
			}
		}
		if (streamNumber == STREAM2) {
			if (!endOfStream2_i.equals(streamEnd) && !endOfStream2.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 2 is incorrect!!!");
			}
		}

		if (streamNumber == STREAM3) {
			if (!endOfStream3_i.equals(streamEnd) && !endOfStream3.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 3 is incorrect!!!");
			}
		}
	}

	/**
	 * This method check end of transmission while taking DC Balance into
	 * consideration
	 * <p>
	 * An exception will be thrown if the mismatch happens
	 * 
	 * @param streamEnd    Ending part of transmission of data stream
	 * @param dcBalance    Dc balance of the given transmission
	 * @param streamNumber Number of stream we are dealing with
	 */
	public void checkEndOfStreamSecond(String streamEnd, int dcBalance, int streamNumber) {
		final String endOfStream1_i = "------++++++";
		final String endOfStream1 = "++++++------";
		final String endOfStream2_i = "----++++====";
		final String endOfStream2 = "++++----====";
		final String endOfStream3_i = "--++==";
		final String endOfStream3 = "++--==";
		if (streamNumber == STREAM1) {
			if (dcBalance == 0 && !endOfStream1_i.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 1 is incorrect!!!");
			}
			if (dcBalance == 1 && !endOfStream1.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 1 is incorrect!!!");
			}
		}
		if (streamNumber == STREAM2) {
			if (dcBalance == 0 && !endOfStream2_i.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 2 is incorrect!!!");
			}
			if (dcBalance == 1 && !endOfStream2.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 2 is incorrect!!!");
			}
		}
		if (streamNumber == STREAM3) {
			if (dcBalance == 0 && !endOfStream3_i.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 3 is incorrect!!!");
			}
			if (dcBalance == 1 && !endOfStream3.equals(streamEnd)) {
				throw new EndOfPacketException("End of Stream 3 is incorrect!!!");
			}
		}
	}

	/**
	 * This method compares start of the stream with constants.
	 * <p>
	 * If mismatch happens StartOfStreamException will be thrown.
	 * 
	 * @param stream       Data stream that will be match with valid start of stream
	 * @param streamNumber Number of Stream that is to be checked
	 */
	public void checkStartOfStream(String stream, int streamNumber) {
		final String startOfStream1 = "+-+-+-+-+-+-+--+";
		final String startOfStream2 = "+-+-+-+-+-+-+-+--+";
		final String startOfStream3 = "+-+-+-+-+-+-+-+-+--+";

		if (streamNumber == STREAM1 && !startOfStream1.equals(stream)) {
			throw new StartOfStreamException("Start of Stream 1 is incorrect!!!");
		}
		if (streamNumber == STREAM2 && !startOfStream2.equals(stream)) {

			throw new StartOfStreamException("Start of Stream 2 is incorrect!!!");
		}
		if (streamNumber == STREAM3 && !startOfStream3.equals(stream)) {
			throw new StartOfStreamException("Start of Stream 3 is incorrect!!!");
		}
	}

	/**
	 * this method converts hexadecimal numbers to decimal numbers
	 * 
	 * @param hexadecimal string is taken as parameter
	 * @return decimal number after conversion from hexadecimal is returned
	 */
	public int convertToDecimal(String hexadecimal) {
		int decimal = Integer.parseInt(hexadecimal, HEXADECIMAL);
		return decimal;
	}

	/**
	 * This method calculates DC Balance of stream.
	 * <p>
	 * if the stream is not made up of multiple of 6 digits , wrong data size
	 * exception will be thrown
	 * 
	 * @param stream       The stream of 6T code whose DC Balance is required
	 * @param streamNumber Number of stream that is to be checked
	 * @return DcBalance will be returned
	 */
	public int checkDcBalance(String stream, int streamNumber) {
		int lengthOfStream = stream.length();
		int dcBalance = 0, check = 0;
		char[] input_array = stream.toCharArray();
		DcCalculator calculate = new DcCalculator();
		for (int i = 0; i < lengthOfStream; i++) {
			if (!(input_array[i] == '+') && !(input_array[i] == '-') && !(input_array[i] == '=')) {
				check++;
			}
		}
		if (check > 0) {
			throw new DecodeException("Wrong data size!!!");

		}
		if (stream.length() % SIXT_CODE_LENGTH == 0) {

			for (int i = 0; i < lengthOfStream; i = i + SIXT_CODE_LENGTH) {
				dcBalance = dcBalance + calculate.dcBalance(stream.substring(i, i + SIXT_CODE_LENGTH));
			}
		} else {
			throw new DecodeException("Wrong data size!!!");
		}
		return dcBalance;
	}

	/**
	 * This method converts the received full data stream into hexadecimal bytes by
	 * looking in the table of 8B6T encoder.
	 * 
	 * @param data         is the 6T code transmission in streams
	 * @param streamNumber could be one of the three streams
	 * @return array of hexadecimal stream will be returned
	 */
	public byte[] stream6TtoByte(String data, int streamNumber) {
		int lengthOfStream = data.length();
		String decodedString = new String();
		String decodedWord = new String();
		decodedString = "";
		Table8B6T table = new Table8B6T();
		int byte_out = 0, index = 0;
		byte[] byteArray = new byte[data.length() / SIXT_CODE_LENGTH];
		if (data.length() % SIXT_CODE_LENGTH == 0) {
			for (int i = 0; i < lengthOfStream; i = i + SIXT_CODE_LENGTH) {
				decodedWord = table.lookInTable(data.substring(i, i + SIXT_CODE_LENGTH));
				if (decodedWord.equals("not found")) {
				 	decodedWord = DataEncoder8B6T.invert(data.substring(i, i + SIXT_CODE_LENGTH));
			  
					decodedWord = table.lookInTable(decodedWord);
					
					if (decodedWord.equals("not found")) {
						 
						throw new DecodeException("Wrong data size!!!");
					}
				}
				decodedString = decodedString + decodedWord;
				byte_out = convertToDecimal(decodedWord);
				byteArray[index] = (byte) byte_out;
				index++;
			}
		} else {
			throw new DecodeException("Wrong data size!!!");
		}
		return byteArray;
	}

}
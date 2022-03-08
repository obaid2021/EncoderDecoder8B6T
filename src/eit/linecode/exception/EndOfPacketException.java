package eit.linecode.exception;

/**
 * This exception occurs if one of the data streams has faulty end delimiters.
 * Accordingly, this is made known to the user with the messages displayed.
 * Depending on the occurrence of the error during decoding, the message End of
 * Stream X is incorrect!!! is generated, where X is the number of the stream
 * containing the error.
 *
 * @author Muhammad Obaid Ullah
 *
 */
@SuppressWarnings("serial")
public class EndOfPacketException extends RuntimeException {
	/**
	 * This method throws EndOfPacketException exception
	 * 
	 * @param message received message will be thrown as EndOfPacketException.
	 */
	public EndOfPacketException(String message) {
		super(message);

	}

}

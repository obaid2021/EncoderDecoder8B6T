package eit.linecode.exception;

/**
 * This exception occurs if one of the data streams has faulty start delimiters.
 * Accordingly, this is made known to the user with the messages displayed.
 * Depending on the occurrence of the error during decoding, the message Start
 * of Stream X is incorrect!!! is generated, where X is the number of the stream
 * containing the error.
 *
 * @author Muhammad Obaid Ullah
 *
 */

@SuppressWarnings("serial")

public class StartOfStreamException extends RuntimeException {
	/**
	 * This method throws StartOfStreamException exception
	 * 
	 * @param message received message will be thrown as StartOfStreamException.
	 */
	public StartOfStreamException(final String message) {
		super(message);
	}
}

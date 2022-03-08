package eit.linecode.exception;

/**
 * This exception occurs if one of the data streams has faulty data.
 * Accordingly, this is made known to the user with the messages displayed.
 * Depending on the occurrence of the error during decoding, the message Wrong
 * data size!!! is generated.
 * 
 * @author Muhammad Obaid Ullah
 *
 */
@SuppressWarnings("serial")

public class DecodeException extends RuntimeException {

	/**
	 * This method throws DecodeException exception
	 * 
	 * @param message received message will be thrown as DecodeException.
	 */
	public DecodeException(final String message) {
		super(message);
	}
}

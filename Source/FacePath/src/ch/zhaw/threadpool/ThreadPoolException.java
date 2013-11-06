package ch.zhaw.threadpool;

/**
 * Just your standard {@link Exception} class to tag exceptions thrown from this package.
 *
 */
public class ThreadPoolException extends Exception {
	public ThreadPoolException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;
}

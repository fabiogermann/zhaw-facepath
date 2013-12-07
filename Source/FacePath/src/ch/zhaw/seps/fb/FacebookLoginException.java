/**
 * Exception, die geworfen wird, wenn der angegebene Login nicht korrekt ist
 * 
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.fb;

public class FacebookLoginException extends java.lang.Exception {
	
	public FacebookLoginException()
    {
        this("Invalid login credentials.");
    }

    public FacebookLoginException(String msg)
    {
        super(msg);
    }

}

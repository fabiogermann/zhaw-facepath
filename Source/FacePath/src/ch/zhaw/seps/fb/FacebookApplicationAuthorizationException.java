/**
 * Exception, die geworfen wird, wenn FacePath keine Berechtigung erhalten hat, um mit dem angegebenen
 * Login arbeiten zu dürfen
 * 
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.fb;

public class FacebookApplicationAuthorizationException extends java.lang.Exception {
	
	public FacebookApplicationAuthorizationException()
    {
        this("Account not authorized to use this app.");
    }

    public FacebookApplicationAuthorizationException(String msg)
    {
        super(msg);
    }

}

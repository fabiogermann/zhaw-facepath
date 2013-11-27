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

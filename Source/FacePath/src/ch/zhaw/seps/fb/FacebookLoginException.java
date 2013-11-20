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

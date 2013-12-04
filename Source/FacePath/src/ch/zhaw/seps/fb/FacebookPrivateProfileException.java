package ch.zhaw.seps.fb;

public class FacebookPrivateProfileException extends java.lang.Exception {
	
	public FacebookPrivateProfileException()
    {
        this("The requested Profile informations are private.");
    }

    public FacebookPrivateProfileException(String msg)
    {
        super(msg);
    }

}

package mocksims.project.backend.exception;

public class MockSimsCustomException extends RuntimeException {
    private final int responseCode;
    private final String responseMessage;

    public MockSimsCustomException(int responseCode, String responseMessage){
        super(responseMessage);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public int getResponseCode(){
        return responseCode;
    }

    public String getResponseMessage(){
        return responseMessage;
    }
}

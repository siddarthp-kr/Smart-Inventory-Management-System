package mocksims.project.backend.exception;

public class MockSimsCustomException extends RuntimeException {
    private final int errorCode;

    public MockSimsCustomException(int errorCode, String responseMessage){
        super(responseMessage);
        this.errorCode = errorCode;
    }

    public int getErrorCode(){
        return errorCode;
    }

}

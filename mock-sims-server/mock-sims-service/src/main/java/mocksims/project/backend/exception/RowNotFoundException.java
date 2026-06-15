package mocksims.project.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RowNotFoundException extends RuntimeException{

    private int errorCode;
    private String errorMessage;

    @Override
    public String getMessage(){
        return this.getErrorMessage();
    }
}

package fiap.sample.login.user.domain.exception;

public class UserConflictException extends Exception {

    public UserConflictException(String message) {
        super(message);
    }

}

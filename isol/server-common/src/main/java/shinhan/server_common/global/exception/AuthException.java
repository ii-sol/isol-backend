package shinhan.server_common.global.exception;

public class AuthException extends Exception {

    private final String message;

    public AuthException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

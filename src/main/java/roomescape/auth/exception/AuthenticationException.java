package roomescape.auth.exception;

import org.springframework.http.HttpStatus;

import roomescape.error.BusinessException;
import roomescape.error.ErrorCode;

public class AuthenticationException extends BusinessException {
    public AuthenticationException() {
        super(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED);
    }
}

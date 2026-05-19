package roomescape.auth.exception;

import org.springframework.http.HttpStatus;

import roomescape.error.BusinessException;
import roomescape.error.ErrorCode;

public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN);
    }
}
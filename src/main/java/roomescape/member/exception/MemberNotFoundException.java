package roomescape.member.exception;

import org.springframework.http.HttpStatus;

import roomescape.error.BusinessException;
import roomescape.error.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다. id=" + id);
    }
}
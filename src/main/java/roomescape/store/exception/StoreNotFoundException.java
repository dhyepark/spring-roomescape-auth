package roomescape.store.exception;

import org.springframework.http.HttpStatus;

import roomescape.error.BusinessException;
import roomescape.error.ErrorCode;

public class StoreNotFoundException extends BusinessException {
    public StoreNotFoundException(Long managerId) {
        super(HttpStatus.NOT_FOUND, ErrorCode.STORE_NOT_FOUND, "매장을 찾을 수 없습니다. managerId=" + managerId);
    }
}
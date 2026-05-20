# spring-roomescape-auth

## 1단계 요구사항

### 로그인
- [x] 사용자는 로그인할 수 있다.
- [x] 로그인에 성공하면 이후 요청에서 같은 사용자를 식별할 수 있어야 한다.
- [x] 로그인에 실패하면 적절한 응답을 반환한다.

### 예약 생성
- [x] 로그인한 사용자는 예약을 생성할 수 있다.
- [x] 예약 생성 시 요청으로 받은 이름이 아니라 로그인한 사용자를 기준으로 예약을 만든다.
- [x] 로그인하지 않은 사용자는 예약을 생성할 수 없다.

### 예약 조회
- [x] 로그인한 사용자는 자신의 예약을 조회할 수 있다.
- [x] 로그인하지 않은 사용자는 인증이 필요한 예약 조회 기능을 사용할 수 없다.

### 인증 공통 처리
- [x] 로그인 여부 확인 로직을 컨트롤러마다 반복하지 않는다.
- [x] 인증이 필요한 API와 필요하지 않은 API를 구분한다.
- [x] 인증 실패 시 일관된 응답을 반환한다.

---

## 2단계 요구사항

### 모바일 로그인
- [x] 모바일 앱 사용자는 로그인할 수 있다.
- [x] 로그인 성공 후 모바일 앱이 이후 요청에서 사용할 인증 정보를 받을 수 있어야 한다.
- [x] 인증 정보는 이후 요청마다 서버가 사용자를 식별할 수 있는 형태여야 한다.

### 모바일 인증 요청
- [x] 모바일 앱은 인증이 필요한 API를 호출할 때 인증 정보를 함께 전달한다.
- [x] 서버는 전달된 인증 정보를 검증한다.
- [x] 인증 정보가 유효하면 로그인한 사용자로 요청을 처리한다.
- [x] 인증 정보가 없거나 유효하지 않으면 요청을 거부한다.

### 웹 인증과의 관계
- 웹 인증 흐름과 모바일 인증 흐름이 어떤 점에서 같은지 설명할 수 있어야 한다.
- 웹 인증 흐름과 모바일 인증 흐름이 어떤 점에서 다른지 설명할 수 있어야 한다.
- 가능한 한 중복된 인증 로직을 줄인다.

---

## 인증 방식

JWT(JSON Web Token) + 쿠키 기반 인증을 사용한다.

- 로그인 성공 시 서버는 JWT를 발급하여 응답 쿠키(`token`)에 담아 반환한다.
- 이후 인증이 필요한 API 요청 시 브라우저가 쿠키를 자동으로 전송하며, 서버는 이를 파싱해 사용자를 식별한다.
- 토큰이 없거나 유효하지 않은 경우 `401 Unauthorized`를 반환한다.

Interceptor와 ArgumentResolver를 사용한다.

- Interceptor가 컨트롤러 진입 전, 사용자 인증 및 인가를 맡는다.
- ArgumentResolver로 인증된 사용자를 파라미터 객체에 매핑한다.

---

## API 명세

### 인증

| Method | URI | 인증 필요 | 설명 |
|--------|-----|----------|------|
| POST | /login | X | 로그인 |

#### POST /login
**Request**
```json
{
  "email": "user@example.com",
  "password": "password"
}
```
**Response** `200 OK`
```
Set-Cookie: token=<JWT>; Path=/; HttpOnly
```

---

### 예약

| Method | URI | 인증 필요 | 설명 |
|--------|-----|----------|------|
| GET | /reservations | O | 내 예약 목록 조회 |
| POST | /reservations | O | 예약 생성 |
| DELETE | /reservations/{id} | O | 예약 취소 |
| PUT | /reservations/{id} | O | 예약 시간 변경 |

#### POST /reservations
**Request**
```json
{
  "themeId": 1,
  "timeId": 1
}
```
**Response** `201 Created`
```json
{
  "id": 1,
  "name": "홍길동",
  "time": {
    "id": 1,
    "startAt": "2025-06-01T10:00",
    "endAt": "2025-06-01T11:00"
  },
  "theme": {
    "id": 1,
    "name": "미궁의 유산",
    "description": "고대 미궁에서 탈출하세요.",
    "imageUrl": "https://example.com/themes/1.png"
  }
}
```

---

### 관리자 예약

| Method | URI | 인증 필요 | 설명 |
|--------|-----|----------|------|
| GET | /admin/reservations | X | 전체 예약 조회 |
| POST | /admin/reservations | X | 예약 생성 (회원 지정) |
| DELETE | /admin/reservations/{id} | X | 예약 삭제 |

#### POST /admin/reservations
**Request**
```json
{
  "memberId": 1,
  "themeId": 1,
  "timeId": 1
}
```

---

### 테마

| Method | URI | 인증 필요 | 설명 |
|--------|-----|----------|------|
| GET | /themes | X | 테마 목록 조회 |
| POST | /themes | X | 테마 생성 |
| DELETE | /themes/{id} | X | 테마 삭제 |
| GET | /themes/best | X | 인기 테마 조회 |

---

### 예약 시간

| Method | URI | 인증 필요 | 설명 |
|--------|-----|----------|------|
| GET | /times | X | 예약 시간 목록 조회 |
| POST | /times | X | 예약 시간 생성 |
| DELETE | /times/{id} | X | 예약 시간 삭제 |
| GET | /times?themeId=&date= | X | 테마·날짜별 예약 가능 시간 조회 |

---

## 에러 응답

모든 에러는 아래 형식으로 반환한다.

```json
{
  "code": "UNAUTHORIZED",
  "message": "인증이 필요합니다."
}
```

| HTTP 상태 | code | 설명 |
|----------|------|------|
| 400 | `INVALID_REQUEST` | 입력값이 유효하지 않음 |
| 400 | `INVALID_FORMAT` | 요청 형식이 올바르지 않음 |
| 400 | `PAST_RESERVATION_CREATE` | 과거 날짜·시간 예약 불가 |
| 400 | `PAST_RESERVATION_UPDATE` | 이미 지난 예약 변경 불가 |
| 400 | `PAST_RESERVATION_CANCEL` | 이미 지난 예약 취소 불가 |
| 401 | `UNAUTHORIZED` | 인증 실패 또는 토큰 없음 |
| 403 | `FORBIDDEN` | 접근 권한 없음 (다른 사용자의 예약 수정/취소 시도) |
| 404 | `MEMBER_NOT_FOUND` | 회원을 찾을 수 없음 |
| 404 | `RESERVATION_NOT_FOUND` | 예약을 찾을 수 없음 |
| 404 | `TIME_NOT_FOUND` | 예약 시간을 찾을 수 없음 |
| 404 | `THEME_NOT_FOUND` | 테마를 찾을 수 없음 |
| 409 | `DUPLICATE_RESERVATION` | 중복 예약 불가 |
| 409 | `TIME_IN_USE` | 해당 시간에 예약이 존재하여 삭제 불가 |
| 500 | `INTERNAL_SERVER_ERROR` | 서버 오류 |

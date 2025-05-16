# 🛡️ SpringGuard

- SpringBoot 기반 인증/인가 API 서버입니다.

---

## 📌 프로젝트 목적

- Spring Security와 JWT 기반 인증 구현
- Swagger(OpenAPI) 문서를 통한 API 명세 자동화
- AWS EC2를 활용한 배포 실습

---

## 🌐 배포 주소 

### GitHub Repository 링크
- **GitHub Repository**: [GitHub Repository](https://github.com/JH1Yoon/springboot-jwt-auth)

### Swagger UI
- **Swagger UI URL**: [http://3.39.209.205:8080/swagger](http://3.39.209.205:8080/swagger)

### AWS EC2에서 실행 중인 API 엔드포인트
- **API 엔드포인트 URL**: [http://3.39.209.205:8080](http://3.39.209.205:8080)

---

## API 명세서

---
### 목차
- **예시 엔드포인트**:
    - 일반 사용자 회원가입: `POST /signup`
    - 관리자 회원가입: `POST /signup/admin`
    - 로그인: `POST /login`
    - 관리자 권한 부여: `PATCH /admin/users/{userId}/roles`

---

### 1. 회원가입 (Sign Up)

- **POST** `/signup`

#### 요청 예시:
##### RequestBody
```json
{
  "username": "JIN HO",
  "password": "12341234",
  "nickname": "Mentos"
}
```
#### 응답 예시:
``` json
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [
    {
      "role": "USER"
    }
  ]
```

#### 상태 코드:
- 201 Created - 성공
- 409 Conflict - 이미 존재하는 유저

---

### 2. 로그인 (Login)

- **POST** `/login`

#### 요청 예시:
##### RequestBody
```json
{
  "username": "adminUser",
  "password": "adminPass123"
}
```
#### 응답 예시:
``` json
{
  "token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL"
}
```

#### 상태 코드:
- 200 OK - 성공
- 401 Conflict - 이미 존재하는 유저

---

### 3. 관리자 권한부여 (grantAdminRole)

- **POST** `/admin/users/{userId}/roles`

#### 요청 예시:
##### PathVariable
```

 userID : 1
```
##### Authorization
```
 eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL
```

#### 응답 예시:
``` json
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [
    {
      "role": "Admin"
    }
  ]
}
```

#### 상태 코드:
- 200 OK - 성공
- 403 Forbidden - 권한 부족 (접근 제한)

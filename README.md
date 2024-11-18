
# Common Library (common-lib)

**common-lib**는 마이크로서비스 아키텍처(MSA) 환경에서 다양한 공통 기능을 제공하기 위해 설계된 라이브러리입니다. 
이 라이브러리는 **JDK 21**을 기반으로 개발되었으며, 다음과 같은 공통 기능을 포함합니다.

---

## 📚 주요 기능

### 1. **ResponseWrapper**
- 모든 컨트롤러에서 반환되는 **Response**를 공통 처리합니다.
- **통일된 API 응답 형식** 제공:
  - **필드**:
    - `code`: 상태 코드 (e.g., 200, 400).
    - `message`: 응답 메시지.
    - `data`: 실제 응답 데이터.


---

### 2. **내부 API Call을 위한 공통 클래스**
- **MSA 구조**에서 서비스 간의 HTTP 통신을 처리하기 위한 공통 클래스입니다.
- **기능**:
  - RestClient를 사용한 HTTP 요청 처리.
  - JWT 토큰 자동 추가.


---

### 3. **JWT Token Request Filter**
- **JWT 인증 필터**를 통해 요청의 헤더에서 JWT 토큰을 검증하고 인증 정보를 설정합니다.
- **기능**:
  - 토큰의 유효성 검증.
  - Spring Security Context에 인증 정보 추가.


---

### 4. **CommonExceptionHandler**
- 공통 예외 처리를 위한 **ControllerAdvice**입니다.
- **기능**:
  - 애플리케이션 전역에서 발생하는 예외를 포착하여 처리.
  - 통일된 에러 응답 형식 제공.


---

### 5. **명세 패턴 기반 Validator**
- 공통적인 검증 로직을 구현합니다.
- **기능**:
  - 커스텀 Validator를 사용하여 명세 패턴 기반 검증 지원.

---

### 6. **Spring Security Context Help Util**
- Spring Security Context에서 현재 유저 정보 및 권한(Role)을 가져오는 유틸리티.
- **기능**:
  - 인증된 사용자 ID 가져오기.
  - 인증된 사용자 역할(Role) 가져오기.

---

### 7. **Enum Code-Description 방식 및 JPA Converter**
- **기능**:
  - Enum의 `code`와 `desc`를 함께 관리하여 코드 가독성 및 응답 일관성 향상.
  - JPA Entity에서 Enum 값을 문자열로 저장하고 읽을 수 있도록 **Converter** 구현.

---

### 8. **Custom ObjectMapper**
- Jackson의 ObjectMapper를 커스터마이징하여 Enum의 `code`, `name`, `desc`를 포함한 응답을 제공합니다.
- **기능**:
  - API 응답에서 Enum을 더 읽기 쉽게 표현.

---

## 🛠️ 설치 및 실행

### 1. 의존성 설치
```bash
./gradlew build
```

### 2. 라이브러리 사용
- Gradle 프로젝트에서 common-lib을 추가하여 사용.

---

## 📚 기술 스택
- **언어**: Java 21 (Temurin JDK 21)
- **프레임워크**: Spring Boot

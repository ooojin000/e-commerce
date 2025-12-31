## 설계 및 구현 시 고민했던 부분

### 1) 주문 테이블 모델링
요구사항이 '상품을 조회/주문/취소하고 재고를 관리'하는 범위이기 때문에 Orders 테이블에 product_id와 quantity를 두어 주문 1건이 단일 상품에 대한 주문을 하도록 단순화했습니다. <br>
추후 한 건의 주문에 여러 상품에 대한 주문을 요구할 경우 `OrderItems` 테이블을 추가하여 확장할 수 있습니다. <br>

### 2) 주문 취소
주문 취소 시 주문을 물리 삭제하지 않고 Enum을 사용하여 status를 변경(ORDERED -> CANCELEDE)하는 방식으로 구현했습니다. <br>
이를 통해 주문 생성과 주문 취소 이력을 보존하고 '이미 취소된 주문을 재취소' 같은 비즈니스 규칙을 명확하게 검증/예외 처리할 수 있도록 했습니다. <br>
또한 취소 시점을 기록하기 위해 canceled_at을 사용하여 null이었다가 주문 취소 시 취소한 시간을 저장하도록 하였습니다. <br>

### 3) 별도의 사용자 테이블 없이 user_id만 받음
인증/인가가 생략되어 User 테이블 없이 userId를 요청 값으로 전달받는다고 가정했습니다.<br>
주문 생성은 요청 데이터 자체가 주문 정보이므로 RequestBody에 userId를 포함했고, <br>
사용자별 주문 조회는 자원 식별이 명확해 GET `/api/users/{userId}/orders` 형태로 설계했습니다.<br>
주문 취소는 주문 자원 `/api/orders/{orderId}/cancel` 과 사용자 식별이 함께 필요하여 orderId는 PathVariable, userId는 Header(USER-ID)로 전달받아 검증했습니다.<br>

<br><br>

## 데이터베이스 ERD 다이어그램
<img width="343" height="444" alt="image" src="https://github.com/user-attachments/assets/e57141b9-0707-4898-ba1d-a15529033a31" />

- Products는 상품/재고를 관리합니다.
- Orders는 사용자별 단일 상품 주문 이력(수량, 상태)을 관리합니다.
- Products(1)–Orders(N) 관계로 연결합니다.

<br>
<br>

## 프로젝트 실행 방법

### 프로젝트 실행 환경
- Spring Boot 3.5.9
- Java 21
- Gradle
- JPA
- H2

<br>

### 로컬 실행

#### 터미널
`./gradlew bootRun`

#### IntelliJ
EcommerceApplication(메인 클래스) 우클릭 → Run

- 애플리케이션이 실행되면 기본 포트는 `8080`입니다.
- Base URL: http://localhost:8080

<br>

### 초기 상품 데이터
- 애플리케이션 실행 시 `data.sql`을 통해 테스트용 상품 데이터가 자동으로 입력됩니다.

<br>

### H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:ecommercedb
- Username: sa
- Password: (빈 값)

<br>

### API 문서 (Swagger)
- http://localhost:8080/swagger-ui/index.html

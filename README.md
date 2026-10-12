# 반려동물 미용 예약 시스템

반려동물 미용 업체의 예약을 관리하는 REST API 서버입니다.
동시성 제어(Optimistic Lock)를 적용하여 같은 시간대에 중복 예약이 발생하지 않도록 설계했습니다.

## 기술 스택

- Java 17
- Spring Boot 4.1
- Spring Data JPA
- MySQL 8.0
- Docker
- Gradle
- JUnit 5

## 주요 기능

- 회원 가입 / 조회 (CUSTOMER, OWNER 역할 구분)
- 업체 등록 / 조회
- 반려동물 등록 / 조회
- 서비스 등록 / 조회
- 타임슬롯 생성 / 조회
- **예약 생성 / 확정 / 완료 / 취소** (상태 머신 기반)
- **동시성 제어** (Optimistic Lock + Unique 제약조건)

## ERD

```
Member (1) ──── (N) Pet
Member (1) ──── (N) Shop
Shop   (1) ──── (N) Service
Shop   (1) ──── (N) TimeSlot
Reservation ───── Member (N:1)
Reservation ───── Pet (N:1)
Reservation ───── Service (N:1)
Reservation ───── TimeSlot (1:1)
```

## 예약 상태 흐름

```
PENDING ──→ CONFIRMED ──→ COMPLETED
   │             │
   └─── CANCELED ←┘
```

- `PENDING` → `CONFIRMED`: 업체가 예약 승인
- `CONFIRMED` → `COMPLETED`: 서비스 완료 처리
- `PENDING` / `CONFIRMED` → `CANCELED`: 예약 취소 (사유 입력)

## API 명세

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/api/members` | 회원가입 |
| GET | `/api/members/{id}` | 회원 조회 |
| POST | `/api/shops` | 업체 등록 |
| GET | `/api/shops/{id}` | 업체 조회 |
| POST | `/api/pets` | 반려동물 등록 |
| GET | `/api/pets/{id}` | 반려동물 조회 |
| POST | `/api/time-slots` | 타임슬롯 생성 |
| GET | `/api/time-slots/{id}` | 타임슬롯 조회 |
| POST | `/api/reservations` | 예약 생성 |
| PATCH | `/api/reservations/{id}/confirm` | 예약 확정 |
| PATCH | `/api/reservations/{id}/complete` | 예약 완료 |
| PATCH | `/api/reservations/{id}/cancel` | 예약 취소 |

## 동시성 처리

같은 타임슬롯에 여러 사용자가 동시에 예약을 요청할 경우, 중복 예약이 발생할 수 있습니다.

### 문제 상황

```
사용자A: isAvailable 확인 → true → 예약 생성 ✅
사용자B: isAvailable 확인 → true → 예약 생성 ✅  ← 중복!
```

확인(SELECT)과 변경(UPDATE) 사이에 다른 요청이 끼어드는 Race Condition이 발생합니다.

### 해결 방법

**1차 방어 — Optimistic Lock (`@Version`)**

```java
@Entity
public class TimeSlot {
    @Version
    private Integer version;
}
```

JPA가 UPDATE 시 `WHERE version = ?` 조건을 자동 추가합니다.
먼저 저장한 트랜잭션이 version을 변경하면, 나중 트랜잭션은 version 불일치로 실패합니다.

**2차 방어 — Unique 제약조건**

```java
@JoinColumn(name = "time_slot_id", unique = true)
private TimeSlot timeSlot;
```

DB 레벨에서 하나의 타임슬롯에 하나의 예약만 허용합니다.

### Optimistic Lock을 선택한 이유

| | Optimistic Lock | Pessimistic Lock |
|--|----------------|-----------------|
| 방식 | 충돌 감지 후 실패 | SELECT FOR UPDATE로 행 잠금 |
| 성능 | 충돌이 적으면 빠름 | 항상 잠금 대기 |
| 적합한 상황 | 충돌이 드문 경우 | 충돌이 빈번한 경우 |

예약 시스템은 같은 시간대에 동시 요청이 빈번하지 않으므로 Optimistic Lock이 적합합니다.

## 설계 특징

### 도메인 모델 패턴

상태 전이 로직을 Service가 아닌 Entity 내부에 배치했습니다.

```java
// Reservation.java
public void confirm() {
    if (this.status != ReservationStatus.PENDING) {
        throw new IllegalArgumentException("승인 대기 상태에서만 확정할 수 있습니다.");
    }
    this.status = ReservationStatus.CONFIRMED;
}
```

- 비즈니스 규칙이 엔티티에 응집되어 유지보수 용이
- Service는 엔티티 조회 + 도메인 메서드 호출만 담당

### 정적 팩토리 메서드

Setter 없이 객체를 생성하여 불변성을 유지합니다.

```java
public static Reservation createReservation(Member member, Pet pet,
                                            Service service, TimeSlot timeSlot) {
    Reservation reservation = new Reservation();
    reservation.member = member;
    reservation.status = ReservationStatus.PENDING;  // 기본값 자동 설정
    return reservation;
}
```

## 실행 방법

### 1. MySQL 실행 (Docker)

```bash
docker run -d \
  --name pet-reservation-mysql \
  -e MYSQL_ROOT_PASSWORD=<비밀번호> \
  -e MYSQL_DATABASE=pet_reservation \
  -p 3307:3306 \
  -v pet-reservation-data:/var/lib/mysql \
  mysql:8.0
```

### 2. application.yaml 설정

`src/main/resources/application.yaml` 파일을 생성합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/pet_reservation
    username: root
    password: <비밀번호>
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        show_sql: true
        format_sql: true
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

## 프로젝트 구조

```
src/main/java/com/pet/reservation/
├── controller/          # REST API 컨트롤러
│   ├── ReservationController
│   ├── MemberController
│   ├── ShopController
│   ├── PetController
│   └── TimeSlotController
├── service/             # 비즈니스 로직
│   ├── ReservationService
│   ├── MemberService
│   ├── ShopService
│   ├── PetService
│   └── TimeSlotService
├── repository/          # 데이터 접근 계층
│   ├── ReservationRepository
│   ├── MemberRepository
│   ├── ShopRepository
│   ├── PetRepository
│   ├── ServiceRepository
│   └── TimeSlotRepository
├── domain/              # 엔티티 + enum
│   ├── Member, Pet, Shop, Service
│   ├── TimeSlot, Reservation
│   ├── MemberRole, ReservationStatus
└── dto/                 # 요청 DTO
    ├── CreateReservationRequest
    ├── CancelReservationRequest
    ├── CreateMemberRequest
    ├── CreateShopRequest
    ├── CreatePetRequest
    └── CreateTimeSlotRequest
```

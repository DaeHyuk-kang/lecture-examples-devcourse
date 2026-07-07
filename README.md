# 태그별 강의 내용 정리

이 레포는 `main`에는 커밋 없이(README만), 각 강의/단계 스냅샷을 **태그**로만 보관합니다.
`git checkout <태그명>`으로 해당 시점 코드를 바로 볼 수 있습니다.

---

## ddd/ — DDD(도메인 주도 설계) 강의

하나의 모놀리식 프로젝트(유저/셀러/상품/장바구니/주문/결제 6개 도메인이 뒤섞인 `tangledmonolith`)를
단계적으로 DDD 원칙에 따라 분리해나가는 강의. `ddd/` 폴더에 스냅샷 존재.

| 태그 | 내용 |
| --- | --- |
| `lectures/ddd/step2` | 최초 상태. 6개 도메인이 하나의 모놀리식 프로젝트에 뒤섞여 있는 "사전 과제" 코드(도메인 간 얽힌 연관관계를 직접 분석해보는 단계) |
| `lectures/ddd/step3-done` | DDD 5대 요소 적용: 트랜잭션 스크립트 방식의 문제 인식 → Money 값 객체(VO) 도입 → 애그리거트 루트 적용(Order ↔ OrderItem) → 도메인 서비스 도입 → Entity에 비즈니스 불변식/캡슐화 |
| `lectures/ddd/step4` | 4계층 분리 전 시작 코드 |
| `lectures/ddd/step4-done` | 도메인별 DDD 4계층(presentation/application/domain/infrastructure) 분리 + 의존성 역전(DIP)으로 도메인의 인프라 의존 제거. 주문 도메인은 강사가 진행, 나머지는 수강생 실습 |
| `lectures/ddd/step5` | 도메인 간 소통(연동) 적용 전 시작 코드 |

---

## settlement/ — Spring Batch 대용량 정산 처리 강의

정산(Settlement) 배치를 소재로 대용량 데이터 처리, 멱등성, 실패 재시작, 멀티스레드/파티셔닝 한계를
다루는 강의. 아키텍처 분리가 아니라 **Spring Batch 처리 패턴**이 주제. `settlement/` 폴더에 스냅샷 존재.

| 태그 | 내용 |
| --- | --- |
| `lectures/settlement/step1` | 정산 배치 초기 세팅. `NaiveSettlementService`로 `findAll` 전량 적재하는 안티패턴 데모(대용량 시 OOM). 대용량 시드 데이터 생성기 포함 |
| `lectures/settlement/step2-done` | Spring Batch Chunk 지향 파이프라인 구현 (Reader→Processor→Writer). 청크 크기만큼만 메모리 사용 → naive 방식(OOM)과 대비해서 검증 |
| `lectures/settlement/step3-done` | 멱등성(Idempotency)과 실패 재시작(Restartability). 50% 지점 장애 주입 시나리오 + 재실행 시 중복 없이 스킵(멱등성) + 실패 지점부터 이어서 재개(재시작) |
| `lectures/settlement/step4` | Step3 상태와 동일 (Step4 실습 시작 코드) |
| `lectures/settlement/step4-done` | 멀티스레드 처리의 한계 규명: Spring Batch 6에서는 표준 Reader가 thread-safe라 데이터 손상은 없지만, 읽기가 직렬화되어 스레드를 늘려도 처리량이 천장에 막힘(실측 ~2.7배). 게다가 멀티스레드는 체크포인트 저장이 꺼져(`saveState(false)`) 재시작 시 처음부터 다시 읽어야 함. **파티셔닝**으로 처리량 한계와 재시작 문제를 동시에 해결(실측 ~5.3배) |

---

## msa/ — 모놀리스 → MSA(마이크로서비스) 전환 강의

`settlement` 강의가 끝난 모놀리스(`tangledmonolith`)를 실제 마이크로서비스로 쪼개나가는 강의.
패키지 경계를 Gradle 모듈 경계로 승격 → 서비스 분리 → Eureka 서비스 디스커버리 → API Gateway 순으로 진행.
`msa/` 폴더에 스냅샷 존재.

| 태그 | 내용 |
| --- | --- |
| `lectures/msa/step1` | 시작 코드. settlement 강의 마지막 상태(모놀리스)와 동일 |
| `lectures/msa/step1-done` | 멀티 모듈 전환: 단일 모놀리스 → common + 서비스 7개(product/payment/user/seller/cart/order/settlement). 서비스별 포트·DB 분리, 도메인 간 호출은 Port + RestClient로 전환, 패키지 경계를 컴파일 타임에 강제 |
| `lectures/msa/step2` | Step1 결과 상태와 동일 (Step2 실습 시작 코드) |
| `lectures/msa/step2-done` | 전 도메인에 Eureka 서비스 디스커버리 적용. user/seller/cart/settlement까지 Eureka 클라이언트로 등록하고, cart→product 호출도 이름 기반(@LoadBalanced) 방식으로 전환 |
| `lectures/msa/step3` | Step2 결과 상태와 동일 (Step3 실습 시작 코드) |
| `lectures/msa/step3-done` | API Gateway로 전체 도메인 라우팅 구성. `gateway-server`(Spring Cloud Gateway WebFlux, :8000) 추가, 7개 도메인 전체 라우트를 Eureka 기반 `lb://`로 이름 해석, 공통 응답 헤더 필터 적용 |

---

## log-statistics/ — 로그 통계 분석 프로그램 (별도 특강)

출처: `Programmers-Hyune-c/devcourse-lecture-25` 레포의 `develop` 브랜치.
Open API 호출 웹 로그를 분석해 통계를 뽑아내는 프로그램을 단계적으로 확장하는 개인 과제.
특강 내용은 실제 현업에서 AI의 입지가 어느 정도인지 체감할 수 있었음.

| 태그 | 내용 |
| --- | --- |
| `devcourse-lecture-25/develop` | STEP1~5 전체가 누적된 최종 상태 (아래는 각 단계별 요구사항) |

- **STEP1**: 로그 파일(`kokoa.log`, bracket 형식 `[상태코드][URL][웹브라우저][호출시간]`)을 분석해 ①최다 호출 APIKEY ②호출 수 상위 3개 API Service ID ③웹브라우저별 사용 비율을 파일로 출력
- **STEP2**: 로그 분석 결과를 반환하는 API 엔드포인트 추가
- **STEP3**: 새로운 로그 포맷(`maver.log`, JSON/Logstash 형태) 지원 추가. 기존 kokoa(bracket) 포맷과 병행 지원
- **STEP4**: 처리 진행 상황/에러를 알 수 없던 문제 해결 (관측성 개선)
- **STEP5**: 더 다양한 로그 패턴 확장을 고려한 모듈 분리 (`log-parser`가 문자열 분석·기록 담당, `spot-statistics`가 통계 작성 담당, `LogOutputPort`로 저장소 교체 가능하도록 설계)

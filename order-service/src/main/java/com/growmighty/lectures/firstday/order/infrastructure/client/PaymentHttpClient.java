package com.growmighty.lectures.firstday.order.infrastructure.client;

import com.growmighty.lectures.firstday.common.exception.ServiceUnavailableException;
import com.growmighty.lectures.firstday.order.application.port.PaymentPort;
import com.growmighty.lectures.firstday.order.application.port.dto.PaymentResult;
import com.growmighty.lectures.firstday.order.infrastructure.client.dto.ApiResponseBody;
import com.growmighty.lectures.firstday.order.infrastructure.client.dto.PayBody;
import com.growmighty.lectures.firstday.order.infrastructure.client.dto.PaymentApiData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHttpClient implements PaymentPort {

    private final RestClient paymentRestClient;
    private final CircuitBreakerFactory circuitBreakerFactory;   // 추상화 타입으로 주입 — 구현체(Resilience4j)를 모른 채 쓴다

    @Override
    public PaymentResult pay(BigDecimal amount) {
        // "payment" 라는 이름의 차단기를 통과시켜 호출한다.
        // 이름이 곧 신원 — 성공/실패 통계와 상태(CLOSED/OPEN/…)가 이 이름 아래에 쌓인다.
        return circuitBreakerFactory.create("payment").run(
            () -> callPay(amount),   // ① 본 호출 (평소엔 이게 실행된다)
            this::payFallback);      // ② 실패했거나, 차단기가 OPEN 이라 거부됐을 때 (plan B)
    }

    // 기존 pay() 의 몸통 — 그대로 옮겨왔을 뿐, 한 글자도 안 바뀌었다.
    private PaymentResult callPay(BigDecimal amount) {
        ApiResponseBody<PaymentApiData> body = paymentRestClient.post()
            .uri("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .body(new PayBody(amount))
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });

        PaymentApiData data = body.data();
        return new PaymentResult(data.paymentId(), data.amount(), data.status());
    }

    private PaymentResult payFallback(Throwable cause) {
        // 여기 들어오는 경로는 두 가지다.
        // ① 결제 호출이 실제로 실패/타임아웃 났다 → 차단기가 이 실패를 '기록'한다
        // ② 차단기가 OPEN 이라 호출 자체가 거부됐다 (CallNotPermittedException)
        //    → payment 에는 요청이 "가지도 않는다". 스레드도 안 잡힌다. 이것이 빠른 실패.
        log.warn("결제 호출 실패 → fallback 실행. 원인: {}", cause.toString());

        // 결제는 '가짜 성공'을 만들 수 없는 핵심 동작이다. 대충 성공시키면 돈이 증발한다.
        // 그래서 결제의 우아한 실패란 — 빠르고(스레드를 안 잡고), 정직하고(503), 친절한(사람의 언어) 실패다.
        throw new ServiceUnavailableException("결제 서비스가 일시적으로 응답하지 않습니다. 잠시 후 다시 시도해 주세요.");
    }

    @Override
    public void cancel(Long paymentId) {
        // 🏋️ 과제 1: cancel 도 같은 차단기("payment")를 통과하도록 바꿔 보자.
        paymentRestClient.post()
            .uri("/payments/{paymentId}/cancel", paymentId)
            .retrieve()
            .toBodilessEntity();
    }
}

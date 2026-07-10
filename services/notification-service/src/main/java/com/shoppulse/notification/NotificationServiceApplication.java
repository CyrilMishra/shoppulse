package com.shoppulse.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Three integration styles side by side — feel the difference (Block 2 N1, Block 5 N2-N3):
 *
 *  N1  EVENT: @KafkaListener on "order.confirmed" -> log "email sent to ...".
 *      Push, decoupled, replayable, consumer controls pace.
 *
 *  N2  WEBHOOK vs POLLING (both against payment-service):
 *      - POST /webhooks/payment receives the payment webhook; verify the
 *        X-Signature HMAC-SHA256 header before trusting the payload
 *        (compute HMAC over the raw body with the shared secret, constant-time compare).
 *        Then break it: stop this service, complete a payment, see the event LOST
 *        -> this is why real webhook senders (Stripe) retry with backoff and
 *        why receivers must be idempotent (store processed event IDs).
 *      - @Scheduled(fixedDelay = 10000) poller calling GET /api/payments/recent
 *        as the fallback. Compare: latency, load, missed events, complexity.
 *      Notification channels (EMAIL/SMS/PUSH) = STRATEGY PATTERN: interface
 *      NotificationChannel { void send(...); } with three implementations,
 *      selected via a Map<ChannelType, NotificationChannel> — your design-patterns story.
 *
 *  N3  SOAP (45 min, legacy talking point): XSD for GetOrderStatusRequest/Response
 *      -> @Endpoint + @PayloadRoot handler -> WSDL auto-generated at
 *      /ws/orders.wsdl. Test with curl POSTing an XML envelope.
 *      Interview: WSDL = machine-readable contract, WS-Security, why enterprises
 *      still run SOAP, REST vs SOAP vs gRPC trade-offs.
 */
@EnableScheduling
@SpringBootApplication
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}

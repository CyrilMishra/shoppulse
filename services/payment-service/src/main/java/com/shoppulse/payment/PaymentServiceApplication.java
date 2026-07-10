package com.shoppulse.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Payments in DynamoDB + retry queue in SQS (LocalStack). Build order (Block 2, Y1-Y4):
 *
 *  Y1  DynamoDbClient + SqsClient beans with endpointOverride from application.yml
 *      and dummy credentials. Create the "payments" table on startup if absent:
 *      partition key = orderId (S). Interview: partition key vs sort key,
 *      single-table design, why no joins, RCU/WCU vs on-demand, GSI vs LSI.
 *  Y2  Kafka listener on "order.inventory-reserved" -> simulate payment
 *      (random 80% success) -> PutItem into DynamoDB -> publish
 *      "payment.completed" or "payment.failed". (The saga's middle leg.)
 *  Y3  On transient failure, SendMessage to SQS queue "payment-retry";
 *      a @Scheduled poller receives, retries, deletes on success.
 *      Learn: visibility timeout (message invisible while processing, reappears
 *      if you crash), DLQ after maxReceiveCount, at-least-once => your handler
 *      MUST be idempotent (check if payment already exists before charging!).
 *  Y4  Webhook OUT: after payment completes, POST a signed JSON payload to
 *      notification-service /webhooks/payment (HMAC-SHA256 signature header —
 *      that's how Stripe/GitHub webhooks work; verify it on the receiving side).
 *
 * Create the queue first:
 *   docker compose exec localstack awslocal sqs create-queue --queue-name payment-retry
 */
@SpringBootApplication
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}

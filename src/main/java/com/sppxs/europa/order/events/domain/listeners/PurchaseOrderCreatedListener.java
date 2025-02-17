package com.sppxs.europa.order.events.domain.listeners;

import com.sppxs.europa.order.events.domain.PurchaseOrderCreatedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PurchaseOrderCreatedListener {

    //@EventListener // -- method will get executed even if the save/update operation was rolled back
    @TransactionalEventListener(classes = PurchaseOrderCreatedEvent.class) // -- will execute only if the transaction is committed
    @Async
    public void handleMyDomainEvent(PurchaseOrderCreatedEvent event) {
        System.out.println("PurchaseOrderCreatedEvent - Guid: " + event.getPurchaseOrderGuid() + " | Amount: " + event.getAmount());
        //Asynchronously send an email to the user
        //Asynchronously send a SQS message notification
        //What if the Publish event fails?? How to handle it??
        // - Retry mechanism
        // - Dead letter queue
        // - Circuit breaker - Write to a log file
        // - Error handling - Write to a log file
        // - And a Job can run later to retry the failed events

        /*
        @EventListener
        public void handleMyDomainEvent(MyDomainEvent event) {
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl("your-sqs-queue-url")
                    .withMessageBody(convertToJson(event));

            amazonSQS.sendMessageAsync(sendMessageRequest, new SendMessageHandler());
        }

        private String convertToJson(Object event) {
            // Implement JSON conversion logic here (e.g., using Jackson or Gson)
            return "{\"entityId\":" + ((MyDomainEvent)event).getEntityId() + ", \"message\":\"" + ((MyDomainEvent)event).getMessage() + "\"}";
        }

        private static class SendMessageHandler implements AmazonSQSAsyncHandler<SendMessageRequest, SendMessageResult> {
            @Override
            public void onSuccess(SendMessageRequest request, SendMessageResult result) {
                // Handle success (e.g., logging)
                System.out.println("Message sent to SQS: " + result.getMessageId());
            }

            @Override
            public void onError(Exception exception) {
                // Handle error (e.g., logging, retry)
                System.err.println("Error sending message to SQS: " + exception.getMessage());
            }
        }

        @Configuration
        public class SQSConfig {
            @Bean
            public AmazonSQSAsync amazonSQS() {
                return AmazonSQSAsyncClientBuilder.defaultClient();
            }
        }
         */


    }
}

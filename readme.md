# Europa

## Description
This pull request integrates payment, shipping, and order functionalities across multiple modules. It enhances the data setup process by adding payment and shipment creation methods, introduces new REST API endpoints for payment processing and order creation, and implements asynchronous operations with thread pools and CompletableFutures. Domain events, new JPA entities, DTOs, and repository interfaces have been added to support payment and shipping processes, while existing entities such as PurchaseOrder have been updated with additional fields.

## Goals
- [Publish a DomainEvent-1](https://courses.baeldung.com/courses/1295711/lectures/30604048)
- [Publish a DomainEvent-2](https://medium.com/@padiahrohit/publishing-domain-events-spring-data-5075505f3f27)
- [Aggregates](https://www.baeldung.com/spring-data-ddd)
- [Publish to SQS](https://anand-guptaa.medium.com/event-driven-architecture-using-aws-sqs-and-spring-boot-d29fc3b1b25b)
- [SNS](https://docs.aws.amazon.com/AmazonS3/latest/userguide/ways-to-add-notification-config-to-bucket.html)
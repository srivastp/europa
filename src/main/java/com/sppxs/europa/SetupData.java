package com.sppxs.europa;

import com.sppxs.europa.order.entity.Item;
import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import com.sppxs.europa.order.enums.POStatus;
import com.sppxs.europa.order.repository.ItemRepository;
import com.sppxs.europa.order.repository.PurchaseOrderRepository;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.entity.PaymentTypeDetail;
import com.sppxs.europa.payment.entity.Transaction;
import com.sppxs.europa.payment.repository.PaymentRepository;
import com.sppxs.europa.payment.repository.PaymentTypeDetailRepository;
import com.sppxs.europa.shipping.entity.Address;
import com.sppxs.europa.shipping.entity.Shipment;
import com.sppxs.europa.shipping.entity.ShippingDetails;
import com.sppxs.europa.shipping.repository.AddressRepository;
import com.sppxs.europa.shipping.repository.ShippingDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static com.sppxs.europa.payment.enums.PaymentStatus.APPROVED;
import static com.sppxs.europa.payment.enums.Transaction.TransactionType.CREDIT;
import static com.sppxs.europa.payment.enums.Transaction.TransactionType.DEBIT;

@Component
@Slf4j
public class SetupData implements CommandLineRunner {
    private final ItemRepository itemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PaymentTypeDetailRepository paymentTypeDetailRepository;
    private final PaymentRepository paymentRepository;
    private final AddressRepository addressRepository;
    private final ShippingDetailsRepository shippingDetailsRepository;

    @Autowired
    public SetupData(ItemRepository itemRepository,
                     PurchaseOrderRepository purchaseOrderRepository,
                     PaymentTypeDetailRepository paymentTypeDetailRepository,
                     PaymentRepository paymentRepository,
                     AddressRepository addressRepository,
                     ShippingDetailsRepository shippingDetailsRepository) {
        this.itemRepository = itemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.paymentTypeDetailRepository = paymentTypeDetailRepository;
        this.paymentRepository = paymentRepository;
        this.addressRepository = addressRepository;
        this.shippingDetailsRepository = shippingDetailsRepository;
    }

    @Override
    //ø@Transactional
    public void run(String... args) {
        System.out.println("Intializing data");

        //Create 10 Items for me based in Item entity
        if (itemRepository.count() == 0) {
            Set<Item> items = createItems();
            itemRepository.saveAll(items);
            System.out.println(">>>> Loaded Items");
        }

        if (purchaseOrderRepository.count() == 0) {
            Set<PurchaseOrder> pos = new HashSet<>();
            for (int i = 0; i < 3; i++) {
                pos.add(createPurchaseOrder());
            }
            purchaseOrderRepository.saveAll(pos);
            System.out.println(">>>> Loaded Purchase Orders with OrderItems");
        }

        if (shippingDetailsRepository.count() == 0) {
            PurchaseOrder po = createPurchaseOrder();
            po = purchaseOrderRepository.saveAndFlush(po);
            Payment payment = createPayment(po);
            paymentRepository.save(payment);
            System.out.println(">>>> Loaded Payments with Transactions");

            ShippingDetails sd = createShipment(po);
            shippingDetailsRepository.save(sd);
            System.out.println(">>>> Loaded Shipping Details");
        }
    }

    private static Set<Item> createItems() {
        Set<Item> items = new HashSet<>();
        for (int i = 1; i < 10; i++) {
            Item item = new Item();
            item.setName("Item - " + (i));
            item.setSku(482L * i);
            item.setDescription("Desc - " + (i));
            item.setUnitPrice(BigDecimal.valueOf((i + 1) * 4.28));
            item.setQuantity((i + 1) * 13);
            item.setCreatedAt(Instant.now());
            item.setUpdatedAt(Instant.now());
            items.add(item);
        }
        return items;
    }

    private PurchaseOrder createPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setGuid(UUID.randomUUID().toString());
        purchaseOrder.setCreatedAt(Instant.now());
        purchaseOrder.setUpdatedAt(Instant.now());
        //purchaseOrder.setCreatedAtZoned(ZonedDateTime.now(ZoneId.of("America/Denver")));
        //purchaseOrder.setUpdatedAtZoned(ZonedDateTime.now(ZoneId.of("America/Denver")));
        //purchaseOrder.setCreatedBy("system");
        //purchaseOrder.setUpdatedBy("system");
        String username = "user-" + new Random().nextInt(100) + 1;
        purchaseOrder.setUsername(username);
        purchaseOrder.setStatus(POStatus.CREATED);

        PurchaseOrderItem poItem = new PurchaseOrderItem();
        //Get a random item from the item repository
        itemRepository.findById(new Random().nextInt(9) + 1L)
                .ifPresentOrElse(
                        poItem::setItem, () -> {
                            throw new IllegalStateException("Item not found");
                        }
                );
        //Set a random quantity
        poItem.setQuantity(new Random().nextInt(9) + 1);
        poItem.setCreatedAt(Instant.now());
        poItem.setUpdatedAt(Instant.now());
        purchaseOrder.addOrderItem(poItem);
        return purchaseOrder;
    }

    private Payment createPayment(PurchaseOrder po) {
        PaymentTypeDetail paymentTypeDetail = new PaymentTypeDetail();
        paymentTypeDetail.setCardNumber("1234-5678-1234-5678");
        paymentTypeDetail.setCardType("VISA");
        paymentTypeDetail.setExpiryDate("12/2024");
        paymentTypeDetail.setCvv("123");
        paymentTypeDetail.setCardHolderName("John Doe");
        paymentTypeDetail.setCreatedAt(Instant.now());
        paymentTypeDetail.setUpdatedAt(Instant.now());

        PaymentTypeDetail ptd1 = paymentTypeDetailRepository.save(paymentTypeDetail);

        PaymentTypeDetail paymentTypeDetail2 = new PaymentTypeDetail();
        paymentTypeDetail2.setBankName("JPMorgan Chase Bank");
        paymentTypeDetail2.setAccountHolderName("Jane Doe");
        paymentTypeDetail2.setAccountNumber("1234567890");
        paymentTypeDetail2.setRoutingNumber("039489");
        paymentTypeDetail2.setCreatedAt(Instant.now());
        paymentTypeDetail2.setUpdatedAt(Instant.now());

        PaymentTypeDetail ptd2 = paymentTypeDetailRepository.save(paymentTypeDetail2);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(UUID.randomUUID().toString());
        transaction1.setAmount(new BigDecimal("20.00"));
        transaction1.setType(CREDIT);
        transaction1.setStatus(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.APPROVED);
        transaction1.setPaymentTypeDetail(ptd1);
        transaction1.setCreatedAt(Instant.now());
        transaction1.setUpdatedAt(Instant.now());

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(UUID.randomUUID().toString());
        transaction2.setAmount(po.getAmount().subtract(transaction1.getAmount()));
        transaction2.setType(DEBIT);
        transaction2.setStatus(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.APPROVED);
        transaction2.setPaymentTypeDetail(ptd2);
        transaction2.setCreatedAt(Instant.now());
        transaction2.setUpdatedAt(Instant.now());

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPurchaseOrderId(po.getGuid());
        payment.setUsername(po.getUsername());
        payment.addTransaction(transaction1);
        payment.addTransaction(transaction2);
        payment.setStatus(APPROVED);
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());
        return payment;
    }


    private ShippingDetails createShipment(PurchaseOrder po) {
        Address address1 = new Address();
        address1.setLine1("123 Main St");
        address1.setCity("New York");
        address1.setState("NY");
        address1.setZipCode("10001");
        address1.setCountry("USA");
        address1.setType("Work");
        address1.setCreatedAt(Instant.now());
        address1.setUpdatedAt(Instant.now());

        Address address2 = new Address();
        address2.setLine1("456 State St");
        address2.setCity("Chicago");
        address2.setState("IL");
        address2.setZipCode("60090");
        address2.setCountry("USA");
        address2.setType("Home");
        address2.setCreatedAt(Instant.now());
        address2.setUpdatedAt(Instant.now());


        Address a1 = addressRepository.save(address1);
        Address a2 = addressRepository.save(address2);

        Shipment shipment1 = new Shipment();
        shipment1.setAddress(a1);
        shipment1.setInstruction("Handle with care");
        shipment1.setStatus("Delivered");
        shipment1.setCreatedAt(Instant.now());
        shipment1.setUpdatedAt(Instant.now());

        Shipment shipment2 = new Shipment();
        shipment2.setAddress(a2);
        shipment2.setInstruction("Leave at entrance");
        shipment2.setStatus("Pending");
        shipment2.setCreatedAt(Instant.now());
        shipment2.setUpdatedAt(Instant.now());

        ShippingDetails sd = new ShippingDetails();
        sd.addShipment(shipment1);
        sd.addShipment(shipment2);
        sd.setPurchaseOrderId(po.getGuid());
        sd.setCreatedAt(Instant.now());
        sd.setUpdatedAt(Instant.now());

        return sd;

    }

}

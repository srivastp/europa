package com.sppxs.europa;

import com.sppxs.europa.order.entity.Item;
import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import com.sppxs.europa.order.repository.ItemRepository;
import com.sppxs.europa.order.repository.PurchaseOrderRepository;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.entity.PaymentTypeDetail;
import com.sppxs.europa.payment.entity.Transaction;
import com.sppxs.europa.payment.repository.PaymentRepository;
import com.sppxs.europa.payment.repository.PaymentTypeDetailRepository;
import com.sppxs.europa.shipping.entity.AddressInfo;
import com.sppxs.europa.shipping.entity.Shipment;
import com.sppxs.europa.shipping.entity.ShippingDetails;
import com.sppxs.europa.shipping.repository.AddressRepository;
import com.sppxs.europa.shipping.repository.ShippingDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class SetupData implements CommandLineRunner {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private PaymentTypeDetailRepository paymentTypeDetailRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ShippingDetailsRepository shippingDetailsRepository;

    @Override
    //@Transactional
    public void run(String... args) {
        System.out.println("Intializing data");
        if (itemRepository.count() == 0) {
            //Create 10 Items for me based in Item entity
            Set<Item> items = new HashSet<>();
            for (int i = 1; i < 10; i++) {
                Item item = new Item();
                item.setName("Item - " + (i));
                item.setSku(482L * i);
                item.setDescription("Desc - " + (i));
                item.setUnitPrice((i + 1) * 88.00);
                item.setQuantity((i + 1) * 13);
                items.add(item);
            }
            itemRepository.saveAll(items);
            System.out.println(">>>> Loaded Items");
        }
/*
        if (purchaseOrderRepository.count() == 0) {
            Set<PurchaseOrder> pos = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setGuid(UUID.randomUUID().toString());

                int repeat = new Random().nextInt(2) + 1;
                for (int j = 0; j < repeat; j++) {
                    PurchaseOrderItem i1 = new PurchaseOrderItem();
                    //Get a random item from the item repository
                    i1.setItem(itemRepository.findById(new Random().nextInt(9) + 1L).get());
                    //Set a random quantity
                    i1.setQuantity(new Random().nextInt(9) + 1);
                    purchaseOrder.addOrderItem(i1);
                }
                pos.add(purchaseOrder);
            }
            purchaseOrderRepository.saveAll(pos);
            System.out.println(">>>> Loaded Orders");
        }

        //Create 2 Purchase Orders
        Set<PurchaseOrder> pos = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            pos.add(createPurchaseOrder());
        }
        purchaseOrderRepository.saveAll(pos);
*/
        //PurchaseOrder po = createPurchaseOrder();
        System.out.println(">>>> Loaded Purchase Order");
        //createPayment(po);
        System.out.println(">>>> Loaded Payments");
        //createShipment(po);
        System.out.println(">>>> Loaded Shipment Details");
    }

    private PurchaseOrder createPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setGuid(UUID.randomUUID().toString());
        //set a random username
        String username = "user-" + new Random().nextInt(100) + 1;
        purchaseOrder.setUsername(username);
        purchaseOrder.setStatus("Created");

        PurchaseOrderItem i1 = new PurchaseOrderItem();
        //Get a random item from the item repository
        i1.setItem(itemRepository.findById(new Random().nextInt(9) + 1L).get());
        //Set a random quantity
        i1.setQuantity(new Random().nextInt(9) + 1);
        purchaseOrder.addOrderItem(i1);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    private void createPayment(PurchaseOrder po) {
        PaymentTypeDetail paymentTypeDetail = new PaymentTypeDetail();
        paymentTypeDetail.setCardNumber("1234-5678-1234-5678");
        paymentTypeDetail.setCardType("VISA");
        paymentTypeDetail.setExpiryDate("12/2024");
        paymentTypeDetail.setCvv("123");
        paymentTypeDetail.setCardHolderName("John Doe");

        PaymentTypeDetail ptd1 = paymentTypeDetailRepository.save(paymentTypeDetail);

        PaymentTypeDetail paymentTypeDetail2 = new PaymentTypeDetail();
        paymentTypeDetail2.setBankName("JPMorgan Chase Bank");
        paymentTypeDetail2.setAccountHolderName("Jane Doe");
        paymentTypeDetail2.setAccountNumber("1234567890");
        paymentTypeDetail2.setRoutingNumber("039489");

        PaymentTypeDetail ptd2 = paymentTypeDetailRepository.save(paymentTypeDetail2);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(UUID.randomUUID().toString());
        transaction1.setAmount(25.00);
        transaction1.setType("CREDIT");
        transaction1.setStatus("Approved");
        transaction1.setPaymentTypeDetail(ptd1);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(UUID.randomUUID().toString());
        transaction2.setAmount(po.getAmount() - transaction1.getAmount());
        transaction2.setType("DEBIT");
        transaction2.setStatus("Approved");
        transaction2.setPaymentTypeDetail(ptd2);

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPurchaseOrderId(po.getGuid());
        payment.setUsername(po.getUsername());
        payment.addTransaction(transaction1);
        payment.addTransaction(transaction2);
        payment.setStatus("Approved");

        paymentRepository.save(payment);
    }


    private void createShipment(PurchaseOrder po) {
        AddressInfo addressInfo1 = new AddressInfo();
        addressInfo1.setLine1("123 Main St");
        addressInfo1.setCity("New York");
        addressInfo1.setState("NY");
        addressInfo1.setZipCode("10001");
        addressInfo1.setCountry("USA");
        addressInfo1.setType("Work");

        AddressInfo addressInfo2 = new AddressInfo();
        addressInfo2.setLine1("456 State St");
        addressInfo2.setCity("Chicago");
        addressInfo2.setState("IL");
        addressInfo2.setZipCode("60090");
        addressInfo2.setCountry("USA");
        addressInfo2.setType("Home");

        AddressInfo a1 = addressRepository.save(addressInfo1);
        AddressInfo a2 = addressRepository.save(addressInfo2);

        Shipment shipment1 = new Shipment();
        shipment1.setAddress(a1);
        shipment1.setInstruction("Handle with care");
        shipment1.setStatus("Delivered");

        Shipment shipment2 = new Shipment();
        shipment2.setAddress(a2);
        shipment2.setInstruction("Leave at entrance");
        shipment2.setStatus("Pending");

        ShippingDetails sd = new ShippingDetails();
        sd.addShipment(shipment1);
        sd.addShipment(shipment2);
        sd.setPurchaseOrderId(po.getGuid());

        shippingDetailsRepository.save(sd);
        System.out.println(">>>> Loaded Shipments");
    }

}

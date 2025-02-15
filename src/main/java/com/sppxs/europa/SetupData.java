package com.sppxs.europa;

import com.sppxs.europa.entity.Item;
import com.sppxs.europa.entity.PurchaseOrder;
import com.sppxs.europa.entity.PurchaseOrderItem;
import com.sppxs.europa.repository.ItemRepository;
import com.sppxs.europa.repository.PurchaseOrderRepository;
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

        if (purchaseOrderRepository.count() == 0) {
            Set<PurchaseOrder> pos = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setGuid(UUID.randomUUID().toString());

                int repeat = new Random().nextInt(8) + 1;
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
    }

    private PurchaseOrder createPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setGuid(UUID.randomUUID().toString());
        PurchaseOrderItem i1 = new PurchaseOrderItem();
        //Get a random item from the item repository
        i1.setItem(itemRepository.findById(new Random().nextInt(9) + 1L).get());
        //Set a random quantity
        i1.setQuantity(new Random().nextInt(9) + 1);
        purchaseOrder.addOrderItem(i1);
        //purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrder;
    }

}

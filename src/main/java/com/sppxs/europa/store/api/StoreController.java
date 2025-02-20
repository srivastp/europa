package com.sppxs.europa.store.api;

import com.sppxs.europa.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<Long> createOrder() {
        Long poId = storeService.createOrder();
        return ResponseEntity.created(null).body(poId);
        //ToDo
        /*
        return ResponseEntity.created(
+            ServletUriComponentsBuilder.fromCurrentRequest()
+                .path("/{id}")
+                .buildAndExpand(poId)
+                .toUri())
+            .body(poId);
         */
    }
}

package com.sppxs.europa.order.repository;

import com.sppxs.europa.order.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

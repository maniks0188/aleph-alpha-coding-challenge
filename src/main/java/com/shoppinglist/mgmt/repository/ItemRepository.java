package com.shoppinglist.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shoppinglist.mgmt.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	@Query("SELECT s FROM Item s WHERE s.itemCode = :code AND s.deleted = false AND s.active = true")
	Optional<Item> findByCode(@Param("code") String code);
	
	@Query("SELECT s FROM Item s WHERE LOWER(s.itemName) LIKE (CONCAT(:prefix,'%')) AND s.deleted = false AND s.active = true")
	List<Item> findItemsByNameStartingWith(@Param("prefix") String prefix, Pageable page);
	
	List<Item> findByItemCodeIn(List<String> itemCodes);
}

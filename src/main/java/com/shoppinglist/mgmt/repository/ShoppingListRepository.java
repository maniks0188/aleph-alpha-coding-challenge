package com.shoppinglist.mgmt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shoppinglist.mgmt.model.ShoppingList;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long>{

	@Query("SELECT s FROM ShoppingList s WHERE s.code = :code AND s.deleted = false")
	Optional<ShoppingList> findByCode(@Param("code") String code);
}

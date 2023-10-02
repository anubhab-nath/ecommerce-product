package dev.anubhav.product_catalog.repos;

import dev.anubhav.product_catalog.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query(value = "select * from categories c where c.name = :name", nativeQuery = true)
    Optional<Category> findByName(@Param("name") String name);
}

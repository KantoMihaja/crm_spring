package site.easy.to.build.crm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Depense;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Integer> {
    @Query("SELECT d FROM Depense d WHERE d.customer.id = :customerId AND d.dateCreation <= :dateCreation")
    List<Depense> findByCustomerAndDateBefore(@Param("customerId") Integer customerId, 
                                              @Param("dateCreation") LocalDateTime dateCreation);
}

package site.easy.to.build.crm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    @Query("SELECT b FROM Budget b WHERE b.customer.id = :customerId AND b.dateCreation <= :date")
    List<Budget> findByCustomerAndDateBefore(@Param("customerId") Integer customerId, 
                                              @Param("date") LocalDateTime date);
}

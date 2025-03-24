package site.easy.to.build.crm.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
public List<Budget> findByCustomerAndDateCreationBefore(long customerId, LocalDateTime date);
    
    Optional<Budget> findById(Long id);  // Changer Integer en Long
    
    void deleteById(Long id);  // Changer Integer en Long

    // List<Budget> findByCustomerAndDateCreationBefore(Integer customerId, LocalDateTime dateCreation);  // Correction du champ dateCreation
}

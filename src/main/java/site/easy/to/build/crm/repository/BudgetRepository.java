package site.easy.to.build.crm.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByCustomer_CustomerIdAndDateCreationBefore(Long customerId, LocalDateTime dateCreation);

    Optional<Budget> findById(Integer id);

    void deleteById(Integer id);

    List<Budget> findByCustomerAndDateBefore(Integer customerId, LocalDateTime dateCreation);
}

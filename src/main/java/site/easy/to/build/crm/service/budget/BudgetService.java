package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    public Optional<Budget> findById(Integer id) {
        return budgetRepository.findById(id);
    }

    public List<Budget> findByCustomer(Integer customerId, LocalDateTime date) {
        if (date == null) date = LocalDateTime.now();
        return budgetRepository.findByCustomer_CustomerIdAndDateCreationBefore(customerId, date);
    }

    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Integer id) {
        budgetRepository.deleteById(id);
    }

    public Optional<Budget> updateBudget(Integer id, Budget updatedBudget) {
        return budgetRepository.findById(id).map(budget -> {
            budget.setDesignation(updatedBudget.getDesignation());
            budget.setMontant(updatedBudget.getMontant());
            budget.setDateCreation(updatedBudget.getDateCreation());
            budget.setDescription(updatedBudget.getDescription());
            budget.setCustomer(updatedBudget.getCustomer());
            return budgetRepository.save(budget);
        });
    }

    // public double depassementBudget(Budget newBudget) {
        
    // }
}
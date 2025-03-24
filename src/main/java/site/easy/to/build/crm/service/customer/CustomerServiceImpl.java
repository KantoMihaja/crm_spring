package site.easy.to.build.crm.service.customer;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.DepenseRepository;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Depense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BudgetRepository budgetRepository;
    private final DepenseRepository depenseRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, BudgetRepository budgetRepository, DepenseRepository depenseRepository) {
        this.customerRepository = customerRepository;
        this.budgetRepository = budgetRepository;
        this.depenseRepository = depenseRepository;
    }

    @Override
    public Customer findByCustomerId(int customer_Id) {
        return customerRepository.findByCustomerId(customer_Id);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getRecentCustomers(int userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customerRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public long countByUserId(int userId) {
        return customerRepository.countByUserId(userId);
    }

    @Override
    public List<Budget> getBudget(int customerId, LocalDateTime date) {
        if (date == null) date = LocalDateTime.now();
        return budgetRepository.findByCustomerAndDateCreationBefore((long) customerId, date);
    }

    @Override
    public List<Depense> getDepense(int customerId, LocalDateTime date) {
        if (date == null) date = LocalDateTime.now();
        return depenseRepository.findByCustomerAndDateBefore(customerId, date);
    }

    @Override
    public BigDecimal getResteBudget(int customerId, LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }
        
        List<Budget> budgets = getBudget(customerId, date);
        List<Depense> depenses = getDepense(customerId, date);
        
        BigDecimal totalBudget = budgets.stream()
                                        .map(Budget::getMontant)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDepense = depenses.stream()
                                        .map(Depense::getMontant)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalBudget.subtract(totalDepense);
    }
}
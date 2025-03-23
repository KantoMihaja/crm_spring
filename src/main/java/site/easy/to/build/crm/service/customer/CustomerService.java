package site.easy.to.build.crm.service.customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.checkerframework.checker.units.qual.C;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Depense;

import java.util.List;

public interface CustomerService {

    public Customer findByCustomerId(int customerId);

    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);

    public List<Customer> findAll();

    public Customer save(Customer customer);

    public void delete(Customer customer);

    public List<Customer> getRecentCustomers(int userId, int limit);

    long countByUserId(int userId);

    public List<Budget> getBudget(int customerId, LocalDateTime date);

    public List<Depense> getDepense(int customerId, LocalDateTime date);
    
    public BigDecimal getResteBudget(int customerId, LocalDateTime date);
}
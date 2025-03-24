package site.easy.to.build.crm.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.google.api.client.util.DateTime;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;

@Controller
@RequestMapping("/employee/budget")
public class BudgetController {
    private final BudgetService budgetService;
    private final UserService userService;
    private final CustomerService customerService;
    private final AuthenticationUtils authenticationUtils;

    public BudgetController(BudgetService budgetService,
            UserService userService,
            CustomerService customerService,
            AuthenticationUtils authenticationUtils) {
        this.budgetService = budgetService;
        this.userService = userService;
        this.customerService = customerService;
        this.authenticationUtils = authenticationUtils;
    }

    // Affiche le formulaire de création d'un nouveau budget
    @GetMapping("/create-budget")
    public String showCreatingForm(Model model, Authentication authentication) {
        List<Customer> customers;
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);

        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            customers = customerService.findAll();
        } else {
            customers = customerService.findByUserId(user.getId());
        }

        model.addAttribute("customers", customers);
        model.addAttribute("budget", new Budget());
        return "budget/create-budget";
    }

    // Insertion des données d'un nouveau budget
    @PostMapping("/create-budget")
    public String createNewBudget(@ModelAttribute("budget") @Validated Budget budget, 
                                  BindingResult bindingResult,
                                  @RequestParam("customerId") int customerId,
                                  @RequestParam Map<String, String> formParams,
                                  Model model,
                                  Authentication authentication) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        
        if (user == null) {
            return "error/500";
        }
        
        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }
        
        if (bindingResult.hasErrors()) {
            List<Customer> customers;
            
            if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
                customers = customerService.findAll();
            } else {
                customers = customerService.findByUserId(user.getId());
            }

            model.addAttribute("customers", customers);
            return "budget/create-budget";
        }

        Customer customer = customerService.findByCustomerId(customerId);
        
        if (customer == null) {
            return "error/500";
        }
        
        if (AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE") && customer.getUser().getId() != userId) {
            return "error/500";
        }

        if (budget.getDateCreation() == null) {
            LocalDateTime dateCreation = LocalDateTime.now();
            budget.setDateCreation(dateCreation);
        }

        budget.setCustomer(customer);
        budgetService.saveBudget(budget);

        return "redirect:/employee/budget/manager/show-all";
    }

    @GetMapping("/manager/show-all")
    public String getAllContracts(Model model) {
        List<Budget> budgets = budgetService.findAll();
        model.addAttribute("budgets", budgets);
        return "budget/budgets";
    }

    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable("id") int id, Authentication authentication){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User loggedInUser = userService.findById(userId);
        if(loggedInUser.isInactiveUser()) {
            return "error/account-inactive";
        }

        Optional<Budget> budgetOptional = budgetService.findById(id);
        if (budgetOptional.isEmpty()) {
            return "error/not-found";
        }

        budgetService.deleteBudget(id);
        return "redirect:/employee/budget/manager/show-all";
    }
}
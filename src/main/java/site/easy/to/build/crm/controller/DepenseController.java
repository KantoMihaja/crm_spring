package site.easy.to.build.crm.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Ticket;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/employee/depense")
public class DepenseController {
    private final UserService userService;
    private final AuthenticationUtils authenticationUtils;
    private final CustomerService customerService;
    private final DepenseService depenseService;

    public DepenseController(UserService userService, AuthenticationUtils authenticationUtils,
            DepenseService depenseService, CustomerService customerService) {
        this.userService = userService;
        this.authenticationUtils = authenticationUtils;
        this.depenseService = depenseService;
        this.customerService = customerService;
    }

    @GetMapping("/manager/show-all")
    public String showAllDepenses(Model model) {
        List<Depense> depenses = depenseService.findAll();
        model.addAttribute("depenses", depenses);
        return "depense/depenses";
    }

    // Affiche le formulaire de création d'un nouveau budget
    @GetMapping("/create-depense")
    public String showDepenseForm(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        List<Customer> customers;

        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            customers = customerService.findAll();
        } else {
            customers = customerService.findByUserId(user.getId());
        }

        model.addAttribute("customers", customers);
        model.addAttribute("depense", new Depense());
        return "depense/create-depense";
    }

    @PostMapping("/create-depense")
    public String createDepense(@ModelAttribute("depense") @Validated Depense depense,
            BindingResult bindingResult,
            @RequestParam("customerId") int customerId,
            @RequestParam("montant") BigDecimal montant,
            @RequestParam Map<String, String> formParams,
            Model model,
            Authentication authentication) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User manager = userService.findById(userId);

        if (manager == null) {
            return "error/500"; 
        }
        if (manager.isInactiveUser()) {
            return "error/account-inactive";
        }

        if (bindingResult.hasErrors()) {
            List<Customer> customers;

            if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
                customers = customerService.findAll();
            } else {
                customers = customerService.findByUserId(manager.getId());
            }

            model.addAttribute("customers", customers);
            return "depense/create-depense";
        }

        Customer customer = customerService.findByCustomerId(customerId);

        if (customer == null) {
            model.addAttribute("errorMessage", "Client non trouvé.");
            return "error/404";
        }

        User employee = userService.findById(userId);
        if (AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE")) {
            if (employee == null || customer.getUser().getId() != userId) {
                model.addAttribute("errorMessage", "Accès non autorisé.");
                return "error/403";
            }
        }

        BigDecimal resteBudget = customerService.getResteBudget(customerId, depense.getDateCreation());

        Depense dep = new Depense(montant, LocalDateTime.now(), customer);
        if (depense.getMontant().compareTo(resteBudget) > 0) { 
            model.addAttribute("confirmationMessage", "Le budget sera dépasser. Voulez-vous continuer ?");
            model.addAttribute("depense", dep);
            return "depense/confirm-depassement"; 
        }

        depenseService.saveDepense(dep);

        return "redirect:/employee/depense/manager/show-all";
    }

    @PostMapping("/confirm-depassement")
    public String confirmDepassementBudget(@RequestParam("customerId") int customerId,
                                        @RequestParam("montant") BigDecimal montant,
                                        @RequestParam("dateCreation") String dateCreation,
                                        Model model, 
                                        Authentication authentication) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User employee = userService.findById(userId);

        if (employee == null) {
            model.addAttribute("errorMessage", "Utilisateur non trouvé.");
            return "error/500";
        }

        Customer customer = customerService.findByCustomerId(customerId);

        if (customer == null) {
            model.addAttribute("errorMessage", "Client non trouvé.");
            return "error/404";
        }

        // Crée et enregistre la dépense
        Depense depense = new Depense(montant, LocalDateTime.parse(dateCreation), customer);
        depenseService.saveDepense(depense);

        model.addAttribute("successMessage", "Dépense enregistrée avec succès.");
        return "redirect:/employee/depense/manager/show-all";
    }
}
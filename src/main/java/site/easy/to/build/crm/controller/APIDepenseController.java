package site.easy.to.build.crm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/depenses")
@CrossOrigin(origins = "*")
public class APIDepenseController {

    private final DepenseService depenseService;
    private final CustomerService customerService;

    public APIDepenseController(DepenseService depenseService, CustomerService customerService) {
        this.depenseService = depenseService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Depense>> getAllDepenses() {
        List<Depense> depenses = depenseService.findAll();
        return new ResponseEntity<>(depenses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepenseById(@PathVariable Integer id) {
        Optional<Depense> depense = depenseService.findById(id);
        if (depense.isPresent()) {
            return new ResponseEntity<>(depense.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Dépense non trouvée", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDepense(@RequestBody Depense depense) {
        if (depense.getCustomer() == null || depense.getCustomer().getId() == null) {
            return new ResponseEntity<>("Le client est obligatoire", HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> customer = customerService.findById(depense.getCustomer().getId());
        if (customer.isEmpty()) {
            return new ResponseEntity<>("Client non trouvé", HttpStatus.NOT_FOUND);
        }

        depense.setCustomer(customer.get());
        Depense savedDepense = depenseService.save(depense);
        return new ResponseEntity<>(savedDepense, HttpStatus.CREATED);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateDepense(@PathVariable Integer id, @RequestBody Depense depense) {
        Optional<Depense> existingDepense = depenseService.findById(id);
        if (existingDepense.isEmpty()) {
            return new ResponseEntity<>("Dépense non trouvée", HttpStatus.NOT_FOUND);
        }

        Depense depenseToUpdate = existingDepense.get();

        depenseToUpdate.setMontant(depense.getMontant());
        depenseToUpdate.setDateCreation(depense.getDateCreation());

        if (depense.getCustomer() != null && depense.getCustomer().getId() != null) {
            Optional<Customer> customer = customerService.findById(depense.getCustomer().getId());
            if (customer.isEmpty()) {
                return new ResponseEntity<>("Client non trouvé", HttpStatus.NOT_FOUND);
            }
            depenseToUpdate.setCustomer(customer.get());
        }

        Depense updatedDepense = depenseService.save(depenseToUpdate);
        return new ResponseEntity<>(updatedDepense, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepense(@PathVariable Integer id) {
        Optional<Depense> depense = depenseService.findById(id);
        if (depense.isEmpty()) {
            return new ResponseEntity<>("Dépense non trouvée", HttpStatus.NOT_FOUND);
        }

        depenseService.delete(depense.get());
        return new ResponseEntity<>("Dépense supprimée avec succès", HttpStatus.OK);
    }
}
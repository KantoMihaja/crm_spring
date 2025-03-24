package site.easy.to.build.crm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.service.lead.LeadService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lead")
public class APILeadController {

    private final LeadService leadService;

    public APILeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        List<Lead> leads = leadService.findAll();
        if (leads.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable int id) {
        Lead lead = leadService.findByLeadId(id);
        if (lead != null) {
            return ResponseEntity.ok(lead);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        try {
            Lead createdLead = leadService.save(lead);
            return new ResponseEntity<>(createdLead, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Lead> updateLead(@PathVariable int id, @RequestBody Lead updatedLead) {
        Lead existingLead = leadService.findByLeadId(id);
        if (existingLead != null) {
            updatedLead.setLeadId(id);
            Lead savedLead = leadService.save(updatedLead);
            return ResponseEntity.ok(savedLead);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable int id) {
        Lead existingLead = leadService.findByLeadId(id);
        if (existingLead != null) {
            leadService.delete(existingLead);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
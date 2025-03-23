package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import site.easy.to.build.crm.service.utildb.ResetDataService;

@Controller
public class ResetDataController {
    @Autowired
    private ResetDataService resetService;

    @PostMapping("/resetData")
    public ResponseEntity<String> resetData() {
        resetService.resetDatabase();
        return ResponseEntity.ok("Données réinitialisées avec succès !");
    }
}
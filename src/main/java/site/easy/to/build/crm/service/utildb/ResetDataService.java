package site.easy.to.build.crm.service.utildb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResetDataService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void resetDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        // Lister les tables à vider
        List<String> tables = List.of(
                "contract_settings",
                // "customer",
                // "customer_login_info",
                "email_template",
                "employee",
                "file",
                "google_drive_file",
                "lead_action",
                "lead_settings",
                "ticket_settings",
                "trigger_lead",
                "trigger_ticket",
                "trigger_contract"
                );

        tables.forEach(table -> jdbcTemplate.execute("TRUNCATE TABLE " + table));
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

        // Réinsérer les données initiales si nécessaire
        // jdbcTemplate.execute("INSERT INTO users (id, name) VALUES (1, 'Admin')");
    }
}
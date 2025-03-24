package site.easy.to.build.crm.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "depense")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Le montant est obligatoire")
    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    @NotNull(message = "La date de création est obligatoire")
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    // Constructeurs
    public Depense() {
    }

    public Depense(BigDecimal montant, LocalDateTime dateCreation, Customer customer) {
        this.montant = montant;
        this.dateCreation = dateCreation;
        this.customer = customer;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
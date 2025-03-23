package site.easy.to.build.crm.service.depense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.DepenseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    public List<Depense> findAll() {
        return depenseRepository.findAll();
    }

    public Optional<Depense> findById(Integer id) {
        return depenseRepository.findById(id);
    }

    public Depense saveDepense(Depense depense) {
        return depenseRepository.save(depense);
    }

    public void deleteDepense(Integer id) {
        depenseRepository.deleteById(id);
    }

    public Optional<Depense> updateDepense(Integer id, Depense updatedDepense) {
        return depenseRepository.findById(id).map(depense -> {
            depense.setMontant(updatedDepense.getMontant());
            depense.setDateCreation(updatedDepense.getDateCreation());
            depense.setCustomer(updatedDepense.getCustomer());
            return depenseRepository.save(depense);
        });
    }
}
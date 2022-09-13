//Los repositorios permiten manipular los datos en el servidor

package com.mindhub.homebanking.repository;

import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository <Loan,Long> {

    Loan findByName(String name);

}
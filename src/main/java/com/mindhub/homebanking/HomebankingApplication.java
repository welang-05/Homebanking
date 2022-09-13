package com.mindhub.homebanking;

import com.mindhub.homebanking.controllers.CardController;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	public PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {  //Inicializar datos utilizando los repositorios para Clientes y Accounts, este requerimiento de inicio, CommandLineRunner es una interfaz, utilizada para ejecutar el código después de que Spring Boot se inicia
		return (args) -> {
			//  Crear un par de clientes iniciales
			Client cliente1 = new Client("Hank", "Scorpio", "mr.scorpio@gmail.com", 45,
					passwordEncoder.encode("12345"));
			//Nuevo objeto
			// cliente
			// con los datos nombre y apellido
			clientRepository.save(cliente1); //Esta forma de crear al cliente nos permite utilizarlo luego para la creación de la cuenta de este
			Client cliente2 =
					new Client("Juanito", "De los Palotes", "yosoyjuanito@gmail.com", 55,
							passwordEncoder.encode("12345"));
			//Nuevo
			// objeto cliente usando otro constructor
			clientRepository.save(cliente2); //Guardarlo en la base de datos

			Client admin = new Client ("Francisco", "Mora", "admin@admin.com", 33, passwordEncoder.encode("admin"));
			clientRepository.save(admin);

			Account cuenta1 = new Account("VIN00000001", LocalDateTime.now(), 500000, AccountType.CURRENT); //1ra cuenta para el cliente_1
			Account cuenta2 = new Account("VIN00000002", LocalDateTime.now().plusDays(1), 750000,AccountType.SAVINGS, cliente1); //Se suministra directamente el cliente_1 a la cuenta 2
			cliente1.addAccount(cuenta1); //Añadir la cuenta1 al cliente_1
			accountRepository.save(cuenta1); //Guardar cuenta1 en la base de datos
			accountRepository.save(cuenta2);  //Guarda cuenta2 en la base de datos

			Account cuenta3=new Account("VIN00000003", LocalDateTime.now().plusHours(5), 150000.12,AccountType.CURRENT, cliente2);
			accountRepository.save(cuenta3); //Se crea una cuenta para el cliente_2, se guarda inmediatamente en la base de datos

			Transaction n1 = new Transaction(cuenta1, TransactionType.DEBIT, -2000, "Primera transacción", LocalDateTime.now());
			transactionRepository.save(n1);
//			accountRepository.save(cuenta1);
			Transaction n2 = new Transaction(cuenta1, TransactionType.CREDIT, 1000, "Segunda", LocalDateTime.now());
			transactionRepository.save(n2);
//			accountRepository.save(cuenta1);

			transactionRepository.save(new Transaction(cuenta2, TransactionType.CREDIT, 5000, LocalDateTime.now()));
//			accountRepository.save(cuenta2);
			transactionRepository.save(new Transaction(cuenta2, TransactionType.DEBIT, -3000, "Así no más", LocalDateTime.now()));
//			accountRepository.save(cuenta2);

			transactionRepository.save(new Transaction(cuenta3, TransactionType.CREDIT, 1000, LocalDateTime.now()));
//			accountRepository.save(cuenta3);
			transactionRepository.save(new Transaction(cuenta3, TransactionType.DEBIT, -12345, "Así no más", LocalDateTime.now()));
//			accountRepository.save(cuenta3);

			Loan hipotecario = new Loan("Mortgage", 500000,1.2, List.of(12, 24, 36, 48, 60));
			Loan personal = new Loan("Personal", 100000,1.3, List.of(6,12, 24));
			Loan automotriz = new Loan("Automotive", 300000,1.25 , List.of(6,12,24,36));
			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(automotriz);

			ClientLoan cl1 = new ClientLoan(400000.0, 60, cliente1, hipotecario);
			ClientLoan cl2 = new ClientLoan(50000.0, 12, cliente1, personal);
			ClientLoan cl3 = new ClientLoan(10000.0, 24, cliente2, personal);
			ClientLoan cl4 = new ClientLoan(200000.0, 36, cliente2, automotriz);

			clientLoanRepository.save(cl1);
			clientLoanRepository.save(cl2);
			clientLoanRepository.save(cl3);
			clientLoanRepository.save(cl4);
			loanRepository.save(personal);
			loanRepository.save(automotriz);
			loanRepository.save(hipotecario);

			Card tarjeta1 = new Card("1111  2222  3333  4444",123, LocalDate.now(),LocalDate.now().plusYears(5),
					cliente1.getName()+" "+cliente1.getLastName(),CardColor.GOLD,CardType.DEBIT,cliente1, cuenta1);
			tarjeta1.setCardQuota(cuenta1.getBalance());
			Card tarjeta2 = new Card("5555  6666  7777  8888",987,LocalDate.now(),LocalDate.now().plusYears(5),
					cliente1.getName()+" "+cliente1.getLastName(), CardColor.TITANIUM, CardType.CREDIT,cliente1);
			tarjeta2.setCardQuota(500000);
			Card tarjeta3 = new Card("1111  6666  7777  8888",112,LocalDate.now().minusYears(5),LocalDate.now().minusDays(2),
					cliente1.getName()+" "+cliente1.getLastName(), CardColor.SILVER, CardType.CREDIT,cliente1);
			tarjeta3.setCardQuota(50000);
			Card tarjeta4 = new Card("1234  1234  1234  1234",222,LocalDate.now(),LocalDate.now().plusYears(5),
					cliente2.getName()+" "+cliente2.getLastName(), CardColor.GOLD, CardType.CREDIT,cliente2);
			tarjeta4.setCardQuota(150000);

			cardRepository.save(tarjeta1);
			cardRepository.save(tarjeta2);
			cardRepository.save(tarjeta3);
			cardRepository.save(tarjeta4);
			clientRepository.save(cliente1);
			clientRepository.save(cliente2);


		};
	}


}

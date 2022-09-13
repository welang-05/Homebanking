package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity //Crear una tabla en la base de datos en base a esta clase
public class Client {

    @Id //La propiedad de tipo Id será la clave primaria de la propiedad definida abajo
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //Le asigna valor a la Id
    @GenericGenerator(name = "native", strategy = "native") //Verifica que no haya una id similar para no repetirla
    private long id; //Propiedad o atributo
    private String name, lastName, email, password;
    private int age;

    @OneToMany(mappedBy="titular", fetch=FetchType.EAGER) //Relación de la clase cliente será de 1 a muchos, un cliente y muchas cuentas, Fetch reserva un espacio de memoria, el EAGER mantiene actualizada la información
    private Set<Account> accountSet = new HashSet<>(); //Permite definir un set(grupo) de cuentas a un client

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER) //Relación de la clase cliente y ClientLoan será de 1 a muchos, un cliente y muchos préstamos
    private Set<ClientLoan> loanSet = new HashSet<>(); //Permite definir un set(grupo) de préstamos a un cliente

    @OneToMany(mappedBy = "titular", fetch = FetchType.EAGER) //Relación de cliente y tarjetas será de uno a muchos
    private Set<Card> cardSet = new HashSet<>();

    public Client() {

    } //Constructor por defecto

    public Client(String nombre, String apellido){ //Constructor 1
        this.name = nombre;
        this.lastName = apellido;
    }

    public Client(String nombre, String apellido, String correo, String contraseña){ //Constructor 2
        this.name = nombre;
        this.lastName = apellido;
        this.email = correo;
        this.password = contraseña;
    }

    public Client(String nombre, String apellido, String correo, int edad, String contraseña){//Constructor 3
        this.name = nombre;
        this.lastName = apellido;
        this.email = correo;
        this.age = edad;
        this.password = contraseña;
    }

//    public Client(String nombre, String apellido, String correo, int edad, Account cuenta, String contraseña){
//        //Constructor4
//        this.name = nombre;
//        this.lastName = apellido;
//        this.email = correo;
//        this.age = edad;
//        this.accountSet.add(cuenta);
//        this.password = contraseña;
//    }

    //Getters and Setters, los métodos(accesores) para definir y para obtener los valores de las propiedades de cada objeto
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccountSet() {
        return accountSet;
    }
    public void setAccountSet(Set<Account> accountSet) {
        this.accountSet = accountSet;
    }
    public void addAccount(Account account){
        account.setTitular(this);
        this.accountSet.add(account);
    }

    public Set<ClientLoan> getLoanSet() {
        return loanSet;
    }
    public void setLoanSet(Set<ClientLoan> loanSet) {
        this.loanSet = loanSet;
    }
    public void addLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        this.loanSet.add(clientLoan);
    }

    public List<Loan> getLoans(){
        return this.loanSet.stream().map(ClientLoan::getLoan).collect(Collectors.toList());
    }

    public Set<Card> getCardSet() {
        return cardSet;
    }
    public void setCardSet(Set<Card> cardSet) {
        this.cardSet = cardSet;
    }
    public void  addCard(Card card){
        card.setTitular(this);
        this.cardSet.add(card);
    }
}

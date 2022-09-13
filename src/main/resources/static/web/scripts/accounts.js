const app = Vue.createApp({
    data() {
        return {
            datosCliente: '',
            tema: '',
            cards: [],
            accounts: [],
            permitido: {
                botonNuevaCuenta: false
            },
            modalLoans: false,
            loanMod: {},
            hover:[],
            errorCards: '',
            modalErrorCards: false,
            modalLoans:false,
            modalDeleteCard: false,
            numberCardToDelete: '',
            confirmationDeleteCard: false,
            deletedCardOk: false,
            today : new Date(),
            modalCreateAccounts:{
                open: false,
                accountType: '',
                modalFin: false,
                message:''          
            },
        }
    },
    created() {
        this.obtenerDatos();
        this.initialTheme();
        this.todayIs();
    },
    mounted() {


    },
    methods: {
        obtenerDatos() {
            this.deletedCardOk=false;
            this.modalCreateAccounts.message='';
            this.modalCreateAccounts.modalFin=false;
            axios.get('/api/clients/current')
                .then(clientesServidor => {
                    this.datosCliente = clientesServidor.data;
                    this.accounts = this.datosCliente.accountSet;
                    this.cards = this.datosCliente.cardSet;
                    this.cards.forEach(a => {
                        this.hover.push(false);
                     });
                    });
        },
        bankLogout(){
            axios.post('/api/logout').then(response => window.location.href='/index.html');
        },
        newAccount(){
            axios.post(`/api/clients/current/accounts?accountType=${this.modalCreateAccounts.accountType}`)
            .catch(error => {
                this.modalCreateAccounts.message=error.response.data;
                this.modalCreateAccounts.modalFin=true;
                setTimeout(()=>this.obtenerDatos(), 1900); 
            })
            .then(response => {
                this.modalCreateAccounts.message=response.data;
                this.modalCreateAccounts.modalFin=true;
                setTimeout(()=>this.obtenerDatos(), 1900); 
            });
        },
        deleteCard(){
            axios.patch(`/api/clients/current/cards?number=${this.numberCardToDelete}`)
            .catch(error => {
                this.errorCards=error.response.data;
                this.modalErrorCards=true;
            })
            .then(response => {
                if(this.errorCards.length==0){
                    this.modalDeleteCard=false;
                    this.numberCardToDelete='';
                    this.confirmationDeleteCard=false;
                    this.deletedCardOk=true;
                    setTimeout(()=>this.obtenerDatos(),1900);
                }
            });
        },
        loanModal(loan,i){
            this.loanMod=loan;
            this.loanMod.id=i+1;
            this.modalLoans=true;

        },
        changeURL(location){
            window.location.href = location;
        },    
        todayIs(){
            var dd = String(this.today.getDate()).padStart(2, '0');
            var mm = String(this.today.getMonth() + 1).padStart(2, '0'); //January is 0!
            var yyyy = this.today.getFullYear();
            this.today = yyyy + '-' + mm + '-' + dd;
        },
        theme(a){
            this.tema = `tema_${a}`;
            localStorage.setItem('theme', JSON.stringify(a));
        },
        initialTheme(){
            if(JSON.parse(localStorage.getItem('theme'))){
                this.theme(JSON.parse(localStorage.getItem('theme')))
            }
        },
        cardHover(i){
                this.hover=this.hover.map(hover => true );
                this.hover[i]=false;
        }
    },
    computed: {

    }
}).mount('#app')
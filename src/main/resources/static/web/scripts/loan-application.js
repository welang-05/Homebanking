/* const { createApp } = Vue; */

const app = Vue.createApp({
    data() {
        return {
            loanInfo: '',
            tema: '',
            accounts: [],
            currentLoan: {
                id: 0,
                amount: 0,
                payments: 0,
                interest: 0,
                accountNumber: ''
            },
            currentLoanExtra:{
                name: '',
                maxAmount: 0,
                payments: []
            },
            error: '',
            modalConfirmation: false,
            allOk: false,
            modalError: false
        }
    },
    created() {
        this.obtainLoanInfo();
        this.getAccounts();
        this.initialTheme();
    },
    mounted() {


    },
    methods: {
        obtainLoanInfo() {
            axios
                .get('/api/loans')
                .then(loans => {
                    this.loanInfo = loans.data;
                })
        },
        getAccounts() {
            axios 
                .get('/api/clients/current')
                .then(clientesServidor => {
                    this.accounts = clientesServidor.data.accountSet;
                });
        },
        bankLogout() {
            axios.post('/api/logout').then(response => this.changeURL('/index.html'));
        },
        loanApplication(){
            axios.post('/api/loans',this.currentLoan)
            .catch(error => {
                this.error=error.response.data;
                this.modalError=true;
            })
            .then(response => {
                if(this.error.length==0){
                    this.allOk=true;
                    setTimeout(()=>this.changeURL('../web/accounts.html'), 1600); 
                }
            });
        },
        changeURL(location){
            window.location.href = location;
        },
        theme(a){
            this.tema = `tema_${a}`;
            localStorage.setItem('theme', JSON.stringify(a));
        },
        initialTheme(){
            if(JSON.parse(localStorage.getItem('theme'))){
                this.theme(JSON.parse(localStorage.getItem('theme')))
            }
        }
    },
    computed: {

    }
}).mount('#app')
/* const { createApp } = Vue; */

const app = Vue.createApp({
    data() {
        return {
            datosCliente: '',
            tema: '',
            accounts: [],
            section: {
                destinySelf: false,
                destinyOther: false,
                sourceSelected: false,
                destinySelected: false,
                amount: false,
                description: false,
                confirm: false
            },
            currentTransfer: {
                from: '',
                to: '',
                amount: 0,
                description: ''
            },
            currentBalance:0,
            error: '',
            modalConfirmation: false,
            modalError: false
        }
    },
    created() {
        this.obtenerDatos();
        this.initialTheme();
    },
    mounted() {


    },
    methods: {
        obtenerDatos() {
            axios
                .get('/api/clients/current')
                .then(clientesServidor => {
                    this.datosCliente = clientesServidor.data;
                    this.accounts = this.datosCliente.accountSet;
                    this.sourceAccounts();
                })
        },
        bankLogout() {
            axios.post('/api/logout').then(response => this.changeURL('/index.html'));
        },
        sourceAccounts() {
            this.accounts.forEach((a, i) => {
                this.section.sourceSelected[i] = false;
            });
        },
        makeTransfer(){
            axios.post(`/api/transactions?amount=${this.currentTransfer.amount}&sourceAccount=${this.currentTransfer.from}&destinyAccount=${this.currentTransfer.to}&description=${this.currentTransfer.description}`)
            .catch(error => {
                this.error=error.response.data;
                this.modalError=true;
            })
            .then(response => {
                if(this.error.length==0){
                    this.section.confirm=true;
                    setTimeout(()=>this.changeURL('../web/accounts.html'), 1600); 
                }
            });
        },
        changeURL(location){
            window.location.href = location;
        },
        balance(numberAccount){
            this.currentBalance = (this.datosCliente.accountSet.filter(a => a.number==numberAccount)[0]).balance;
            
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
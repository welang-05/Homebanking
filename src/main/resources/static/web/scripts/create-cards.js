const app = Vue.createApp({
    data() {
        return {
            tema: '',
            typeReady: false,
            colorReady: false,
            card: {
                type: '',
                color: '',
                account: ''
            },
            boton:{
                DEBIT: false,
                CREDIT: false,
                SILVER: false,
                GOLD: false,
                TITANIUM: false,
            },
            accounts:[],
            error: '',
            modalError: false,
            cardOk: false
        }
    },
    created() {
        this.initialTheme();
        this.obtenerDatos();
    },
    mounted() {


    },
    methods: {
        obtenerDatos() {
            axios.get('/api/clients/current')
                .then(datosCliente => {
                    this.accounts = datosCliente.data.accountSet;
                    });
        },
       bankLogout(){
        axios.post('/api/logout').then(response => window.location.href='/index.html');
       },
       newCard(){
        axios.post(`../api/clients/current/cards?cardColor=${this.card.color}&cardType=${this.card.type}${this.card.account}`)
        .catch(error => {
            this.error=error.response.data;
            this.modalError=true;
        })
        .then(response => {
            if(this.error.length==0){
            this.cardOk=true;
            setTimeout(()=>this.changeURL('../web/cards.html'), 1600);
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
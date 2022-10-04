const app = Vue.createApp({
    data() {
        return {
            datosCliente: '',
            datosCuenta: {},
            transactions: {},
            accountId: '',
            tema: '',
            otherAccounts: [],
            modalDeleteAccounts:{
                open: false,
                destiny: '',
                OK: false,
                error: false,
                message: ''
            },
            hover:[]
        }
    },
    created() {
        this.obtenerDatos();
        this.initialTheme();
    },
    mounted() {
        this.addClassY('navbar',80,'glass2');
    },
    methods: {
        obtenerDatos() {
            this.modalDeleteAccounts.message = '';
            this.modalDeleteAccounts.error = false;
            this.accountId = location.search.split("?id=").join("");
            axios 
                .get('/api/clients/current')
                .then(clientesServidor => {
                    this.datosCliente = clientesServidor.data;
                    this.datosCuenta = this.datosCliente.accountSet.filter(account => account.id==this.accountId)[0];
                    this.otherAccounts = this.datosCliente.accountSet.filter(account => account.id != this.accountId);
                    this.transactions = this.datosCuenta.transactionSet;
                    this.otherAccounts.forEach(a => {
                        this.hover.push(false);
                    });
                });
        },deleteAccount(){
            axios.patch(`../api/clients/currents/accounts?accountToDelete=${this.datosCuenta.number}&destinyAccount=${this.modalDeleteAccounts.destiny}`)
            .catch(error=>{
                this.modalDeleteAccounts.message=error.response.data;
                this.modalDeleteAccounts.error=true;
                this.modalDeleteAccounts.destiny = '';
            })
            .then(response => {
                if(this.modalDeleteAccounts.message==''){
                    this.modalDeleteAccounts.message=response.data;
                    this.modalDeleteAccounts.OK=true;
                    setTimeout(()=>this.changeURL('./accounts.html'),1900);
                }
            });  
        },
        bankLogout(){
            axios.post('/api/logout').then(response => window.location.href='/index.html');
        },        
        changeURL(location){
            window.location.href = location;
        },
        theme(a){
            if(a>4 && parseInt(this.tema.slice(-1))>2){
                a=0;
            }else if(a>4 && parseInt(this.tema.slice(-1))<3){
                a=parseInt(this.tema.slice(-1))+1;
            }
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
        },
        addClassY(ref,yTrigger,classToAdd) {
            const navbar = eval(`this.$refs.${ref}`);
            window.addEventListener("scroll", () => {
                if (window.scrollY > yTrigger) {
                    navbar.classList.add(classToAdd);
                } else {
                    navbar.classList.remove(classToAdd);
                }
            })
        }
    },
    computed: {

    }
}).mount('#app')
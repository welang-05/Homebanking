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
        this.addClassY('navbar',80,'glass2');
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
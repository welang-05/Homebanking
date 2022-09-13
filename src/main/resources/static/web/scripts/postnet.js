const app = Vue.createApp({
    data() {
        return {
            tema: '',
            cards: [],
            paymentDTO:{
                cardNumber: '',
                description: '',
                cvv: 0,
                amount: 0
            },
            modal:false,
            error:false,
            messageModal: ''
        }
    },
    created() {
        this.initialTheme();
        this.obtenerDatos();
    },
    mounted() {


    },
    methods: {
        obtenerDatos(){
            axios.get('/api/clients/current')
        .then(clientesServidor => {
            this.cards = clientesServidor.data.cardSet;
        });
        },
        cardPayment(){
            axios.post('/api/post_net',this.paymentDTO)
                .catch(error=>{
                    this.messageModal=error.response.data;
                    console.log(error)
                    this.modal=true;
                    this.error=true;
                })
                .then(reponse=>{
                    if(this.error==false){
                        this.messageModal=reponse.data;
                        this.modal=true;
                        setTimeout(()=>this.changeURL("/web/accounts.html"), 1900);
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
            this.tema = `tema_${a}`;
            localStorage.setItem('theme', JSON.stringify(a));
        },
        initialTheme(){
            if(JSON.parse(localStorage.getItem('theme'))){
                this.theme(JSON.parse(localStorage.getItem('theme')))
            }
        },
    },
    computed: {

    }
}).mount('#app')
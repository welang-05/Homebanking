const app = Vue.createApp({
    data() {
        return {
            login: {
                name: '',
                pwd: ''
            },
            registerOn: false,
            register: {
                name: '',
                lastName: '',
                email: '',
                pwd: '',
                errorEmail: '',
                errorPwd: '',
            },
            randomBackground: 0,
            error: '',
            modalConfirmation: false,
            modalError: false,
            typingTimer: 0,
            newAccountOK: false
        }
    },
    created() {
        this.randomBg();
    },
    mounted() {


    },
    methods: {
        bankLogin() {
            axios.post('/api/login',`email=${this.login.name}&password=${this.login.pwd}`)
                .catch(error => {
                    this.error = error.response.data.error;
                    this.modalError = true;
                })
                .then(response => {
                    if ((this.error.length == 0) && (!this.newAccountOK)) {
                        window.location.href = '/web/accounts.html';
                    } else {
                        this.newAccount();
                    }
                });
        },
        bankSignIn() {
            if (this.register.pwd.length < 7) {
                this.error = "Password too short";
                this.modalError = true;
                return 0;
            }
            axios.post('/api/clients',`name=${this.register.name}&lastName=${this.register.lastName}&email=${this.register.email}&password=${this.register.pwd}`)
                .catch(error => {
                    this.error = error.response.data;
                    this.modalError = true;
                })
                .then(response => {
                    if (this.error.length == 0) {
                        this.login.name = this.register.email;
                        this.login.pwd = this.register.pwd;
                        this.modalConfirmation = true;
                        this.newAccountOK=true;
                        setTimeout(() => this.bankLogin(), 1600);
                    }
                })
        },newAccount(){
            axios.post('/api/clients/current/accounts?accountType=CURRENT')
            .then(response => {
                window.location.href = '/web/accounts.html';
            });
        },
        randomBg() {
            randomBackground = Math.floor(Math.random() * 17) + 1;
            var bodyStyles = document.body.style;
            bodyStyles.setProperty('--fondo-index-nativa', `url("/web/assets/f_index${randomBackground}.jpg")`);
        },
        changeURL(location) {
            window.location.href = location;
        },
        pwdErrorMessage() {
            this.register.errorPwd = "Enter a password with 7 or more characters";
        },
        verifyPassword() {
            if (this.register.pwd.length < 7 && this.register.errorPwd == "") {
                clearTimeout(this.typingTimer);
                typingTimer = setTimeout(() => this.pwdErrorMessage(), 1000);
            } else if (this.register.pwd.length > 6) {
                this.register.errorPwd = "";
            }
        },
        allowedCharactersName(string, option) {
            let allowed="abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ'- "
            if(!allowed.includes(string.slice(-1))){
                if(option==1){
                    this.register.name=string.slice(0, -1);
                }
                if(option==2){
                    this.register.lastName=string.slice(0,-1);
                } 
            }
        }
    },
    computed: {

    }
}).mount('#app')
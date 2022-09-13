const app = Vue.createApp({
  data() {
    return {
      datosClientes: [],
      prueba: [],
      nuevoCliente: { name: '', lastName: '', email: '', age: '' },
      editarCliente: [],
      formularioEditar: [],
      detallesCuentas: [],
      LoanDTO: {
        id:0,
        name:'',
        maxAmount: 0,
        interest: 0,
        payments: []
      },
      payments: '',
      errorLoan: ''
    }
  },
  created() {
    this.obtenerDatos();
  },
  mounted() {


  },
  methods: {
    obtenerDatos() {
      axios
        .get('/api/clients')
        .then(clientesServidor => {
          this.datosClientes = clientesServidor.data;
          console.table(this.datosClientes);
          formularioEditar=[];
          editarCliente=[];
          this.datosClientes.forEach((cliente,i) => {
            this.formularioEditar[i]=
              {
                permisoEditar: false,
                permisoBorrar: false,
                idCliente: cliente.id
              };
            this.editarCliente[i]={name: '', lastName: '', email: '', age: '' };
            this.detallesCuentas[i]=
              {
                idCliente: cliente.id,
                permisoDetalles: false,
                url: `/web/accounts.html?id=${cliente.id}`
              }
          });

        })
    },

    eliminarCliente(id) {
      axios.delete(`/rest/clients/${id}`)
        .then(res => { console.log(res) })
        .then(() => this.obtenerDatos())
        .catch(err => { console.error(err) })
    },

    subirCliente() {
      axios.post('/rest/clients', this.nuevoCliente)
        .then(res => { console.log(res) })
        .then(() => this.obtenerDatos())
        .catch(err => { console.error(err) })
    },

    actualizarCliente(Cliente, id){
      let datosActualizados = this.clienteSinNull(Cliente);
      console.log(datosActualizados);
      axios.patch(`/rest/clients/${id}`, datosActualizados)
      .then(res => { console.log(res) })
      .then(() => this.obtenerDatos())
      .catch(err => { console.error(err) })
    },

    clienteSinNull(Cliente){
      for(var i in Cliente){
        if(Cliente[i] === "" || Cliente[i].lenght == 0){
          delete Cliente[i];
        }
      }
      return Cliente;
    },
    bankLogout(){
      axios.post('/api/logout').then(response => window.location.href='/index.html');
    },
    subirLoan(){
      this.LoanDTO.payments= this.payments.split(',').map(Number);
      axios.post('/api/loans/types',this.LoanDTO)
        .catch(error=>{
          console.log(error.response.data);
          this.errorLoan=error.response.data;
        })
        .then(response=>{
          if(this.errorLoan==''){
            console.log(response.data);
          }
        })
    }   
  },
computed: {

}
}).mount('#app')
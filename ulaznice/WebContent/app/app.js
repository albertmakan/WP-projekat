const Pocetna = { template: '<pocetna></pocetna>' }
const Prijava = { template: '<prijava></prijava>' }
const Registracija = { template: '<registracija></registracija>' }


const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: Pocetna },
		{ path: '/prijava', component: Prijava },
	    { path: '/registracija', component: Registracija }
	  ]
});

var app = new Vue({
	router,
	el: '#ulaznice'
});


/*var app = new Vue({ 
    el: '#hello',
    data() {
       	return {
			selectedFile: null
		}
    },
    
    methods: {
    	onFileSelected(event) {
    		this.selectedFile = event.target.files[0]   
    	},
    	onUpload() {
			const fd = new FormData();
			fd.append('file', this.selectedFile)
    		axios.post('rest/manifestacije/poster', fd)
    	}
   	}
});		*/
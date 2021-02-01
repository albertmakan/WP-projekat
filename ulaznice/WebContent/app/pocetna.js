Vue.component("pocetna", {
	data: function() {
		return {
			manifestacije: [],
			mesta: [],
			parametri: {},
			naziv: ""
		}
	},
	template: ` 
		<div>
			<div class="komb-pretraga">
				<input type="text" placeholder="Naziv" v-model="naziv">
				<button v-on:click="pretragaNaziv()">Pretrazi</button>
				<input type="text" placeholder="Mesto" list="mesta" v-model="parametri.mesto">
				<datalist id="mesta">
				    <option v-for="mesto in mesta">{{mesto}}</option>
			  	</datalist>
				<input type="date" placeholder="Datum od" v-model="parametri.datumod">
				<input type="date" placeholder="Datum do" v-model="parametri.datumdo">
				<input type="number" placeholder="Cena od" v-model="parametri.cenaod">
				<input type="number" placeholder="Cena do" v-model="parametri.cenado">
				<button v-on:click="kombinovanaPretraga()">Pretrazi</button>
			</div>
			<div v-for="m in manifestacije" class="manifestacija-mali" :id="m.id">
				<a target="_blank" href="#">
					<img class="poster-mali" :src="'data/posteri/'+m.id+'.png'"
					onerror="this.onerror=null; this.src='data/posteri/noposter.jpg'" alt="Poster" width="250px" height="150px" />
				</a>
				<div class="podaci-mali">
					<h2>{{m.naziv}}</h2>
					<div>{{m.lokacija.naziv}}</div>
					<div>{{dateFormat(m.datumVreme,"DD.MM.YYYY. HH:mm")}}</div>
					<div>{{m.tip}}</div>
					<div>{{m.cenaKarte.toFixed(2)}} din.</div>
				</div>
			</div>
		</div>
`
	,
	methods: {
		dateFormat: function(value, format) {
    		return moment(value).format(format);
    	},
		pretragaNaziv: function() {
			axios
			.get("/manifestacije/pretraga/naziv", {
				params: {naziv: this.naziv}
			})
			.then(response => { this.manifestacije = response.data; });
		},
		kombinovanaPretraga: function() {
			axios
			.get("/manifestacije/pretraga/komb", {
				params: this.parametri
			})
			.then(response => { this.manifestacije = response.data; });
		}
	},
	mounted() {
		axios
			.get("/manifestacije/")
			.then(response => { this.manifestacije = response.data; });
		axios
			.get("/manifestacije/mesta")
			.then(response => { this.mesta = response.data; 
				console.log(this.mesta)
			});
	}
});
Vue.component("pocetna", {
	data: function() {
		return {
			manifestacije: [],
			mesta: [],
			tipovi: [],
			parametri: {},
			naziv: "",
			krit: "datumVreme", smer: "1",
			izabraniTipovi: [],
			nerasprodate: false,
			filtrirane:[]
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
		<button v-on:click="kombinovanaPretraga()">Pretrazi</button><br>

		<div style="float:right;margin:3px;">
			<select v-model="krit">
				<option value="" selected="selected">Sortiranje</option>
				<option value="naziv">Naziv</option>
				<option value="datumVreme">Datum i vreme</option>
				<option value="cenaKarte">Cena karte</option>
				<option value="lokacija">Lokacija</option>
			</select><br>
			<input type="radio" id="rastuce" v-model="smer" value="1">
			<label for="rastuce">rastuce</label>
			<input type="radio" id="opadajuce" v-model="smer" value="-1">
			<label for="opadajuce">opadajuce</label>
		</div>
		<div class="dropdown">
			<button class="dropbtn">Filtriranje</button>
			<div class="dropdown-content">
				<div><input type="checkbox" v-model="nerasprodate"/>Samo nerasprodate</div>
		    	<div v-for="t in tipovi"><input type="checkbox" :value=t v-model="izabraniTipovi"/>{{t}}</div>
		    	<button style="margin:3px;" @click="filtriraj()">Primeni</button>
			</div>
		</div>
	</div>
	<div v-for="m in sortirane" class="manifestacija-mali" :id="m.id">
		<a :href="'#/manifestacija/'+m.id" target="_blank">
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
					params: { naziv: this.naziv }
				})
				.then(response => { this.manifestacije = response.data; this.filtriraj();});
		},
		kombinovanaPretraga: function() {
			axios
				.get("/manifestacije/pretraga/komb", {
					params: this.parametri
				})
				.then(response => { this.manifestacije = response.data; this.filtriraj();});
		},
		filtriraj: function() {
			if (!this.izabraniTipovi && !this.nerasprodate) {
				this.filtrirane = this.manifestacije;
				return;
			}
			this.filtrirane = this.manifestacije.filter(m => {
				if (this.izabraniTipovi.length!=0 && !this.izabraniTipovi.includes(m.tip))
					return false;
				if (this.nerasprodate && m.brojKarata==0)
					return false;
				return true;
			});
		}
	},
	computed: {
		sortirane: function() {
			if (this.krit == "") return this.filtrirane;
			if (this.krit == "lokacija")
				return this.filtrirane.sort((m1 ,m2) => {
					if(m1.lokacija.mesto < m2.lokacija.mesto) return -1 * parseInt(this.smer);
					if(m1.lokacija.mesto > m2.lokacija.mesto) return 1 * parseInt(this.smer);
					return 0;
				});
		    return this.filtrirane.sort((m1 ,m2) => {
				if(m1[this.krit] < m2[this.krit]) return -1 * parseInt(this.smer);
				if(m1[this.krit] > m2[this.krit]) return 1 * parseInt(this.smer);
				return 0;
			});
		},
	},
	
	mounted() {
		axios
			.get("/manifestacije/")
			.then(response => {
				this.manifestacije = response.data;
				this.filtriraj();
			});
		axios
			.get("/manifestacije/mesta")
			.then(response => {
				this.mesta = response.data;
				console.log(this.mesta)
			});
		axios
			.get("/manifestacije/tipovi")
			.then(response => {
				this.tipovi = response.data;
				console.log(this.tipovi)
			});
	}
});
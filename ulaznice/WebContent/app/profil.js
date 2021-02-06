Vue.component("profil", {
	data: function() {
		return {
			korisnik: {},
			karte: null,
			manifestacije: null,
			korpa: [],
			izmena: false, izmenjeniPodaci: {},
			parametriPretrage: {},
			izabraniTipovi: [], statusKarte: false,
			filtriraneKarte: [],
			krit: "", smer: "1",
			izabranaKarta: {}
		}
	},
	template: ` 
<div>
	<h2>Moj profil</h2>
	<div class="forma">
		<div style="float:left;padding:20px;">
		    <img src="data/korisnici/avatar.jpg" alt="Avatar" width="100" height="100">
		    <h2>{{korisnik.ime + " " + korisnik.prezime}}</h2>
		    <h3>{{korisnik.korisnickoIme}}</h3><br>
			<div v-if="korisnik.uloga==='KUPAC'">
				Bodovi: {{this.korisnik.bodovi}}<br>
				Tip: {{korisnik.tip.imeTipa}}
			</div>
		</div>
		<div style="float:left;padding:10px;">
		  <table class="info">
		  	<tr><td>Pol:</td><td>{{korisnik.pol}}</td></tr>
		  	<tr><td>Datum rodjenja:</td><td>{{dateFormat(this.korisnik.datumRodjenja,"DD.MM.YYYY.")}}</td></tr>
		  	<tr><td>Uloga:</td><td>{{korisnik.uloga}}</td></tr>
		  </table><br>
			<button @click="izmena=true">Izmeni podatke</button>
			<div v-if="izmena">
				<table>
					<tr><td><label>Ime:</label></td>
						<td><input type="text" v-model="izmenjeniPodaci.ime"/></td></tr>
					<tr><td><label>Prezime:</label></td>
						<td><input type="text" v-model="izmenjeniPodaci.prezime"/></td></tr>
					<tr><td><label>Nova lozinka:</label></td>
						<td><input type="password" v-model="izmenjeniPodaci.lozinka1"/></td></tr>
					<tr><td><label>Lozinka opet:</label></td>
						<td><input type="password" v-model="izmenjeniPodaci.lozinka2"/></td></tr>
					<tr><td><label>Datum rodjenja:</label></td>
						<td><input type="date" v-model="izmenjeniPodaci.dr"/></td></tr>
					<tr><td><label>Pol:</label></td>
						<td>
							<input type="radio" id="m" v-model="izmenjeniPodaci.pol" value="M">
							<label for="m">muski</label>
							<input type="radio" id="z" v-model="izmenjeniPodaci.pol" value="Z">
							<label for="z">zenski</label>
						</td></tr>
					<tr><td><button @click="izmena=false">Odustani</button></td>
						<td><button @click="sacuvajPromene()">Sacuvaj</button></td></tr>
				</table>
			</div>
		</div>
		<div style="padding:10px;" v-if="korisnik.uloga==='KUPAC'">
		  	<button type="button" class="collapsible" @click="Korpa()">Moja Korpa</button>
			<div class="content" id="sadrzaj">
				<table border="1">
				  <tr bgcolor="lightgrey">
					<th>Manifestacija</th><th>Tip karte</th><th>Cena 1 karte</th><th>Kolicina</th></tr>
					<tr v-for="s in korpa">
						<td>{{s.naziv}}</td>
						<td>{{s.tipKarte}}</td>
						<td>{{s.cena.toFixed(2)}} din</td>
						<td>{{s.komada}}</td>
					</tr>
					<tr><td></td><td></td><td colspan="2" align="right">{{ukupnaCena.toFixed(2)}} din</td></tr>
				</table><br>
				<button type="button" @click="isprazni()">Isprazni korpu</button>
				<button type="button" @click="rezervisi()">Rezervisi karte</button>
			</div>
		</div>
	</div>
	<div v-if="korisnik.uloga==='KUPAC'">
		<button type="button" class="collapsible" @click="Moje()">Moje Karte</button>
		<div class="content" id="moje">
				
			<div class="komb-pretraga">
				<input type="text" placeholder="Manifestacija" v-model="parametriPretrage.naziv">
				<button @click="pretragaPoManifestaciji()">Pretrazi</button>
				<input type="date" placeholder="Datum od" v-model="parametriPretrage.datumod">
				<input type="date" placeholder="Datum do" v-model="parametriPretrage.datumdo">
				<button @click="pretragaPoDatumu()">Pretrazi</button>
				<input type="number" placeholder="Cena od" v-model="parametriPretrage.cenaod">
				<input type="number" placeholder="Cena do" v-model="parametriPretrage.cenado">
				<button @click="pretragaPoCeni()">Pretrazi</button>
				<div class="dropdown">
					<button class="dropbtn">Filtriranje</button>
					<div class="dropdown-content">
						<div><input type="checkbox" value="REGULAR" v-model="izabraniTipovi"/>REGULAR</div>
						<div><input type="checkbox" value="FAN_PIT" v-model="izabraniTipovi"/>FAN_PIT</div>
						<div><input type="checkbox" value="VIP" v-model="izabraniTipovi"/>VIP</div>
				    	<button style="margin:3px;" @click="filtrirajKarte()">Primeni</button>
					</div>
				</div><br>
				<button @click="odustanak()" :disabled="izabranaKarta.id==undefined || (izabranaKarta.datumVreme-new Date().getTime())/86400000.0<7">
					Odustani od rezervacije</button>
			</div>
				
			<table border="1" class="karte">
				<tr bgcolor="lightgrey">
					<th>ID karte</th>
					<th @click="sort('nazivManifestacije')">Manifestacija</th>
					<th @click="sort('datumVreme')">Datum</th>
					<th>Tip</th>
					<th @click="sort('cena')">Cena</th>
				</tr>
				<tr v-for="k in sortiraneKarte" @click="izabranaKarta=k" :class="{selected: izabranaKarta.id===k.id}">
					<td>{{k.id}}</td>
					<td>{{k.nazivManifestacije}}</td>
					<td>{{dateFormat(k.datumVreme, "DD.MM.YYYY. HH:mm")}}</td>
					<td>{{k.tipKarte}}</td>
					<td>{{k.cena}}</td>
				</tr>
			</table>
		</div>
	</div>
	<div v-else-if="korisnik.uloga==='PRODAVAC'">
		<button type="button" class="collapsible" @click="Moje()">Moje Manifestacije</button>
		<div class="content" id="moje">
			<table border="1">
				<tr bgcolor="lightgrey">
					<th>ID</th>
					<th>Naziv</th>
					<th>Datum</th>
					<th>Tip</th>
					<th></th>
				</tr>
				<tr v-for="m in manifestacije">
					<td>{{m.id}}</td>
					<td>{{m.naziv}}</td>
					<td>{{dateFormat(m.datumVreme, "DD.MM.YYYY. HH:mm")}}</td>
					<td>{{m.tip}}</td>
					<td><a :href="'#/manifestacija/'+m.id" target="_blank">Pregled</a></td>
				</tr>
			</table>
		</div>
	</div>
</div>
`
	,
	methods: {
		init: function() {
			axios
				.get("/trenutniKorisnik")
				.then(response => {
					this.korisnik = response.data;
					this.izmenjeniPodaci = {korisnickoIme:this.korisnik.korisnickoIme, ime:this.korisnik.ime, prezime:this.korisnik.prezime, lozinka1:null, lozinka2:null,
											dr:this.dateFormat(this.korisnik.datumRodjenja, "YYYY-MM-DD"), pol:this.korisnik.pol};
					if (this.korisnik.uloga == "KUPAC")
						this.prikazKarata();
					else if (this.korisnik.uloga == "PRODAVAC")
						this.prikazManifestacija();
				});
			if (this.korisnik.uloga == "KUPAC")
				axios
					.get("/korpa")
					.then(response => { this.korpa = response.data; })
		},
		dateFormat: function(value, format) {
			return moment(value).format(format);
		},
		prikazKarata: function() {
			axios
				.get("/karte/")
				.then(response => { this.karte = response.data.filter(karta=>{return !karta.odustanak;}); this.filtrirajKarte();})
		},
		prikazManifestacija: function() {
			axios
				.get("/manifestacije/moje")
				.then(response => { this.manifestacije = response.data; })
		},
		Korpa: function() {
			var content = document.getElementById("sadrzaj");
			if (content.style.display === "block")
				content.style.display = "none";
			else
				content.style.display = "block";
		},
		isprazni: function() {
			if (this.korpa.length === 0) return;
			if (confirm('Da li ste sigurni?'))
				axios
					.get("/isprazniKorpu")
					.then(response => {this.korpa = [];})
		},
		rezervisi: function() {
			if (this.korpa.length === 0) return;
			if (confirm('Da li ste sigurni?'))
				axios
					.post("/karte/rezervacija", this.korpa)
					.then(response => {this.korpa = [];})
		},
		Moje: function() {
			var content = document.getElementById("moje");
			if (content.style.display === "block")
				content.style.display = "none";
			else
				content.style.display = "block";
		},
		sacuvajPromene: function() {
			if (confirm('Da li ste sigurni?')) {
				if (this.izmenjeniPodaci.lozinka1===this.izmenjeniPodaci.lozinka2) {
					this.izmenjeniPodaci.datumRodjenja = new Date(this.izmenjeniPodaci.dr).getTime();
					axios
						.put("/korisnici/promenaPodataka", this.izmenjeniPodaci)
						.then(response => {this.korisnik = response.data;})
					this.izmena = false;
				}
				else toast("Lozinke nisu iste!")
			}
		},
		pretragaPoManifestaciji: function() {
			axios
				.get("/karte/pretraga/manifestacija", {
					params: { naziv: this.parametriPretrage.naziv }
				}).then(response => { this.karte = response.data; this.filtrirajKarte();});
		},
		pretragaPoCeni: function() {
			axios
				.get("/karte/pretraga/cena", {
					params: { cenaod: this.parametriPretrage.cenaod, cenado: this.parametriPretrage.cenado }
				}).then(response => { this.karte = response.data; this.filtrirajKarte();});
		},
		pretragaPoDatumu: function() {
			axios
				.get("/karte/pretraga/datum", {
					params: { datumod: this.parametriPretrage.datumod, datumdo: this.parametriPretrage.datumdo }
				}).then(response => { this.karte = response.data; this.filtrirajKarte();});
		},
		filtrirajKarte: function() {
			if (this.izabraniTipovi.length==0) {
				this.filtriraneKarte = this.karte;
				return;
			}
			this.filtriraneKarte = this.karte.filter(k => {
				return this.izabraniTipovi.includes(k.tipKarte);
			})
		},
		sort: function(s) {
		    if (s === this.krit)
		    	this.smer = this.smer==='1'?'-1':'1';
		    this.krit = s;
		},
		odustanak: function() {
			if (confirm('Da li ste sigurni da odustanete od rezervacije karte '+this.izabranaKarta.id+' ?')) {
				axios
					.put("/karte/odustanak", this.izabranaKarta)
					.then(response => {
						toast("Odustanak od rezervacije karte "+response.data.id);
						this.karte = this.karte.filter(karta=>{return !karta.odustanak;});
						this.filtrirajKarte();
					})
			}
		}
	},
	computed: {
		ukupnaCena: function() {
			var ukupna = 0.0;
			for (s of this.korpa)
				ukupna += s.komada * s.cena;
			return ukupna;
		},
		sortiraneKarte: function() {
			if (this.krit == "") return this.filtriraneKarte;
		    return this.filtriraneKarte.sort((k1 ,k2) => {
				if(k1[this.krit] < k2[this.krit]) return -1 * parseInt(this.smer);
				if(k1[this.krit] > k2[this.krit]) return 1 * parseInt(this.smer);
				return 0;
			});
		}
	},
	mounted() {
		this.init();
	}
});
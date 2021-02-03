Vue.component("profil", {
	data: function() {
		return {
			korisnik: {},
			karte: null,
			manifestacije: null,
			korpa: []
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
			<button>Izmeni podatke</button>
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
	</div><br>
	<div v-if="korisnik.uloga==='KUPAC'">
		<button type="button" class="collapsible" @click="Moje()">Moje Karte</button>
		<div class="content" id="moje">
			<table border="1">
			  <tr bgcolor="lightgrey">
				<th>ID karte</th><th>Manifestacija</th><th>Datum</th><th>Tip</th><th>Cena</th></tr>
				<tr v-for="k in karte">
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
				<th>Manifestacija</th><th>Tip karte</th><th>Cena 1 karte</th><th>Kolicina</th></tr>
				<tr v-for="s in korpa">
					<td>{{s.naziv}}</td>
					<td>{{s.tipKarte}}</td>
					<td>{{s.cena}}</td>
					<td>{{s.komada}}</td>
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
					if (this.korisnik.uloga == "KUPAC")
						this.prikazKarata();
					else if (this.korisnik.uloga == "PRODAVAC")
						this.prikazManifestacija();
				});
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
				.then(response => { this.karte = response.data; })
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
					.then(response => {
						this.korpa = [];
					})
		},
		rezervisi: function() {
			if (this.korpa.length === 0) return;
			if (confirm('Da li ste sigurni?'))
				axios
					.post("/karte/rezervacija", this.korpa)
					.then(response => {
						this.korpa = [];
					})
		},
		Moje: function() {
			var content = document.getElementById("moje");
			if (content.style.display === "block")
				content.style.display = "none";
			else
				content.style.display = "block";
		}
	},
	computed: {
		ukupnaCena: function() {
			var ukupna = 0.0;
			for (s of this.korpa)
				ukupna += s.komada * s.cena;
			return ukupna;
		}
	},
	mounted() {
		this.init();
	}
});
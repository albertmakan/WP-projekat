Vue.component("profil", {
	data: function() {
		return {
			korisnik: {},
			karte: null,
			manifestacije: null
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
			<p id="bodovi"></p>
			<p id="tip"></p>
		</div>
		<div style="padding:10px;">
		  <table class="korisnik-info">
		  	<tr><td>Pol:</td><td>{{korisnik.pol}}</td></tr>
		  	<tr><td>Datum rodjenja:</td><td>{{dateFormat(this.korisnik.datumRodjenja,"DD.MM.YYYY.")}}</td></tr>
		  	<tr><td>Uloga:</td><td>{{korisnik.uloga}}</td></tr>
		  </table><br>
			<button>Izmeni podatke</button>
		</div>
		
	</div>
	<div>
		<table border="1">
			
		</table>
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
					if (this.korisnik.uloga == "KUPAC") {
						document.getElementById("bodovi").innerHTML = "Bodovi: "+this.korisnik.bodovi;
						document.getElementById("tip").innerHTML = "Tip: "+this.korisnik.tip.imeTipa;
						this.prikazKarata();
					} else if (this.korisnik.uloga == "PRODAVAC") {
						this.prikazManifestacija();
					}
				});
		},
		dateFormat: function (value, format) {
    		return moment(value).format(format);
    	},
		prikazKarata: function() {
			axios
			.get("/karte/")
			.then(response => {this.karte = response.data;})
		},
		prikazManifestacija: function() {
			axios
			.get("/manifestacije/moje")
			.then(response => {this.manifestacije = response.data;})
		}
	},
	mounted() {
		this.init();
	}
});
Vue.component("manifestacija", {
	data: function() {
		return {
			manifestacija: { lokacija: {}, id: this.$route.params.id },
			map: null,
			komentari: [], karte: null,
			korisnik: {},
			komentar: {},
			mozeOstavitiKomentar: false,
			izabranKoment: {}
		}
	},
	template: ` 
<div>
	<div>
		<div class="poster">
			<img :src="'data/posteri/'+manifestacija.id+'.png'"
					onerror="this.onerror=null; this.src='data/posteri/noposter.jpg'" alt="Poster" width="400px" height="600px" />
		</div>
		<div style="display:inline-block;margin:20px;">
			<h1>{{manifestacija.naziv}}</h1>
			<table class="info">
				<tr><td>Tip: </td><td>{{manifestacija.tip}}</td></tr>
				<tr><td>Pocetak: </td><td>{{dateFormat(manifestacija.datumVreme,"DD.MM.YYYY. HH:mm")}}</td></tr>
				<tr><td>Broj mesta: </td><td>{{manifestacija.brojMesta}}</td></tr>
				<tr><td>Preostali broj katrata: </td><td>{{manifestacija.brojKarata}}</td></tr>
				<tr><td>Status: </td><td>{{manifestacija.aktivna? "aktivan":"neaktivan"}}</td></tr>
			</table>
			<div style="margin:20px;">
				<div id="map" class="map"></div>
				<div>{{manifestacija.lokacija.adresa}}, {{manifestacija.lokacija.mesto}}, {{manifestacija.lokacija.postBroj}}</div>
			</div>
		</div>
		<div style="display:inline-block;margin:20px;">
			<div v-if="korisnik.uloga=='ADMIN' || korisnik.uloga=='PRODAVAC'">
				<button type="button" class="collapsible" @click="Karte()">Karte</button>
				<div class="content" id="karte">
					<table border="1" class="karte">
						<tr bgcolor="lightgrey">
							<th>ID karte</th>
							<th>Kupac</th>
							<th>Tip</th>
							<th>Status</th>
						</tr>
						<tr v-for="k in karte">
							<td>{{k.id}}</td>
							<td>{{k.kupac}}</td>
							<td>{{k.tipKarte}}</td>
							<td align="right">{{k.odustanak?'O':'R'}}</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div><br>
	
	<div class="rezervacija" v-if="korisnik.uloga=='KUPAC'">
		<table border="1">
			<tr>
				<td>Regular</td><td>{{manifestacija.cenaKarte}} din</td>
				<td>
					<input id="REGULAR" type="number" size="3" min="1" :max=manifestacija.brojKarata value="0"> 
					<button v-on:click="dodajUKorpu('REGULAR')">Dodaj</button>
				</td>
			</tr>
			<tr>
				<td>Fan pit</td><td>{{manifestacija.cenaKarte*2}} din</td>
				<td>
					<input id="FAN_PIT" type="number" size="3" min="1" :max=manifestacija.brojKarata value="0"> 
					<button v-on:click="dodajUKorpu('FAN_PIT')">Dodaj</button>
				</td>
			</tr>
			<tr>
				<td>VIP</td><td>{{manifestacija.cenaKarte*4}} din</td>
				<td>
					<input id="VIP" type="number" size="3" min="1" :max=manifestacija.brojKarata value="0"> 
					<button v-on:click="dodajUKorpu('VIP')">Dodaj</button>
				</td>
			</tr>
		</table>
	</div>
	<div v-else-if="korisnik.uloga=='ADMIN'">
		<button v-on:click="odobri()">Odobri manifestaciju</button>
	</div>
	
	<div class="komentari">
		<h3>Komentari ({{this.komentari.length}})</h3>
		<button v-if="korisnik.uloga=='PRODAVAC'" v-on:click="odobriKomentar()">Odobri komentar</button>
		<div style="max-height:300px;overflow:scroll;">
			<div class="komentar" v-for="kom in komentari" @click="izabranKoment=kom">
				<i>{{kom.kupac}}</i><br>
				{{kom.tekst}}<br>
				Ocena: {{kom.ocena}}
			</div>
		</div>
		<div class="komentar" v-if="mozeOstavitiKomentar">
			<textarea v-model="komentar.tekst" rows="4" cols="50"></textarea>
			<button @click="dodajKomentar()" :disablsed="!komentar.tekst||!komentar.ocena">Posalji</button><br>
			Ocena: 
			<input type="radio" id="1" v-model="komentar.ocena" value="1"><label for="1">1</label>
			<input type="radio" id="2" v-model="komentar.ocena" value="2"><label for="2">2</label>
			<input type="radio" id="3" v-model="komentar.ocena" value="3"><label for="3">3</label>
			<input type="radio" id="4" v-model="komentar.ocena" value="4"><label for="4">4</label>
			<input type="radio" id="5" v-model="komentar.ocena" value="5"><label for="5">5</label>
		</div>
	</div>
	
</div>
`
	,
	methods: {
		dateFormat: function(value, format) {
			return moment(value).format(format);
		},
		dodajUKorpu: function(tip) {
			if (!this.manifestacija.aktivna) {
				alert("Manifestacija nije jos aktivna");
				return;
			}
			let kom = document.getElementById(tip).value;
			axios
				.post("/dodajUKorpu", {idManifestacije: this.manifestacija.id, komada: kom, tipKarte: tip})
				.then(response => {toast(response.data)});
		},
		odobri: function() {
			if (this.manifestacija.aktivna)
				toast("Ova manifestacija je vec aktivna.")
			else {
				axios
					.put("/manifestacije/odobri", this.manifestacija)
					.then(response => {
						toast("Manifestacija je odobrena.");
						this.manifestacija.aktivna = true;
					})
			}
		},
		Karte: function() {
			var content = document.getElementById("karte");
			if (content.style.display === "block")
				content.style.display = "none";
			else {
				if (this.karte==null) {
					axios
						.get("/karte/"+this.manifestacija.id)
						.then(response => {
							if (this.korisnik.uloga=='ADMIN')
								this.karte = response.data;
							else
								this.karte = response.data.filter(karta=>{return !karta.odustanak;});
						})
				}
				content.style.display = "block";
			}
		},
		dodajKomentar: function() {
			this.komentar.idManifestacije = this.manifestacija.id;
			axios
				.post("/manifestacije/dodajKomentar", this.komentar)
				.then(response => {
					toast("Komentar je poslat.");
					this.komentar = {};
				})
		},
		odobriKomentar: function() {
			axios
				.put("/manifestacije/odobriKomentar", this.izabranKoment)
				.then(response => {
					toast("komentar je odobren")
				})
		}
	},
	mounted() {
		axios
			.get("/trenutniKorisnik")
			.then(response => {
				this.korisnik = response.data;
				if (this.korisnik == null) this.korisnik = {uloga: ''};
			})
		axios
			.get("/manifestacije/" + this.$route.params.id)
			.then(response => {
				this.manifestacija = response.data;

				var marker = new ol.Feature({
					geometry: new ol.geom.Point(ol.proj.fromLonLat([this.manifestacija.lokacija.geoDuzina, this.manifestacija.lokacija.geoSirina]))
				});
				marker.setStyle(new ol.style.Style({ image: new ol.style.Icon(({ crossOrigin: 'anonymous', src: 'marker.png' })) }));
				this.map = new ol.Map({
					target: 'map',
					layers: [new ol.layer.Tile({ source: new ol.source.OSM() }),
					new ol.layer.Vector({ source: new ol.source.Vector({ features: [marker] }) })
					],
					view: new ol.View({
						center: ol.proj.fromLonLat([this.manifestacija.lokacija.geoDuzina, this.manifestacija.lokacija.geoSirina]),
						zoom: 15
					})
				});
			});
		axios
			.get("/manifestacije/"+this.$route.params.id+"/komentari")
			.then(response => {
				this.komentari = response.data? response.data:[];
				if (this.korisnik.uloga!='ADMIN' && this.korisnik.uloga!='PRODAVAC')
					this.komentari = this.komentari.filter(k=>{return k.odobren;});
			});
		axios
			.get("/moguDaOstavimKomentar/"+this.$route.params.id)
			.then(response => {this.mozeOstavitiKomentar=response.data;})
	},
});
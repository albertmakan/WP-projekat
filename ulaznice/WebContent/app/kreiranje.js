Vue.component("kreiranje", {
	data: function() {
		return {
			manifestacija: {},
			tipovi: ["koncert", "festival", "pozoriste"],
			lokacija: {},
			map: null,
			selectedFile: null,
		}
	},
	template: ` 
	<div>
		<h2>Nova manifestacija</h2>
		<div class="forma">
			<div style="float:left;">
			<form id="manifForm">
				<table>
					<tr>
						<td><label>Naziv:</label></td>
						<td><input type="text" v-model="manifestacija.naziv" required/></td>
					</tr>
					<tr>
						<td><label>Tip:</label></td>
						<td><input list="tipovi" type="text" v-model="manifestacija.tip" required/>
							<datalist id="tipovi">
							    <option v-for="t in tipovi">{{t}}</option>
						  	</datalist>
						</td>
					</tr>
					<tr>
						<td><label>Broj mesta:</label></td>
						<td><input type="number" v-model="manifestacija.brojMesta" min="10" max="5000" required></td>
					</tr>
					<tr>
						<td><label>Datum odrzavanja:</label></td>
						<td><input type="datetime-local" v-model="manifestacija.datumVreme" required/></td>
					</tr>
					<tr>
						<td><label>Cena karte:</label></td>
						<td><input type="number" v-model="manifestacija.cenaKarte" min="10" max="20000" step="0.1" required></td>
					</tr>
					<tr>
						<td><label>Lokacija:</label></td>
						<td><input type="number" v-model="lokacija.geoDuzina" 
								placeholder="Geo duzina" min="-90" max="90" step="0.001" style="width:70px;" required>
							<input type="number" v-model="lokacija.geoSirina" 
								placeholder="Geo sirina" min="-180" max="180" step="0.001" style="width:70px;" required><br>
							<input type="text" v-model="lokacija.naziv" placeholder="Naziv" required/><br>
							<input type="text" v-model="lokacija.adresa" placeholder="Adresa" required/><br>
							<input type="text" v-model="lokacija.mesto" placeholder="Mesto" required/><br>
							<input type="text" v-model="lokacija.postBroj" placeholder="Postanski broj" required/><br>
							</td>
					</tr>
					<tr>
						<td><label>Poster:</label></td>
						<td>
							<input type="file" ref="file" accept="image/*" v-on:change="onFileSelected()"/>
						</td>
					</tr>
					<tr>
						<td></td>
						<td><button v-on:click="kreiraj()">Kreiraj</button></td>
					</tr>
				</table>
			</form>
			</div>
			<div style="margin-left: 50px;">
				<div id="map" class="map"></div>
				<input type="text" id="search" placeholder="Pronadji..."/>
				<button v-on:click="pronadjiLokaciju()">OK</button>
			</div>
		</div>
	</div>
`
	,
	methods: {
		kreiraj: function() {
			var form = document.getElementById("manifForm");
			if (!form.checkValidity()) { toast("invalid"); return; }
			var m = {
				naziv: this.manifestacija.naziv, tip: this.manifestacija.tip, brojMesta: this.manifestacija.brojMesta,
				datumVreme: new Date(this.manifestacija.datumVreme).getTime(), cenaKarte: this.manifestacija.cenaKarte, lokacija: this.lokacija
			};
			axios
				.post("/manifestacije/nova", m)
				.then(response => {
					this.uploadPoster(response.data.id);
					alert("Manifestacija" + response.data.naziv + " [" + response.data.id + "] je uspesno kreirana.");
				});
		},
		onFileSelected: function() {
			this.selectedFile = this.$refs.file.files[0];
		},
		uploadPoster: function(manif_id) {
			const fd = new FormData();
			fd.append('file', this.selectedFile);
			axios.post("/manifestacije/poster/"+manif_id, fd)
			.then(response => {toast(response.data)});
		},
		pronadjiLokaciju: function() {
			var query = {
				"format": "json", "addressdetails": 1, "limit": 1,
				"q": document.getElementById("search").value
			};
			$.ajax({
				method: "GET",
				url: "https://nominatim.openstreetmap.org",
				data: query,
				success: msg => {
					console.log(msg);
					this.lokacija = {
						geoDuzina: parseFloat(msg[0].lon),
						geoSirina: parseFloat(msg[0].lat),
						naziv: msg[0].address[msg[0].class],
						adresa: msg[0].address.road + " " + msg[0].address.house_number,
						mesto: msg[0].address.city,
						postBroj: msg[0].address.postcode
					}
					this.map.getView().setCenter(ol.proj.transform(
						[this.lokacija.geoDuzina, this.lokacija.geoSirina],
						'EPSG:4326', 'EPSG:3857'
					));
					this.map.getView().setZoom(15);
					var layer = new ol.layer.Vector({
						source: new ol.source.Vector({
							features: [
								new ol.Feature({
									geometry: new ol.geom.Point(ol.proj.fromLonLat([this.lokacija.geoDuzina, this.lokacija.geoSirina]))
								})
							]
						})
					});
					this.map.addLayer(layer);
				}
			})
		},

	},
	mounted() {
		this.map = new ol.Map({
			target: 'map',
			layers: [new ol.layer.Tile({ source: new ol.source.OSM() })],
			view: new ol.View({
				center: ol.proj.transform([20, 45], 'EPSG:4326', 'EPSG:3857'),
				zoom: 4
			})
		});
		axios
			.get("/manifestacije/tipovi")
			.then(response => { this.tipovi.concat(response.data) });
	}
});
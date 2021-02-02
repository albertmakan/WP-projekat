Vue.component("manifestacija", {
	data: function() {
		return {
			manifestacija: { lokacija: {}, id: this.$route.params.id },
			map: null,
			komentari: []
		}
	},
	template: ` 
<div><div>
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
	</div></div><br>
	
	<div class="rezervacija">
		<table border="1">
			<tr>
				<td>Regular</td><td>{{manifestacija.cenaKarte}} din</td>
				<td>
					<input type="number" size="3" min="1" :max=manifestacija.brojKarata> 
					<button v-on:click="">Dodaj</button>
				</td>
			</tr>
			<tr>
				<td>Fan pit</td><td>{{manifestacija.cenaKarte*2}} din</td>
				<td>
					<input type="number" size="3" min="1" :max=manifestacija.brojKarata> 
					<button v-on:click="">Dodaj</button>
				</td>
			</tr>
			<tr>
				<td>VIP</td><td>{{manifestacija.cenaKarte*4}} din</td>
				<td>
					<input type="number" size="3" min="1" :max=manifestacija.brojKarata> 
					<button v-on:click="">Dodaj</button>
				</td>
			</tr>
		</table>
	</div>
	<div class="komentari">
		<h3>Komentari ({{this.komentari.length}})</h3>
		<table>
		
		</table>
	</div>
	
</div>
`
	,
	methods: {
		dateFormat: function(value, format) {
			return moment(value).format(format);
		},
	},
	mounted() {
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
			.then(response => {this.komentari = response.data? response.data:[];});
	}
});
Vue.component("kreiranje", {
	data: function() {
		return {
			manifestacija: {lokacija:{}},
			selectedFile: null
		}
	},
	template: ` 
	<div>
		<h2>Nova manifestacija</h2>
		<div class="forma">
		<form id="regForm">
			<table>
				<tr>
					<td><label>Naziv:</label></td>
					<td><input type="text" v-model="manifestacija.naziv" required/></td>
				</tr>
				<tr>
					<td><label>Tip:</label></td>
					<td><input type="text" v-model="manifestacija.tip" required/></td>
				</tr>
				<tr>
					<td><label>Broj mesta:</label></td>
					<td><input type="number" v-model="manifestacija.brojMesta" min="10" max="5000" required></td>
				</tr>
				<tr>
					<td><label>Datum odrzavanja:</label></td>
					<td><input type="date" v-model="manifestacija.datumVreme" required/></td>
				</tr>
				<tr>
					<td><label>Cena karte:</label></td>
					<td><input type="number" v-model="manifestacija.cenaKarte" min="10" max="20000" step="0.1" required></td>
				</tr>
				<tr>
					<td><label>Lokacija:</label></td>
					<td><input type="number" v-model="manifestacija.lokacija.geoDuzina" placeholder="Geo duzina" required>
						<input type="number" v-model="manifestacija.lokacija.geoSirina" placeholder="Geo sirina" required><br>
						<input type="text" v-model="manifestacija.lokacija.naziv" placeholder="Naziv" required/><br>
						<input type="text" v-model="manifestacija.lokacija.adresa" placeholder="Adresa" required/><br>
						<input type="text" v-model="manifestacija.lokacija.mesto" placeholder="Mesto" required/><br>
						<input type="text" v-model="manifestacija.lokacija.postBroj" placeholder="Postanski broj" required/><br>
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
					<td><button v-on:click="kreiraj(manifestacija)">Kreiraj</button></td>
				</tr>
			</table>
		</form>
		</div>
	</div>
`
	,
	methods: {
		kreiraj: function(manifestacija) {
    		var m = {naziv: manifestacija.naziv};

			this.onUpload();
    	},
		onFileSelected: function() {
    		this.selectedFile = this.$refs.file.files[0];  
    	},
    	onUpload: function() {
			const fd = new FormData();
			fd.append('file', this.selectedFile)
    		axios.post('rest/manifestacije/poster', fd)
    	}
	},
	mounted() {

	}
});
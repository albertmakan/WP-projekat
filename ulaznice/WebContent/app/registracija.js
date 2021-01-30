Vue.component("registracija", {
	data: function() {
		return {
			korisnik: {}
		}
	},
	template: ` 
	<div>
		<h2>Registrujte se</h2>
		<div class="forma">
			<table>
				<tr>
					<td><label>Ime:</label></td>
					<td><input type="text" v-model="korisnik.ime" /></td>
				</tr>
				<tr>
					<td><label>Prezime:</label>
					<td><input type="text" v-model="korisnik.prezime" /></td>
				</tr>
				<tr>
					<td><label>Korisnicko ime:</label>
					<td><input type="text" v-model="korisnik.korisnickoIme" /></td>
				</tr>
				<tr>
					<td><label>Lozinka:</label>
					<td><input type="password" v-model="korisnik.lozinka" /></td>
				</tr>
				<tr>
					<td><label>Datum rodjenja:</label>
					<td><input type="date" v-model="korisnik.datumRodjenja" /></td>
				</tr>
				<tr>
					<td><label>Pol:</label></td>
					<td><input type="radio" id="m" v-model="korisnik.pol" value="M">
						<label for="m">muski</label> <input type="radio" id="z"
						v-model="korisnik.pol" value="Z"> <label for="z">zenski</label></td>
				</tr>
				<tr>
					<td></td>
					<td><button v-on:click="registracija(korisnik)">Registruj se</button></td>
				</tr>
			</table>
		</div>
	</div>
`
	,
	methods: {
		registracija : function(korisnik) {
    		var k = {korisnickoIme: korisnik.korisnickoIme, lozinka: korisnik.lozinka, pol: korisnik.pol,
					ime: korisnik.ime, prezime: korisnik.prezime, datumRodjenja: new Date(korisnik.datumRodjenja).getTime()};
    		axios
    		.post("/registracija", k)
    		.then(response => toast(response.data));
    	},
	},
	mounted() {

	}
});
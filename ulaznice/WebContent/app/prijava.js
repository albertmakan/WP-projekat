Vue.component("prijava", {
	data: function() {
		return {
			korisnik: {}
		}
	},
	template: ` 
	<div>
		<h2>Prijavite se</h2>
		<div class="forma">
			<table>
				<tr>
					<td><label>Korisnicko ime:</label>
					<td><input type="text" v-model="korisnik.korisnickoIme" /></td>
				</tr>
				<tr>
					<td><label>Lozinka:</label>
					<td><input type="password" v-model="korisnik.lozinka" /></td>
				</tr>
				<tr>
					<td></td>
					<td><button v-on:click="login(korisnik)">Uloguj se</button></td>
				</tr>
			</table>
			<a href="#/registracija">Registruj se</a>
		</div>
	</div>
`
	,
	methods: {
		login : function(korisnik) {
    		var k = {korisnickoIme: korisnik.korisnickoIme, lozinka: korisnik.lozinka};
    		axios
    		.post("/login", k)
    		.then(response => toast(response.data));
    	},
	},
	mounted() {

	}
});
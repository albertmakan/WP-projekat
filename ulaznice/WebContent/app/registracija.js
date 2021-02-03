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
	<form id="regForm">
		<table>
			<tr>
				<td><label>Ime:</label></td>
				<td><input type="text" v-model="korisnik.ime" required/></td>
			</tr>
			<tr>
				<td><label>Prezime:</label></td>
				<td><input type="text" v-model="korisnik.prezime" required/></td>
			</tr>
			<tr>
				<td><label>Korisnicko ime:</label></td>
				<td><input type="text" v-model="korisnik.korisnickoIme" required pattern="[a-zA-Z0-9._]{4,30}"/></td>
			</tr>
			<tr>
				<td><label>Lozinka:</label></td>
				<td><input type="password" v-model="korisnik.lozinka" required/></td>
			</tr>
			<tr>
				<td><label>Datum rodjenja:</label></td>
				<td><input type="date" v-model="korisnik.datumRodjenja" required/></td>
			</tr>
			<tr>
				<td><label>Pol:</label></td>
				<td>
					<input type="radio" id="m" v-model="korisnik.pol" value="M">
					<label for="m">muski</label>
					<input type="radio" id="z" v-model="korisnik.pol" value="Z">
					<label for="z">zenski</label>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><button v-on:click="registracija(korisnik)">Registruj se</button></td>
			</tr>
		</table>
	</form>
	</div>
</div>
`
	,
	methods: {
		registracija: function(korisnik) {
			var form = document.getElementById("regForm");
			if (!form.checkValidity()) { toast("invalid"); return; }
			var k = {
				korisnickoIme: korisnik.korisnickoIme, lozinka: korisnik.lozinka, pol: korisnik.pol,
				ime: korisnik.ime, prezime: korisnik.prezime, datumRodjenja: new Date(korisnik.datumRodjenja).getTime()
			};
			axios
				.post("/registracija", k)
				.then(response => {
				if (response.data == "OK") {
					alert("Uspesna registracija")
					window.location.href = "#/";
					document.getElementById("korisnik_link").innerHTML = "<a href='#/profil'>"+korisnik.korisnickoIme+"</a>"+
																		"<a href='javascript:logout()'>Odjava</a>";
				} else {
					toast(response.data);
				}
			});
		},
	},
	mounted() {

	}
});
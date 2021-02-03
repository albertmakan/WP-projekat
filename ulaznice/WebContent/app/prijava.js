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
				<td><label>Korisnicko ime:</label></td>
				<td><input type="text" v-model="korisnik.korisnickoIme" /></td>
			</tr>
			<tr>
				<td><label>Lozinka:</label></td>
				<td><input type="password" v-model="korisnik.lozinka" /></td>
			</tr>
			<tr>
				<td></td>
				<td><button v-on:click="login(korisnik)">Uloguj se</button></td>
			</tr>
			<tr>
				<td><a href="#/registracija">Registruj se</a></td>
			</tr>
		</table>
	</div>
</div>
`
	,
	methods: {
		login : function(korisnik) {
    		var k = {korisnickoIme: korisnik.korisnickoIme, lozinka: korisnik.lozinka};
    		axios
    		.post("/login", k)
    		.then(response => {
				if (response.data == "KUPAC") {
					window.location.href = "#/";
					//document.getElementById("aktivnosti").innerHTML = "probaKupac";
					document.getElementById("korisnik_link").innerHTML = "<a href='#/profil'>"+korisnik.korisnickoIme+"</a>"+
																		"<a href='javascript:logout()'>Odjava</a>";
				} else if (response.data == "PRODAVAC") {
					window.location.href = "#/";
					document.getElementById("aktivnosti").innerHTML = "<a href='#/kreiranje'>Kreiranje manifestacije</a>";
					document.getElementById("korisnik_link").innerHTML = "<a href='#/profil'>"+korisnik.korisnickoIme+"</a>"+
																		"<a href='javascript:logout()'>Odjava</a>";
				} else if (response.data == "ADMIN") {
					window.location.href = "#/";
					document.getElementById("aktivnosti").innerHTML = "<a href='#/registracija'>Registracija prodavca</a>"+
																		"<a href='#'>Pregled korisnika</a>";
					document.getElementById("korisnik_link").innerHTML = "<a href='#/profil'>"+korisnik.korisnickoIme+"</a>"+
																		"<a href='javascript:logout()'>Odjava</a>";
				} else toast(response.data);
			});
    	},
	},
	mounted() {

	}
});
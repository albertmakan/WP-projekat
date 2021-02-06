Vue.component("korisnici", {
	data: function() {
		return {
			korisnici: [],
			kritPretrage: "0", ime: "",
			krit: "", smer: "1",
			izabraneUloge: [], izabraniTipovi: [], izabranKupac: false,
			filtrirani: [],
			izabranKorisnik: {}
		}
	},
	template: ` 
<div>
	<div class="komb-pretraga">
		<div class="dropdown">
			<button class="dropbtn">Filtriranje</button>
			<div class="dropdown-content">
				<div><input type="checkbox" value="KUPAC" v-model="izabranKupac"/>Kupci</div>
				<div><input type="checkbox" value="PRODAVAC" v-model="izabraneUloge"/>Prodavci</div>
				<div><input type="checkbox" value="ADMIN" v-model="izabraneUloge"/>Admini</div>
				<div>Tipovi kupca:</div>
				<div><input type="checkbox" :disabled="!izabranKupac" value="ZELENI" v-model="izabraniTipovi"/>Zeleni</div>
				<div><input type="checkbox" :disabled="!izabranKupac" value="BRONZANI" v-model="izabraniTipovi"/>Bronzani</div>
				<div><input type="checkbox" :disabled="!izabranKupac" value="SREBRNI" v-model="izabraniTipovi"/>Srebrni</div>
				<div><input type="checkbox" :disabled="!izabranKupac" value="ZLATNI" v-model="izabraniTipovi"/>Zlatni</div>
		    	<button style="margin:3px;" @click="filtriraj()">Primeni</button>
			</div>
		</div>
		<div style="display:inline-block;margin-left:30px;">
			<input type="text" placeholder="Pretraga..." v-model="ime">
			<button v-on:click="pretraga()">Pretrazi</button><br>
			<input type="radio" id="korisnicko" v-model="kritPretrage" value="0">
			<label for="korisnicko">korisnicko</label>
			<input type="radio" id="ime" v-model="kritPretrage" value="1">
			<label for="ime">ime</label>
			<input type="radio" id="prezime" v-model="kritPretrage" value="2">
			<label for="prezime">prezime</label>
		</div>
		<button style="float:right;" @click="blokiraj()" :disabled="izabranKorisnik.korisnickoIme==undefined || izabranKorisnik.uloga=='ADMIN'">Blokiraj korisnika</button>
		<button style="float:right;" @click="sumnjivi()">Prikaz sumnjivih</button>
	</div>
	<table border="1" class="korisnici">
	  	<tr bgcolor="lightgrey">
			<th @click="sort('korisnickoIme')">Korisnicko ime</th>
			<th @click="sort('ime')">Ime</th>
			<th @click="sort('prezime')">Prezime</th>
			<th>Uloga</th>
			<th @click="sort('bodovi')">Bodovi</th>
			<th>Tip</th>
		</tr>
		<tr v-for="k in sortirani" @click="izabranKorisnik=k" :class="{selected: izabranKorisnik.korisnickoIme===k.korisnickoIme}">
			<td>{{k.korisnickoIme}}</td>
			<td>{{k.ime}}</td>
			<td>{{k.prezime}}</td>
			<td>{{k.uloga}}</td>
			<td>{{k.bodovi}}</td>
			<td>{{k.tip?k.tip.imeTipa:""}}</td>
		</tr>
	</table>
</div>
`
	,
	methods: {
		pretraga: function() {
			axios
				.get("/korisnici/pretraga/", {
					params: { ime: this.ime, krit: this.kritPretrage }
				})
				.then(response => { this.korisnici = response.data; this.filtriraj();});
		},
		filtriraj: function() {
			if (this.izabraniTipovi.length==0 && !this.izabranKupac) {
				this.filtrirani = this.korisnici;
				return;
			}
			this.filtrirani = this.korisnici.filter(k => {
				if (this.izabraneUloge.includes(k.uloga))
					return true;
				if (k.uloga == 'KUPAC' && this.izabranKupac)
					if (this.izabraniTipovi.includes(k.tip.imeTipa))
						return true;
				return false;
			});
		},
		sort: function(s) {
		    if (s === this.krit)
		    	this.smer = this.smer==='1'?'-1':'1';
		    this.krit = s;
		},
		blokiraj: function() {
			if (confirm('Da li ste sigurni da blokirate '+this.izabranKorisnik.korisnickoIme+'?')) {
				axios
					.put("/korisnici/blokiranje", this.izabranKorisnik)
					.then(response => {toast(response.data)})
			}
		},
		sumnjivi: function() {
			axios
				.get("/korisnici/sumnjivi")
				.then(response => {toast("Not implemented")})
		}
	},
	computed: {
		sortirani: function() {
			if (this.krit == "") return this.filtrirani;
		    return this.filtrirani.sort((k1 ,k2) => {
				if(k1[this.krit] < k2[this.krit]) return -1 * parseInt(this.smer);
				if(k1[this.krit] > k2[this.krit]) return 1 * parseInt(this.smer);
				return 0;
			});
		},
	},
	mounted() {
		axios
			.get("/korisnici/")
			.then(response => {this.korisnici = response.data; this.filtriraj();})
	}
});
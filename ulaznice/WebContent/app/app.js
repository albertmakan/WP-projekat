const Pocetna = { template: '<pocetna></pocetna>' }
const Prijava = { template: '<prijava></prijava>' }
const Registracija = { template: '<registracija></registracija>' }
const Profil = { template: '<profil></profil>' }
const Kreiranje = { template: '<kreiranje></kreiranje>' }
const Manifestacija = { template: '<manifestacija></manifestacija>' }


const router = new VueRouter({
	mode: 'hash',
	routes: [
		{ path: '/', component: Pocetna },
		{ path: '/prijava', component: Prijava },
		{ path: '/registracija', component: Registracija },
		{ path: '/profil', component: Profil },
		{ path: '/kreiranje', component: Kreiranje },
		{ path: '/manifestacija/:id', component: Manifestacija },
	]
});

var app = new Vue({
	router,
	el: '#ulaznice'
});

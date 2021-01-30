Vue.component("pocetna", {
	data: function() {
		return {
			manifestacije: null
		}
	},
	template: ` 
		
`
	,
	methods: {

	},
	mounted() {
		axios
			.get("/manifestacije/")
			.then(response => { this.manifestacije = response.data; });
	}
});
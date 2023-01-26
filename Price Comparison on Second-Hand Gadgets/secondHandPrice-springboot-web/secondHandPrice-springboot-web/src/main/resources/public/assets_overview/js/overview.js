var eventBus = new Vue();
var keyword;
var router = new VueRouter({
    mode: 'history',
    routes: []
});

var app = new Vue({
  router,
  el: '#app',
  data: {
    prices: {},
    products: [],
    shouldShowDetail: false
  },
  methods: {
    
    fetchOverview(keyword) {

      fetch('http://104.248.78.115/api/overview?product_name=' + keyword)
      .then( response => {
        if(response.ok) {
          return response.json()
        }
        throw new Error("Network response was not ok.")
      })
      .then(result => {
        console.log(result)
        this.prices = {
          minPrice: result.min_prince,
          maxPrice: result.max_price,
          avgPrice: result.avg_price.toFixed(2),
          lastTransPrice: result.last_transaction_price
        }

        this.products = result.products

      })
      .catch( error => {
        window.location.href = "error_NoItem";
        //alert('Connection Error! Please try again later.')
      })

    }
  }, 
  mounted() {
    keyword = this.$route.query.product_name
    console.log(keyword)
    this.fetchOverview(keyword)
  }
})

function nextTab_AllProducts(){
    window.location.href = "all_products?product_name=" + keyword;
}

function nextTab_Trend(){
    window.location.href = "trend?product_name=" + keyword;
}

function nextTab_Home(){
    window.location.href = "http://104.248.78.115";
}
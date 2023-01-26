var keyword;
var router = new VueRouter({
    mode: 'history',
    routes: []
});

var app = new Vue({
    router,
    el: '#app',
    data: {
      columns: {
        base: 'Base',
        target: 'Target',
        rate: 'Rate',
      },
      rows:[],
      product_name:"iphone xs",
      sort:"lastUpdateDate",
      order:"ASC",
      page:"1",
      pageNum:"",
      KEY:"",
      currentPage:"",
      total:"",
      sortby:'Oldest Update Date'
    },
    mounted:function (){
        this.product_name = this.$route.query.product_name;
        keyword = this.product_name;
        var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page})
        fetch(url)
        .then((response) => {
              return response.json();
              })
          .then((movies) => {
                this.KEY=Object.keys(movies);
                this.pageNum=parseInt(this.KEY);
                this.total=this.pageNum*10;
                this.rows = movies;
          })
          .catch( error => {
            window.location.href = "error_NoItem";
            //alert('Connection Error! Please try again later.')
          })
   },
    methods:{
      sortTabled(){
      this.sortby="Descending Price";
      this.sort="secondHandPrice";
      this.order="DESC";
      this.page="1";
      this.currentPage=1;
          var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page});
        fetch(url)
        .then((response) => {
              return response.json();
              })
         .then((movies) => {
               this.rows = movies;
           
                            
          })
          $('html').scrollTop(0);
    },
    sortTablea() {
        this.sortby="Ascending Price";
      this.sort="secondHandPrice";
      this.order="ASC";
      this.page="1";
      this.currentPage=1;

        var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page});
        fetch(url)
        .then((response) => {
              return response.json();
              })
         .then((movies) => {
               this.rows = movies;
               
                           
          })
        $('html').scrollTop(0);
    },  
    sortTablel() {
        this.sortby="Latest Update Date";
      this.sort="lastUpdateDate";
      this.order="DESC";
      this.page="1"; 
      this.currentPage=1;
        var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page});
        fetch(url)
        .then((response) => {
              return response.json();
              })
          .then((movies) => {
               this.rows = movies;
                              
          })
        $('html').scrollTop(0);
    }, 
    sortTableo() {
        this.sortby="Oldest Update Date";
      this.sort="lastUpdateDate";
      this.order="ASC";
      this.page="1";
      this.currentPage=1;
        var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page});
        fetch(url)
        .then((response) => {
              return response.json();
              })
          .then((movies) => {
               this.rows = movies;      
          })
        $('html').scrollTop(0);
    },
    navigator(num){
      this.page=num.toString();
      var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page});
        fetch(url)
        .then((response) => {
              return response.json();
              })
          .then((movies) => {
            $('html').scrollTop(0);
               this.rows = movies;  
            
          })
        
    },
    }
  });
  
 function nextTab_Overview(){
    window.location.href = "overview?product_name=" + keyword
}

function nextTab_Trend(){
    window.location.href = "trend?product_name=" + keyword
}

function nextTab_Home(){
    window.location.href = "http://104.248.78.115"
}
 
  
       
  
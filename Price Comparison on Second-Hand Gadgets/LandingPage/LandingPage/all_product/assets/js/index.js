var app = new Vue({
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
    },
    mounted:function (){
        var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page})
        fetch(url)
        .then((response) => {
              return response.json();
              })
          .then((movies) => {
                this.KEY=Object.keys(movies);
                this.pageNum=parseInt(this.KEY);
                console.log(this.pageNum);
                this.rows = movies;
               //console.log(this.rows);
               console.log(this.rows[this.KEY][0].productName);
               //console.log(JSON.stringify(this.row));
          })
   },
    methods:{
      sortTabled(){
      this.sort="Price";
      this.order="DSC";
      this.page="1";
    },
    sortTablea() {
      this.sort="Price";
      this.order="ASC";
      this.page="1";
    },  
    sortTablel() {
      this.sort="lastUpdateDate";
      this.order="DSC";
      this.page="1";
    }, 
    sortTableo() {
      this.sort="lastUpdateDate";
      this.order="ASC";
      this.page="1";
    },
    navigator(num){
      this.page=num.toString();
      //alert(this.page);  
      var url = "http://104.248.78.115/api/all_products?" + $.param({product_name: this.product_name, sort: this.sort, order: this.order, page: this.page});
        console.log(url);
        fetch(url)
        .then((response) => {
              return response.json();
              })
          .then((movies) => {
               this.rows = movies;
               //console.log(this.rows[0].productName);               
          })
        
    },
    }
  });
  
 
 
  
       
  
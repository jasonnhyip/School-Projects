var keyword;
var router = new VueRouter({
    mode: 'history',
    routes: []
});

var product_chart = {
    product_name: 'iphone xs',
    records: [{
        source: '',
        id: '',
        productName: '',
        secondHandPrice: '',
        description: '',
        warrantyDesc: '',
        lastUpdateDate: '',
        imageUrl: '',
        availability: '',
        productUrl: ''
    }]
}

var data = {
    //labels: ["2018-11-04", "2018-11-05", "2018-11-06", "2018-11-07", "2018-11-08", "2018-11-09", "2018-11-10"],
    labels: ["Test Date 1", "Test Date2", "Test Date 1", "Test Date2", "Test Date 1", "Test Date2", "Test Date 1"],
    datasets: [
        {
            label: "My First dataset",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: [],
            URL: ["https://www.facebook.com/",
                "https://www.facebook.com/CityUSecretsPage?fref=ts",
                "https://www.facebook.com/cityuge?fref=ts",
                "https://docs.google.com/spreadsheets/d/1ad12Rut-xT1fdNMFicMtVCyL73Sf7e8FwEqRC3h7o-w/edit#gid=0",
                "https://www.youtube.com/watch?v=AcHEwx2mx_c",
                "https://sheet.host/sheet/vkrmZF",
                "https://www.youtube.com/watch?v=6jQsl2nGQrw"]
        }]
};

Tread = new Vue({
    router,
    el: '#tread',
    data: {
        product_name: 'iphone xs',
        rows: [{
            source: 'Test Source 1',
            id: 'Test ID 1',
            productName: 'Test Product Name 1',
            secondHandPrice: '5000',
            description: 'Test Description 1',
            warrantyDesc: 'Testing WarrantDesc',
            lastUpdateDate: 'Testing Date 1',
            imageUrl: '',
            availability: 'Testing availabilty',
            productUrl: 'https://www.facebook.com/'
        },
        {
            source: 'Test Source 2',
            id: 'Test ID 2',
            productName: 'Test Product Name 2',
            secondHandPrice: '5000',
            description: 'Test Description 2',
            warrantyDesc: 'Testing WarrantDesc',
            lastUpdateDate: 'Testing Date 2',
            imageUrl: '',
            availability: 'Testing availabilty',
            productUrl: 'https://www.facebook.com/CityUSecretsPage?fref=ts'
        }],
        pointAt: -1
    },
    mounted: function () {
        keyword = this.$route.query.product_name;
        product_chart.product_name = keyword;
        //var url = 'http://104.248.78.115/api/low_price_per_date?product_name=iphone%20xs';
        //var url="http://104.248.78.115/api/low_price_per_date?" + $.param({product_name: this.product_name})
        var url="http://104.248.78.115/api/low_price_per_date?product_name=" + keyword;
        
        fetch(url)
            .then(response => {
                return response.json();
            })
            .then((products) => {
                var i = 0;
                this.product_name = product_chart.product_name;
                product_chart.records = products;

                data.labels[0] = product_chart.records[0].lastUpdateDate.substring(0, 10);
                data.datasets[0].data[0] = product_chart.records[0].secondHandPrice;
                data.datasets[0].URL[0] = product_chart.records[0].productUrl;
                this.rows[0] = product_chart.records[0];

                for (i = 1, j = 1; j < product_chart.records.length; j++) {
                    if (product_chart.records[j].lastUpdateDate.substring(0, 10) !== product_chart.records[j - 1].lastUpdateDate.substring(0, 10)) {

                        data.labels[i] = product_chart.records[j].lastUpdateDate.substring(0, 10);
                        data.datasets[0].data[i] = product_chart.records[j].secondHandPrice;
                        data.datasets[0].URL[i] = product_chart.records[j].productUrl;

                        this.rows[i] = product_chart.records[j];

                        i++;
                    }
                }
            })
            .then(function(){
                drawChartFunction();
            })
            .catch( error => {
                window.location.href = "error_NoItem";
                //alert('Connection Error! Please try again later.')
            })
    },
    methods: {
        myMouseMovement(evt) {
            var points = chart.getPointsAtEvent(evt);
            if (chart.datasets[0].points.indexOf(points[0]) !== -1) {
                this.pointAt = chart.datasets[0].points.indexOf(points[0]);
            }
            chart.update();
        },
        myMouseClicking(evt) {
            var points = chart.getPointsAtEvent(evt);
            window.open(data.datasets[0].URL[chart.datasets[0].points.indexOf(points[0])]);  // open new window to URL
            chart.update();
        }
    }
})

var canvas = document.getElementById("chart");
var ctx = canvas.getContext("2d");

drawChartFunction = function(){
    chart = new Chart(ctx).Line(data);
}

var router = new VueRouter({
    mode: 'history',
    routes: []
});

/*
var canvas = document.getElementById("chart");
var ctx = canvas.getContext("2d");
var chart = new Chart(ctx).Line(data);

updateMyChart = function () {
    chart.update();
}*/

/*
setTimeout(function () {
    var canvas = document.getElementById("chart");
    var ctx = canvas.getContext("2d");
    var chart = new Chart(ctx).Line(data);
    chart.update();
}, 5000);
*/


/*
//<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
var chart = new Chart(document.getElementById("chart"), {
    type: 'line',
    data: data,
    options: {
      title: {
        display: true,
        text: 'World population per region (in millions)'
      }
    }
  });
*/

//<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.1.1/Chart.min.js"></script> 




/*canvas.onclick = function (evt) {
    var points = chart.getPointsAtEvent(evt);
    alert(chart.datasets[0].points.indexOf(points[0]));
    alert(data.datasets[0].URL[chart.datasets[0].points.indexOf(points[0])]);
    window.open(data.datasets[0].URL[chart.datasets[0].points.indexOf(points[0])]);  // open new window to URL
};
canvas.onmousemove = function (evt) {
    var points = chart.getPointsAtEvent(evt);
    console.log(chart.datasets[0].points.indexOf(points[0]));
    if (chart.datasets[0].points.indexOf(points[0]) !== -1) {
        show = 1;

    }
};*/



function nextTab_Overview(){
    window.location.href = "overview?product_name=" + keyword
}

function nextTab_AllProducts(){
    window.location.href = "all_products?product_name=" + keyword
}

function nextTab_Home(){
    window.location.href = "http://104.248.78.115"
}
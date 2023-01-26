var product_chart = {
    searchKey: 'iphone xs',
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
    labels: ["2018-11-04", "2018-11-05", "2018-11-06", "2018-11-07", "2018-11-08", "2018-11-09", "2018-11-10"],
    datasets:[
    {
        label: "My First dataset",
        fillColor: "rgba(220,220,220,0.2)",
        strokeColor: "rgba(220,220,220,1)",
        pointColor: "rgba(220,220,220,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(220,220,220,1)",
        data: [8000, 7500, 2250, 7300, 5483, 8000, 6500],
        URL: ["https://www.facebook.com/",
            "https://www.facebook.com/CityUSecretsPage?fref=ts",
            "https://www.facebook.com/cityuge?fref=ts",
            "https://docs.google.com/spreadsheets/d/1ad12Rut-xT1fdNMFicMtVCyL73Sf7e8FwEqRC3h7o-w/edit#gid=0",
            "https://www.youtube.com/watch?v=AcHEwx2mx_c",
            "https://sheet.host/sheet/vkrmZF",
            "https://www.youtube.com/watch?v=6jQsl2nGQrw"]
    }]
};

chart_inLine = new Vue({
    el: '#chart',
    data: {
        tittle: '123'
    },
    mounted: function () {
        fetch('http://104.248.78.115/api/low_price_per_date?product_name=iphone%20xs')
            .then((response) => {
                return response.json();
            })
            .then((products) => {
                
                alert("stop!");
                var i = 0;
                this.tittle = product_chart.searchKey;
                product_chart.records = products;

                data.labels[0] = product_chart.records[0].lastUpdateDate.substring(0, 10);
                data.datasets[0].data[0] = product_chart.records[0].secondHandPrice;
                data.datasets[0].URL[0] = product_chart.records[0].productUrl;



                for (i = 1, j = 1; j < product_chart.records.length; j++) {
                    if (product_chart.records[j].lastUpdateDate.substring(0, 10) !== product_chart.records[j - 1].lastUpdateDate.substring(0, 10)) {
                        console.log("i=" + i + "  j=" + j);

                        data.labels[i] = product_chart.records[j].lastUpdateDate.substring(0, 10);
                        data.datasets[0].data[i] = product_chart.records[j].secondHandPrice;
                        data.datasets[0].URL[i] = product_chart.records[j].productUrl;
                        console.log(data.labels[i]);
                        console.log(typeof data.datasets[0].data[i]);
                        console.log(data.datasets[0].URL[i]);
                        i++;
                    }
                }
                console.log(data.labels);
                console.log(data.datasets.data);


            })
            .then()
    }
})

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

var canvas = document.getElementById("chart");
var ctx = canvas.getContext("2d");
var chart = new Chart(ctx).Line(data);


canvas.onclick = function (evt) {
    var points = chart.getPointsAtEvent(evt);
    alert(chart.datasets[0].points.indexOf(points[0]));
    alert(data.datasets[0].URL[chart.datasets[0].points.indexOf(points[0])]);
    window.open(data.datasets[0].URL[chart.datasets[0].points.indexOf(points[0])]);  // open new window to URL
};


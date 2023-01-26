Vue.component('line-chart', {
  extends: VueChartJs.Scatter,
  data() {
    return {
      points: [],
    }
  },
  props: {
    products: {
      type: Array,
    }
  },
  methods: {

    handleHover(event, elements) {

      if (elements.length > 0) {
        const index = elements[0]._index
        const product = this.products[index]
        eventBus.$emit('update-detail', product)
      }
    }

  },
  updated() {
    console.log("CHART: updated: Size:" + this.products.length)
  },
  mounted() {
    console.log("CHART: mounted: Size:" + this.products.length)
    this.products.forEach(product => {
      const p = {
        x: product.secondHandPrice,
        y: 0
      }
      points.push(p)
    });
    console.log("Chart Points: " + this.products.length)

    eventBus.$on('update-chart', () => {
      console.log("Attempt to update chart")
      console.log("CHART: eventBus: Size:" + this.products.length)
      this.products.forEach(product => {
          const p = {
            x: product.secondHandPrice,
            y: 0
          }
          this.points.push(p)
      });
      console.log("POINT: Size:" + this.points.length)

      this.renderChart(
        {
          datasets:
            [
              {
                label: 'Scatter Dataset 1',
                fill: false,
                borderColor: '#f87979',
                backgroundColor: '#f87979',
                data: this.points,
              },
            ],
        }
        ,
        {
          responsive: true,
          maintainAspectRatio: false,
          legend: {
            display: false,
          },
          scales: {
            xAxes: [{
              gridLines: {
                display: false,
              }
            }],
            yAxes: [{
              gridLines: {
                drawBorder: false,
              },
              ticks: {
                beginAtZero: false,
                fontSize: 0,
                maxTicksLimit: 1
              }
            }]
          },
          hover: {
            onHover: (event, elements) => {
              this.handleHover(event, elements)
            }
          },
          tooltips: {
            enabled: false
          }

        })

    })
  }
})

Vue.component('priceline-panel', {
  template: `
    <div id="priceline-panel-container" class="card">
      <h3 class="card-header">
      Past Week Records
      </h3>

      <div class="card-body">
        <line-chart 
          v-bind:height="100"
          v-bind:prices="prices"
          v-bind:products="products">
        </line-chart>
      
      </div>
      

      
    </div>
  `,
  props: {
    prices: {
      type: Object
    }, 
    products: {
      type: Array,
    }
  },
  data() {
    return {

    }
  },
  methods: {

  },
  mounted() {
    console.log("mounted: Size:" + this.products.length)
  },
  updated() {
    console.log("updated: Size:" + this.products.length)
    eventBus.$emit('update-chart')
  }
})
Vue.component('detail-panel', {
  template: `
  <div id="detail-panel-parent container">
    <div class="row">
      <div class="image-container col-lg-3">
        <img v-bind:src="this.product.imageUrl" class="img-fluid" />
      </div>
     
      <div class="info-container col-lg-5">
        <h3>{{ this.product.productName }}</h3>
        <p class="text-warning">Second hand price: <strong> {{ this.product.secondHandPrice }} </strong> </p>
        <p>Source: <a target="_blank" v-bind:href="this.product.productUrl"> {{ this.product.source}} </a> </p>
        <p>Warranty: </p>
        <p> {{ this.product.warrantyDesc }} </p>
        <p>Transcation availability: </p>
        <p> {{ this.product.availability }} </p>
        <p>Last update date: </p>
        <p> {{ 
          this.product.lastUpdateDate
          .substring(0, this.product.lastUpdateDate.indexOf('.'))
          .replace("T", " ") }} </p>
      </div>

      <div class="desc-container card" style="width: 18rem;">
        <div class="card-header">
          <h5>Description</h5>
        </div>
        <div class="card-body">
          <p class="card-text"> {{ this.product.description }} </p>
        </div>
      </div>
    </div>
  </div>
  `,
  data() {
    return {
      product: {},
      visible: false
    }
  },
  mounted() {
    eventBus.$on('update-detail', (product) => {
      this.product = product
      this.$emit('display-detail')
    })
  }


})
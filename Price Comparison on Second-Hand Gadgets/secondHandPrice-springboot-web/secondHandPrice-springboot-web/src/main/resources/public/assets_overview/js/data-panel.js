Vue.component('data-panel', {
  template: `
  <div id="data-panel-parent" class="card">



    <h3 class="card-header">
      Overview
    </h3>

    <div class="card-body">
      <table>
        <tr v-for="(title, i) in titles">
          <th class="text-info"> {{title}} : </th>
          <td class="text-warning"> <strong> {{prices[i]}} </strong> </td>
        </tr>
      </table>
    </div>
  </div>
  `,
  props: {
    prices: {
      type: Object,
    }
  },
  data() {
    return {
      titles: {
        minPrice: "Min Price",
        maxPrice: "Max Price",
        avgPrice: "Average Price",
        lastTransPrice: "Last Transaction Price"
      }
    }
  },
  methods: {

  }
})
import { createApp, h, provide } from 'vue'
import { DefaultApolloClient } from '@vue/apollo-composable'
import App from './App.vue'
import router from './router'
import apolloClient from '@/common/apollo-client.ts'

// ✅ PrimeVue setup (STYLED MODE)
import PrimeVue from 'primevue/config'
import Aura from '@primevue/themes/aura'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.css'
import './style.css'

const app = createApp({
  setup() {
    provide(DefaultApolloClient, apolloClient)
  },
  render: () => h(App)
})

app.use(PrimeVue, {
  theme: {
    preset: Aura,
    options: {
      prefix: 'p',        // giữ class gốc (p-inputtext, p-iconfield)
      darkModeSelector: false,
      cssLayer: {
        name: 'primevue',
        order: 'tailwind-base, primevue, tailwind-utilities'
      }
    }
  }
})

import InputText from 'primevue/inputtext'
import IconField from 'primevue/iconfield'
import InputIcon from 'primevue/inputicon'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Dropdown  from 'primevue/dropdown'
import Checkbox  from 'primevue/checkbox'
import RadioButton  from 'primevue/radiobutton'
import InputSwitch   from 'primevue/inputswitch'
import Textarea from 'primevue/textarea';
import OverlayPanel from "primevue/overlaypanel";
import Button from "primevue/button";
app.component('InputText', InputText)
app.component('IconField', IconField)
app.component('InputIcon', InputIcon)
app.component('DataTable', DataTable)
app.component('Column', Column)
app.component('Dropdown', Dropdown)
app.component('Checkbox', Checkbox)
app.component('RadioButton', RadioButton)
app.component('InputSwitch', InputSwitch)
app.component('Textarea', Textarea)
app.component("OverlayPanel", OverlayPanel);
app.component("Button", Button);
app.use(router)
app.mount('#app')

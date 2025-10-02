import { createApp, h, provide } from 'vue'
import { DefaultApolloClient } from '@vue/apollo-composable'
import App from './App.vue'
import router from './router'
import apolloClient from '@/common/apollo-client.ts'   // <== import ApolloClient
import './style.css'
import 'primeicons/primeicons.css'

const app = createApp({
  setup() {
    provide(DefaultApolloClient, apolloClient) // <== inject apollo toàn cục
  },
  render: () => h(App)
})

app.use(router)
app.mount('#app')

// vite.config.js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// 👇 thêm 3 import này
import Components from 'unplugin-vue-components/vite'
import Icons from 'unplugin-icons/vite'
import IconsResolver from 'unplugin-icons/resolver'

export default defineConfig({
  plugins: [
    vue(),

    // 👇 plugin tự động import component + icon
    Components({
      resolvers: IconsResolver({
        prefix: '', // để có thể dùng <RxPlus /> thay vì <IconRadixIconsPlus />
      }),
    }),
    Icons(),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})

const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8082,
    proxy: {
      '/api': {
        target: process.env.VUE_APP_API_URL, // 백엔드 서버 주소
        changeOrigin: true,
      },
    },
  },
});

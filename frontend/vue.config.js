const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8082,
    proxy: {
      '/api': {
        target: 'http://localhost:8081', // 백엔드 서버 주소
        changeOrigin: true,
      },
    },
  },
});

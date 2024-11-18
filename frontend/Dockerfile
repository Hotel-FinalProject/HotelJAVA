#node.js 이미지
FROM node:18-alpine as build

#작업 디렉토리 설정
WORKDIR /app
COPY package*.json ./


RUN npm install
COPY ..
RUN npm run build

#빌드된 정적 파일 제공을 위한 nginx 이미지
FROM nginx:1.25-alpine

#nginx 설정 덮어쓰기
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Vue 빌드 결과를 Nginx html 폴더로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# 컨테이너가 실행될 때 Nginx 실행
CMD ["nginx", "-g", "daemon off;"]


# Node.js 이미지
FROM node:18-alpine as build

# 작업 디렉토리 설정
WORKDIR /app
COPY package*.json ./

# 의존성 설치
RUN npm install

# 소스 파일 전체 복사
COPY . .

RUN npm run build

# Nginx 이미지
FROM nginx:1.25-alpine

# Nginx 설정 덮어쓰기
COPY ./nginx.conf /etc/nginx/nginx.conf

# Vue 빌드 결과물을 Nginx HTML 폴더로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
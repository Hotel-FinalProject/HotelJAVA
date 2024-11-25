import axios from 'axios';

/** axios 기본 url 및 헤더 설정 */
const config = axios.create({
  baseURL: '/api',  // Spring Boot API 경로
  headers: {
    'Content-Type': 'application/json'
  }
});

function getHotelsReviewsTop(){
    return config.get('/hotels/top10');
}


export {
    getHotelsReviewsTop
}
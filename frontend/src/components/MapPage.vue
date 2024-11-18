<template>
    <div class="map-page">
      <h2>지도</h2>
      <button @click="goToSearchPage" class="back-button">검색 페이지로 돌아가기</button>
      <div id="map"></div>
    </div>
  </template>
  
  <script>
  /* global kakao */
  import axios from "axios";
  
  export default {
    name: "MapPage",
    props: {
      hotels: {
        type: Array,
        required: false,
        default: () => [],
      },
      location: {
        type: String,
        required: false,
      },
    },
    data() {
      return {
        mapHotels: [], // 지도에 표시할 호텔 목록
      };
    },
    async mounted() {
      if (this.hotels.length > 0) {
        this.mapHotels = this.hotels;
      } else {
        await this.fetchHotels();
      }
      this.loadKakaoMap();
    },
    beforeUnmount() {
      const kakaoScript = document.querySelector(
        "script[src*='//dapi.kakao.com/v2/maps/sdk.js']"
      );
      if (kakaoScript) {
        kakaoScript.remove();
        delete window.kakao;
      }
    },
    methods: {
      async fetchHotels() {
        try {
          const response = await axios.get(
            `http://localhost:8081/api/hotels/search?location=${
              this.location || ""
            }`
          );
          console.log("API response:", response.data); // API 응답 확인용 로그
          this.mapHotels = Array.isArray(response.data) ? response.data : [];
          console.log("mapHotels:", this.mapHotels); // mapHotels 설정 확인용 로그
        } catch (error) {
          console.error("호텔 목록을 가져오는 중 오류 발생:", error);
          this.mapHotels = []; // 오류 시 빈 배열로 초기화
        }
      },
      loadKakaoMap() {
        if (typeof kakao === "undefined") {
          const script = document.createElement("script");
          const apiKey = process.env.VUE_APP_KAKAO_API_KEY;
          script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${apiKey}&autoload=false`;
          script.onload = this.initMap;
          document.head.appendChild(script);
        } else {
          this.initMap();
        }
      },
      initMap() {
        kakao.maps.load(() => {
          const container = document.getElementById("map");
          const options = {
            center: new kakao.maps.LatLng(37.5665, 126.978),
            level: 5,
          };
          const map = new kakao.maps.Map(container, options);
  
          const bounds = new kakao.maps.LatLngBounds();
          const markerImageSrc =
            "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
  
          if (Array.isArray(this.mapHotels) && this.mapHotels.length > 0) {
            this.mapHotels.forEach((hotel) => {
              if (hotel.mapY && hotel.mapX) {
                const markerPosition = new kakao.maps.LatLng(
                  hotel.mapY,
                  hotel.mapX
                );
  
                const imageSize = new kakao.maps.Size(24, 35);
                const markerImage = new kakao.maps.MarkerImage(
                  markerImageSrc,
                  imageSize
                );
  
                const marker = new kakao.maps.Marker({
                  map: map,
                  position: markerPosition,
                  title: hotel.name,
                  image: markerImage,
                });
  
                bounds.extend(markerPosition);
  
                const infoWindowContent = `
                  <div style="
                      width: 200px; 
                      padding: 10px; 
                      font-family: Arial, sans-serif; 
                      color: #333;
                      border-radius: 8px;
                  ">
                      <img src="${hotel.imageUrl}" 
                          alt="${hotel.name}" 
                          style="width: 100%; height: auto; border-radius: 8px; margin-bottom: 8px;" />
                      <strong style="display: block; font-size: 16px; font-weight: bold; color: #333; margin-bottom: 5px;">
                      ${hotel.name}
                      </strong>
                      <span style="font-size: 14px; color: #666; line-height: 1.5;">
                      ${hotel.address}
                      </span>
                  </div>
                  `;
  
  
                const infoWindow = new kakao.maps.InfoWindow({
                  content: infoWindowContent,
                  removable: false,
                });
  
                kakao.maps.event.addListener(marker, "mouseover", () => {
                  infoWindow.open(map, marker);
                });
  
                kakao.maps.event.addListener(marker, "mouseout", () => {
                  infoWindow.close();
                });
  
                // 마커 클릭 시 호텔 상세 페이지로 이동
                kakao.maps.event.addListener(marker, "click", () => {
                  this.$router.push(`/hotel-details/${hotel.hotelId}`);
                });
              }
            });
            map.setBounds(bounds);
          } else {
            console.warn("mapHotels 배열이 비어 있습니다.");
          }
        });
      },
      goToSearchPage() {
      // 검색 페이지로 이동하면서 query 파라미터에 검색어를 전달
      this.$router.push({
        path: "/search-results",
        query: { query: this.$route.query.query }
      });
    },
  },
};
</script>
  
  <style scoped>
  .map-page {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
  }

  .back-button-container {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  padding: 10px 0;
}

.back-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.back-button:hover {
  background-color: #0056b3;
}
  
  #map {
    width: 100%;
    height: 600px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    margin-top: 20px;
  }
  </style>
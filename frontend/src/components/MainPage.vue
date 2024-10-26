<template>
<div class = "main-container">
    <div class= "input-bar">
      <input class = "search-bar" type="text" />
    </div>
    <div class = "hotel_list_conatiner">
        <div class = "hotel_grid">
            <div class = "hotel-container">
                <router-link to ="/hotel-details">
                    <img class = "img-container" src = "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg">
                </router-link>
                <div class="hotel-name">호텔명</div>
                <div class="hotel-info">
                    <span class="racting">⭐4.5</span><span>(리뷰갯수)</span>
                </div>
            </div>
        </div>

        <h2 class="review-title"> 리뷰 Top 10 </h2>
        <div class ="hotel_grid">
            <div class = "hotel-container">
                <img class = "img-container"  src = "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg">
                <div class="hotel-name">호텔명</div>
                <div class="hotel-info">
                    <span class="racting">⭐4.5</span><span>(리뷰갯수)</span>
                </div>
            </div>  
        </div>

        <h2 class="review-title"> 별점 Top 10 </h2>
        <div class ="hotel_grid">
            <div class = "hotel-container">
                <img class = "img-container" src = "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg">
                <div class="hotel-name">호텔명</div>
                <div class="hotel-info">
                    <span class="racting">⭐4.5</span><span>(리뷰갯수)</span>
                </div>
            </div>  
        </div>
    </div>

</div>
</template>
<script>
export default {
  name: 'MainPage',
};
</script>
<style scoped>
.main-container{
    width : 60%;
    margin : auto;

}

.search-bar{
  width: 100%;
  height: 40px;
  margin-bottom : 20px;
  font-size: 15px;
  border: 0;
  border-radius: 15px;
  outline: none;
  padding-left: 10px;
  background-color: rgb(233, 233, 233);

}
.hotel-conatiner{
    width: 100%;
}
.hotel_grid{
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 16px;
    margin-bottom : 30px;
    
}

.img-container{
    width : 100%;
    height: 200px;
    object-fit: cover;
}
.hotel-name{
    font-size : 20px;
    font-weight : bold;
    text-align: center;
}
.hotel-info{
    text-align: center;
}

</style>
import { createStore } from "vuex";
import mutations from './mutations.js';
import actions from './actions.js';

const store = new createStore({
    // 변수
    state:{
        users: []
    },
    getters:{
        users(state) {
            return state.users;
        }
    },
    // axios하는 기능 actions.js에 작성  
    actions,
    // 동작의 결과 변수에 저장 -> mutations.js에 작성   
    mutations,
})

export default store;
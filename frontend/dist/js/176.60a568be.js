"use strict";(self["webpackChunkfrontend"]=self["webpackChunkfrontend"]||[]).push([[176],{9176:function(e,t,n){n.r(t),n.d(t,{default:function(){return Le}});var a=n(6768),s=n(4232),o=n(5130);const r={class:"menu-container"},i={class:"main-content"},l={key:0},d={class:"dashboard-summary"},u={class:"dashboard-item"},c={class:"dashboard-item"},p={class:"dashboard-item"},h={key:1},v={class:"top-container"},k={class:"search-container"},m={class:"user-table-container"},L={class:"user-activeBtn-container"},w=["onClick"],C=["onClick"],f={key:2},b={class:"top-container"},g={class:"search-container"},y={class:"user-table-container"},A={class:"user-activeBtn-container"},M=["onClick"],R=["onClick"],S={key:3},I={class:"user-table-container"},V=["onClick"],_={class:"user-activeBtn-container"},U=["onClick"];function O(e,t,n,O,T,K){const H=(0,a.g2)("HotelAdminModal"),E=(0,a.g2)("PasswordVerification"),X=(0,a.g2)("ReviewModal"),$=(0,a.g2)("SidebarLayout");return(0,a.uX)(),(0,a.Wv)($,{title:"시스템 관리자"},{menu:(0,a.k6)((()=>[(0,a.Lk)("div",r,[(0,a.Lk)("a",{href:"#",class:(0,s.C4)({active:"Dashboard"===T.currentView}),onClick:t[0]||(t[0]=(0,o.D$)((e=>T.currentView="Dashboard"),["prevent"]))},t[9]||(t[9]=[(0,a.Lk)("span",{class:"icon"},"📋",-1),(0,a.eW)("대시보드 ")]),2),(0,a.Lk)("a",{href:"#",class:(0,s.C4)({active:"UserManagement"===T.currentView}),onClick:t[1]||(t[1]=(0,o.D$)((e=>T.currentView="UserManagement"),["prevent"]))},t[10]||(t[10]=[(0,a.Lk)("span",{class:"icon"},"🔍",-1),(0,a.eW)(" 사용자 관리 ")]),2),(0,a.Lk)("a",{href:"#",class:(0,s.C4)({active:"HotelAdminAccounts"===T.currentView}),onClick:t[2]||(t[2]=(0,o.D$)((e=>T.currentView="HotelAdminAccounts"),["prevent"]))},t[11]||(t[11]=[(0,a.Lk)("span",{class:"icon"},"🏨",-1),(0,a.eW)(" 호텔 관리자 계정 관리 ")]),2),(0,a.Lk)("a",{href:"#",class:(0,s.C4)({active:"ReviewReports"===T.currentView}),onClick:t[3]||(t[3]=(0,o.D$)((e=>T.currentView="ReviewReports"),["prevent"]))},t[12]||(t[12]=[(0,a.Lk)("span",{class:"icon"},"🚨",-1),(0,a.eW)(" 리뷰 관리 ")]),2)])])),default:(0,a.k6)((()=>[(0,a.Lk)("div",i,["Dashboard"===T.currentView?((0,a.uX)(),(0,a.CE)("div",l,[t[16]||(t[16]=(0,a.Lk)("h2",null,"대시보드",-1)),t[17]||(t[17]=(0,a.Lk)("p",null,"시스템의 전체 상태를 한눈에 확인하세요.",-1)),(0,a.Lk)("ul",d,[(0,a.Lk)("li",u,[t[13]||(t[13]=(0,a.Lk)("h3",null,"🔍 사용자 관리",-1)),(0,a.Lk)("p",null,"총 사용자 수: "+(0,s.v_)(O.totalUserCount)+"명",1),(0,a.Lk)("p",null,"활성 계정: "+(0,s.v_)(O.activeUserCount)+"명",1),(0,a.Lk)("p",null,"비활성 계정: "+(0,s.v_)(O.inactiveUserCount)+"명",1)]),(0,a.Lk)("li",c,[t[14]||(t[14]=(0,a.Lk)("h3",null,"🏨 호텔 관리자 계정 관리",-1)),(0,a.Lk)("p",null,"등록된 호텔 관리자: "+(0,s.v_)(O.totalHotelCount)+"명",1),(0,a.Lk)("p",null,"활성 계정: "+(0,s.v_)(O.activeHotelCount)+"명",1),(0,a.Lk)("p",null,"비활성 계정: "+(0,s.v_)(O.inactiveHotelCount)+"명",1)]),(0,a.Lk)("li",p,[t[15]||(t[15]=(0,a.Lk)("h3",null,"🚨 리뷰 관리",-1)),(0,a.Lk)("p",null,"신고된 리뷰: "+(0,s.v_)(O.totalReportCount)+"건",1),(0,a.Lk)("p",null,"검토 완료 리뷰: "+(0,s.v_)(O.completeReportCount)+"건",1),(0,a.Lk)("p",null,"미검토 리뷰: "+(0,s.v_)(O.incompleteReportCount)+"건",1)])])])):(0,a.Q3)("",!0),"UserManagement"===T.currentView&&T.isVerified?((0,a.uX)(),(0,a.CE)("div",h,[(0,a.Lk)("div",v,[t[18]||(t[18]=(0,a.Lk)("div",{class:"title"},[(0,a.Lk)("h2",null,"사용자 관리")],-1)),(0,a.Lk)("div",k,[(0,a.bo)((0,a.Lk)("input",{class:"search-input",type:"text",placeholder:"이름, 이메일로 검색해주세요.","onUpdate:modelValue":t[4]||(t[4]=e=>O.searchKeyword=e),onInput:t[5]||(t[5]=(...e)=>O.handleUserSearch&&O.handleUserSearch(...e))},null,544),[[o.Jo,O.searchKeyword]])])]),t[20]||(t[20]=(0,a.Lk)("hr",null,null,-1)),(0,a.Lk)("div",m,[t[19]||(t[19]=(0,a.Lk)("div",{class:"user-table-header"},[(0,a.Lk)("span",null,"계정 상태"),(0,a.Lk)("span",null,"Index"),(0,a.Lk)("span",null,"이름"),(0,a.Lk)("span",null,"이메일"),(0,a.Lk)("span",null,"전화번호"),(0,a.Lk)("span",null,"관리")],-1)),((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(O.userList,(e=>((0,a.uX)(),(0,a.CE)("div",{key:e.id,class:"user-table-row"},[(0,a.Lk)("span",{class:(0,s.C4)(e.isActive?"user-active":"user-deactive")},(0,s.v_)(e.isActive),3),(0,a.Lk)("span",null,(0,s.v_)(e.userId),1),(0,a.Lk)("span",null,(0,s.v_)(e.name),1),(0,a.Lk)("span",null,(0,s.v_)(e.email),1),(0,a.Lk)("span",null,(0,s.v_)(e.phone),1),(0,a.Lk)("div",L,[e.isActive?((0,a.uX)(),(0,a.CE)("button",{key:0,onClick:t=>O.handleUserStatusChange(e.userId)},"정지",8,w)):((0,a.uX)(),(0,a.CE)("button",{key:1,onClick:t=>O.handleUserStatusChange(e.userId)},"활성화",8,C))])])))),128))])])):(0,a.Q3)("",!0),"HotelAdminAccounts"===T.currentView&&T.isVerified?((0,a.uX)(),(0,a.CE)("div",f,[(0,a.Lk)("div",b,[t[21]||(t[21]=(0,a.Lk)("div",{class:"title"},[(0,a.Lk)("h2",null,"호텔 관리자 계정 관리")],-1)),(0,a.Lk)("div",g,[(0,a.bo)((0,a.Lk)("input",{class:"search-input",type:"text",placeholder:"이름, 이메일로 검색해주세요.","onUpdate:modelValue":t[6]||(t[6]=e=>O.searchKeyword=e),onInput:t[7]||(t[7]=(...e)=>O.handleHotelSearch&&O.handleHotelSearch(...e))},null,544),[[o.Jo,O.searchKeyword]]),(0,a.Lk)("button",{class:"styled-button",onClick:t[8]||(t[8]=(...e)=>K.openModal&&K.openModal(...e))},"호텔 관리자 계정 생성"),(0,a.bF)(H,{isOpen:T.isModalOpen,adminToken:T.adminToken,onClose:K.closeModal},null,8,["isOpen","adminToken","onClose"])])]),t[23]||(t[23]=(0,a.Lk)("hr",null,null,-1)),(0,a.Lk)("div",y,[t[22]||(t[22]=(0,a.Lk)("div",{class:"user-table-header"},[(0,a.Lk)("span",null,"계정 상태"),(0,a.Lk)("span",null,"Index"),(0,a.Lk)("span",null,"호텔명"),(0,a.Lk)("span",null,"이메일"),(0,a.Lk)("span",null,"전화번호"),(0,a.Lk)("span",null,"관리")],-1)),((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(O.hotelManagerList,(e=>((0,a.uX)(),(0,a.CE)("div",{key:e.id,class:"user-table-row"},[(0,a.Lk)("span",{class:(0,s.C4)(e.isActive?"user-active":"user-deactive")},(0,s.v_)(e.isActive),3),(0,a.Lk)("span",null,(0,s.v_)(e.userId),1),(0,a.Lk)("span",null,(0,s.v_)(e.name),1),(0,a.Lk)("span",null,(0,s.v_)(e.email),1),(0,a.Lk)("span",null,(0,s.v_)(e.phone),1),(0,a.Lk)("div",A,[e.isActive?((0,a.uX)(),(0,a.CE)("button",{key:0,onClick:t=>O.handleAccountStatusChange(e.userId)},"정지",8,M)):((0,a.uX)(),(0,a.CE)("button",{key:1,onClick:t=>O.handleAccountStatusChange(e.userId)},"활성화",8,R))])])))),128))])])):(0,a.Q3)("",!0),"ReviewReports"===T.currentView?((0,a.uX)(),(0,a.CE)("div",S,[t[25]||(t[25]=(0,a.Lk)("h2",null,"리뷰 관리",-1)),t[26]||(t[26]=(0,a.Lk)("hr",null,null,-1)),(0,a.Lk)("div",I,[t[24]||(t[24]=(0,a.Lk)("div",{class:"review-table-header"},[(0,a.Lk)("span",null,"계정 상태"),(0,a.Lk)("span",null,"Index"),(0,a.Lk)("span",null,"이름"),(0,a.Lk)("span",null,"리뷰 내용"),(0,a.Lk)("span",null,"신고자 이름"),(0,a.Lk)("span",null,"관리")],-1)),((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(O.reportList,(e=>((0,a.uX)(),(0,a.CE)("div",{key:e.id,class:"user-table-row"},[(0,a.Lk)("span",{class:(0,s.C4)("신고 접수됨"!==e.status?"review-active":"review-deactive")},(0,s.v_)("신고 접수됨"===e.status?"미처리":"처리완료"),3),(0,a.Lk)("span",null,(0,s.v_)(e.reportId),1),(0,a.Lk)("span",null,(0,s.v_)(e.reportedName),1),(0,a.Lk)("span",{class:"review-content",onClick:t=>K.openReviewModal(e)},(0,s.v_)(e.content),9,V),(0,a.Lk)("span",null,(0,s.v_)(e.reporterName),1),(0,a.Lk)("div",_,["신고 접수됨"===e.status?((0,a.uX)(),(0,a.CE)("button",{key:0,onClick:t=>O.handleHideReport(e.reportId)}," 숨김 처리 ",8,U)):(0,a.Q3)("",!0)])])))),128))])])):(0,a.Q3)("",!0)]),(0,a.bF)(E,{isOpen:e.isPasswordModalOpen,adminToken:T.adminToken,onClose:K.closePasswordModal,onVerified:K.handleVerified},null,8,["isOpen","adminToken","onClose","onVerified"]),(0,a.bF)(X,{isOpen:T.isReviewModalOpen,review:e.selectedReview,onClose:K.closeReviewModal},null,8,["isOpen","review","onClose"])])),_:1})}var T=n(3083);const K={key:0,class:"modal"},H={class:"modal-content"},E={class:"form-group"},X={class:"form-group"},$={class:"form-group"},B={class:"button-group"},D={key:0,class:"error-message"};function P(e,t,n,r,i,l){return n.isOpen?((0,a.uX)(),(0,a.CE)("div",K,[(0,a.Lk)("div",H,[t[9]||(t[9]=(0,a.Lk)("h2",null,"호텔 관리자 계정 생성",-1)),(0,a.Lk)("form",{onSubmit:t[4]||(t[4]=(0,o.D$)(((...e)=>l.handleCreateAdmin&&l.handleCreateAdmin(...e)),["prevent"])),class:"form-container"},[(0,a.Lk)("div",E,[t[5]||(t[5]=(0,a.Lk)("label",{for:"hotelId"},"호텔 Index:",-1)),(0,a.bo)((0,a.Lk)("input",{"onUpdate:modelValue":t[0]||(t[0]=e=>i.newAdmin.hotelId=e),type:"number",id:"hotelId",class:"form-input",required:""},null,512),[[o.Jo,i.newAdmin.hotelId]])]),(0,a.Lk)("div",X,[t[6]||(t[6]=(0,a.Lk)("label",{for:"name"},"이름:",-1)),(0,a.bo)((0,a.Lk)("input",{"onUpdate:modelValue":t[1]||(t[1]=e=>i.newAdmin.name=e),type:"text",id:"name",class:"form-input",required:""},null,512),[[o.Jo,i.newAdmin.name]])]),(0,a.Lk)("div",$,[t[7]||(t[7]=(0,a.Lk)("label",{for:"email"},"이메일:",-1)),(0,a.bo)((0,a.Lk)("input",{"onUpdate:modelValue":t[2]||(t[2]=e=>i.newAdmin.email=e),type:"email",id:"email",class:"form-input",required:""},null,512),[[o.Jo,i.newAdmin.email]])]),(0,a.Lk)("div",B,[t[8]||(t[8]=(0,a.Lk)("button",{type:"submit",class:"btn btn-primary"},"계정 생성",-1)),(0,a.Lk)("button",{type:"button",onClick:t[3]||(t[3]=(...e)=>l.closeModal&&l.closeModal(...e)),class:"btn btn-secondary"},"취소")])],32),i.errorMessage?((0,a.uX)(),(0,a.CE)("div",D,(0,s.v_)(i.errorMessage),1)):(0,a.Q3)("",!0)])])):(0,a.Q3)("",!0)}var x=n(4373);const q=x.A.create({baseURL:"http://43.200.45.122/api/admin/auth/",headers:{"Content-Type":"application/json"}});function z(e){return q.get("user-read",{headers:{Authorization:`Bearer ${e}`}})}function Q(e){return q.get("hotelAdmin-read",{headers:{Authorization:`Bearer ${e}`}})}function F(e,t){return q.get("user-search",{params:{search:t},headers:{Authorization:`Bearer ${e}`}})}function J(e,t){return q.get("hotelAdmin-search",{params:{search:t},headers:{Authorization:`Bearer ${e}`}})}function W(e,t){return q.post("hotel",null,{params:{userId:t},headers:{Authorization:`Bearer ${e}`}})}function N(e,t){return q.post("review-report",null,{params:{reportId:t},headers:{Authorization:`Bearer ${e}`}})}function j(e){return q.get("review-read",{headers:{Authorization:`Bearer ${e}`}})}function Y(e){return q.get("userInfo",{headers:{Authorization:`Bearer ${e}`}})}function G(e){return q.get("reviewReportInfo",{headers:{Authorization:`Bearer ${e}`}})}function Z(e,t,n){return q.post(`hotelAdmin-create/${n}`,t,{headers:{Authorization:`Bearer ${e}`}})}var ee={props:{isOpen:{type:Boolean,required:!0},onClose:{type:Function,required:!0},adminToken:{type:String,required:!0}},data(){return{newAdmin:{email:"",name:"",hotelId:null},errorMessage:""}},methods:{async handleCreateAdmin(){try{const e=await Z(this.adminToken,{email:this.newAdmin.email,name:this.newAdmin.name,phone:this.newAdmin.phone},this.newAdmin.hotelId);alert(e.data),this.closeModal()}catch(e){this.errorMessage=e.response?.data||"오류가 발생했습니다."}},closeModal(){this.errorMessage="",this.$emit("close")}}},te=n(1241);const ne=(0,te.A)(ee,[["render",P],["__scopeId","data-v-71eb0244"]]);var ae=ne;const se={key:0,class:"modal"},oe={class:"modal-content"},re={class:"btn-container"},ie={key:0,class:"error-message"};function le(e,t,n,r,i,l){return n.isOpen?((0,a.uX)(),(0,a.CE)("div",se,[(0,a.Lk)("div",oe,[t[5]||(t[5]=(0,a.Lk)("h2",null,"비밀번호 인증",-1)),(0,a.Lk)("form",{onSubmit:t[2]||(t[2]=(0,o.D$)(((...e)=>l.handleSubmit&&l.handleSubmit(...e)),["prevent"]))},[(0,a.Lk)("div",null,[t[3]||(t[3]=(0,a.Lk)("label",{for:"password",class:"text"},"비밀번호:",-1)),(0,a.bo)((0,a.Lk)("input",{type:"password",id:"password","onUpdate:modelValue":t[0]||(t[0]=e=>i.password=e),required:""},null,512),[[o.Jo,i.password]])]),(0,a.Lk)("div",re,[t[4]||(t[4]=(0,a.Lk)("button",{type:"submit",class:"comfirm-btn"},"확인",-1)),(0,a.Lk)("button",{type:"button",onClick:t[1]||(t[1]=(...e)=>l.closeModal&&l.closeModal(...e)),class:"cancel-btn"},"취소")])],32),i.errorMessage?((0,a.uX)(),(0,a.CE)("div",ie,(0,s.v_)(i.errorMessage),1)):(0,a.Q3)("",!0)])])):(0,a.Q3)("",!0)}var de=n(412),ue={props:{isOpen:{type:Boolean,required:!0},onClose:{type:Function,required:!0},adminToken:{type:String,required:!0}},data(){return{password:"",errorMessage:""}},methods:{async handleSubmit(){try{await(0,de.PY)(this.password,this.adminToken),this.$emit("verified"),this.closeModal()}catch(e){this.errorMessage=e.response?.data||"비밀번호가 틀렸습니다."}},closeModal(){this.errorMessage="",this.$emit("close")}}};const ce=(0,te.A)(ue,[["render",le],["__scopeId","data-v-0965327a"]]);var pe=ce,he=n(7367),ve=n(144),ke={name:"SystemAdminPage",components:{SidebarLayout:T.A,ReviewModal:he.A,HotelAdminModal:ae,PasswordVerification:pe},data(){return{currentView:"Dashboard",isModalOpen:!1,isVerified:!1,isReviewModalOpen:!1,adminToken:sessionStorage.getItem("token")}},setup(){const e=(0,ve.KR)([]),t=(0,ve.KR)([]),n=(0,ve.KR)([]),a=(0,ve.KR)(""),s=(0,ve.KR)(0),o=(0,ve.KR)(0),r=(0,ve.KR)(0),i=(0,ve.KR)(0),l=(0,ve.KR)(0),d=(0,ve.KR)(0),u=(0,ve.KR)(0),c=(0,ve.KR)(0),p=(0,ve.KR)(0),h=sessionStorage.getItem("token"),v=async()=>{const t=await z(h);e.value=t.data},k=async()=>{const e=await Q(h);n.value=e.data},m=async()=>{const e=await j(h);t.value=e.data},L=async()=>{const e=sessionStorage.getItem("token"),t=await Y(e),n=await G(e);s.value=t.data.userAllCount,o.value=t.data.hotelAllCount,r.value=n.data.reportCount,i.value=t.data.userActiveCount,l.value=t.data.userUnActiveCount,d.value=t.data.hotelActiveCount,u.value=t.data.hotelUnActiveCount,c.value=n.data.reportInComplete,p.value=n.data.reportComplete},w=async e=>{try{const t=await N(h,e);console.log(t.data),m(),L()}catch(t){console.error("리뷰 숨김처리 실패 ",t)}},C=async e=>{try{const t=await W(h,e);console.log(t.data),k(),L()}catch(t){console.error("계정 상태 변경 실패 ",t)}},f=async e=>{try{const t=await W(h,e);console.log(t.data),v(),L()}catch(t){console.error("계정 상태 변경 실패 ",t)}},b=async()=>{try{if(""===a.value.trim())await v();else{const t=await F(h,a.value);e.value=t.data}}catch(t){console.error("사용자 검색 실패",t)}},g=async()=>{try{if(""===a.value.trim())await k();else{const e=await J(h,a.value);n.value=e.data}}catch(e){console.error("사용자 검색 실패",e)}};return{userList:e,hotelManagerList:n,reportList:t,totalUserCount:s,totalHotelCount:o,totalReportCount:r,activeUserCount:i,inactiveUserCount:l,activeHotelCount:d,inactiveHotelCount:u,completeReportCount:c,incompleteReportCount:p,searchKeyword:a,fetchUserList:v,fetchHotelManagerList:k,fetchReportList:m,fetchDashboard:L,handleHideReport:w,handleAccountStatusChange:C,handleUserStatusChange:f,handleUserSearch:b,handleHotelSearch:g}},mounted(){this.fetchUserList(),this.fetchHotelManagerList(),this.fetchDashboard(),this.fetchReportList(),this.checkSessionValidity()},watch:{searchKeyword(e){"UserManagement"===this.currentView.valueOf?this.handleUserSearch(e):"HotelAdminAccounts"===this.currentView.valueOf&&this.handleHotelSearch(e)},currentView(e){"UserManagement"!==e&&"HotelAdminAccounts"!==e||this.isVerified?"Dashboard"!==e&&"ReviewReports"!==e||(this.isPasswordModalOpen=!1):this.openPasswordModal()}},methods:{openModal(){this.isModalOpen=!0},closeModal(){this.isModalOpen=!1},openPasswordModal(){this.isPasswordModalOpen=!0},closePasswordModal(){this.isPasswordModalOpen=!1},handleVerified(){this.isVerified=!0,this.isPasswordModalOpen=!1;const e=(new Date).getTime();sessionStorage.setItem("isVerified","true"),sessionStorage.setItem("verifiedTime",e.toString()),this.startSessionTimeout(15)},startSessionTimeout(e){const t=60*e*1e3;setTimeout((()=>{this.expireSession()}),t)},expireSession(){this.isVerified=!1,sessionStorage.removeItem("isVerified"),sessionStorage.removeItem("verifiedTime"),alert("인증이 만료되었습니다. 다시 인증해주세요."),this.openPasswordModal()},handleBeforeUnload(){sessionStorage.removeItem("isVerified"),sessionStorage.removeItem("verifiedTime")},checkSessionValidity(){const e=sessionStorage.getItem("verifiedTime");if(e){const t=(new Date).getTime(),n=t-parseInt(e,10),a=9e5;n<=a?(this.isVerified=!0,this.startSessionTimeout((a-n)/6e4)):this.expireSession()}},openReviewModal(e){this.selectedReview=e,this.isReviewModalOpen=!0},closeReviewModal(){this.isReviewModalOpen=!1,this.selectedReview=null}},beforeRouteLeave(){sessionStorage.removeItem("isVerified"),sessionStorage.removeItem("verifiedTime")},beforeUnmount(){window.removeEventListener("beforeunload",this.handleBeforeUnload)}};const me=(0,te.A)(ke,[["render",O],["__scopeId","data-v-34c524af"]]);var Le=me}}]);
//# sourceMappingURL=176.60a568be.js.map
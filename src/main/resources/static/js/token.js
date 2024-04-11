const token = searchParam('token');

if(token){ // 파라미터로 받은 토큰이 있다면
    localStorage.setItem("access_token", token) // 로컬 저장소에 저장
}
function searchParam(key){
    return new URLSearchParams(location.search).get(key);
}
const deleteButton = document.getElementById("delete-button");
const updateButton = document.getElementById("update-button");
const createButton = document.getElementById("create-button");
const homeButton = document.getElementById("home-button");


if(deleteButton){
    deleteButton.addEventListener("click", event =>{
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {
            method: "DELETE",
        })
            .then(() =>{
                alert("삭제가 완료되었습니다.");
                location.replace("/articles");
            })
    })
}

if (updateButton){
    updateButton.addEventListener("click", event =>{
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        fetch(`/api/articles/${id}`, {
            method: "PUT",
            headers: {
                "content-Type": "application/json",
            },
            body: JSON.stringify({
                title : document.getElementById('title').value,
                content: document.getElementById('content').value,
            })
        })
            .then(() =>{
                alert("수정이 완료되었습니다.");
                location.replace(`/articles/${id}`);
            })
    })
}

if(createButton){
    createButton.addEventListener("click", event =>{
        fetch(`/api/articles`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById("title").value,
                content: document.getElementById("content").value,
            }),
        }).then(() =>{
            alert("등록이 완료되었습니다.");
            location.replace("/articles");
        })
    })
}
if(homeButton){
    homeButton.addEventListener("click", event =>{
        location.replace("/articles");
});
}
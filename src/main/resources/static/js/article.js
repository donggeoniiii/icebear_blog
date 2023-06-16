// 생성기능, html에서 create-btn과 연결할 엘리먼트
const createBtn = document.getElementById("create-btn");

// 생성 기능 구현
if (createBtn){
    // 페이지에서 생성 버튼을 누르면
    createBtn.addEventListener('click', event => {
        // controller로 fetch를 보내 create(POST) 요청
        fetch(`/api/article`, {
            method: 'POST',
            // json 형태로 전달할거니까 전달할 데이터 형식 알려주고
            headers: {
                'Content-Type': "application/json",
            },
            // 폼으로 입력받은 정보 전달
            body: JSON.stringify({
                title: document.getElementById("title").value,
                content: document.getElementById("content").value,
            }),
        })
            // 등록 알림, 처음으로 이동
            .then(() => {
                alert("등록되었습니다.");
                location.replace('/blog/article');
            });
    })
}

// 수정기능, html에서 update-btn과 연결할 엘리먼트
const updateBtn = document.getElementById("update-btn");

// 수정 기능 구현
if (updateBtn) {
    // 수정페이지에서 수정 버튼을 누르면
    updateBtn.addEventListener('click', event => {
        // location.search로 쿼리 스트링 정보 가져오기.
        let params = new URLSearchParams(location.search);

        // 쿼리스트링에서 수정할 게시글 id 가져오고(이미 이 버튼이 눌린 시점에서 무조건 null은 아니어야 함)
        let id = params.get('id');

        // fetch로 update(PUT) 요청
        fetch(`/api/article/${id}`, {
            method: 'PUT',
            // json 형식으로 보낼거니까 contents type 설정하고
            headers: {
                "Content-Type": "application/json",
            },
            // JSON 형태로 form과 textarea에 입력된 수정사항 전달
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            // 수정 알림, 해당 게시글의 상세 보기 페이지로 이동
            .then(() => {
                alert('수정되었습니다.');
                location.replace(`/blog/article/${id}`);
            });
    })
}

// 삭제기능, html에서 delete-btn class와 연결할 엘리먼트
const deleteBtn = document.getElementById('delete-btn');

// 삭제기능 구현
if (deleteBtn){

    // 클릭버튼이 눌리면
    deleteBtn.addEventListener('click', event => {
        // document에서 article-id라는 element값을 받아오기
        let id = document.getElementById('article-id').value;

        // controller로 fetch로 DELETE 요청
        fetch(`/api/article/${id}`, {
            method: 'DELETE'
        })
            // 그러고 나서 삭제되었다고 알림, 전체 글 목록으로 돌아가기
            .then(() => {
                alert('삭제되었습니다.');
                location.replace('/blog/article');
            })
    })
}

function sign_out() {
    alert('다음에 또 뵙겠습니다. ^^')
    $.removeCookie('mytoken',{path:'/'})
    window.location.href = "/user/logout"
}

function fileupload() {
    const fileInput = document.querySelector('#file-js-example input[type=file]');

    fileInput.onchange = () => {
        if (fileInput.files.length > 0) {
            const fileName = document.querySelector('#file-js-example .file-name');
            fileName.textContent = fileInput.files[0].name;
        }
    }
}

function get_write_posts(user_id) {
    $.ajax({
        type: "GET",
        url: `/user/get_write_posts/${user_id}`,
        data: {},
        success: function (response) {
            $("#card-box-post").empty();

            for (let i = 0; i < response.length; i++) {
                make_post(response[i], "post");
            }
//            let comments = response["comments"];
//            let reviews = response["reviews"];
//            for (let i = 0; i < comments.length; i++) {
//                make_post(comments[i], "comment");
//            }
        }
    });
}

function get_like_posts(user_id) {
    $.ajax({
        type: "GET",
        url: `/user/get_like_posts/${user_id}`,
        data: {},
        success: function (response) {
            $("#card-box-like").empty();
            for (let i = 0; i < response.length; i++) {
                make_post(response[i], "like");
            }
        }
    });
}

function make_post(post, type) {
    let temp_html = `<div class="col" style="cursor: pointer">
                                <div class="card h-100" id="card-${post['postId']}">
                                    <img src="${post['postImgs'][0]['imgUrl']}" class="card-img-top image" onclick="location.href='/post/${post["postId"]}'">
                                    <div class="card-body">
                                        <h5 class="card-title" onclick="location.href='/post/${post['postId']}'">${post['title']}</h5>
                                        <p class="card-text" style="font-weight: bold;">${post['price']}원</p>
                                        <p class="address-text">${post['address']}</p>
                                        <p class="card-text small-text">관심 ${post['likeCount']}</p>
                                    </div>
                                </div>
                            </div>`
    $(`#card-box-${type}`).append(temp_html)
}

function is_password(asValue) {
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{8,20}$/;
    return regExp.test(asValue);
}

function check_dup_nick() {
    let nickname = $("#input-nickname").val()

    if (nickname == "") {
        $("#help-nickname").text("닉네임을 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-nickname").focus()
        return;
    }
    $("#help-nickname").addClass("is-loading")
    $.ajax({
        type: "POST",
        url: "/user/sign_up/check_dup_nick",
        data: {
            'nickname': nickname
        },
        success: function (data) {
            if (data >= 1) {
                $("#help-nickname").text("이미 존재하는 닉네임입니다.").removeClass("is-safe").addClass("is-danger")
                $("#input-nickname").focus()
            } else {
                $("#help-nickname").text("사용할 수 있는 닉네임입니다.").removeClass("is-danger").addClass("is-success")
            }
            $("#help-nickname").removeClass("is-loading")
        }
    });
}



function juso() {
    new daum.Postcode({
        oncomplete: function (data) {
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ''; // 참고 항목 변수

            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            if (extraRoadAddr !== '') {
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }

            newjuso = data.jibunAddress.split(' ').slice(0, -1).join(' ')
            document.getElementById("input-address").value = newjuso // 지번주소
        }
    }).open();
}

//카카오 초기 비밀번호 알려주기
function kakao_pw_check(){
    alert("카카오 로그인 시 초기 비밀번호는 카카오 이메일의 \"@\"앞 부분 입니다!\nEx) 이메일 : goguma@naver.com -> 비밀번호 : goguma")
}
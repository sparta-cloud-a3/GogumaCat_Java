
function sign_in() {
    let username = $("#input-username").val()
    let password = $("#input-password").val()

    if (username == "") {
        $("#help-id-login").text("아이디를 입력해주세요.")
        $("#input-username").focus()
        return;
    } else {
        $("#help-id-login").text("")
    }

    if (password == "") {
        $("#help-password-login").text("비밀번호를 입력해주세요.")
        $("#input-password").focus()
        return;
    } else {
        $("#help-password-login").text("")
    }
    $.ajax({
        type: "POST",
        url: "/user/login1",
        data: JSON.stringify ({
            'username': username,
            'password': password
        }),
        contentType: 'application/json',
        success: function (response) {
            $.cookie('mytoken', response, {path: '/'});
            window.location.replace("/")
        },
        error : function (response){
            alert("ID와 PW를 확인해 주세요.")
            $("#input-username").focus()
            $("#input-username").val("")
            $("#input-password").val("")



        }
    });
}

let url = 'https://389rjyao80.execute-api.us-east-1.amazonaws.com/default/project-lambda';
function sign_up() {
    let username = $("#input-username").val()
    let nickname = $("#input-nickname").val()
    let password = $("#input-password").val()
    let password2 = $("#input-password2").val()
    let address = $("#input-address").val()
    console.log(username, nickname, password, password2, address)


    if ($("#help-id").hasClass("is-danger")) {
        alert("아이디를 다시 확인해주세요.")
        return;
    } else if (!$("#help-id").hasClass("is-success")) {
        alert("아이디 중복확인을 해주세요.")
        return;
    }

    if ($("#help-nickname").hasClass("is-danger")) {
        alert("닉네임을 다시 확인해주세요.")
        return;
    } else if (!$("#help-nickname").hasClass("is-success")) {
        alert("닉네임 중복확인을 해주세요.")
        return;
    }

    if (password == "") {
        $("#help-password").text("비밀번호를 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-password").focus()
        return;
    } else if (!is_password(password)) {
        $("#help-password").text("비밀번호의 형식을 확인해주세요. 영문과 숫자 필수 포함, 특수문자(!@#$%^&*) 사용가능 8-20자").removeClass("is-safe").addClass("is-danger")
        $("#input-password").focus()
        return
    } else {
        $("#help-password").text("사용할 수 있는 비밀번호입니다.").removeClass("is-danger").addClass("is-success")
    }
    if (password2 == "") {
        $("#help-password2").text("비밀번호를 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-password2").focus()
        return;
    } else if (password2 != password) {
        $("#help-password2").text("비밀번호가 일치하지 않습니다.").removeClass("is-safe").addClass("is-danger")
        $("#input-password2").focus()
        return;
    } else {
        $("#help-password2").text("비밀번호가 일치합니다.").removeClass("is-danger").addClass("is-success")
    }
    if (address == "") {
        $("#help-address").text("주소를 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-address").focus()
        return;
    }
    $.ajax({
        type: "POST",
        url: "/user/signup",
        data: {
            'username': username,
            'password': password,
            'nickname': nickname,
            'address': address
        },
        success: function (response) {
            alert("회원가입을 축하드립니다!")
            window.location.replace("/user/login")
        }
    });

}

function is_id(asValue) {
    var regExp = /^(?=.*[a-zA-Z])[-a-zA-Z0-9_.]{2,10}$/;
    return regExp.test(asValue);
}

function is_password(asValue) {
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{8,20}$/;
    return regExp.test(asValue);
}

function check_dup() {
    let username = $("#input-username").val()
    console.log(username)
    if (username == "") {
        $("#help-id").text("아이디를 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-username").focus()
        return;
    }
    if (!is_id(username)) {
        $("#help-id").text("아이디의 형식을 확인해주세요. 영문과 숫자, 일부 특수문자(._-) 사용 가능. 2-10자 길이").removeClass("is-safe").addClass("is-danger")
        $("#input-username").focus()
        return;
    }
    $("#help-id").addClass("is-loading")
    $.ajax({
        type: "POST",
        url: "/user/sign_up/check_dup",
        data: {'username': username },
        dataType : "JSON",
        success: function (data) {
            console.log(data);
            if (data >= 1) {
                $("#help-id").text("이미 존재하는 아이디입니다.").removeClass("is-safe").addClass("is-danger")
                $("#input-username").focus()
            } else {
                $("#help-id").text("사용할 수 있는 아이디입니다.").removeClass("is-danger").addClass("is-success")
            }
            $("#help-id").removeClass("is-loading")
        }
    });
}

function check_dup_nick() {
    let nickname = $("#input-nickname").val()
    console.log(nickname)
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
            var jibun = data.autoJibunAddress
            var extraRoadAddr = ''; // 참고 항목 변수

            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            if (extraRoadAddr !== '') {
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }if (jibun == "") {
                newjuso = data.jibunAddress.split(' ').slice(0,-1).join(' ')
                document.getElementById("input-address").value = newjuso
            } else {
                newjuso = data.jibunAddress.split(' ').slice(0,-1).join(' ')
                document.getElementById("input-address").value = newjuso
            }
        }
    }).open();
}
function kakao_login(){
    alert("원할한 진행을 위해 꼭 카카오 이메일도 동의 부탁드려요 ㅠㅠ")
    window.location.href='https://kauth.kakao.com/oauth/authorize?client_id=1cc8b1fcd1283d896e04d323749f5c38&redirect_uri=http://localhost:8080/user/kakao/callback&response_type=code'
}
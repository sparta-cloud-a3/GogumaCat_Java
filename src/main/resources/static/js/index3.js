(() => {
    function listing(order_type) {
        $.ajax({
            type: "GET",
            url: "/all",
            data: {
                'orderType': order_type
            },
            success: function (response) {
                document.querySelector(".card-wrap").empty();

                for (let i = 0; i < 8; i++) {
                    make_post(response[i]);
                }
            }
        });
    }
    function make_post(post) {
        let temp_html = `<article class="card" id="card-${post['postId']}">
                            <a href="'/post/${post["postId"]}'" class="crad-link">
                                <div class="card-img">
                                    <img src="${post['postImgs'][0]['imgUrl']}" alt="title">
                                </div>
                                <div class="card-desc">
                                    <p class="card-title">${post['title']}</p>
                                    <p class="card-price">${post['price']}원</p>
                                    <p class="card-address">${post['address']}</p>
                                    <p class="card-text">관심 ${post['likeCount']}</p>
                                </div>
                            </a>
                        </article>`
        $("#card-box").append(temp_html)
    }
    window.addEventListener('load' , ()=>{
        listing("latest")
    })
})()
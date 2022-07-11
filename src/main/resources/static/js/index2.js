(() => {
    let yOffset = 0; //window.pageYOffset 변수
    let prevScrollHeight = 0; //현재 스크롤 보다 이전에 스크롤 섹션들의 높이 합
    let nowSection = 0; //현재 보고있는 섹션
    let lodingBox = 0; //로딩 박스 채우기
    let whiteMove = 0; //로딩 박스 움직이기
    let canvasMove = 0; //캔버스 애니메이션
    let logoMove = 0; //로고 애니메이션
    let messageOpacity = 0; //메인메세지 오퍼서티

    const nav_info = [{
        type : 'nav',
        height : document.querySelector('.logo-nav').clientHeight,
        objs : {
            contain : document.querySelector('.logo-container'),
            logoImage : document.querySelector('#logo'),
            mainNav : document.querySelector('.main-nav-list'),
            searchBar : document.querySelector('.search-bar')
        }
    }
    ]

    const scene_info = [
        {
            //0
            type : 'sticky',
            //height : 4,
            scrollHeight : document.querySelector('#section-0').clientHeight,
            objs: {
                contain : document.querySelector('#section-0'),
                message : document.querySelector('.main-message'),
                messageB : document.querySelector('.main-message a'),
                messageA : document.querySelector('.main-message b'),
                canvas : document.querySelector('.main-canvas'),
                context : document.querySelector('.main-canvas').getContext('2d'),
                image_path : [
                    '/img/logo-image.jpg'
                ],
                image : []
            } ,
            values : {
                rect1X : [0, 0 , {start : 0, end : 0}],
                rect2Y : [0, 0 , {start : 0, end : 0}],
                rect3X : [0, 0 , {start : 0, end : 0}],
                rect4Y : [0, 0 , {start : 0, end : 0}],


            }
        },{
            //1
            type : 'normal',
            scrollHeight : document.querySelector('#section-1').clientHeight,
            objs : {
                contain : document.querySelector('#section-1'),

                desc : document.querySelector('#section-2 .desc-0'),
                descImg_0 : document.querySelector('#section-2 .desc-img'),
                descImg_1: document.querySelector('#section-2 .desc-img-2'),
                descA : document.querySelector('#section-2 .desc-0-a')

            },
            values :{
                desc_OpacityIn : [0 , 0.8 , { start: 0.8, end: 1 }], //섹션 2 미리띄우기
                desc_TranslateIn : [10 , 0, { start: 0.8, end: 1 }],
                descImg_0_OpacityIn : [0 , 0.8 , { start: 0.9, end: 1 }],
                descImg_0_TranslateIn : [10 , 0 , { start: 0.9, end: 1 }],
            }
        },{
            //2
            type : 'sticky',
            //height : 3,
            scrollHeight : document.querySelector('#section-2').clientHeight,
            objs : {
                contain : document.querySelector('#section-2'),
                desc : document.querySelector('#section-2 .desc-0'),
                descImg_0 : document.querySelector('#section-2 .desc-img'),
                descImg_1: document.querySelector('#section-2 .desc-img-2'),
                descA : document.querySelector('#section-2 .desc-0-a')
            },
            values :{
                desc_OpacityIn : [0.8 , 1 , { start: 0, end: 0.1 }],
                // desc_TranslateIn : [10 , 0, { start: 0, end: 0.1 }],
                descImg_0_OpacityIn : [0.8 , 1 , { start: 0, end: 0.1 }],
                // descImg_0_TranslateIn : [10 , 0 , { start: 0, end: 0.1 }],

                descImg_1_OpacityIn : [0 , 1 , { start: 0.14, end: 0.3 }],
                descImg_1_TranslateIn : [20 , 0 , { start: 0.14, end: 0.3 }],
                descA_OpacityIn : [0 , 1 , { start: 0.3, end: 0.4 }],
                descA_TranslateIn : [20 , 0 , { start: 0.3, end: 0.4 }],
            }
        },{
            //3
            type : 'sticky',
            //height : 3,
            scrollHeight : document.querySelector('#section-3').clientHeight,
            objs : {
                contain : document.querySelector('#section-3'),
                desc : document.querySelector('.desc-1'),
            },
            values :{

            }
        },{
            //4
            type : 'normal',
            scrollHeight : document.querySelector('#section-4').clientHeight,
            objs : {
                contain : document.querySelector('#section-4')
            }
        }
    ];


    //canvas 실행이후 메뉴바가 아래로 내려오게 만듬
    function logoY() {
        let logoNav = nav_info[0].objs.contain
        let logoHeight = nav_info[0].height
        if(logoMove < logoHeight){
            logoNav.style.transform = `translateY(${-logoHeight+logoMove}px)`

            logoMove +=8;

            let logoAni = requestAnimationFrame(logoY)
            if(logoMove > logoHeight) {
                cancelAnimationFrame(logoAni)
                setTimeout(() => {
                    mainMessageOpacity()
                }, 450 )

            }
        }
    }

    //메인 캔버스 그리기
    function canvas() {
        const objs = scene_info[0].objs;
        const values = scene_info[0].values;
        objs.canvas.width = window.innerWidth;
        objs.canvas.height = window.innerHeight + 50

        let imgElem = new Image();
        imgElem.src = objs.image_path[0]
        objs.image.push(imgElem)

        objs.context.fillStyle = 'white'

        //좌우 흰색박스
        const leftWhiteRect = objs.canvas.width * 0.5
        const rightWhiteRect = objs.canvas.width * 0.5

        values.rect1X[1] = values.rect1X[0] - leftWhiteRect;
        values.rect3X[0] = leftWhiteRect;
        values.rect3X[1] = objs.canvas.width

        //상하 흰색박스
        const topWhiteRect = objs.canvas.height * 0.5;
        const bottomWhiteRect = objs.canvas.height * 0.5;

        values.rect2Y[1] = values.rect2Y[0] - topWhiteRect;
        values.rect4Y[0] = topWhiteRect ;
        values.rect4Y[1] = objs.canvas.height

        //이미지 그리기
        objs.context.drawImage(objs.image[0], 0 , 0, objs.canvas.width,objs.canvas.height)
        //좌우 흰색박스 그리기
        objs.context.fillRect(
            parseInt(values.rect1X[0]-canvasMove),
            0,
            parseInt(leftWhiteRect),
            objs.canvas.height
        );
        objs.context.fillRect(
            parseInt(values.rect3X[0]+canvasMove),
            0,
            parseInt(rightWhiteRect),
            objs.canvas.height
        );

        // 위아래 흰색박스 그리기

        objs.context.fillRect(
            0,
            parseInt(values.rect2Y[0]-canvasMove),
            objs.canvas.height,
            parseInt(topWhiteRect),
        )
        objs.context.fillRect(
            0,
            parseInt(values.rect4Y[0]+canvasMove),
            objs.canvas.height,
            parseInt(bottomWhiteRect),
        )
        canvasMove += 30

        let canvasAni = requestAnimationFrame(canvas)



        if ( canvasMove > (window.innerWidth+100)) {
            cancelAnimationFrame(canvasAni)
            logoY()

        }
    }

    //메인 메세지 띄우기
    function mainMessageOpacity() {
        const mainMs = scene_info[0].objs.message
        if(messageOpacity <= 1) {
            mainMs.style.opacity = messageOpacity
            messageOpacity += 0.1
            let messageAni = requestAnimationFrame(mainMessageOpacity)
            if(messageOpacity>1) {
                cancelAnimationFrame(messageAni)
            }
        }
    }


    //새로고침이나 처음 들어왔을 때 세팅되는 섹션 번호
    function setLayout() {
        yOffset = window.pageYOffset
        let totalScroll = 0;
        for( let i=0; i <scene_info.length; i++) {
            totalScroll += scene_info[i].scrollHeight
            if (totalScroll >= yOffset) {
                nowSection = i;
                break;
            }
        }
    }

    //현재 스크롤 하고 있는 위치와 섹션 구하는 함수
    function scroll() {
        yOffset = pageYOffset
        prevScrollHeight = 0;
        for (let i = 0; i < nowSection; i++) {
            prevScrollHeight += scene_info[i].scrollHeight
        }

        if ( yOffset > prevScrollHeight + scene_info[nowSection].scrollHeight) {
            nowSection ++;
        } else if(yOffset < prevScrollHeight) {
            if(nowSection>0) { // 씬이 - 되는 것을 방지
                nowSection --;
            }
        }
       // scrollPlay()
    }

    // function transformAnimation() {
    //     let sectionTransform = 30; //섹션 애니메이션의 트렌스폼 값
    //         sectionTransform -=1
    //     let transformAni = requestAnimationFrame(transformAnimation)
    //     if(sectionTransform ==0) {
    //         cancelAnimationFrame(transformAni)
    //     }
    // }

    // function opacityAnimation() {
    //     let sectionOpacity = 0; //섹션 애니메이션의 오퍼서티값
    //         sectionOpacity +=0.1

    //     let opacityAni = requestAnimationFrame(opacityAnimation)
    //     if(sectionOpacity == 1){
    //         cancelAnimationFrame(opacityAni)
    //     }
    // }
    // function scrollPlay() {
    //     const objs = scene_info[nowSection].objs
    //     const values = scene_info[nowSection].values
    //     const currentYoffset = yOffset - prevScrollHeight
    //     const scrollHeight = scene_info[nowSection].scrollHeight
    //     const ratio = currentYoffset / scrollHeight

    //     switch (nowSection) {
    //         case 2 :
    //             console.log(nowSection , ratio)
    //                 if(objs.desc.style.opacity === 0) {
    //                     objs.desc.style.opacity = opacityAnimation();
    //                     objs.desc.style.transform = `translateY(${transformAnimation()}%)`;
    //                     objs.descImg_0.style.opacity = opacityAnimation();
    //                     objs.descImg_0.style.transform = `translateY(${transformAnimation()}%)`;
    //                     objs.descImg_1.style.opacity = opacityAnimation();
    //                     objs.descImg_1.style.transform = `translateY(${transformAnimation()}%)`;
    //                     objs.descA.style.opacity = opacityAnimation();
    //                     objs.descA.style.transform = `translateY(${transformAnimation()}%)`;
    //                 }
    //             break;
    //         case 3 :

    //             break;
    //     }
    // }
    // 첫 GOGUMAKET 로딩 함수
    function loding() {
        const box = document.querySelector('#box')
        box.style.width = `${lodingBox}px`
        lodingBox+=24
        // boxWidth++
        let raf = requestAnimationFrame(loding)
        if(lodingBox>800){
            cancelAnimationFrame(raf);
            setTimeout(() => {
                lodingMove()
            }, 550 )
        }
    }
    // 첫 로딩 함수 이후 하얗게 만드는 로딩
    function lodingMove() {
        const whiteBox = document.querySelector('#white-box')

        whiteBox.style.width = `${whiteMove}px`

        whiteMove +=30
        let whiteBoxMove = requestAnimationFrame(lodingMove)
        if(whiteMove > 1500) {
            cancelAnimationFrame(whiteBoxMove)
            document.querySelector('#loding').remove()
            canvas()
        }
    }




    window.addEventListener('load', () => {
        loding()
        setLayout()
        console.log(scene_info[2].objs.desc.style.translateY)
        // canvas()
    } )
    window.addEventListener('resize', () => {
        //nav_info[0].objs.contain.transform = translateY(-nav_info[0].height)
        canvas()
        setLayout()
    })
    window.addEventListener('scroll', () => {
        scroll()

    })
}) ();
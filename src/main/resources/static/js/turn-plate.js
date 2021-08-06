var turnplate = {
    restaraunts: [],				//大转盘奖品名称
    colors: [],					//大转盘奖品区块对应背景颜色
    outsideRadius: 212,			//大转盘外圆的半径
    textRadius: 155,				//大转盘奖品位置距离圆心的距离
    insideRadius: 45,			//大转盘内圆的半径
    startAngle: 0,				//开始角度
    bRotate: false				//false:停止;ture:旋转
};

$(document).ready(function () {

    //动态添加大转盘的奖品与奖品区域背景颜色
    turnplate.restaraunts = ["小米电视", "苹果耳机", "摄影背包", "三脚架套餐", "移动电源", "记事本"];
    turnplate.colors = ["transparent", "transparent", "transparent", "transparent", "transparent", "transparent"];

    //旋转转盘 item:奖品位置; txt：提示语;
    var rotateFn = function (item, txt) {
        var angles = item * (360 / turnplate.restaraunts.length) - (360 / (turnplate.restaraunts.length * 2));
        if (angles < 270) {
            angles = 270 - angles;
        } else {
            angles = 360 - angles + 270;
        }
        $('#wheelcanvas').stopRotate();
        $('#wheelcanvas').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function () {
                if (item ==-1) {
                    $('.shadowBox .prize .pic img').attr('src', 'images/pz1.png');
                }
                if (item ==-0) {
                    $('.shadowBox .prize .pic img').attr('src', 'images/pz2.png');
                }
                if (item ==1) {
                    $('.shadowBox .prize .pic img').attr('src', 'images/pz3.png');
                }
                if (item ==2) {
                    $('.shadowBox .prize .pic img').attr('src', 'images/pz4.png');
                }
                if (item ==3) {
                    $('.shadowBox .prize .pic img').attr('src', 'images/pz5.png');
                }
                if (item ==4) {
                    $('.shadowBox .prize .pic img').attr('src', 'images/pz6.png');
                }

                $('.shadowBox .prize .title').text(txt);
                $('.shadowBox').fadeIn();
                $('.shadowBox .prize').fadeIn();

                turnplate.bRotate = !turnplate.bRotate;
            }
        });
    };
    $('.shadowBox .itemBox .close').click(function () {
        $('.shadowBox .itemBox').fadeOut();
        $('.shadowBox').fadeOut();
        loadRecordData();
    })
    //立刻抽奖
    // $('.shadowBox .itemBox .close2,.hadList .back').click(function () {
    //     window.location.reload();
    // })
    $('.shadowBox .prize .btn').click(function () {
        //$('.shadowBox .writeInfo').fadeIn();
        $('.shadowBox .itemBox').fadeOut();
        $('.shadowBox').fadeOut();
        loadRecordData();
    })
    // $('.shadowBox .writeInfo .sub').click(function () {
    //     $('.shadowBox .subSuccess').fadeIn();
    // })
    // $('.subSuccess .my').click(function () {
    //     $('.shadowBox .hadList').fadeIn();
    // })

    $('.pointer').click(function () {
        if (turnplate.bRotate) return;
        turnplate.bRotate = !turnplate.bRotate;
        //获取随机数(奖品个数范围内)
        $.ajax({
            type:"get",
            url: "/lottery/1",
            cache:false,
            async:false,
            success: function(data){
                if(data.code == "0000"){
                    console.log(data);
                    document.getElementById("spDrawTimes").innerText=data.result["drawTimes"];
                    //奖品数量等于10,指针落在对应奖品区域的中心角度[252, 216, 180, 144, 108, 72, 36, 360, 324, 288]
                    rotateFn(data.result.level-2, data.result.prizeName);

                } else{
                    alert(data.msg);
                    turnplate.bRotate = !turnplate.bRotate;
                }
            }
        });
    });
});
//页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
window.onload = function () {
    drawRouletteWheel();
};

function drawRouletteWheel() {

    var canvas = document.getElementById("wheelcanvas");
    if (canvas.getContext) {
        //根据奖品个数计算圆周角度
        var arc = Math.PI / (turnplate.restaraunts.length / 2);
        var ctx = canvas.getContext("2d");
        //在给定矩形内清空一个矩形
        ctx.clearRect(0, 0, 422, 422);
        //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式
        ctx.strokeStyle = "transparent";
        //font 属性设置或返回画布上文本内容的当前字体属性
        ctx.font = '16px Microsoft YaHei';
        for (var i = 0; i < turnplate.restaraunts.length; i++) {
            var angle = turnplate.startAngle + i * arc;
            ctx.fillStyle = turnplate.colors[i];
            ctx.beginPath();
            //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）
            ctx.arc(211, 211, turnplate.outsideRadius, angle, angle + arc, false);
            ctx.arc(211, 211, turnplate.insideRadius, angle + arc, angle, true);
            ctx.stroke();
            ctx.fill();
            //锁画布(为了保存之前的画布状态)
            ctx.save();

            //----绘制奖品开始----
            ctx.fillStyle = "transparent";
            var text = turnplate.restaraunts[i];
            var line_height = 17;
            //translate方法重新映射画布上的 (0,0) 位置
            ctx.translate(211 + Math.cos(angle + arc / 2) * turnplate.textRadius, 211 + Math.sin(angle + arc / 2) * turnplate.textRadius);
            //rotate方法旋转当前的绘图
            ctx.rotate(angle + arc / 2 + Math.PI / 2);
        }
    }
}

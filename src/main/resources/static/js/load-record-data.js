$(function () {
    loadRecordData();

    // var marquee = document.getElementById('marquee');
    // var offset = 0;
    // var scrollheight = marquee.offsetHeight;
    // var firstNode = marquee.children[0].cloneNode(true);
    // marquee.appendChild(firstNode);//还有这里

    // function mq() {
    //     if (offset == scrollheight) {
    //         offset = 0;
    //     }
    //     marquee.style.marginTop = "-" + offset + "px";
    //     offset += 1;
    // }

    // var t = setInterval(mq, 50);

    // $("#marquee").hover(function () {
    //     window.clearInterval(t);
    // }, function () {
    //     t = setInterval(mq, 50);
    // });
    // if (($('#marquee tr').length )/ 2 <= 4) {
    //     window.clearInterval(t);
    // }
    // $('#marquee tr').each(function (index, domEle) {
    //     // domEle == this
    //     var name = $(domEle).find('td:eq(1)').text();
    //     var nameAfter = name.substr(0, 3) + "****" + name.substr(7);
    //     $(domEle).find('td:eq(1)').text(nameAfter);
    // });
});



function loadRecordData(){

    $.ajax({
        type: "get",
        url: "/lottery-record",
        cache: false,
        async: false,
        success: function (data) {
            var html="";
            data.result.forEach(function(item,index){
                var trStr="<tr><td width='30%'>"+item.prizeName+"</td></td><td width='30%'>"+item.accountName+"</td><td width='40%'>"+item.createTime+"</td></tr>";
                html+=trStr;
            });
            if(html!=""){
                $("#marquee").html(html);
            }
        }
    });
}
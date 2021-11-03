
function showLoadingScreen(){
    var width = $(window).width();
    var height = $(window).height();
    //화면을 가리는 레이어의 사이즈 조정
    $(".backLayer").width(width);
    $(".backLayer").height(height);
    //화면을 가리는 레이어를 보여준다
    $(".backLayer").css("display","block");
    //팝업 레이어 보이게
    var loadingDivObj = $("#loadingDiv");
    loadingDivObj.css("top", $(window).height()/2-75);
    loadingDivObj.css("left",$(window).width()/2-50);
    loadingDivObj.css("display","flex");
    loadingDivObj.css("justify-content", "center");
    loadingDivObj.css("align-items", "center");
    loadingDivObj.css("flex-direction","column");
};

$(window).resize(function(){
    var width = $(window).width();
    var height = $(window).height();
    $(".backLayer").width(width).height(height);
    var loadingDivObj = $("#loadingDiv");
    loadingDivObj.css("top", height/2-50);
    loadingDivObj.css("left",width/2-100);
});

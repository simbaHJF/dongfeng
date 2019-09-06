<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>dongfeng admin</title>

    <link rel="stylesheet" type="text/css" href="/dongfeng/css/index.css">
    <script type="text/javascript" src="/dongfeng/js/jquery-3.4.1.js"></script>

    <link rel="stylesheet" type="text/css" href="/dongfeng/css/bootstrap.min.css">
    <script type="text/javascript" src="/dongfeng/js/bootstrap.min.js"></script>



</head>
<body>

<!--顶部导航栏部分-->
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" title="logoTitle" href="#">Dong Feng</a>
        </div>
        <div class="collapse navbar-collapse">
            <#--<ul class="nav navbar-nav navbar-right">
                <li role="presentation">
                    <a href="#">当前用户：<span class="badge">TestUser</span></a>
                </li>
                <li>
                    <a href="#">
                        <span class="glyphicon glyphicon-lock"></span>退出登录</a>
                </li>
            </ul>-->
        </div>
    </div>
</nav>
<!-- 中间主体内容部分 -->
<div class="pageContainer">
    <!-- 左侧导航栏 -->
    <div class="pageSidebar">
        <ul class="nav nav-stacked nav-pills">
            <li role="presentation">
                <a href="admin/dagIndex" target="mainFrame" >dag配置</a>
            </li>
            <li role="presentation">
                <a href="admin/jobIndex" target="mainFrame">job配置</a>
            </li>
            <li role="presentation">
                <a href="admin/dagLogIndex" target="mainFrame">dagLog记录</a>
            </li>
            <li role="presentation">
                <a href="admin/jobLogIndex" target="mainFrame">jobLog记录</a>
            </li>
        </ul>
    </div>
    <!-- 左侧导航和正文内容的分隔线 -->
    <div class="splitter"></div>
    <!-- 正文内容部分 -->
    <div class="pageContent">
        <iframe id="mainFrame" name="mainFrame" frameborder="0" width="100%"  height="100%" frameBorder="0"></iframe>
    </div>
</div>


<!-- 底部页脚部分 -->
<div class="footer">
    <p class="text-center">
        dongfeng schedule center
    </p>
</div>

<script>
    $(".nav li").click(function() {
        $(".active").removeClass('active');
        $(this).addClass("active");
    });
</script>
</body>
</html>

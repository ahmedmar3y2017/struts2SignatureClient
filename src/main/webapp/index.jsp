<%--
  Created by IntelliJ IDEA.
  User: ahmed.marey
  Date: 3/22/2019
  Time: 11:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>

<head>
  <title>Task</title>
  <meta charset="UTF-8">
  <!--Internet explorer-->
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <!--first Mobile Meta-->
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Title</title>

  <!--bootstrap stylesheet-->
  <link rel="stylesheet" href="css/bootstrap.css">
  <link rel="stylesheet" href="css/font-awesome.min.css">
  <link rel="stylesheet" href="css/css.css">
  <link rel="stylesheet" href="css/style.css">

  <!--[if lt IE 9]>
  <script src="js/html5shiv.min.js"></script>
  <script src="js/respond.min.js"></script>


  <![endif]-->
  <!--jquery lib-->
  <script src="js/jquery-2.0.0.js"></script>
  <!--bootstrap lib-->
  <script src="js/bootstrap.min.js"></script>
  <!--custom js file -->
  <script src="js/script.js"></script>


</head>
  <body>
  <script >


      function openSettingDialog()
      {
          var url='<%=request.getContextPath()%>/setting?userId=2';
          console.log(url);

          openPopUpWindow(url,'openSettingsWindow',750,600);
      }
      function openPopUpWindow(url,winName,width,height,paramWinFeatures)
      {
          var popUpWindowWidth=width;
          var popUpWindowHeight=height;
          var centerLeft=(window.screen.width-popUpWindowWidth)/2;
          var centerTop=(window.screen.height-popUpWindowHeight)/2;
          var features = 'width='+width+',height='+height+',left='+centerLeft+',top='+10;
          if(paramWinFeatures != null)
          {
              features += ','+paramWinFeatures
          }
          else
          {
              features += ',menubar=no,resizable=no,scrollbars=no,status=no';
          }
          var popUpWindow=window.open(url,winName,features);
          return popUpWindow;
      }
  </script>
  <button  class="btn-primary" onclick="openSettingDialog();">اعدادات التوقيع</button>

  <s:url action="setting" var="urlTag">
    <s:param name="userId" value="2" />
  </s:url>
  <a href='<s:property value="#urlTag" />' >  URL Tag Action (via property)</a>
  </body>
</html>

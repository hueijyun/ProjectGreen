<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<table border="1">
<c:forEach var='resType' items='${resTypes}' >
	<tr>
		<td>${resType.restaurantType}</td>
	</tr>
</c:forEach>
<c:forEach var='tType' items='${tTypes}' >
	<tr>
		<td>${tType.travelType}</td>
	</tr>
</c:forEach>
</table>
</body>
</html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="/static/css/friends.css"/>
</head>
<body>
<%@include file="headerWithLogout.jsp" %>
<div class="wall">
    <%@include file="menu.jsp" %>
    <div class="right">
        <div class="search">
            <form action="/user/searchFriends">Search people
                <input type="text" name="search"/>
                <input type="submit"/>
            </form>
        </div>
        <c:if test="${fn:length(applicationsFriends) > 0 }">
            Applications To Friends
        </c:if>
        <c:forEach items="${applicationsFriends}" var="application">
            <div class="friend">
                <div class="friends_avatar">
                    <a href="/user/id${application.user.id}/home">
                        <img src="${application.user.avatar}">
                    </a>
                </div>
                <a href="/user/id${application.user.id}/home">
                    <div class="name">${application.user.firstName} ${application.user.lastName}</div>
                </a>
                <div class="action">
                    <a href="/user/confirmFriends?id=${application.id}">
                        <input type="submit" value="Confirm">
                    </a>
                    <a href="/user/rejectFriends?id=${application.id}">
                        <input type="submit" value="Reject">
                    </a>
                </div>
            </div>
        </c:forEach>
        <c:if test="${fn:length(friends) > 0 }">
            You Friends
        </c:if>
        <c:forEach items="${friends}" var="friend">
            <div class="friend">
                <div class="friends_avatar">
                    <a href="/user/id${friend.friend.id}/home">
                        <img src="${friend.friend.avatar}">
                    </a>
                </div>
                <a href="/user/id${friend.friend.id}/home">
                    <div class="name">${friend.friend.firstName} ${friend.friend.lastName}</div>
                </a>
                <div>${friend.friendstatus}</div>
                <div class="action">
                    <c:if test="${friend.friendstatus=='REJECTED'}">
                        <a href="/user/addFriends?id=${friend.friend.id}&idFriend=${friend.id}">
                            <input type="submit" value="Add">
                        </a>
                        <a href="/user/deleteFriends?id=${friend.id}">
                            <input type="submit" value="Delete">
                        </a>
                    </c:if>
                    <c:if test="${friend.friendstatus=='WAIT'}">
                        <a href="/user/deleteFriends?id=${friend.id}">
                            <input type="submit" value="Delete">
                        </a>
                    </c:if>
                    <c:if test="${friend.friendstatus=='CONFIRM'}">
                        <a href="/user/deleteFriends?id=${friend.id}&idFriend=${friend.friend.id}&idUser=${friend.user.id}">
                            <input type="submit" value="Delete">
                        </a>
                    </c:if>
                </div>
            </div>
        </c:forEach>
        <c:if test="${fn:length(users) > 0 }">
            Search Friends
        </c:if>
        <c:forEach items="${users}" var="user">
            <div class="friend">
                <div class="friends_avatar">
                    <a href="/user/id${user.id}/home">
                        <img src="${user.avatar}">
                    </a>
                </div>
                <div class="name">
                    <a href="/user/id${user.id}/home">${user.firstName} ${user.lastName}
                    </a>
                </div>
                <div class="action">
                    <a href="/user/addFriends?id=${user.id}">
                        <input type="submit" value="Add">
                    </a>
                </div>
            </div>
        </c:forEach>
    </div>
    <%@include file="avatar.jsp" %>
</div>
</body>
</html>

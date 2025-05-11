<%--
  Created by IntelliJ IDEA.
  User: Camila
  Date: 11/5/2025
  Time: 01:03:a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME</title>
    <!-- Bootstrap CSS -->
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4" style="position: sticky; top: 0; z-index: 1030;">
    <div class="container-fluid">
        <!-- Inicio: Sección izquierda -->
        <div class="d-flex">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/ManageProductsController?route=list&view=home">
                        <i class="fas fa-home"></i> Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/ManageProductsController?route=list&view=user">
                        <i class="fas fa-box"></i> My Products
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/RespondOfferController?route=list">
                        <i class="fas fa-handshake"></i> Offers
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/TradeDeliveryController?route=listDeliveries">
                        <i class="fas fa-truck"></i> My Deliveries
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/FavoriteController?route=listFavorites">
                        <i class="fas fa-heart"></i> Mis Favoritos
                    </a>
                </li>
            </ul>
        </div>

        <!-- Fin: Sección derecha -->
        <div class="d-flex">
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link text-danger" href="${pageContext.request.contextPath}/LoginController?route=logOut">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="container my-4">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1 class="mb-0"> Favorite Products </h1>
            </div>
            <p>Total favoritos encontrados: ${fn:length(favorites)}</p>
            <c:forEach var="favorite" items="${favorites}">
                <div class="col-md-6 mb-3">
                    <div class="card border-0 rounded-3">
                        <div class="card-body p-4">
                            <a class="card-title text-dark fw-bold"
                               href="${pageContext.request.contextPath}/MakeOfferController?route=select&view=product&id=${favorite.product.idProduct}">
                                    ${favorite.product.title}
                            </a>
                            <p class="card-text text-secondary small mb-4">
                                <i class="fa-solid fa-align-left me-2"></i>${favorite.product.description}
                            </p>
                            <p class="card-text text-secondary small mb-4">
                                <i class="fa-solid fa-layer-group me-2"></i>${favorite.product.state}
                            </p>
                            <p class="card-text text-secondary small mb-4">
                                <i class="fa-solid fa-calendar me-2"></i>${favorite.product.datePublication}
                            </p>
                            <p class="card-text text-secondary small mb-4">
                                <c:choose>
                                    <c:when test="${favorite.product.isAvailable}">
                                        <i class="fa-solid fa-check me-2"></i>Available
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa-solid fa-x me-2"></i> Not available
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </div>
            </c:forEach>


            <c:if test="${empty favorite}">
                <div class="alert alert-warning text-center">There are currently no favorite products to display.</div>
            </c:if>
        </div>
    </div>
</main>
</html>

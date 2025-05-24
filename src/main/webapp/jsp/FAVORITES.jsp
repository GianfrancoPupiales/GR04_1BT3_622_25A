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
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/ManageProductsController?route=list&view=home">
                        <i class="fas fa-home"></i> Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/ManageProductsController?route=list&view=user">
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
                        <i class="fas fa-heart"></i> My Favorites
                    </a>
                </li>
            </ul>
        </div>

        <!-- Fin: Sección derecha -->
        <div class="d-flex">
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/ProfileController?route=show">
                        <i class="fas fa-user"></i> My Profile
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link text-danger"
                       href="${pageContext.request.contextPath}/LoginController?route=logOut">
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
                <h1 class="mb-0">Favorite Products</h1>
            </div>

            <!-- Feedback Message -->
            <c:if test="${not empty sessionScope.message}">
                <div id="notification"
                     class="alert text-center ${sessionScope.messageType == 'info' ? 'alert-success' : 'alert-danger'}"
                     role="alert">
                        ${sessionScope.message}
                </div>
                <c:remove var="message" scope="session"/>
                <c:remove var="messageType" scope="session"/>
            </c:if>

            <div class="row">
                <c:forEach var="favorite" items="${favorites}">
                    <div class="col-md-6 mb-3">
                        <div class="card border-0 rounded-3" style="height: 100%;">
                            <div class="card-body p-4 d-flex">
                                <div class="flex-grow-1 pe-3">
                                    <h3 class="card-title fw-bold">
                                        <a class="text-dark text-decoration-underline" href="${pageContext.request.contextPath}/MakeOfferController?route=select&view=product&id=${favorite.product.idProduct}">
                                                ${favorite.product.title}
                                        </a>
                                    </h3>
                                    <c:choose>
                                        <c:when test="${not empty favorite.product.user.profile}">
                                            <p class="card-text text-secondary small mb-2">
                                                <i class="fa-solid fa-user me-2"></i>
                                                <a href="${pageContext.request.contextPath}/ProfileController?route=public&id=${favorite.product.user.idUser}&from=home"
                                                   class="text-decoration-none">
                                                        ${favorite.product.user.profile.firstName} ${favorite.product.user.profile.lastName}
                                                </a>
                                            </p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="card-text text-muted"><i class="fa-solid fa-user me-2"></i>
                                                Unknown user</p>
                                        </c:otherwise>
                                    </c:choose>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-align-left me-2"></i>${favorite.product.description}
                                    </p>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-layer-group me-2"></i>${favorite.product.state}
                                    </p>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-tags me-2"></i>${favorite.product.category}
                                    </p>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-calendar me-2"></i>${favorite.product.datePublication}
                                    </p>
                                    <p class="card-text text-secondary small mb-3">
                                        <c:choose>
                                            <c:when test="${favorite.product.isAvailable}">
                                                <i class="fa-solid fa-check me-2"></i>Available
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa-solid fa-x me-2"></i> Not available
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <!-- Botón para eliminar de favoritos -->
                                    <form action="${pageContext.request.contextPath}/FavoriteController?route=removeFavorite"
                                          method="post">
                                        <input type="hidden" name="productId" value="${favorite.product.idProduct}">
                                        <button type="submit" class="btn btn-danger">
                                            <i class="fa-solid fa-trash-alt me-2"></i> Remove from Favorites
                                        </button>
                                    </form>
                                </div>
                                <!-- Imagen al lado derecho, ocupa tamaño completo de la card -->
                                <div style="width: 50%; max-width: 250px;">
                                    <c:if test="${not empty favorite.product.photo}">
                                        <img src="${pageContext.request.contextPath}/product-images/${favorite.product.photo}"
                                             alt="Product Photo"
                                             class="img-fluid h-100" style="object-fit: cover; border-radius: 0.25rem;">
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty favorites}">
                    <div class="alert alert-warning text-center">
                        You don't have any favourite products yet
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>
</html>

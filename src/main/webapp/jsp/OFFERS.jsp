<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Pending Offers</title>
    <!-- Bootstrap CSS -->
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
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
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="mb-0">Pending Offers </h1>
    </div>
    <!-- Mostrar mensajes dinámicos -->
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Mostrar ofertas pendientes -->
    <c:choose>
        <c:when test="${not empty offers}">
            <div class="row">
                <c:forEach var="offer" items="${offers}">
                    <div class="col-md-6 mb-3">
                        <div class="card border-0 shadow rounded-3 h-100">
                            <div class="card-body p-4">
                                <h5 class="card-title fw-bold mb-3">
                                    <i class="fas fa-gift me-2 text-primary"></i> Offered Products
                                </h5>
                                <ul class="list-unstyled ps-3 mb-3">
                                    <c:forEach var="product" items="${offer.offeredProducts}">
                                        <li>
                                            <i class="fas fa-box-open text-secondary me-2"></i>
                                            <a href="${pageContext.request.contextPath}/ManageProductsController?route=select&idProduct=${product.idProduct}"
                                               class="text-decoration-underline text-dark">
                                                    ${product.title}
                                            </a>

                                        </li>
                                    </c:forEach>
                                </ul>

                                <p class="card-text mb-2">
                                    <i class="fas fa-hand-holding me-2 text-success"></i>
                                    <strong>Requested:</strong>
                                    <a href="${pageContext.request.contextPath}/ManageProductsController?route=select&idProduct=${offer.productToOffer.idProduct}"
                                       class="text-decoration-underline text-dark">
                                            ${offer.productToOffer.title}
                                    </a>
                                </p>

                                <p class="card-text mb-4">
                                    <i class="fas fa-user me-2 text-muted"></i>
                                    <strong>User:</strong>
                                    <a href="${pageContext.request.contextPath}/ProfileController?route=public&id=${offer.offeredByUser.idUser}&from=offers"
                                       class="text-decoration-underline text-dark">
                                            ${offer.offeredByUser.profile.firstName} ${offer.offeredByUser.profile.lastName}
                                    </a>

                                </p>

                                <form action="RespondOfferController" method="post" class="d-flex gap-2">
                                    <input type="hidden" name="route" value="respond">
                                    <input type="hidden" name="offerId" value="${offer.idOffer}">

                                    <button type="submit" name="status" value="accepted"
                                            class="btn btn-outline-success w-50">
                                        <i class="fas fa-check"></i> Accept
                                    </button>
                                    <button type="submit" name="status" value="rejected"
                                            class="btn btn-outline-danger w-50">
                                        <i class="fas fa-times"></i> Reject
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning text-center">No pending offers found</div>
        </c:otherwise>
    </c:choose>
</main>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
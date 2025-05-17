<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>

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
                    <a class="nav-link text-danger" href="${pageContext.request.contextPath}/LoginController?route=logOut">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm border-0 rounded-3">
                <div class="card-body p-5">
                    <div class="d-flex align-items-center mb-4">
                        <!-- Avatar simulado -->
                        <div class="me-4">
                            <div class="rounded-circle bg-secondary d-flex justify-content-center align-items-center" style="width: 100px; height: 100px; font-size: 1.5rem; color: white;">
                                <i class="fas fa-user"></i>
                            </div>
                        </div>
                        <div>
                            <h4 class="mb-0">${profile.firstName} ${profile.lastName}</h4>
                            <small class="text-muted">Profile photo: <em>${profile.photo}</em></small>
                        </div>
                    </div>

                    <div class="mb-3">
                        <h5>Description</h5>
                        <p class="text-muted">${profile.description}</p>
                    </div>
                    <!-- Datos para la reputación-->
                    <c:if test="${not empty profile.user.reputation}">
                        <p><strong>Reputation:</strong> ${profile.user.reputation.averageScore}</p>
                        <p><small>(${profile.user.reputation.totalVotes} votes)</small></p>
                    </c:if>
                    <c:if test="${empty profile.user.reputation}">
                        <p><strong>Reputation:</strong> Not available</p>
                    </c:if>


                </div>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
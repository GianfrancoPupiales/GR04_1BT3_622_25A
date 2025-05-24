<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>
<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm border-0 rounded-3">
                <div class="card-body p-5">
                    <div class="d-flex align-items-center mb-4">
                        <div class="me-4">
                            <c:choose>
                                <c:when test="${not empty profile.photo and profile.photo ne 'null' and fn:trim(profile.photo) ne ''}">
                                    <img src="${pageContext.request.contextPath}/images/${profile.photo}"
                                         alt="Profile Photo"
                                         class="rounded-circle"
                                         style="width: 100px; height: 100px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/default-user.png"
                                         alt="Default Photo"
                                         class="rounded-circle"
                                         style="width: 100px; height: 100px; object-fit: cover;">
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div>
                            <h4 class="mb-0">${profile.firstName} ${profile.lastName}</h4>
                            <small class="text-muted">DNI: <em>${profile.user.dni}</em></small>
                        </div>
                    </div>

                    <div class="mb-3">
                        <h5>Description</h5>
                        <p class="text-muted">${profile.description}</p>
                    </div>

                    <!-- Reputación -->
                    <c:if test="${not empty profile.user.reputation}">
                        <p><strong>Reputation:</strong> ${profile.user.reputation.averageScore}</p>
                        <p><small>(${profile.user.reputation.totalVotes} votes)</small></p>
                    </c:if>
                    <c:if test="${empty profile.user.reputation}">
                        <p><strong>Reputation:</strong> Not available</p>
                    </c:if>

                    <!-- Botón de regreso -->
                    <div class="text-end mt-4">
                        <a href="<c:choose>
                                    <c:when test='${from eq "home"}'>
                                        ${pageContext.request.contextPath}/ManageProductsController?route=list&view=home
                                    </c:when>
                                    <c:otherwise>
                                        ${pageContext.request.contextPath}/ManageProductsController?route=list&view=user
                                    </c:otherwise>
                                 </c:choose>" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Lista de productos publicados -->
    <div class="row mt-5">
        <div class="col-12">
            <h3>Published Products</h3>
            <div class="row">
                <c:forEach var="product" items="${profile.user.products}">
                    <div class="col-md-6 mb-3">
                        <div class="card border-0 rounded-3" style="height: 100%;">
                            <div class="card-body p-4 d-flex">
                                <div class="flex-grow-1 pe-3">
                                    <h3 class="card-title fw-bold">
                                        <a class="text-dark text-decoration-underline" href="${pageContext.request.contextPath}/MakeOfferController?route=select&view=product&id=${product.idProduct}">
                                                ${product.title}
                                        </a>
                                    </h3>
                                    <c:choose>
                                        <c:when test="${not empty product.user.profile}">
                                            <p class="card-text text-secondary small mb-2">
                                                <i class="fa-solid fa-user me-2"></i>
                                                <a href="${pageContext.request.contextPath}/ProfileController?route=public&id=${product.user.idUser}&from=home" class="text-decoration-none">
                                                        ${product.user.profile.firstName} ${product.user.profile.lastName}
                                                </a>
                                            </p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="card-text text-muted"><i class="fa-solid fa-user me-2"></i> Unknown user</p>
                                        </c:otherwise>
                                    </c:choose>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-align-left me-2"></i>${product.description}
                                    </p>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-layer-group me-2"></i>${product.state}
                                    </p>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-tags me-2"></i>${product.category}
                                    </p>
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-calendar me-2"></i>${product.datePublication}
                                    </p>
                                    <p class="card-text text-secondary small mb-3">
                                        <c:choose>
                                            <c:when test="${product.isAvailable}">
                                                <i class="fa-solid fa-check me-2"></i>Available
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa-solid fa-x me-2"></i> Not available
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <form action="${pageContext.request.contextPath}/FavoriteController?route=add"
                                          method="post">
                                        <input type="hidden" name="idProduct" value="${product.idProduct}">
                                        <button type="submit" class="btn btn-outline-warning">
                                            <i class="fa-solid fa-star"></i> Add to Favorites
                                        </button>
                                    </form>
                                </div>
                                <!-- Imagen al lado derecho, ocupa tamaño completo de la card -->
                                <div style="width: 50%; max-width: 250px;">
                                    <c:if test="${not empty product.photo}">
                                        <img src="${pageContext.request.contextPath}/product-images/${product.photo}"
                                             alt="Product Photo"
                                             class="img-fluid h-100" style="object-fit: cover; border-radius: 0.25rem;">
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty profile.user.products}">
                    <div class="alert alert-warning text-center mt-3">
                        No products published by this user.
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

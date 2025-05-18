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
                        <div class="card border-0 rounded-3 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">
                                    <a href="${pageContext.request.contextPath}/MakeOfferController?route=select&view=product&id=${product.idProduct}"
                                       class="text-decoration-underline text-dark fw-bold">
                                            ${product.title}
                                    </a>
                                </h5>
                                <p class="card-text text-muted">${product.description}</p>
                                <p class="card-text small">
                                    <i class="fa-solid fa-layer-group me-1"></i> ${product.state}
                                </p>
                                <p class="card-text small">
                                    <i class="fa-solid fa-calendar me-1"></i> ${product.datePublication}
                                </p>
                                <p class="card-text small">
                                    <c:choose>
                                        <c:when test="${product.isAvailable}">
                                            <i class="fa-solid fa-check text-success"></i> Available
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fa-solid fa-times text-danger"></i> Not Available
                                        </c:otherwise>
                                    </c:choose>
                                </p>
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

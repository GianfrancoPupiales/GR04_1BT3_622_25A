<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delivery - Ratings</title>
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
        <h1 class="mb-0">Offers Pending Delivery</h1>
    </div>
    <div class="row">
        <c:forEach var="offer" items="${pendingDeliveries}">
            <div class="col-md-4 mb-4">
                <div class="card shadow-sm h-100">
                    <div class="card-header bg-primary text-white d-flex align-items-center">
                        <i class="fas fa-box-open me-2 fs-4"></i>
                        <h5 class="mb-0 flex-grow-1">${offer.productToOffer.title}</h5>
                    </div>
                    <div class="card-body">
                        <img src="${pageContext.request.contextPath}/product-images/${offer.productToOffer.photo}"
                             alt="${offer.productToOffer.title}"
                             class="img-fluid rounded mx-auto d-block my-3" style="max-height: 180px; object-fit: contain;" />
                        <h6 class="text-center text-secondary mb-3">Information</h6>
                        <ul class="list-unstyled mb-4">
                            <li><strong>ID:</strong> ${offer.idOffer}</li>
                            <li><strong>Owner DNI:</strong> ${offer.productToOffer.user.dni}</li>
                            <li><strong>Offerer DNI:</strong> ${offer.offeredByUser.dni}</li>
                            <c:choose>
                                <c:when test="${not empty offer.productToOffer.user.profile}">
                                    <p class="card-text text-secondary small mb-2">
                                        <i class="fa-solid fa-user me-2"></i>
                                        <a href="${pageContext.request.contextPath}/ProfileController?route=public&id=${offer.productToOffer.user.idUser}&from=home" class="text-decoration-none">
                                                ${offer.productToOffer.user.profile.firstName} ${offer.productToOffer.user.profile.lastName}
                                        </a>
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <p class="card-text text-muted">
                                        <i class="fa-solid fa-user me-2"></i> Unknown user
                                    </p>
                                </c:otherwise>
                            </c:choose>

                            <li><strong>Contact this number to complete the delivery:</strong>
                                <c:choose>
                                    <c:when test="${not empty offer.offeredByUser.profile.phoneNumber}">
                                        ${offer.offeredByUser.profile.phoneNumber}
                                    </c:when>
                                    <c:otherwise>
                                        <em>Not available</em>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </ul>

                        <div class="d-flex gap-2">
                            <button class="btn btn-outline-success w-50" data-bs-toggle="modal"
                                    data-bs-target="#RATE_MODAL_${offer.idOffer}">
                                <i class="fas fa-check"></i> Received
                            </button>

                            <form action="${pageContext.request.contextPath}/TradeDeliveryController" method="POST" class="w-50 m-0 p-0">
                                <input type="hidden" name="route" value="rejectOffer">
                                <input type="hidden" name="offerId" value="${offer.idOffer}">
                                <button type="submit" class="btn btn-outline-danger w-100">
                                    <i class="fas fa-times"></i> Not Received
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal para calificar al usuario -->
            <div class="modal fade" id="RATE_MODAL_${offer.idOffer}" tabindex="-1"
                 aria-labelledby="RATE_MODAL_Label_${offer.idOffer}" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <form id="rateForm_${offer.idOffer}"
                              action="${pageContext.request.contextPath}/TradeDeliveryController?route=rateUser"
                              method="POST">
                            <div class="modal-header bg-primary text-white">
                                <h5 class="modal-title" id="RATE_MODAL_Label_${offer.idOffer}">
                                    <i class="fas fa-star me-2"></i> Rate User
                                </h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <input type="hidden" name="toUserId" value="${offer.offeredByUser.idUser}"/>
                                <!-- El dueño del producto -->
                                <input type="hidden" name="offerId" value="${offer.idOffer}"/> <!-- ID de la oferta -->

                                <div class="mb-3">
                                    <label for="score_${offer.idOffer}" class="form-label fw-semibold">
                                        Score (1-5)
                                    </label>
                                    <input type="number" id="score_${offer.idOffer}" name="score"
                                           class="form-control" min="1" max="5" required/>
                                </div>
                            </div>
                            <div class="modal-footer justify-content-center">
                                <button type="submit" class="btn btn-primary">
                                    Submit Rating
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Mensaje si no hay entregas pendientes -->
    <c:if test="${empty pendingDeliveries}">
        <div class="alert alert-warning text-center">No pending deliveries found.</div>
    </c:if>

    <!-- Mensajes de éxito o error -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success text-center mt-3">${successMessage}</div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger text-center mt-3">${errorMessage}</div>
    </c:if>
</main>


<!-- BOOTSTRAP JS para modales -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>

<script>
    // Función para manejar el envío del formulario mediante AJAX
    function submitRating(deliveryId) {
        const form = document.getElementById('rateForm_' + deliveryId);
        event.preventDefault();

        const formData = new FormData(form);

        fetch(form.action, {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const successMessageDiv = document.getElementById('successMessage');
                    successMessageDiv.innerText = data.message;
                    successMessageDiv.classList.remove('d-none');

                    const modal = new bootstrap.Modal(document.getElementById('RATE_MODAL_' + deliveryId));
                    modal.hide();
                }
            })
            .catch(error => console.error('Error al enviar la calificación:', error));
    }
</script>

</html>

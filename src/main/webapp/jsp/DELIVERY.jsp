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
            </ul>
        </div>

        <!-- Fin: Sección derecha -->
        <div class="d-flex">
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

<main class="container mt-5">
    <h2 class="mb-4">Offers Pending Delivery</h2>

    <div class="row">
        <c:forEach var="offer" items="${pendingDeliveries}">
            <div class="col-md-4 mb-4">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <!-- Nombre del producto que recibió la oferta -->
                        <h4 class="card-title"><i class="fas fa-box-open me-2"></i> ${offer.productToOffer.title}</h4>
                        <h5 style="text-align:center"> Information </h5>
                        <p class="card-text">
                            <!-- Información de IDs y DNIs -->
                            <strong>ID :</strong> ${offer.idOffer} <br>
                            <strong>Owner :</strong> ${offer.productToOffer.user.dni} <br>
                            <strong>Offerer:</strong> ${offer.offeredByUser.dni} <br>
                        </p>

                        <!-- Botones de acción -->
                        <div class="d-flex justify-content-between">
                            <!-- Confirmar entrega -->
                            <button class="btn btn-primary btn-sm px-3" data-bs-toggle="modal"
                                    data-bs-target="#RATE_MODAL_${offer.idOffer}">
                                <i class="fas fa-check"></i> Confirm
                            </button>

                            <!-- Rechazar oferta -->
                            <form action="${pageContext.request.contextPath}/TradeDeliveryController" method="POST" class="d-inline">
                                <input type="hidden" name="route" value="rejectOffer">
                                <input type="hidden" name="offerId" value="${offer.idOffer}">
                                <button type="submit" class="btn btn-danger btn-sm px-3">
                                    <i class="fas fa-times"></i> Reject
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
        <div class="alert alert-warning text-center mt-4">No pending deliveries found.</div>
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

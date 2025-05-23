<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MAKE AN OFFER</title>
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
<!-- Main Content -->
<main class="container my-4">
    <!-- Feedback Message -->
    <c:if test="${not empty sessionScope.message}">
        <div id="notification" class="alert alert-danger"
             style="background-color: #f8d7da; color: #721c24;" role="alert">
                ${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <div class="row">
        <!-- PRODUCT TO OFFER Section -->
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1 class="mb-0"> Make An Offer </h1>
            </div>
            <div class="row">
                <c:forEach var="product" items="${products}">
                    <div class="col-md-6 mb-3">
                        <div class="card border-0 rounded-3" style="height: 100%;">
                            <div class="card-body p-4 d-flex">
                                <!-- Texto ocupa el espacio restante -->
                                <div class="flex-grow-1 pe-3">
                                    <h2 class="card-title text-dark fw-bold">${product.title}</h2>
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
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox"
                                               name="selectedProducts" value="${product.idProduct}" id="productCheckbox${product.idProduct}">
                                        <label class="form-check-label text-primary fw-bold" for="productCheckbox${product.idProduct}">
                                            Offer this one ...
                                        </label>
                                    </div>
                                </div>
                                <!-- Imagen al lado derecho, ocupa tamaño completo de la card -->
                                <div style="width: 50%; max-width: 250px;">
                                    <c:if test="${not empty product.photo}">
                                        <img src="${pageContext.request.contextPath}/images/products/${product.photo}"
                                             alt="Product Photo"
                                             class="img-fluid h-100" style="object-fit: cover; border-radius: 0.25rem;">
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#confirmModal">
                    Confirm Offer
                </button>

            </div>
            <c:if test="${empty products}">
                <div class="alert alert-warning text-center">You have not created any product.</div>
            </c:if>
        </div>
    </div>
</main>

<!-- Modal para mensajes informativos y de error -->
<div class="modal modal-info" id="infoModal" tabindex="-1"
     aria-labelledby="infoModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body ${messageType == 'info' ? 'info' : 'error'}">
                <i
                        class="fas ${messageType == 'info' ? 'fa-info-circle text-info' : 'fa-exclamation-circle text-danger'}"></i>
                <span>${message}</span>
            </div>
        </div>
    </div>
</div>

<!-- Modal de confirmación -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-3">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="confirmModalLabel">Are you sure?</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                Do you want to complete this offer with selected products?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-dismiss="modal">No</button>
                <button type="button" id="confirmOfferBtn" class="btn btn-success">Yes</button>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Mostrar modal informativo si hay mensaje
    const message = "${message}";
    if (message !== "") {
        const infoModalElement = document.getElementById("infoModal");
        if (infoModalElement) {
            const infoModal = new bootstrap.Modal(infoModalElement, {
                backdrop: false, // Sin fondo oscuro
                keyboard: false  // Desactiva cerrar con teclado
            });
            infoModal.show();

            // Cerrar automáticamente después de 1 segundo
            setTimeout(() => {
                infoModal.hide();
            }, 1000);
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        const notification = document.getElementById("notification");
        if (notification) {
            // Oculta el mensaje después de 2 segundos
            setTimeout(() => {
                notification.style.transition = "opacity 0.5s";
                notification.style.opacity = "0";
                setTimeout(() => notification.remove(), 1000);
            }, 2000);
        }
    });
</script>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        document.getElementById("confirmOfferBtn").addEventListener("click", function () {
            const selected = document.querySelectorAll('input[name="selectedProducts"]:checked');
            if (selected.length === 0) {
                alert("No has seleccionado ningún producto.");
                return;
            }
            console.log("Select: " + selected.length)
            const ids = (Array.from(selected).map(cb => cb.value));
            console.log("captured id: " + ids)
            const selectedIds = ids.join(",");
            console.log(`MakeOfferController?route=confirm&listOfferedProducts=` + selectedIds);
            //console.log("final selected ids: " + selectedIds)
            // Redirige solo si hay productos
            window.location.href = `MakeOfferController?route=confirm&listOfferedProducts=` + selectedIds;
        });
    });
</script>


</body>
</html>

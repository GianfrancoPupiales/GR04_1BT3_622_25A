<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm border-0 rounded-3">
                <div class="card-body p-5">
                    <div class="d-flex align-items-center mb-4">
                        <div class="me-4">
                            <c:choose>
                                <c:when test="${not empty profile.photo and profile.photo ne 'null' and fn:trim(profile.photo) ne ''}">
                                    <img src="${pageContext.request.contextPath}/profile-images/${profile.photo}"
                                         alt="Foto de perfil"
                                         class="rounded-circle"
                                         style="width: 100px; height: 100px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/default-user.png"
                                         alt="Imagen por defecto"
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
                    <!-- Datos para la reputación-->
                    <c:if test="${not empty profile.user.reputation}">
                        <p><strong>Reputation:</strong> ${profile.user.reputation.averageScore}</p>
                        <p><small>(${profile.user.reputation.totalVotes} votes)</small></p>
                    </c:if>
                    <c:if test="${empty profile.user.reputation}">
                        <p><strong>Reputation:</strong> Not available</p>
                    </c:if>

                    <div class="text-end">
                        <a href="ProfileController?route=editForm" class="btn btn-primary">
                            Editar Perfil
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <c:if test="${not empty message}">
        <div id="notification"
             class="alert alert-info text-center position-fixed top-0 start-50 translate-middle-x mt-3"
             style="z-index: 1055; width: 300px;">
                ${message}
        </div>
    </c:if>

</main>

<!-- Modal de edición de perfil -->
<div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="editProfileModalLabel">
                    <i class="fas fa-edit"></i> Editar Perfil
                </h5>
                <a href="ProfileController?route=show" class="btn-close" aria-label="Close"></a>
            </div>

            <form action="ProfileController?route=edit" method="POST" enctype="multipart/form-data">
                <div class="modal-body">
                    <input type="hidden" name="id" value="${profile.idProfile}"/>
                    <input type="hidden" name="existingPhoto" value="${profile.photo}"/>
                    <div class="mb-3">
                        <label for="firstName" class="form-label fw-bold">Nombre</label>
                        <input type="text" class="form-control" id="firstName" name="firstName"
                               value="${profile.firstName}" required>
                    </div>
                    <div class="mb-3">
                        <label for="lastName" class="form-label fw-bold">Apellido</label>
                        <input type="text" class="form-control" id="lastName" name="lastName"
                               value="${profile.lastName}" required>
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label fw-bold">Descripción</label>
                        <textarea class="form-control" id="description" name="description"
                                  rows="3">${profile.description}</textarea>
                    </div>
                    <div class="mb-3">
                        <label for="photoFile" class="form-label fw-bold">Foto de perfil</label>
                        <input type="file" class="form-control" id="photoFile" name="photoFile" accept="image/*">
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="ProfileController?route=show" class="btn btn-danger">Cancelar</a>
                    <button type="submit" class="btn btn-primary">Guardar cambios</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal Informativo -->
<div class="modal fade" id="infoModal" tabindex="-1" aria-labelledby="infoModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-body text-center py-4">
                <!-- Icono opcional -->
                <i class="fas fa-info-circle fa-2x text-primary mb-2"></i>

                <!-- Mensaje -->
                <p class="mb-0 fw-bold">${message}</p>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.onload = function () {
        var route = "${param.route}";
        if (route === "editForm") {
            const modal = new bootstrap.Modal(document.getElementById('editProfileModal'), {
                keyboard: false,
                backdrop: 'static'
            });
            modal.show();
        }
    };

    document.addEventListener("DOMContentLoaded", function () {
        const notification = document.getElementById("notification");
        if (notification) {
            setTimeout(() => {
                notification.style.transition = "opacity 0.5s";
                notification.style.opacity = "0";
                setTimeout(() => notification.remove(), 1000);
            }, 2000);
        }
    });
</script>

</body>
</html>
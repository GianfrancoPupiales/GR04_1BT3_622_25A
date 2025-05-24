<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Products</title>
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
        <!-- PRODUCT Section -->
        <div class="col-12">

            <div class="d-flex justify-content-between align-items-center mb-3">

                <h1 class="mb-0">
                    My Products
                </h1>

                <a href="ManageProductsController?route=add"
                   class="btn btn-primary"> <i class="fas fa-plus-circle"></i>
                    Publish product
                </a>

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
                                    <div class="d-flex justify-content-start">
                                        <a href="ManageProductsController?route=edit&idProduct=${product.idProduct}"
                                           class="btn btn-primary btn-sm me-2 px-3">
                                            <i class="fas fa-pen"></i> Edit
                                        </a>
                                        <a href="ManageProductsController?route=delete&idProduct=${product.idProduct}"
                                           class="btn btn-danger btn-sm px-3">
                                            <i class="fas fa-trash-alt"></i> Delete
                                        </a>
                                    </div>
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
            </div>
            <c:if test="${empty products}">
                <div class="alert alert-warning text-center">You have not published any products yet</div>
            </c:if>
        </div>
    </div>
</main>

<!-- NEW PRODUCT FORM -->
<div class="modal fade" id="NEW_PRODUCT" tabindex="-1"
     aria-labelledby="NEW_PRODUCTLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="NEW_PRODUCTLabel">
                    <i class="fa-solid fa-box-open me-2"></i> Add Product
                </h5>
                <a href="ManageProductsController?route=list" class="btn-close"
                   aria-label="Close"></a>
            </div>
            <form action="ManageProductsController?route=saveNew"
                  method="POST" enctype="multipart/form-data">
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="txtTitleAdd" class="form-label fw-semibold">Title</label>
                        <input type="text" class="form-control" id="txtTitleAdd" name="txtTitle" required>
                    </div>
                    <div class="mb-3">
                        <label for="txtDescriptionAdd" class="form-label fw-semibold">Description</label>
                        <textarea class="form-control" id="txtDescriptionAdd" name="txtDescription" rows="4"
                                  required></textarea>
                        <span id="charCountAdd" class="form-text text-muted">0 / 200</span>
                    </div>
                    <div class="mb-3">
                        <label for="txtState" class="form-label fw-semibold">State</label>
                        <select id="txtState" name="txtState" class="form-select" required>
                            <option value="">Select a state</option>
                            <option value="New" <c:if test="${product.state == 'New'}">selected</c:if>>New</option>
                            <option value="Semi_new" <c:if test="${product.state == 'Semi_new'}">selected</c:if>>
                                Semi-new
                            </option>
                            <option value="Repaired" <c:if test="${product.state == 'Repaired'}">selected</c:if>>
                                Repaired
                            </option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="category" class="form-label fw-semibold">Category</label>
                        <select id="category" name="category" class="form-select" required>
                            <option value="">Select a category</option>
                            <option value="Books">Books</option>
                            <option value="Stationery">Stationery</option>
                            <option value="Electronics">Electronics</option>
                            <option value="Furniture">Furniture</option>
                            <option value="Clothing">Clothing</option>
                            <option value="Services">Services</option>
                            <option value="Entertainment">Entertainment</option>
                            <option value="Food">Food</option>
                            <option value="Transport">Transport</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="photo" class="form-label fw-semibold">Product Photo</label>
                        <input type="file" id="photo" name="photo" class="form-control" accept="image/*">
                    </div>
                </div>
                <div class="modal-footer justify-content-center">
                    <a href="ManageProductsController?route=list"
                       class="btn btn-danger"> Cancel </a>
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal for Edit PRODUCT -->
<div class="modal fade" id="EDIT_PRODUCT" tabindex="-1"
     aria-labelledby="EDIT_PRODUCTLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="EDIT_PRODUCTLabel">
                    <i class="fas fa-edit"></i> Edit Product
                </h5>
                <a href="ManageProductsController?route=list" class="btn-close"
                   aria-label="Close"></a>
            </div>
            <form action="ManageProductsController?route=saveExisting"
                  method="POST" enctype="multipart/form-data">
                <div class="modal-body">
                    <input type="hidden" name="txtIdProduct" value="${product.idProduct}">
                    <div class="mb-3">
                        <label for="TitleEdit" class="form-label fw-bold">Title</label>
                        <input id="TitleEdit" type="text" class="form-control"
                               name="txtTitle" value="${product.title}" placeholder="Enter title"
                               required>
                    </div>
                    <div class="mb-3">
                        <label for="DescriptionEdit" class="form-label fw-bold">Description
                        </label>
                        <textarea id="DescriptionEdit" class="form-control" name="txtDescription"
                                  rows="4" placeholder="Enter description" maxlength="200"
                                  required>${product.description}</textarea>
                        <span id="charCountEdit" class="form-text text-muted">0 / 200</span>
                    </div>
                    <div class="mb-3">
                        <label for="txtState" class="form-label fw-semibold">State</label>
                        <select id="txtState" name="txtState" class="form-select" required>
                            <option value="">Select a state</option>
                            <option value="New" <c:if test="${product.state == 'New'}">selected</c:if>>New</option>
                            <option value="Semi_new" <c:if test="${product.state == 'Semi_new'}">selected</c:if>>
                                Semi-new
                            </option>
                            <option value="Repaired" <c:if test="${product.state == 'Repaired'}">selected</c:if>>
                                Repaired
                            </option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="category" class="form-label fw-semibold">Category</label>
                        <select id="category" name="category" class="form-select" required>
                            <option value="">Select a category</option>
                            <option value="Books" <c:if test="${product.category == 'Books'}">selected</c:if>>Books
                            </option>
                            <option value="Stationery" <c:if test="${product.category == 'Stationery'}">selected</c:if>>
                                Stationery
                            </option>
                            <option value="Electronics"
                                    <c:if test="${product.category == 'Electronics'}">selected</c:if>>Electronics
                            </option>
                            <option value="Furniture" <c:if test="${product.category == 'Furniture'}">selected</c:if>>
                                Furniture
                            </option>
                            <option value="Clothing" <c:if test="${product.category == 'Clothing'}">selected</c:if>>
                                Clothing
                            </option>
                            <option value="Services" <c:if test="${product.category == 'Services'}">selected</c:if>>
                                Services
                            </option>
                            <option value="Entertainment"
                                    <c:if test="${product.category == 'Entertainment'}">selected</c:if>>Entertainment
                            </option>
                            <option value="Food" <c:if test="${product.category == 'Food'}">selected</c:if>>Food
                            </option>
                            <option value="Transport" <c:if test="${product.category == 'Transport'}">selected</c:if>>
                                Transport
                            </option>
                            <option value="Other" <c:if test="${product.category == 'Other'}">selected</c:if>>Other
                            </option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="photo" class="form-label fw-semibold">Product Photo</label>
                        <input type="file" id="photo" name="photo" class="form-control" accept="image/*">
                        <input type="hidden" name="existingPhoto" value="${product.photo}">
                    </div>
                </div>
                <div class="modal-footer justify-content-center">
                    <a href="ManageProductsController?route=list"
                       class="btn btn-danger"> Cancel </a>
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete PRODUCT Modal -->
<div class="modal fade" id="DELETE_PRODUCT" tabindex="-1"
     aria-labelledby="DELETE_PRODUCTLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header"
                 style="background-color: #dc3545; color: #fff;">
                <h5 class="modal-title" id="DELETE_PRODUCTLabel">
                    <i class="fa-solid fa-trash me-2"></i> Delete Product
                </h5>
                <a href="ManageProductsController?route=list" class="btn-close"
                   aria-label="Close"></a>
            </div>
            <div class="modal-body text-center">
                <p>Are you sure you want to delete this product?</p>
                <h3 class="card-title text-dark fw-bold">${product.title}</h3>
                <p class="card-text text-secondary small mb-4">
                    <i class="fa-solid fa-align-left me-2"></i>${product.description}
                </p>
                <p class="card-text text-secondary small mb-4">
                    <i class="fa-solid fa-layer-group me-2"></i>${product.state}
                </p>
                <p class="card-text text-secondary small mb-4">
                    <i class="fa-solid fa-calendar me-2"></i>${product.datePublication}
                </p>
                <p class="card-text text-secondary small mb-4">
                    <c:choose>
                        <c:when test="${product.isAvailable}">
                            <i class="fa-solid fa-check me-2"></i>Available
                        </c:when>
                        <c:otherwise>
                            <i class="fa-solid fa-x me-2"></i> Not available
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div class="modal-footer justify-content-center">
                <!-- Cancel button, closes the modal -->
                <a href="ManageProductsController?route=list"
                   class="btn btn-danger">Cancel</a>

                <!-- Form to confirm the deletion -->
                <form
                        action="ManageProductsController?route=accept&idProduct=${product.idProduct}"
                        method="POST">
                    <button type="submit" class="btn btn-success">Accept</button>
                </form>
            </div>
        </div>
    </div>
</div>

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
<!-- Bootstrap JS -->
<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.onload = function () {
        var route = "${param.route}";

        if (route === "add") {
            var myModal = new bootstrap.Modal(document
                .getElementById('NEW_PRODUCT'), {
                keyboard: false,
                backdrop: 'static'
            });
            document.body.classList.remove('modal-open');
            myModal.show();
        } else if (route === "edit" && "${param.idProduct}") {
            var myModal = new bootstrap.Modal(document
                .getElementById('EDIT_PRODUCT'), {
                keyboard: false,
                backdrop: 'static'
            });
            document.body.classList.remove('modal-open');
            myModal.show();
        } else if (route === "delete" && "${param.idProduct}") {
            var myModal = new bootstrap.Modal(document
                .getElementById('DELETE_PRODUCT'), {
                keyboard: false,
                backdrop: 'static'
            });
            document.body.classList.remove('modal-open');
            myModal.show();
        }
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
    };

    function updateCharCount(idInput, idCounter) {
        const input = document.getElementById(idInput);
        const counter = document.getElementById(idCounter);
        const max = 200;

        input.addEventListener("input", () => {
            const length = input.value.length;
            counter.textContent = length + " / " + max;

            if (length > max) {
                counter.classList.add("text-danger");
                input.classList.add("is-invalid"); // Bordes rojos opcionales
            } else {
                counter.classList.remove("text-danger");
                input.classList.remove("is-invalid");
            }
        });

        input.dispatchEvent(new Event("input"));
    }


    // Validar longitud al enviar formulario (ADD)
    document.querySelector('form[action*="saveNew"]').addEventListener("submit", function (e) {
        const description = document.getElementById("txtDescriptionAdd").value;
        if (description.length > 200) {
            e.preventDefault(); // Evita el envío
            displayAlert("The description exceeds the 200 character limit.");
        }
    });

    // Validar longitud al enviar formulario (EDIT)
    document.querySelector('form[action*="saveExisting"]').addEventListener("submit", function (e) {
        const description = document.getElementById("DescriptionEdit").value;
        if (description.length > 200) {
            e.preventDefault(); // Evita el envío
            displayAlert("La descripción excede el límite de 200 caracteres.");
        }
    });

    // Función para mostrar alerta como el mensaje de productos vacíos
    function displayAlert(mensaje) {
        // Determinar cuál campo de descripción está visible
        const descriptionAdd = document.getElementById("txtDescriptionAdd");
        const descriptionEdit = document.getElementById("DescriptionEdit");

        let targetField = null;

        if (descriptionAdd && descriptionAdd.offsetParent !== null) {
            targetField = descriptionAdd;
        } else if (descriptionEdit && descriptionEdit.offsetParent !== null) {
            targetField = descriptionEdit;
        }

        if (!targetField) return;

        // Verificar si ya existe un mensaje previo
        let alertDiv = document.getElementById("charLimitAlert");
        if (!alertDiv) {
            alertDiv = document.createElement("div");
            alertDiv.id = "charLimitAlert";
            alertDiv.className = "alert alert-danger mt-2";
            const descriptionGroup = targetField.closest(".mb-3");
            descriptionGroup.appendChild(alertDiv);
        }

        alertDiv.textContent = mensaje;

        // Ocultar después de 3 segundos
        setTimeout(() => {
            alertDiv.remove();
        }, 3000);
    }


    document.addEventListener("DOMContentLoaded", function () {
        const notification = document.getElementById("notification");
        if (notification) {
            // Oculta el mensaje después de 2 segundos
            setTimeout(() => {
                notification.style.transition = "opacity 0.5s";
                notification.style.opacity = "0";
                setTimeout(() => notification.remove(), 1000); // Remueve el elemento después de la transición
            }, 2000);
        }
        // Contadores de descripción
        updateCharCount("txtDescriptionAdd", "charCountAdd");
        updateCharCount("DescriptionEdit", "charCountEdit");
    });
</script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>TRUKEA</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light d-flex align-items-center justify-content-center" style="height: 100vh;">

<div class="card shadow p-4" style="width: 100%; max-width: 400px;">
    <h2 class="text-center mb-4">TRUKEA</h2>

    <!-- Mensajes de feedback -->
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType == 'error' ? 'danger' : 'info'} text-center" role="alert">
                ${message}
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/LoginController?route=login">
        <input type="hidden" name="route" value="login"/>

        <div class="mb-3">
            <label for="txtDni" class="form-label">DNI</label>
            <input type="text" class="form-control" id="txtDni" name="txtDni" required>
        </div>

        <div class="mb-3">
            <label for="txtPassword" class="form-label">Password</label>
            <input type="password" class="form-control" id="txtPassword" name="txtPassword" required>
        </div>

        <div class="d-grid">
            <button type="submit" class="btn btn-primary">Login</button>
        </div>
    </form>

    <!-- Enlace para redirigir al formulario de registro -->
    <div class="mt-3 text-center">
        <p>Don't have an account? <a href="${pageContext.request.contextPath}/RegisterController?route=enter" class="btn btn-link">Register here</a></p>
    </div>
</div>
</body>
</html>

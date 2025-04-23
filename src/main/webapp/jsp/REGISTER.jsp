<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Registration</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="text-center mb-4">Register User</h2>
            <form action="${pageContext.request.contextPath}/RegisterController?route=save" method="post">
                <div class="mb-3">
                    <label for="txtDni" class="form-label">DNI</label>
                    <input type="text" class="form-control" id="txtDni" name="txtDni" required>
                </div>
                <div class="mb-3">
                    <label for="txtPassword" class="form-label">Password</label>
                    <input type="password" class="form-control" id="txtPassword" name="txtPassword" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Register</button>
            </form>
            <div class="text-center">
                <a href="${pageContext.request.contextPath}/LoginController?route=enter" class="text-decoration-none">Already have an account? Login here</a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>

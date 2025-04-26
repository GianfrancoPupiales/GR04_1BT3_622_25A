<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Respond to Offer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<main class="container my-4">
    <h1>Respond to Offer</h1>
    <c:if test="${not empty offer}">
        <div class="offer-card">
            <h2>Pending Offers</h2>
            <p><strong>Offered Products:</strong> ${offer.offeredProducts}</p>
            <p><strong>Requested Product:</strong> ${offer.productToOffer}</p>
            <form action="RespondOfferController" method="post">
                <input type="hidden" name="offerId" value="${offer.idOffer}">
                <button type="submit" name="action" value="accept" class="btn btn-success">Accept</button>
                <button type="submit" name="action" value="reject" class="btn btn-danger">Reject</button>
            </form>
        </div>
    </c:if>
</main>
</body>
</html>
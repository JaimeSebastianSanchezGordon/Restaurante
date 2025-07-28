<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/80cfa4399f.js" crossorigin="anonymous"></script>
    <title>Login</title>
    <link rel="stylesheet" href="../css/login.css">
</head>

<body>
    <section class="login">
        <div class="imagenLogin">
            <img src="../imagenes/registro.png" alt="Login Image">
        </div>
        <div class="formulario">
            <h1>Welcome!</h1>
            <form action="<%= request.getContextPath() %>/ingreso" method="post">
                <div class="input-group">
                    <i class="fa-solid fa-at color_naranja"></i>
                    <input type="email" name="email" placeholder="Email Address - Empleado" required>
                </div>
                
                <div class="input-group">
                    <i class="fa-solid fa-lock color_naranja"></i>
                    <input type="password" name="password" placeholder="Password - Empleado" required>
                </div>
                
                <button type="submit" class="btnLogin">Ingreso - Empleado</button>
            </form>
            <a href="registro.jsp" class="btnLogin registro-link">Crear cuenta de empleado</a>
            <hr>
            <a href="${pageContext.request.contextPath}/ListarPlatos" class="btnLogin registro-link">Ingresar como cliente</a>

        </div>
    </section>
</body>

</html>
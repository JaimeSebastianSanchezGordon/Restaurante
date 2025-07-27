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
                    <i class="fa-solid fa-user color_naranja"></i>
                    <select name="usuario" required>
                        <option value="" disabled selected>Seleccionar tipo de usuario</option>
                        <option value="cliente">Cliente</option>
                        <option value="empleado">Empleado</option>
                    </select>
                </div>
                
                <div class="input-group">
                    <i class="fa-solid fa-at color_naranja"></i>
                    <input type="email" name="email" placeholder="Email Address" required>
                </div>
                
                <div class="input-group">
                    <i class="fa-solid fa-lock color_naranja"></i>
                    <input type="password" name="password" placeholder="Password" required>
                </div>
                
                <button type="submit" class="btnLogin">Ingreso</button>
            </form>
            <hr>
            <a href="registro.jsp" class="registro-link">Crear cuenta de cliente</a>
        </div>
    </section>
</body>

</html>
<?php
session_start();
require 'database.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = $_POST['email'];
    $password = $_POST['password'];

    // Hash password (MD5 for now, same as stored)
    $hashed = md5($password);

    $stmt = $conn->prepare("SELECT * FROM users WHERE email=? AND password=? AND role='admin'");
    $stmt->bind_param("ss", $email, $hashed);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows == 1) {
        $_SESSION['admin_logged_in'] = true;
        $_SESSION['admin_email'] = $email;
        header("Location: admin-dashboard.php");
        exit;
    } else {
        $error = "Incorrect email or password!";
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login | Speakers Hive</title>
    <link rel="stylesheet" href="style.css">
</head>
<body class="honey-bg">

<div class="auth-container">

    <img src="mic.png" class="logo-mic">

    <h2 class="auth-title">Admin Login</h2>
    <h3 class="sub-title">Manage Members</h3>

    <form method="POST">
        <input type="text" name="email" placeholder="Email ID" class="auth-input" required>
        <input type="password" name="password" placeholder="Password" class="auth-input" required>
        <button type="submit" class="auth-btn">Login</button>
    </form>

    <?php if(isset($error)) echo "<h4 class='sub-title' style='color:#ff6666;'>$error</h4>"; ?>

</div>

</body>
</html>

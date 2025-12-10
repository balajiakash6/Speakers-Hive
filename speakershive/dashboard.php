<?php
session_start();
if(!isset($_SESSION['user_logged_in'])){
    header("Location: login.html");
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Dashboard | Speakers Hive</title>
    <link rel="stylesheet" href="style.css">
</head>
<body class="honey-bg">

<div class="auth-container">

    <img src="mic.png" class="logo-mic">

    <h2 class="auth-title">Welcome, Member!</h2>
    <h3 class="sub-title">Your dashboard</h3>

    <p class="sub-title">Here you can see your account info and upcoming events.</p>

    <a href="logout.php"><button class="auth-btn" style="margin-top:20px;">Logout</button></a>

</div>

</body>
</html>

<?php
session_start();
require 'database.php';

// Protect page: only admin
if(!isset($_SESSION['admin_logged_in'])){
    header("Location: login.html");
    exit;
}

// Enable error reporting
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$success = "";

if(isset($_POST['create_member'])){
    $name = $_POST['name'];
    $email = $_POST['email'];

    // Temporary password
    $temp_pass = bin2hex(random_bytes(4)); // 8-char temp password
    $hashed_pass = hash('sha256', $temp_pass); // hash before storing

    // Generate reset token
    $token = bin2hex(random_bytes(16));
    $expiry = date('Y-m-d H:i:s', strtotime('+1 day'));

    // Insert member into DB
    $stmt = $conn->prepare("INSERT INTO users (name,email,password,role,token,token_expiry) VALUES (?,?,?,?,?,?)");
    $role = 'member';
    $stmt->bind_param("ssssss", $name, $email, $hashed_pass, $role, $token, $expiry);
    $stmt->execute();

    // Generate reset link
    $reset_link = "http://localhost:8080/speakershive/update-password.html?token=$token";

    // SHOW EMAIL ON SCREEN (instead of sending)
    $success = "Member created!<br><br>";
    $success .= "<b>Email Preview:</b><br>";
    $success .= "To: $email<br>";
    $success .= "Hello $name,<br>";
    $success .= "Temporary Password: <b>$temp_pass</b><br>";
    $success .= "<a href='$reset_link'>Reset Password Link</a><br>";
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | Speakers Hive</title>
    <link rel="stylesheet" href="style.css">
</head>
<body class="honey-bg">

<div class="auth-container">

    <img src="mic.png" class="logo-mic">
    <h2 class="auth-title">Admin Dashboard</h2>

    <form method="POST">
        <input type="text" name="name" placeholder="Member Name" class="auth-input" required>
        <input type="email" name="email" placeholder="Member Email" class="auth-input" required>
        <button type="submit" name="create_member" class="auth-btn">Create Member</button>
    </form>

    <?php if($success != "") echo "<div style='margin-top:20px; color:#FFD600; font-weight:bold;'>$success</div>"; ?>

</div>

</body>
</html>

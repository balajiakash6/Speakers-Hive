<?php
require 'database.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = $_POST['email'];

    // Check if user exists
    $stmt = $conn->prepare("SELECT * FROM users WHERE email=?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if($result->num_rows === 0){
        echo "<script>alert('Email not registered'); window.location='forgot-password.html';</script>";
        exit;
    }

    $token = bin2hex(random_bytes(16));
    $expiry = date('Y-m-d H:i:s', strtotime('+1 day'));

    // Save token and expiry
    $stmt = $conn->prepare("UPDATE users SET reset_token=?, token_expiry=? WHERE email=?");
    $stmt->bind_param("sss", $token, $expiry, $email);
    $stmt->execute();

    // Send reset link
    $resetLink = "http://localhost:8080/speakershive/update-password.html?email=$email&token=$token";
    $subject = "Password Reset – Speakers Hive";
    $message = "Click the link to reset your password: $resetLink";
    $headers = "From: littlemitra@gmail.com";

    mail($email, $subject, $message, $headers);

    echo "<script>alert('Reset link sent to your email'); window.location='login.html';</script>";
}
?>

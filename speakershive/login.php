<?php
require 'database.php';
session_start();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = $_POST["email"];
    $password = $_POST["password"];
    $hashed = md5($password); // match DB

    $stmt = $conn->prepare("SELECT * FROM users WHERE email=? AND password=?");
    $stmt->bind_param("ss", $email, $hashed);
    $stmt->execute();
    $result = $stmt->get_result();

    if($result->num_rows == 1){
        $row = $result->fetch_assoc();

        $_SESSION["user_logged_in"] = true;
        $_SESSION["user_id"] = $row["id"];
        $_SESSION["role"] = $row["role"];

        if($row["role"] == "admin"){
            $_SESSION['admin_logged_in'] = true;
            header("Location: admin-dashboard.php");
        } else {
            header("Location: dashboard.php");
        }
        exit;
    } else {
        echo "<script>alert('Invalid email or password'); window.location.href='login.html';</script>";
    }
}
?>

<?php
require 'server.php';  // DB connection file

$email = $_POST['email'];
$password = $_POST['password'];

$sql = "SELECT * FROM users WHERE email = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {

    $user = $result->fetch_assoc();

    // password_verify checks hashed password
    if (password_verify($password, $user['password_hash'])) {
        echo "Login Successful!";
        
    } else {
        echo "Wrong Password";
    }

} else {
    echo "No user found with this email";
}
?>

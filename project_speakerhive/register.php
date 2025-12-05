<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

require 'server.php';  // DB connection file

echo "Reached register.php<br>";  // DEBUG: see if script runs

// AUTO PASSWORD FUNCTION
function generatePassword($name, $email, $mobile) {

    $part1 = substr(strtolower($name), 0, 3);  // first 3 letters of name
    $part2 = substr($mobile, -4);             // last 4 digits of phone

    $chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ@#$%";
    $part3 = $chars[rand(0, strlen($chars)-1)] . $chars[rand(0, strlen($chars)-1)];

    return ucfirst($part1) . $part2 . $part3;
}

// get data from form
$name  = $_POST['name'] ?? '';
$email = $_POST['email'] ?? '';
$phone = $_POST['phone'] ?? '';

echo "POST data: name=$name, email=$email, phone=$phone<br>";  // DEBUG

// generate password BEFORE hashing
$autoPassword = generatePassword($name, $email, $phone);

// hash it
$hash = password_hash($autoPassword, PASSWORD_BCRYPT);

// insert into database
$sql = "INSERT INTO users (name, email, phone, password_hash) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);

if (!$stmt) {
    die("Prepare failed: " . $conn->error);
}

$stmt->bind_param("ssss", $name, $email, $phone, $hash);

if ($stmt->execute()) {
    echo "Registered successfully<br>";
    echo "Temporary password: " . $autoPassword;
} else {
    echo "Error: " . $stmt->error;
}
?>

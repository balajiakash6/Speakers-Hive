<?php
$host = "localhost";
$user = "root";          // default XAMPP username
$pass = "";              // default XAMPP password is empty
$db   = "speakershive";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Failed to connect to the database: " . $conn->connect_error);
}
// echo "Connected OK";  
?>


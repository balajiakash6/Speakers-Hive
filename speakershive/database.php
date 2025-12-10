<?php
$host = "localhost";
$user = "root";
$pass = "Mi@10312006";
$db = "speakershive";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Connection Failed: " . $conn->connect_error);
}
?>

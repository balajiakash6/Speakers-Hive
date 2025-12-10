<?php
session_start();
require 'database.php';

if(isset($_POST['login'])){
    $email=$_POST['email'];
    $password=$_POST['password'];

    $stmt=$conn->prepare("SELECT * FROM users WHERE email=? AND role='admin'");
    $stmt->bind_param("s",$email);
    $stmt->execute();
    $result=$stmt->get_result();

    if($result->num_rows==1){
        $user=$result->fetch_assoc();
        if(hash_equals($user['password'], hash('sha256',$password))){
            $_SESSION['admin_logged_in']=true;
            $_SESSION['admin_email']=$user['email'];
            header("Location: admin-dashboard.php");
            exit;
        }else $error="Incorrect password!";
    }else $error="Admin not found!";
}
?>
<!DOCTYPE html>
<html>
<head>
<title>Admin Login</title>
</head>
<body>
<h2>Admin Login</h2>
<form method="POST">
<input type="email" name="email" placeholder="Admin Email" required>
<input type="password" name="password" placeholder="Password" required>
<button type="submit" name="login">Login</button>
<?php if(isset($error)) echo "<p style='color:red;'>$error</p>"; ?>
</form>
</body>
</html>

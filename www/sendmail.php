<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';
require 'conn.php';

$user_email = $_POST["email"];
$temp_password = rand(1000,9999);

$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);
$sql_query = "select * from `user` where email like '$user_email';";
$result = mysqli_query($conn, $sql_query);
$sql_query2 = "update `user` set temp_password = '$temp_password' where email like '$user_email';";

if ($result->num_rows > 0) {
    mysqli_query($conn, $sql_query2);
    $mail = new PHPMailer(true);

    try {
        $mail->SMTPDebug = 0;
        $mail->isSMTP();
        $mail->Host = 'smtp.gmail.com';
        $mail->SMTPAuth = true;
        $mail->Username = 'testapplication1243@gmail.com';
        $mail->Password = 'testappl';
        $mail->SMTPSecure = 'ssl';
        $mail->Port = 465;

        $mail->setFrom('testapplication1243@gmail.com', 'no-reply@tidytimetable.com');
        $mail->addAddress($user_email);

        $mail->isHTML();
        $mail->Subject = 'Reinitialiser votre mot de passe';
        $mail->Body = 'Votre code de reinitialisation est: '.$temp_password.'. Ce code expirera dans 2 minutes.<br/><br/>Merci,<br/><br/>Staff TidyTimeTable';

        $mail->send();
    } catch (Exception $e) {

    }
    echo "1";
} else {
    echo "-1";
}


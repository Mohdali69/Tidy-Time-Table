<?php

require("conn.php");

$user_lastname = $_POST["lastname"];
$user_name = $_POST["name"];
$user_email = $_POST["email"];
$user_password = $_POST["password"];

$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
$sql_query = "insert into `user` (lastname,`name`,email,password,statut) values ('$user_lastname','$user_name','$user_email','$user_password',true);";

if (mysqli_query($conn, $sql_query)) {
    echo "1";
} else {
    echo "-1";
}

<?php

require("conn.php");

$user_email = $_POST["email"];
$user_password = $_POST["password"];

$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);
$sql_query = "insert into `user` values ('$user_email','$user_password',null);";

if (mysqli_query($conn, $sql_query)) {
    echo "1";
} else {
    echo "-1";
}

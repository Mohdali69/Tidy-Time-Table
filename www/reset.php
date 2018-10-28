<?php

require("conn.php");

$temp_password = $_POST["code"];
$user_password = $_POST["password"];

$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);
$sql_query = "select * from `user` where temp_password like '$temp_password';";
$result = mysqli_query($conn, $sql_query);
$sql_query2 = "update `user` set password = '$user_password' where temp_password like '$temp_password';";
$sql_query3 = "update `user` set temp_password = null where password like '$user_password';";

if ($result->num_rows > 0) {
    mysqli_query($conn, $sql_query2);
    mysqli_query($conn, $sql_query3);
    echo "1";
} else {
    echo "-1";
}

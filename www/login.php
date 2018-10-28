<?php

require("conn.php");

$user_email = $_POST["email"];
$user_password = $_POST["password"];
$true_password = "";
$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);
$sql_query = "select password from `user` where email like '$user_email';";
$result = mysqli_query($conn, $sql_query);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $true_password = $row["password"];
        if ($user_password == $true_password) {
            echo "1";
        } else {
            echo "-1";
        }
    }
} else {
    echo "-1";
}

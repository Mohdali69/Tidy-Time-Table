<?php

require("conn.php");

$user_email = $_POST["email"];
$user_password = $_POST["password"];
$true_password = "";
$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
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

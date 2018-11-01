<?php

require("conn.php");

$current_user = $_POST['current_user'];
$statut = $_POST['statut'];

$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

$sql_query = "update `user` set statut = $statut where id = $current_user;";

if(mysqli_query($conn, $sql_query)) {
    echo "1";
} else {
    echo "-1";
}

mysqli_close($conn);

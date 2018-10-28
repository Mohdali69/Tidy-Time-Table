<?php

require("conn.php");

$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);
$sql_query = "update `user` set temp_password = null;";

mysqli_query($conn, $sql_query);

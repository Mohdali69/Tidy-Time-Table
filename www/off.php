<?php

require("conn.php");

$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
$sql_query = "update `user` set temp_password = null;";

mysqli_query($conn, $sql_query);

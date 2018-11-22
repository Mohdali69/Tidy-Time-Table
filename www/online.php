<?php

require("conn.php");

$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
$sql_query = "select `name`,lastname,id from `user` where statut = true;";
$result = mysqli_query($conn, $sql_query);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $id = $row['id'];
        $lastname = $row['lastname'];
        $name = $row['name'];
    }
    echo $id."/".$lastname."~".$name;
} else {
    echo "-1";
}

mysqli_close($conn);
